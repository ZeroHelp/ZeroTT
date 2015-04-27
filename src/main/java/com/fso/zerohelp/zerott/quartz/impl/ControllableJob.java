package com.fso.zerohelp.zerott.quartz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fso.zerohelp.zerott.quartz.JobDataMapKey;
import com.fso.zerohelp.zerott.quartz.QuartzJob;
import com.fso.zerohelp.zerott.quartz.QuartzScheduler;
import com.fso.zerohelp.zerott.quartz.info.JobState;
import com.fso.zerohelp.zerott.quartz.info.JobTracker;
import com.fso.zerohelp.zerott.quartz.util.OsUtil;

/**
 * 该类实现了对QuartzJob的基本方法的实现，可以被业务类继承
 * @author xuming
 *
 */
public abstract class ControllableJob implements QuartzJob{
	public static final Logger logger = LoggerFactory.getLogger(ControllableJob.class);

	private boolean alwaysRun = false; //是否永远运转，不依赖配置开关;如果为true，只不依赖开关直接运行

	private String jobName; //job名称

	public boolean isAlwaysRun() {
		return alwaysRun;
	}
	public void setAlwaysRun(boolean alwaysRun) {
		this.alwaysRun = alwaysRun;
	}
	public void setJobName(String name){
		this.jobName = name;
	}

	@Override
	public String getJobName() {
		return this.jobName;
	}

	/* 此方法内部取出了保存在jobDataMap中保存的原始bean对象，并调用该对象的run方法
	 * @context 此参数用于保存传递给框架的相关参数，包括runObject/runScheduler/runTrigger及其他必要的运行时对象
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			QuartzJob runObject = (ControllableJob) context.getTrigger().getJobDataMap().get(JobDataMapKey.RUN_OBJECT);
			QuartzScheduler runScheduler = (QuartzScheduler) context.getTrigger().getJobDataMap().get(JobDataMapKey.RUN_SCHEDULER);
			logger.info("Job " + runObject.getJobName() + " begin to run at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			JobState state = new JobState(runScheduler.getSchedulerName(), runObject.getJobName(), new Date(),
					Thread.currentThread().getId(), OsUtil.currentPid());
			JobTracker.collect(state);
			runObject.run(context);
			state.setLastEndTime(new Date());
			JobTracker.finish(state);
			logger.info("Job " + runObject.getJobName() + " finished at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
