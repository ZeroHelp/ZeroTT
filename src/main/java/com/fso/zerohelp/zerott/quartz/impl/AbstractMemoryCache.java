package com.fso.zerohelp.zerott.quartz.impl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fso.zerohelp.zerott.quartz.MemoryCache;

public class AbstractMemoryCache<M, T> implements MemoryCache<M, T>{
	private ConcurrentHashMap<M, T> map;
	
	public AbstractMemoryCache(){
		map = new ConcurrentHashMap<M, T>();
	}
	
	public Set<M> getKeyset(){
		return map.keySet();
	}
	
	@Override
	public String toString() {
		StringBuffer txt = new StringBuffer();
		for(M key : map.keySet()){
			T config = map.get(key);
			txt.append(config.toString() + "\n");
		}
		return txt.toString();
	}

	@Override
	public T get(M key){
		return map.get(key);
	}


	@Override
	public boolean set(M key, T value){
		map.put(key, value);
		return true;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void remove(M key) {
		map.remove(key);
	}
}
