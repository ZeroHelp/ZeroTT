package com.fso.zerohelp.zerott.quartz.demo;

import com.fso.zerohelp.zerott.quartz.Context;
import com.fso.zerohelp.zerott.quartz.ContextSupport;
import com.fso.zerohelp.zerott.quartz.Switch;
import com.fso.zerohelp.zerott.quartz.impl.SwitchCache;

public class HelloWorldManager implements ContextSupport{
	SwitchCache switchCache;

	String getThreadInfo(){
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		return Thread.currentThread().getName() + Thread.currentThread().getId();
	}

	public void setSwitchCache(SwitchCache switchCache) {
		this.switchCache = switchCache;
	}

	public void sayHello(){
		System.out.println("[" +  this.getThreadInfo() + "] Hello......");
	}

	public void singletonJob(){
		try {
			int i = 0;
			while(i++ < 20){
				Thread.sleep(1000);
				System.out.println("[" +  this.getThreadInfo() + "] " + i + "]There will be only one instance of this timetask");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void interruptableJob(){
		try {
			while(true){
				Thread.sleep(1000);
				if(quartzCtx != null){
					if(quartzCtx.isInterrupted()){
						System.out.println("[" +  this.getThreadInfo() + "] Job is interrupted");
						break;
					}
				}
				System.out.println("[" +  this.getThreadInfo() + "] Interruptable job runned");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void checkSwitch(String switchId){
		Switch sw = switchCache.get(switchId);
		if(sw != null){
			if(sw.isOn()){
				System.out.print(switchId + " is On");
				return;
			}
		}else{
			System.out.print(switchId + " doesn't exist");
		}
		System.out.print(switchId + " is Off");
	}

	public void sleep10(String id){
		try {
			Thread.sleep(10000);
			System.out.println("[" +  this.getThreadInfo() + "] instance " + id + " executed once");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saySsqBook(String agentId, String lotteryTypeId){
		try {
		System.out.println("-----------saySsqBook-----------running");
		/*getThreadInfo();*/
		System.out.println("-----------[" +  this.getThreadInfo() + "] SSQ BOOK " + agentId + "," + lotteryTypeId + "] running");
		
		//Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void saySsqAward(String agentId, String lotteryTypeId){
		System.out.println("-----------[" +  this.getThreadInfo() + "] SSQ Award " + agentId + "," + lotteryTypeId + "] running");
	}


	Context quartzCtx;
	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub
		quartzCtx = context;
	}


	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return quartzCtx;
	}
}
