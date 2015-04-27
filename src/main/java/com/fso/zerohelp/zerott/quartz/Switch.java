package com.fso.zerohelp.zerott.quartz;

import java.util.Date;

import com.fso.zerohelp.zerott.quartz.enumeration.SwitchStatus;

public class Switch {
	private String id;
	private String name;
	private int status;
	private String category;
	
    private Date gmtCreated; //当前记录插入时间
    private Date gmtModified; //当前记录修改时间
    
	public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOff() {
		return status == SwitchStatus.OFF.getStatus();
	}
	public boolean isOn() {
		return status == SwitchStatus.ON.getStatus();
	}
	
}
