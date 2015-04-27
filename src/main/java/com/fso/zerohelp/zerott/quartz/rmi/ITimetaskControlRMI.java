package com.fso.zerohelp.zerott.quartz.rmi;

import java.util.List;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;

public interface ITimetaskControlRMI {
	
	/**
     * 一次获取所有时间程序配置
     * 
     * @return 所有时间程序配置列表
     */
	public List<TimetaskConfig> getAllTimetaskConfig();
	
	/**
     * 开关时间程序
     * 
     * @param id 要改变状态的时间程序ID，数据库主键
     * @param toStatus 改变为的状态 1:启动  2:停止
     * @return 成功为true，失败为false
     */
	public boolean updateTimetaskStatus(String id, int toStatus);
	
	/**
     * 批量切换IP，目的是批量将时间程序从一个机器转移到另一个机器
     * 
     * @param ids 要切换的时间程序ID列表，数据库主键
     * @param dstIP 目的IP
     * @return 成功为true，失败为false
     */
	public boolean batchReplaceIP(List<Integer> ids, String dstIP);
	 /**
     * 切换IP，目的是将时间程序从一个机器转移到另一个机器
     * 
     * @param srcIP 源IP
     * @param dstIP 目的IP
     * @return 成功为true, 失败为false
     */
	public boolean batchReplaceIP(String srcIP, String dstIP);
}
