package com.fso.zerohelp.zerott.quartz;

public interface Synchronizer {
	public String getName();
	public void sync();
	public void start();
	public void stop();
}
