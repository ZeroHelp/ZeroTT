package com.fso.zerohelp.zerott.quartz.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

import com.fso.zerohelp.zerott.quartz.Context;
import com.fso.zerohelp.zerott.quartz.ContextSupport;
import com.fso.zerohelp.zerott.quartz.JobDataMapKey;
import com.fso.zerohelp.zerott.quartz.QuartzJob;
import com.fso.zerohelp.zerott.quartz.QuartzScheduler;
import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.enumeration.QuartzJobTypeEnum;
import com.fso.zerohelp.zerott.quartz.enumeration.SchedulerJobStatusEnum;

public class SchedulerImpl implements QuartzScheduler,InitializingBean, ApplicationContextAware {
    
	public static Logger logger = LoggerFactory.getLogger(SchedulerImpl.class);

	private ApplicationContext appContext;
	
	private boolean autoStartup;
	private String schedulerName;
	private int threadCount = 10; //默认10个线程
	private int threadPoolType = 0; // 0: 每个scheduler单独的线程池  1: 所有scheduler共享线程池

	private Map<String, QuartzJob> jobMap;
	private Map<Integer, SchedulerJobStatusEnum> jobStatusMap;

	private final String PROP_THREAD_COUNT="org.quartz.threadPool.threadCount";

	//如果使用spring配置的executor则需要用到如下配置
	private TaskExecutor taskExecutor;
	private static final ThreadLocal<TaskExecutor> taskExecutorHolder = new ThreadLocal<TaskExecutor>();

	public SchedulerImpl(){
		jobMap = new ConcurrentHashMap<String, QuartzJob>();
		jobStatusMap = new ConcurrentHashMap<Integer, SchedulerJobStatusEnum>();
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public static TaskExecutor getTaskExecutor(){
		return taskExecutorHolder.get();
	}

	public void addJob(QuartzJob j){
        JobDetail job = new JobDetail(j.getJobName(), this.getSchedulerName(), j.getClass());
        job.setDurability(true); //防止删除trigger时删除job
        try {
            this.getScheduler().addJob(job, true);
        } catch (SchedulerException e) {
            logger.error("addJob " + j.getJobName() + " to Scheduler exception", e);
        }
	}

	public void removeJob(QuartzJob job){
		return;
	}

	public SchedulerJobStatusEnum getJobStatus(Integer key){
		return this.jobStatusMap.get(key);
	}

	public void setJobDetails(List<QuartzJob> jobDetails){
		for(QuartzJob job : jobDetails){
			this.putJob(job);
		}
	}

	public int getThreadPoolType() {
		return threadPoolType;
	}

	public void setThreadPoolType(int threadPoolType) {
		this.threadPoolType = threadPoolType;
	}

	private void putJob(QuartzJob j) {
		try {
			if(jobMap.get(j.getJobName()) == null){
				/*JobDetail job = new JobDetail(j.getJobName(), this.getSchedulerName(), j.getClass());
				this.getScheduler().addJob(job, true);*/
				jobMap.put(j.getJobName(), j);
			}else{
			}
		} catch (Exception e) {
			logger.error("添加job " + j.getJobName() + "错误", e);
		}
	}

	public boolean isAutoStartup() {
		return autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	@Override
	public void start(TimetaskConfig config) {
		//判断job是否已经被调度
		QuartzJob j = jobMap.get(config.getTaskId());
		if(j == null) {
            if (config.getJobType() == QuartzJobTypeEnum.DBJOB.getType()) {
                MethodInvokingJob m = new MethodInvokingJob();
                String magic = config.getMagic();
                try {
                    JSONObject json = new JSONObject(magic);
                    m.setJobName(config.getTaskId());
                    m.setTargetObject(appContext.getBean(json.getString("targetObject")));
                    m.setTargetMethod(json.getString("targetMethod"));
                    if (json.has("requireJobContext")) {
                        m.setRequireJobContext(json.getBoolean("requireJobContext"));
                    }
                    if (json.has("arguments")) {
                        JSONArray arguments = json.getJSONArray("arguments");
                        if (arguments != null && arguments.length() > 0) {
                            Object[] args = new Object[arguments.length()];
                            for (int i = 0; i < arguments.length(); i++) {
                                JSONObject arg = arguments.getJSONObject(i);
                                String type = arg.getString("type");
                                String value = arg.getString("value");
                                if ("int".equals(type)) {
                                    args[i] = new Integer(value);
                                } else if ("long".equals(type)) {
                                    args[i] = new Long(value);
                                } else if ("string".equals(type)) {
                                    args[i] = value;
                                } else if ("bean".equals(type)) {
                                    args[i] = appContext.getBean(value);
                                }
                            }
                            m.setArguments(args);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("start dbjob failed. taskId = " + config.getTaskId(), e);
                    return;
                }
                j = m;
                this.putJob(m);
                this.addJob(m);
            } else {
                logger.warn("start job failed. maybe job's config is not correct. taskId = {}", config.getTaskId());
		        return;
		    }
		}
		SchedulerJobStatusEnum status = jobStatusMap.get(config.getId());
		if(status == SchedulerJobStatusEnum.SCHEDULED){
			//如果程序已经被调度，检查cronExpression表达式是否已经被修改过,若修改过需要使用新的trigger替换现有的
			try {
				Trigger trigger = this.getScheduler().getTrigger(this.getTriggerName(j), this.getTriggerGroup(j));
				if(trigger instanceof CronTrigger){
					CronTrigger cronTrigger = (CronTrigger)trigger;
                    if (!cronTrigger.getCronExpression().equals(config.getCronExpression())
                            || !trigger.getJobDataMap().getString(JobDataMapKey.RUN_MAGIC).equals(config.getMagic())) { // 替换
						this.stop(config);
						this.start(config);
                        logger.warn("Job " + j.getJobName() + "'s trigger expresion replaced from "
                                + cronTrigger.getCronExpression() + " to " + config.getCronExpression()
                                + ", magic replaced from " + trigger.getJobDataMap().getString(JobDataMapKey.RUN_MAGIC)
                                + " to " + config.getMagic());
					}
				}
			} catch (SchedulerException e) {
				logger.error("Get trigger " + this.getTriggerName(j) + " ERROR", e);
			}
			return;
		}
		//至此没有被调度，调度之
		if(this.scheduleJob(j, config)){
			jobStatusMap.put(config.getId(), SchedulerJobStatusEnum.SCHEDULED);
			logger.info("job " + j.getJobName() + " started.");
		}
	}

	@Override
	public void stop(TimetaskConfig config) {
		//判断job是否已经停止
		QuartzJob j = jobMap.get(config.getTaskId());
		if(j == null) return;
		SchedulerJobStatusEnum status = jobStatusMap.get(config.getId());
		if(status == SchedulerJobStatusEnum.SCHEDULED){
			//至此任务已经被调度，停止之
			try {
				this.getScheduler().unscheduleJob(this.getTriggerName(j), this.getTriggerGroup(j));

				//设置interrupt
				if(j instanceof MethodInvokingJob){
					MethodInvokingJob methodInvokJob = (MethodInvokingJob)j;
					if(methodInvokJob.getTargetObject() instanceof ContextSupport){
						ContextSupport contextSupportJob = (ContextSupport)methodInvokJob.getTargetObject();
						Context ctx = contextSupportJob.getContext();
						ctx.setInterrupted(true);
					}
				}

                if (config.getJobType() == QuartzJobTypeEnum.DBJOB.getType()) {
                    jobMap.remove(config.getTaskId());
                    jobStatusMap.remove(config.getId());
                } else {
                    jobStatusMap.put(config.getId(), SchedulerJobStatusEnum.PAUSED);
                }
				logger.info("job " + j.getJobName() + " stoped.");
			} catch (SchedulerException e) {
				logger.error("pause job " + j.getJobName() + " error ", e);
			}
		}
	}

	private String getTriggerName(QuartzJob job){
		return "Trigger" + job.getJobName();
	}
	private String getTriggerGroup(QuartzJob job){
		return "Group" + job.getJobName();
	}

	private boolean scheduleJob(QuartzJob j, TimetaskConfig config){
			try {
				CronTrigger trigger = new CronTrigger();
				trigger.setName(this.getTriggerName(j));
				trigger.setCronExpression(config.getCronExpression());
				trigger.setGroup(this.getTriggerGroup(j));

				JobDataMap data = new JobDataMap();
	            data.put(JobDataMapKey.RUN_OBJECT, j); // 保存bean对象实例
	            data.put(JobDataMapKey.RUN_SCHEDULER, this); // 保存scheduler对象实例，备用
	            data.put(JobDataMapKey.RUN_TASK_ID, config.getTaskId()); // 时间任务名称
	            data.put(JobDataMapKey.RUN_ID, config.getId()); // 数据库ID
	            data.put(JobDataMapKey.RUN_TRIGGER, trigger); // 保存trigger对象实例，备用
	            data.put(JobDataMapKey.RUN_MAGIC, config.getMagic()); // 保存magic参数，具体意义由时间程序解释

				logger.info("job " + j.getJobName()	+ " added.");

				trigger.setJobName(j.getJobName());
				trigger.setJobGroup(this.getSchedulerName());
				trigger.setJobDataMap(data);
				//设置interrupt
				if(j instanceof MethodInvokingJob){
					MethodInvokingJob methodInvokJob = (MethodInvokingJob)j;
					if(methodInvokJob.getTargetObject() instanceof ContextSupport){
						ContextSupport contextSupportJob = (ContextSupport)methodInvokJob.getTargetObject();
						contextSupportJob.setContext(new Context());
					}
				}

				this.getScheduler().scheduleJob(trigger);
				return true;
			} catch (Exception e) {
			    logger.error(e.getMessage(), e);
				return false;
			}
	}

	private Scheduler scheduler;
	private StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

	/**
	 * Load and/or apply Quartz properties to the given SchedulerFactory.
	 * @param schedulerFactory the SchedulerFactory to initialize
	 */
	private void initSchedulerFactory(SchedulerFactory schedulerFactory)
			throws SchedulerException, IOException {
		Properties mergedProps = new Properties();

		mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true"); //跳过版本检查
		if(taskExecutor != null){
			mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SpringExecutorThreadPool.class.getName());
		}else{
			mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
			mergedProps.setProperty(PROP_THREAD_COUNT, Integer.toString(this.threadCount));
		}

		// Make sure to set the scheduler name as configured in the Spring configuration.
		if (this.schedulerName != null) {
			mergedProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, this.schedulerName);
		}

		((StdSchedulerFactory) schedulerFactory).initialize(mergedProps);
	}

	private synchronized Scheduler getScheduler(){
		if(this.scheduler == null){
			try {
				this.initSchedulerFactory(this.schedulerFactory);
				this.scheduler = this.createScheduler(this.schedulerFactory, schedulerName);
				logger.warn("QuartzScheduler " + schedulerName + "[" + this.scheduler + "] created");
			} catch (Exception e) {
				logger.error("Error when creating scheduler" + this.getSchedulerName(), e);
			}
		}
		return this.scheduler;
	}

	/**
	 * Create the Scheduler instance for the given factory and scheduler name.
	 * Called by {@link #afterPropertiesSet}.
	 * <p>The default implementation invokes SchedulerFactory's <code>getScheduler</code>
	 * method. Can be overridden for custom Scheduler creation.
	 * @param schedulerFactory the factory to create the Scheduler with
	 * @param schedulerName the name of the scheduler to create
	 * @return the Scheduler instance
	 * @throws SchedulerException if thrown by Quartz methods
	 * @see #afterPropertiesSet
	 * @see org.quartz.SchedulerFactory#getScheduler
	 */
	protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName)
			throws SchedulerException {

		// Override thread context ClassLoader to work around naive Quartz ClassLoadHelper loading.
		/*
		Thread currentThread = Thread.currentThread();

		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();

		boolean overrideClassLoader = (this.resourceLoader != null &&
				!this.resourceLoader.getClassLoader().equals(threadContextClassLoader));
		if (overrideClassLoader) {
			currentThread.setContextClassLoader(this.resourceLoader.getClassLoader());
		}
		*/
		try {
			SchedulerRepository repository = SchedulerRepository.getInstance();
			synchronized (repository) {
				Scheduler existingScheduler = (schedulerName != null ? repository.lookup(schedulerName) : null);
				Scheduler newScheduler = schedulerFactory.getScheduler();
				if (newScheduler == existingScheduler) {
					throw new IllegalStateException("Active Scheduler of name '" + schedulerName + "' already registered " +
							"in Quartz SchedulerRepository. Cannot create a new Spring-managed Scheduler of the same name!");
				}
				/*
				if (!this.exposeSchedulerInRepository) {
					// Need to explicitly remove it if not intended for exposure,
					// since Quartz shares the Scheduler instance by default!
					SchedulerRepository.getInstance().remove(newScheduler.getSchedulerName());
				}
				*/
				return newScheduler;
			}
		}
		finally {
			/*
			if (overrideClassLoader) {
				// Reset original thread context ClassLoader.
				currentThread.setContextClassLoader(threadContextClassLoader);
			}
			*/
		}
	}

	public void start(){
		try {
			Set<String> jobNames = this.jobMap.keySet();
			for(String key : jobNames){
				QuartzJob j = jobMap.get(key);
				this.addJob(j);
			}
            if (!this.getScheduler().isStarted()) {
                this.getScheduler().start();
            }
		} catch (Exception e) {
			logger.error("Start Scheduler error:", e);
		}
	}

    @Override
    public void stop() {
        try {
            if (!this.getScheduler().isShutdown()) {
                this.getScheduler().shutdown(true);
            }
        } catch (SchedulerException e) {
            logger.error("停止时间程序调度器失败: ", e);
        }
    }

    /**
	 * 获取当前scheduler的信息
	 * 信息格式如下：
	 * SchedulerName: xxx
	 * ActiveJobCount: N
	 * ActiveJobNameList: Name1,Name2,Name3....
	 * @return scheduler信息
	 */
	public String getSummary() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SchedulerName: " + this.getSchedulerName() + "\n");

			String[] groupNames = this.getScheduler().getJobGroupNames();
			sb.append("ActiveJobGroupCount: " + groupNames.length + "\n");
			sb.append("ActiveJobGroupNames: ");
			for(String name: groupNames){
				sb.append(name + ",");
			}
			String[] jobNames = this.getScheduler().getJobNames(this.getSchedulerName());
			sb.append("\nActiveJobCount: " + jobNames.length + "\n");
			sb.append("ActiveJobNameList: ");
			for(String name: jobNames){
				sb.append(name + ",");
			}
			return sb.toString();
		} catch (SchedulerException e) {
			logger.error("错误", e);
			return e.toString();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化一下scheduler，然后清除静态容器中的taskExecutor对象；
		//原因是每个scheduler配置的executor可能不一样，所以在具体的scheduler生成完毕后，句柄已经被保存在对象里面的前提下，必须删除当前的executor；
		//这样下一个scheduler初始化的时候才能使用它自己的executor对象；
		//这么二的实现方式都是源于quartz的SchedulerFactory中ThreadPool对象是反射创建的，无法动态注入
		if (this.taskExecutor != null) {
			taskExecutorHolder.set(this.taskExecutor);
			this.getScheduler();
			taskExecutorHolder.remove();
		}
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
