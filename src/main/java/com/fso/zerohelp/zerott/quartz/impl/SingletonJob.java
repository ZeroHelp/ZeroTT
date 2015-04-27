package com.fso.zerohelp.zerott.quartz.impl;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.fso.zerohelp.zerott.quartz.JobDataMapKey;
import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.dao.TimetaskDAO;
import com.fso.zerohelp.zerott.quartz.util.IpAddressUtil;

public class SingletonJob extends MethodInvokingJob{
	public static Logger logger = LoggerFactory.getLogger(SingletonJob.class);
	@Autowired
	TransactionTemplate ttcontrolTransactionTemplate;
	private boolean runOnDbError = false;
	
	public void setRunOnDbError(boolean runOnDbError) {
		this.runOnDbError = runOnDbError;
	}

	public void setTtcontrolTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.ttcontrolTransactionTemplate = transactionTemplate;
	}

	private TimetaskDAO timetaskDAO; //同步数据源 
	public void setTimetaskDAO(TimetaskDAO timetaskDAO) {
		this.timetaskDAO = timetaskDAO;
	}
	
	public void run(JobExecutionContext context){
		JobDataMap data  = context.getTrigger().getJobDataMap();
		final int id = data.getInt(JobDataMapKey.RUN_ID);
		boolean transactionError = false;
		Integer rownum = 0;
		//事务性检查并更新last_target_ip字段
		try{
		 rownum = ttcontrolTransactionTemplate.execute(new TransactionCallback<Integer>() {
				public Integer doInTransaction(TransactionStatus status) {
			    	try {
			    		TimetaskConfig config = timetaskDAO.getConfigForUpdate(id);
				    	String lastTargetIp = config.getLastTargetIp();
						//如果last_target_ip字段存储的不是ip地址，则说明没有服务器占用此地址
						//如果last_target_ip与本服务器地址匹配，则说明上次占用此地址的就是本服务器，可能是由于意外原因，没有清空就运行了，那么这程序直接运行
						if(!IpAddressUtil.matchIpPattern(lastTargetIp) || IpAddressUtil.matchIP(config.getLastTargetIp())){
							return timetaskDAO.updateTimetaskLastStatus(id, config.getTargetIp(), new Date(), null);
						}else{
							//其他情况，说明last_target_ip是其它服务器的IP地址，则此时间程序有可能在别的地方运行，等待下次运行周期
							logger.warn("Timetask " + config.getTaskId() + "(" + config.getId() + ") maybe already running @" + config.getLastTargetIp());
						}
	               }catch (Throwable t) {
	            	   status.setRollbackOnly();
	            	   logger.error(t.getMessage(), t);
	            	   return -1;
	               }
	               return 0;
			    }
			});
		}catch(TransactionException e){
			logger.error("TransactionException on SingletonJob " + this.getJobName() + "trying to run, runOnDbError=" + runOnDbError);
			transactionError = true;
			logger.error(e.getMessage(), e);
		}
		
		
		if(rownum > 0){
			super.run(context);
			if(timetaskDAO.updateTimetaskLastStatus(id, "", null, new Date()) < 0){
				//更新失败，此处应打log，下次调度会清除此信息
				logger.error("Clear timetask last_target_ip failed, id=" + id);
			}
		}else if((rownum < 0) || transactionError){ //DB error
			if(runOnDbError) super.run(context);
		}else{
			//wait;
		}
	}
}
