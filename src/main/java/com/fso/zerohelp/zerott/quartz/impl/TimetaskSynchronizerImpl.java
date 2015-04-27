package com.fso.zerohelp.zerott.quartz.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import com.fso.zerohelp.zerott.quartz.Persistent;
import com.fso.zerohelp.zerott.quartz.QuartzScheduler;
import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.dao.TimetaskDAO;
import com.fso.zerohelp.zerott.quartz.enumeration.QuartzJobStatusEnum;
import com.fso.zerohelp.zerott.quartz.enumeration.SchedulerJobStatusEnum;
import com.fso.zerohelp.zerott.quartz.util.IpAddressUtil;

public class TimetaskSynchronizerImpl extends AbastractSynchronizerImpl<Integer, TimetaskConfig>{
	public static Logger logger = LoggerFactory.getLogger(TimetaskSynchronizerImpl.class);

	private List<QuartzScheduler> schedulerList; //经过封装的主时间程序调度器
	private TimetaskDAO timetaskDAO; //同步数据源

	public void setTimetaskDAO(TimetaskDAO timetaskDAO) {
		this.timetaskDAO = timetaskDAO;
	}

	public void setSchedulerList(List<QuartzScheduler> scheduler) {
		this.schedulerList = scheduler;
	}
	
	public String getName(){
		return "Timetask";
	}
	//判断target_ip是否是当前机器IP
	private boolean isIPMatch(TimetaskConfig config){
		return IpAddressUtil.matchIP(config.getTargetIp());
	}
	//判断last_target_ip是否是当前机器IP
	private boolean isLastIPMatch(TimetaskConfig config){
		return IpAddressUtil.matchIP(config.getLastTargetIp());
	}

	private boolean first = true; //同步时间程序是否是第一次运行
	private Map<String, Boolean> localLoadedMap = new HashMap<String, Boolean>();

	public void sync(){
		List<TimetaskConfig> allConfigs = new ArrayList<TimetaskConfig>();
		boolean exceptionOccurred = false;

        if (!localOnly) {
            try {
                if (StringUtils.hasText(appName)) {
                    allConfigs = timetaskDAO.getAllConfigByAppName(appName);
                } else {
                    allConfigs = timetaskDAO.getAllConfig();
                }
            } catch (Exception e) {
                exceptionOccurred = true;
                logger.error(e.getMessage(), e);
            }
        }

        for (QuartzScheduler scheduler : schedulerList) {
            if (localOnly || exceptionOccurred) {
                syncFromLocal(scheduler);
            } else {
                Date lastTime = lastSyncTimeMap.get(scheduler.getSchedulerName());
                List<TimetaskConfig> changedConfigs = new LinkedList<TimetaskConfig>();
                List<TimetaskConfig> schedulerConfigs = new LinkedList<TimetaskConfig>();
                for (TimetaskConfig c : allConfigs) {
                    if (scheduler.getSchedulerName().equals(c.getScheduler().trim())) {
                        schedulerConfigs.add(c);
                        if (lastTime == null || c.getGmtModified().compareTo(lastTime) > 0) {
                            // 当前配置的修改时间大于本scheduler上次同步配置的最大时间
                            changedConfigs.add(c);
                        }
                    }
                }
                syncFromDatabase(scheduler, schedulerConfigs, changedConfigs);
            }
        }
		first = false;
	}

    private void syncFromLocal(QuartzScheduler scheduler) {
        logger.debug("Trying to sync scheduler " + scheduler.getSchedulerName() + " from localFile");
        String schedulerName = scheduler.getSchedulerName();
        Persistent<TimetaskConfig> persistentor = this.getPersistentor(schedulerName);
        List<TimetaskConfig> configs = null;

        if (localLoadedMap.get(schedulerName) == null || !localLoadedMap.get(schedulerName)) { // 如果没有载入过，从本地文件读取配置
            configs = persistentor.readConfig();
            if (configs != null && configs.size() != 0)
                localLoadedMap.put(schedulerName, true);
        } else {
            return;
        }

        if (configs == null || configs.size() == 0)
            return;

        doSync(scheduler, configs);
    }

	private void syncFromDatabase(QuartzScheduler scheduler, List<TimetaskConfig> schedulerConfigs, List<TimetaskConfig> changedConfigs){
		if(changedConfigs.size() == 0) return;
		String schedulerName = scheduler.getSchedulerName();
		Date maxDate = doSync(scheduler, changedConfigs);
		lastSyncTimeMap.put(scheduler.getSchedulerName(), maxDate);

		Persistent<TimetaskConfig> persistentor = this.getPersistentor(schedulerName);
		persistentor.writeConfig(schedulerConfigs);
		Date syncTime = lastSyncTimeMap.get(schedulerName);
		logger.warn("Scheduler " + schedulerName + " updated, lastUpdateTime = " + syncTime == null ? "-" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(syncTime)
				+ ", changed " + changedConfigs.size()
				+ ", total size " + schedulerConfigs.size());
	}

	private Date doSync(QuartzScheduler scheduler, List<TimetaskConfig> changedconfigs){
		Date maxDate = null;

		for(TimetaskConfig config : changedconfigs){
			//如果IP地址匹配，则设置成数据库时间程序状态，并直接设置成禁止运行状态
			logger.debug("config = " + config.toString());
			boolean ipMatched = isIPMatch(config);
			if(ipMatched){
				if(config.getStatus() == QuartzJobStatusEnum.RUN.getStatus()){
					scheduler.start(config);
					logger.debug("时间程序" + config.getId() + " 启动");
				}else{
					scheduler.stop(config);
					logger.debug("时间程序" + config.getId() + "停止：时间程序开关状态为" + config.getStatus() + " 配置IP[" + config.getTargetIp() + "]");
				}
			}else{
				SchedulerJobStatusEnum status = scheduler.getJobStatus(config.getId());
				if(status != null && status == SchedulerJobStatusEnum.SCHEDULED){
					scheduler.stop(config);
				}
				if(config.getLastTargetIp() != null && isLastIPMatch(config)){
					//如果本程序是第一次运行，则说明上次从本服务器运行该时间程序时未正常更新状态就结束了；
					//此时，需要将last_target_ip字段清空，让别的服务器可以顺利运行时间程序
					if(timetaskDAO.updateTimetaskLastStatus(config.getId(), "", null, null) == 0){
						logger.error("can't update last_target_ip=" + config.getLastTargetIp() + " where id=" + config.getId());
					}
				}
			}

            if (maxDate == null) {
                maxDate = config.getGmtModified();
            } else {
                if (maxDate.compareTo(config.getGmtModified()) < 0) {
                    maxDate = config.getGmtModified();
                }
            }
		}

		return maxDate;
	}

	protected Persistent<TimetaskConfig> newPersistentorInstance(String prefix, String suffix, String dir, int backup){
		return new TimetaskConfigPersistent(prefix, suffix, dir, backup);
	}

	@Override
    public void onApplicationEvent(ApplicationEvent event) {
        super.onApplicationEvent(event);
        if (event instanceof ContextRefreshedEvent) {
            if (this.first) {
                for (final QuartzScheduler s : schedulerList) {
                    Thread schedulerThread = new Thread() {
                        public void run() {
                            s.start();
                        }
                    };
                    schedulerThread.setName("Quartz Scheduler [" + s.getSchedulerName() + "]");
                    schedulerThread.start();
                }
                this.first = false;
            }
        } else if (event instanceof ContextClosedEvent) {
            for (QuartzScheduler s : schedulerList) {
                s.stop();
            }
        }
    }
}
