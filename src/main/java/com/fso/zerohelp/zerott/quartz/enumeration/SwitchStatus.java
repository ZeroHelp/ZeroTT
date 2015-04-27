package com.fso.zerohelp.zerott.quartz.enumeration;

public enum SwitchStatus {
	ON("打开", 1),
	OFF("关闭", 0);
	
	private final String desc;
	private final int status;
	
	SwitchStatus(String desc, int status){
		this.desc = desc;
		this.status = status;
	}

	public String getDesc(){
		return desc;
	}
	
	public int getStatus(){
		return status;
	}
}
