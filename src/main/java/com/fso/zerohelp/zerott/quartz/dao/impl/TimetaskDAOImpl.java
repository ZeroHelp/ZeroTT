package com.fso.zerohelp.zerott.quartz.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.dao.TimetaskDAO;

public class TimetaskDAOImpl extends SqlMapClientDaoSupport implements TimetaskDAO{
	@Override
	public boolean addConfig(TimetaskConfig config){
		this.getSqlMapClientTemplate().insert("ApolloTimetaskControl.insertConfig", config);
		return true;
	}

	@Override
	public boolean deleteConfig(int id) {
		return this.getSqlMapClientTemplate().delete("ApolloTimetaskControl.deleteConfigByID", id) >= 0;
	}

	@Override
	public List<TimetaskConfig> getAllConfig() {
		return this.getConfigList(new HashMap<String,Object>());
	}

	@Override
    public List<TimetaskConfig> getAllConfigByAppName(String appName) {
        Map<String, Object> queryArgs = new HashMap<String, Object>();
        queryArgs.put("appName", appName);
        return this.getConfigList(queryArgs);
    }
	
	@Override
	public List<TimetaskConfig> getAllConfig(String scheduler) {
		Map<String, Object> queryArgs = new HashMap<String, Object>();
		queryArgs.put("scheduler", scheduler);
		return this.getConfigList(queryArgs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimetaskConfig> getConfigList(Map<String,Object> queryParams) {
		if(queryParams.get("status") != null && !(queryParams.get("status") instanceof List)){
			List<Object> statusList = new LinkedList<Object>();
			statusList.add(queryParams.get("status"));
			queryParams.put("status", statusList);
		}
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listAllQuartzConfig", queryParams);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllSchedulerNames() {
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listAllSchedulerNames");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimetaskConfig> getConfigByIp(String ip) {
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listConfigByIp", ip);
	}

	public TimetaskConfig getConfig(String taskId, String ip){
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskId", taskId);
		map.put("ip", ip);
		return (TimetaskConfig)this.getSqlMapClientTemplate().queryForObject("ApolloTimetaskControl.listConfigByTaskidIp", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimetaskConfig> getConfigSince(Date since) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("since", since);
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listConfigSince", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimetaskConfig> getConfigSince(String scheduler, Date since) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("since", since);
		map.put("scheduler", scheduler);
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listConfigSince", map);
	}

	@Override
	public boolean updateConfig(TimetaskConfig c) {
		return this.getSqlMapClientTemplate().update("ApolloTimetaskControl.updateConfig", c) >= 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimetaskConfig> getConfig(String taskId) {
		return this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.listConfigByTaskId", taskId);
	}

	@Override
	public List<TimetaskConfig> getCertainConfig(Map<String, Object> map) {
		/*
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("status", String.valueOf(status));
		map.put("category", category);
		*/
		return (List<TimetaskConfig>)this.getSqlMapClientTemplate().queryForList("ApolloTimetaskControl.getCertainTimetask", map);
	}


	@Override
	public boolean updateTimetaskStatus(String id, int toStatus) {
		Map<String, String> queryArgs = new HashMap<String, String>();
		queryArgs.put("id", id);
		queryArgs.put("status", String.valueOf(toStatus));
		int count= this.getSqlMapClientTemplate().update("ApolloTimetaskControl.updateTimetaskStatus", queryArgs);
		return count>0?true:false;
	}

	public int updateTimetaskLastStatus(final int id, String lastTargetIp, Date lastStartTime, Date lastEndTime){
		Map<String, Object> queryArgs = new HashMap<String, Object>();
		queryArgs.put("id", id);
		queryArgs.put("lastTargetIp", lastTargetIp);
		if(lastStartTime != null) queryArgs.put("lastStartTime", lastStartTime);
		if(lastEndTime != null) queryArgs.put("lastEndTime", lastEndTime);
		return this.getSqlMapClientTemplate().update("ApolloTimetaskControl.updateLastStatus", queryArgs);
	}
	/**
	 * ��ҳ��ѯʱʹ��
	 */
	@Override
	public int getTimetaskSumCount(Map<String, Object> map) {
		if(map.get("status") != null && !(map.get("status") instanceof List)){
			List<Object> statusList = new LinkedList<Object>();
			statusList.add(map.get("status"));
			map.put("status", statusList);
		}
		return (Integer) this.getSqlMapClientTemplate().queryForObject("ApolloTimetaskControl.getTimetaskSumCount", map);
	}

	@Override
	public TimetaskConfig getConfig(int id) {
		return (TimetaskConfig)this.getSqlMapClientTemplate().queryForObject("ApolloTimetaskControl.listConfigById", id);
	}

	@Override
	public TimetaskConfig getConfigForUpdate(int id) {
		return (TimetaskConfig)this.getSqlMapClientTemplate().queryForObject("ApolloTimetaskControl.listConfigByIdForUpdate", id);
	}

	@Override
	public boolean batchReplaceIP(String srcIP, String dstIP) {
		Map<String, Object> queryArgs = new HashMap<String, Object>();
		queryArgs.put("srcIP", srcIP);
		queryArgs.put("dstIP", dstIP);
		return this.getSqlMapClientTemplate().update("ApolloTimetaskControl.batchReplaceIP", queryArgs) > 0;
	}

	@Override
	public boolean batchReplaceIP(List<Integer> ids, String dstIP) {
		Map<String, Object> queryArgs = new HashMap<String, Object>();
		queryArgs.put("idlist", ids);
		queryArgs.put("dstIP", dstIP);
		return this.getSqlMapClientTemplate().update("ApolloTimetaskControl.batchReplaceIpByIdlist", queryArgs) > 0;
	}

	@Override
	public void switchHighRisk(Integer taskId, int priority) {
		Map<String, Object> queryArgs = new HashMap<String, Object>();
		queryArgs.put("taskId", taskId);
		queryArgs.put("priority", priority);
		this.getSqlMapClientTemplate().update("ApolloTimetaskControl.switchHighRisk", queryArgs);		
	}
}
