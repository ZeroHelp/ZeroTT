package com.fso.zerohelp.zerott.quartz;


/**
 * 如果实现了该接口，被调度对象可以在运行期取出context对象，内容是运行期信息
 * @author xuming
 *
 */
public interface ContextSupport {
	public void setContext(Context context);
	public Context getContext();
}
