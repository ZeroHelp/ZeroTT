package com.fso.zerohelp.zerott.quartz.enumeration;

public enum QuartzJobStatusEnum {
	RUN("可以运行", 1),
	STOP("停止运行", 0),
	FORBIDDEN("临时删除", 2);

	private final String desc;
	private final int status;

	QuartzJobStatusEnum(String desc, int status){
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
