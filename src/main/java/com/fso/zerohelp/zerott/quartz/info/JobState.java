package com.fso.zerohelp.zerott.quartz.info;

import java.util.Date;

public class JobState {
	private String jobName;
	private String schedulerName;
	private Date lastStartTime;
	private Date lastEndTime;
	private long threadId; //线程ID
	private long processId; //进程ID

	public JobState(String scheduler, String name, Date start, long tid, long pid){
		this.schedulerName = scheduler;
		this.jobName = name;
		this.lastStartTime = start;
		this.lastEndTime = null;
		this.threadId = tid;
		this.processId = pid;
	}
	/**
	 * 返回时间程序运行时间，单位ms
	 *
	 * @return
	 */
	long runTime(){
		if(this.lastEndTime == null){
			return new Date().getTime() - this.lastStartTime.getTime();
		}else{
			return this.lastEndTime.getTime() - this.lastStartTime.getTime();
		}
	}

	boolean isWorking(){
		return lastEndTime == null;
	}

	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public long getThreadId() {
		return threadId;
	}
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	public long getProcessId() {
		return processId;
	}
	public void setProcessId(long processId) {
		this.processId = processId;
	}
	public Date getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	public Date getLastEndTime() {
		return lastEndTime;
	}
	public void setLastEndTime(Date lastEndTime) {
		this.lastEndTime = lastEndTime;
	}
}
