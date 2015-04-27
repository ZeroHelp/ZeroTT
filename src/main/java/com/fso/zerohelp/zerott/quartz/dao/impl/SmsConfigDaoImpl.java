package com.fso.zerohelp.zerott.quartz.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.fso.zerohelp.zerott.quartz.dao.SmsConfigDao;
import com.fso.zerohelp.zerott.quartz.info.SmsConfig;
import com.fso.zerohelp.zerott.quartz.info.SmsSend;

public class SmsConfigDaoImpl extends SqlMapClientDaoSupport implements SmsConfigDao {

	@SuppressWarnings("deprecation")
	@Override
	public List<SmsSend> findByCategory(String category) {
		return this.getSqlMapClientTemplate().queryForList("SmsConfigControl.findByCategory", category);
	}
	
	@Override
	public SmsConfig findByConfigId(Integer id) {
		return (SmsConfig) this.getSqlMapClientTemplate().queryForObject("SmsConfigControl.findByConfigId", id);
	}

	@Override
	public void update(SmsSend smsConfig) {
		this.getSqlMapClientTemplate().update("SmsConfigControl.update", smsConfig);
	}

	

}
