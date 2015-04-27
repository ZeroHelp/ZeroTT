package com.fso.zerohelp.zerott.quartz.util;

import java.lang.management.ManagementFactory;
import java.util.Map;

public class OsUtil {
	//获取当前应用的进程ID
	public static long currentPid(){
		return Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
	}
	//获取当前应用线程stacktrace
	public static StackTraceElement[] getThreadStackTrace(long threadId){
		Map<Thread,StackTraceElement[]> st = Thread.getAllStackTraces();
		for(Map.Entry<Thread, StackTraceElement[]> e: st.entrySet()){
			Thread t = e.getKey();
			if(t.getId() == threadId){
				return e.getValue();
			}
		}
		return null;
	}
}
