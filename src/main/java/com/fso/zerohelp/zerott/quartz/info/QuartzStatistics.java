package com.fso.zerohelp.zerott.quartz.info;

import java.util.Date;

public class QuartzStatistics {
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getTimes() {
		return times;
	}
	public void setTimes(Long times) {
		this.times = times;
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
	
	private Date startTime; //第一次运行时间
	private Date endTime; //最后一次运行时间
	private Long times; //总运行次数
	private Date lastStartTime; //最后一次运行开始时间
	private Date lastEndTime; //最后一次运行结束时间
	
	public QuartzStatistics(){
		times = 0L;
	}
}
