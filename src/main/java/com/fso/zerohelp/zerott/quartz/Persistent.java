package com.fso.zerohelp.zerott.quartz;

import java.util.List;


public interface Persistent<T>{
	public List<T> readConfig();
	public boolean writeConfig(List<T> configList);
	public boolean writeConfig(String name, List<T> configList);	
}