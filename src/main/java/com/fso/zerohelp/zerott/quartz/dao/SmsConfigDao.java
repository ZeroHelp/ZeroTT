package com.fso.zerohelp.zerott.quartz.dao;

import java.util.List;

import com.fso.zerohelp.zerott.quartz.info.SmsConfig;
import com.fso.zerohelp.zerott.quartz.info.SmsSend;

public interface SmsConfigDao {

	public List<SmsSend> findByCategory(String category);
	public SmsConfig findByConfigId(Integer id);
	public void update(SmsSend smsSend);
}
