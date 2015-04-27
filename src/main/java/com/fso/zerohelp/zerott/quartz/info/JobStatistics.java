package com.fso.zerohelp.zerott.quartz.info;


public  class JobStatistics{
	private String name;
	private long runTimes;
	private long avgRunTime;
	private long lastRunTime;
	private JobState lastJobState;

	public JobState getLastJobState() {
		return lastJobState;
	}
	public void setLastJobState(JobState lastJobState) {
		this.lastJobState = lastJobState;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getRunTimes() {
		return runTimes;
	}
	public void setRunTimes(long runTimes) {
		this.runTimes = runTimes;
	}
	public long getAvgRunTime() {
		return avgRunTime;
	}
	public void setAvgRunTime(long avgRunTime) {
		this.avgRunTime = avgRunTime;
	}
	public long getLastRunTime() {
		return lastRunTime;
	}
	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
}
