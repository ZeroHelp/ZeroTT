package com.fso.zerohelp.zerott.quartz.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;

public interface TimetaskDAO {
    /**
     * 一次获取所有时间程序配置
     * 
     * @return 所有时间程序配置列表
     */
    public List<TimetaskConfig> getAllConfig();

    public List<TimetaskConfig> getAllConfig(String scheduler);

    /**
     * 获取在指定时间since之后gmt_modified字段被更新过的配置
     * 
     * @param since 起始时间
     * @return 所有在since之后被更新的配置；如果没有返回空集；
     */
    public List<TimetaskConfig> getConfigSince(Date since);

    public List<TimetaskConfig> getConfigSince(String scheduler, Date since);

    /**
     * 获取指定IP可以运行的时间程序列表
     * 
     * @param ip IP地址，格式为 "xxx.xxx.xxx.xxx"
     * @return 所有在ip上面可以运行的时间程序配置列表
     */
    public List<TimetaskConfig> getConfigByIp(String ip);

    public TimetaskConfig getConfig(String taskId, String ip);

    /**
     * 根据条件筛选指定的时间程序配置
     * 
     * @return 符合筛选条件的配置
     */
    public List<TimetaskConfig> getConfig(String taskId);

    public TimetaskConfig getConfig(int id);

    public TimetaskConfig getConfigForUpdate(int id);

    public List<TimetaskConfig> getConfigList(Map<String, Object> queryParams);

    /**
     * 获取所有scheduler类型的列表
     * 
     * @return list类型的胡scheduler类型列表
     */
    public List<String> getAllSchedulerNames();

    /**
     * 添加配置
     * 
     * @param c 配置对象
     * @return 添加成功为true，添加失败为false
     */
    public boolean addConfig(TimetaskConfig c);

    public boolean updateConfig(TimetaskConfig c);

    public boolean deleteConfig(int id);

    /**
     * 批量切换IP，目的是批量将时间程序从一个机器转移到另一个机器
     * 
     * @param srcIP 源IP
     * @param dstIP 目的IP
     * @return 成功为true, 失败为false
     */
    public boolean batchReplaceIP(String srcIP, String dstIP);

    /**
     * 批量切换IP，目的是批量将时间程序从一个机器转移到另一个机器
     * 
     * @param ids 要切换的时间程序ID列表，数据库主键
     * @param dstIP 目的IP
     * @return 成功为true，失败为false
     */
    public boolean batchReplaceIP(List<Integer> ids, String dstIP);

    /**
     * 根据ip，status，category的组合进行查询
     * 
     * @param map
     * @return
     */
    public List<TimetaskConfig> getCertainConfig(Map<String, Object> map);

    public boolean updateTimetaskStatus(String id, int toStatus);

    // 更新last_target_ip, last_start_time, last_end_time字段内容
    public int updateTimetaskLastStatus(final int id, String lastTargetIp, Date lastStartTime, Date lastEndTime);

    public int getTimetaskSumCount(Map<String, Object> map);

    public void switchHighRisk(Integer valueOf, int priority);

    /**
     * 根据应用名查询时间程序
     * 
     * @param appName 应用名
     * @return List<TimetaskConfig>
     */
    List<TimetaskConfig> getAllConfigByAppName(String appName);
}
