package com.fso.zerohelp.zerott.quartz;

import java.util.Set;

public interface MemoryCache<M, T> {
	public T get(M key);
	public boolean set(M key, T value);
	public String toString();
	public Set<M> getKeyset();
	public void remove(M key);
	public void clear();
}
