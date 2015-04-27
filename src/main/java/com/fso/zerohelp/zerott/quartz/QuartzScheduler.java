package com.fso.zerohelp.zerott.quartz;

import com.fso.zerohelp.zerott.quartz.enumeration.SchedulerJobStatusEnum;

public interface QuartzScheduler {
	public String getSchedulerName();
	/**
	 * 向scheduler中添加job
	 * @param job 要添加的job
	 */
	public void addJob(QuartzJob job);
	/**
	 * 从scheduler中删除job
	 * @param job 要删除的job
	 */
	public void removeJob(QuartzJob job);
	/**
	 * 暂停时间任务
	 * @param config 时间任务名称
	 * @return 暂停成功返回true, 暂停失败返回false
	 */
	public void stop(TimetaskConfig config);
	/**
	 * 启动时间任务
	 * @param config 时间任务名称
	 * @return 启动成功返回true, 启动失败返回false
	 */
	public void start(TimetaskConfig config);
	/**
	 * 启动本scheduler
	 * @return 启动成功返回true，启动失败返回false
	 */
	public void start();

    /**
     * 停止scheduler
     */
    void stop();

	/**
	 * 获取任务状态
	 * @param key
	 * @return 如果任务不存在，返回null，否则返回此任务状态
	 */
	public SchedulerJobStatusEnum getJobStatus(Integer key);
	/**
	 * 获取当前scheduler的信息
	 * 信息格式如下：
	 * SchedulerName: xxx
	 * ActiveJobCount: N
	 * ActiveJobNameList: Name1,Name2,Name3....
	 * @return scheduler信息
	 */
	public String getSummary();
	/**
	 * 获取当前scheduler使用哪种类型的线程池
	 * 线程池类型：0: 每个scheduler单独的线程池  1: 所有scheduler共享线程池
	 * @return int
	 */
	public int getThreadPoolType();
}
