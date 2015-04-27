package com.fso.zerohelp.zerott.quartz;

import java.io.Serializable;
import java.util.Date;

public class TimetaskConfig implements Serializable{
	private int id; //主键
	private String name; //时间任务描述
	private String taskId; //时间任务名称
	private int jobType; //时间程序类型
	private String cronExpression; //时间程序周期表达式
	private String targetIp; //允许运行该程序的服务器IP
	private String magic; //该时间任务对应magic参数
	private int status; //该时间程序当前状态 1:启动  2:停止
	private String lastTargetIp; //最后一次运行该程序的服务器IP
	private Date lastStartTime; //最后一次运行改程序的开始时间
	private Date lastEndTime; //最后一次运行该程序的结束时间
	private String category; //类别
	private String scheduler; //时间程序所在Scheduler Id
	private String appName; // 时间程序所属应用
	
	private Date gmtCreated; //当前记录插入时间
	private Date gmtModified; //当前记录修改时间
	/**
	 * 时间程序优先级：9-高危
	 */
	private int priority;

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("LotteryQuartzConfigDO@(");
		sb.append(" id=" + id);
		sb.append(" taskid=" + taskId);
		sb.append(" name=" + name);
		sb.append(" jobType=" + jobType);
		sb.append(" targetIp=" + targetIp);
		sb.append(" magic=" + magic);
		sb.append(" status=" + status);
		sb.append(" lastTargetIp=" + lastTargetIp);
		sb.append(" lastStartTime=" + lastStartTime);
		sb.append(" lastEndTime=" + lastEndTime);
		sb.append(" appName=" + appName);
		sb.append(" gmtCreated=" + gmtCreated);
		sb.append(" gmtModified=" + gmtModified);
		sb.append(")");
		return sb.toString();
	}

	public String toXml(){
		StringBuffer txt = new StringBuffer();
		txt.append("<config");
		txt.append(" id=\"" + this.getId() + "\"");
		txt.append(" cronexp=\"" + this.getCronExpression() + "\"");
		txt.append(" jobType=\"" + this.getJobType() + "\"");
		txt.append(" name=\"" + this.getName() + "\"");
		txt.append(" targetIp=\"" + this.getTargetIp() + "\"");
		txt.append(" magic=\"" + this.getMagic() + "\"");
		txt.append(" status=\"" + this.getStatus() + "\"");
		txt.append(" category=\"" + this.getCategory() + "\"");
		txt.append("/>");
		return txt.toString();
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getJobType(){
		return this.jobType;
	}

	public String getTargetIp() {
		return targetIp;
	}
	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLastTargetIp() {
		return lastTargetIp;
	}
	public void setLastTargetIp(String lastTargetIp) {
		this.lastTargetIp = lastTargetIp;
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
	public Date getGmtCreated() {
		return gmtCreated;
	}
	public void setGmtCreated(Date gmtCreated) {
		this.gmtCreated = gmtCreated;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMagic() {
        if (magic == null) {
            magic = "";
        }
		return magic;
	}

	public void setMagic(String magic) {
		this.magic = magic;
	}

	public String getScheduler() {
		return scheduler;
	}

	public void setScheduler(String shceduler) {
		this.scheduler = shceduler;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getAppName() {
        return appName;
    }
	
	public void setAppName(String appName) {
        this.appName = appName;
    }
}