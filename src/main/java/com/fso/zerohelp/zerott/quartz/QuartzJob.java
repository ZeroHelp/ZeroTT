package com.fso.zerohelp.zerott.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;

public interface QuartzJob extends StatefulJob {
	/**
	 * 获取job唯一key
	 * @return 该job的唯一名称
	 */
	public String getJobName();
	/**
	 * 真正的执行方法
	 * @param context
	 */
	public void run(JobExecutionContext context);
}
