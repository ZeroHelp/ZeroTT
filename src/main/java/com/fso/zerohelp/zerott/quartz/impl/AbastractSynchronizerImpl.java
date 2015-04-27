package com.fso.zerohelp.zerott.quartz.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*import com.taobao.eagleeye.EagleEye;*/
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.StatefulJob;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.fso.zerohelp.zerott.quartz.JobDataMapKey;
import com.fso.zerohelp.zerott.quartz.Persistent;
import com.fso.zerohelp.zerott.quartz.Synchronizer;

public abstract class AbastractSynchronizerImpl<M, T> implements Synchronizer, StatefulJob, ApplicationListener{
	public static org.slf4j.Logger logger = LoggerFactory.getLogger(AbastractSynchronizerImpl.class);
	private boolean started = false;

    private static final Object lock = new Object();

	private int repeatInterval; //向master同步周期，单位为秒
	private int timeout; //同步超时时间
	private int warningInterval; //报警间隔时间，单位为秒
	protected Date lastSyncTime; //最后一次同步的时间
	protected boolean lastSyncFromDb; //最后一次是否是从DB读取配置
	private Scheduler syncScheduler; //同步时间程序调度器
	protected boolean localOnly = false; //仅仅从本地文件中加载配置文件，而不从数据库同步数据，用于特殊情况
	protected boolean localLoaded = false; //是否已经从本地加载过配置
	protected String persistDir; //时间程序开关配置存储目录
	protected int backup; //时间程序开关配置历史配置保留个数

	protected Map<String, Date> lastSyncTimeMap = new HashMap<String, Date>();
	protected Map<String, Persistent<T>> schedulerPersistMap = new HashMap<String, Persistent<T>>();

    protected String appName; // 应用名称

	public void setBackup(int backup) {
		this.backup = backup;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
        File dir = new File(persistDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setWarningInterval(int warningInterval) {
		this.warningInterval = warningInterval;
	}

	public void setSyncScheduler(Scheduler syncScheduler) {
		this.syncScheduler = syncScheduler;
	}

	public void setLocalOnly(boolean localOnly) {
		this.localOnly = localOnly;
	}

    public void setAppName(String appName) {
        this.appName = appName;
    }

	protected Scheduler getSyncScheduler() {
        if (syncScheduler == null) {
            synchronized (lock) {
                if (syncScheduler == null) {
                    Properties mergedProps = new Properties();
                    mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
                    mergedProps.setProperty("org.quartz.threadPool.threadCount", "1");
                    mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true"); // 跳过版本检查
                    mergedProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "QuartzTimetaskSynchronizer");
                    SchedulerFactory schedulerFactory = new StdSchedulerFactory();

                    try {
                        ((StdSchedulerFactory) schedulerFactory).initialize(mergedProps);
                        syncScheduler = schedulerFactory.getScheduler();
                    } catch (SchedulerException e) {
                        logger.error("Error when creating timetask synchronizer for " + this.getName(), e);
                    }
                }
            }
        }
        return syncScheduler;
    }

	abstract protected Persistent<T> newPersistentorInstance(String prefix, String suffix, String dir, int backup);

	protected Persistent<T> getPersistentor(String name){
		Persistent<T> persistentor = schedulerPersistMap.get(name);
		if(persistentor == null){
			persistentor = this.newPersistentorInstance(name, ".xml", persistDir, backup);
			schedulerPersistMap.put(name, persistentor);
		}
		return persistentor;
	}

	/**
	 * 启动定时调度程序
	 */
	public synchronized void start() {
        if (started) {
            return;
        }
        try {
            Scheduler scheduler = this.getSyncScheduler();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }

            CronTrigger trigger = new CronTrigger();
            trigger.setName(this.getName() + "SynchronizerTrigger");
            trigger.setCronExpression("0/" + this.repeatInterval + " * * * * ?");
            trigger.setGroup(this.getName() + "SynchronizerGroup");

            String jobDetailName = this.getName() + "SynchronizeJob";
            String jobDetailGroup = this.getName() + "SynchronizeSchduler";
            JobDetail job = new JobDetail(jobDetailName, jobDetailGroup, this.getClass());
            job.getJobDataMap().put(JobDataMapKey.RUN_OBJECT, this);
            job.getJobDataMap().put(JobDataMapKey.RUN_MAGIC, appName);

            JobDetail j = scheduler.getJobDetail(jobDetailName, jobDetailGroup);
            if (j == null) {
                scheduler.scheduleJob(job, trigger);
                logger.info(this.getName() + "SynchronizeJob started");
            } else {
                logger.warn("{}SynchronizeJob already started.", this.getName());
            }
        } catch (Exception e) {
            logger.error("启动时间程序同步程序失败: ", e);
        }
    }

	public synchronized void stop(){
		try {
            if (!this.getSyncScheduler().isShutdown()) {
                this.getSyncScheduler().unscheduleJob(this.getName() + "SynchronizerTrigger", this.getName() + "SynchronizerGroup");
                this.getSyncScheduler().shutdown();
            }
		} catch (SchedulerException e) {
			logger.error("停止时间程序同步程序失败: ", e);
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Synchronizer runObject = (Synchronizer) context.getJobDetail().getJobDataMap().get(JobDataMapKey.RUN_OBJECT);
        try {
            String currentAppName = (String) context.getJobDetail().getJobDataMap().get(JobDataMapKey.RUN_MAGIC);
            /*EagleEye.startTrace(EagleEye.generateTraceId(null), "time://" + currentAppName + "/" + getName() + "Synchronizer");*/
            runObject.sync();
        } finally {
            /*EagleEye.endTrace();*/
        }
    }

	/*
	 * 由于希望该程序在spring初始化完毕后自动启动，故监听spring context初始化完毕消息
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent){
			if(!started){
				this.start();
				started = true;
			}
		}else if(event instanceof ContextClosedEvent){
			this.stop();
		}
	}
}
