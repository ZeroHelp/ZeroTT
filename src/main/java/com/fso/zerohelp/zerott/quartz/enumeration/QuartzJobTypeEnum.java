package com.fso.zerohelp.zerott.quartz.enumeration;

public enum QuartzJobTypeEnum {
	AGENTTYPE("与agent和lotteryType相关", 1),
	COMMON("常规时间程序", 2),
	TURNOFF("常规开关", 3),
	DBJOB("基于数据库的任务实例", 4);
	
	private final String desc;
	private final int type;
	
	QuartzJobTypeEnum(String desc, int type){
		this.desc = desc;
		this.type = type;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public int getType(){
		return type;
	}
}
