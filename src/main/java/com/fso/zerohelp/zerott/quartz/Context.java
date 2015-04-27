package com.fso.zerohelp.zerott.quartz;

public class Context {
	private boolean interrupted;

	public Context(){
		this.interrupted = false;
	}
	
	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}	
}
