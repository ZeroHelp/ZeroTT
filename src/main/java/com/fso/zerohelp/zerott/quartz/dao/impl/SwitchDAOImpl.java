package com.fso.zerohelp.zerott.quartz.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.fso.zerohelp.zerott.quartz.Switch;
import com.fso.zerohelp.zerott.quartz.dao.SwitchDAO;

public class SwitchDAOImpl extends SqlMapClientDaoSupport implements SwitchDAO{

	@Override
	public boolean add(Switch s) {
		this.getSqlMapClientTemplate().insert("ApolloSwitch.insertSwitch", s);
		return true;
	}

	@Override
	public boolean delete(String s) {
		return this.getSqlMapClientTemplate().delete("ApolloSwitch.deleteSwitch", s) >= 0;
	}

	@Override
	public Switch get(String id) {
		return (Switch)this.getSqlMapClientTemplate().queryForObject("ApolloSwitch.getSwitch", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Switch> getAll() {
		return this.getSqlMapClientTemplate().queryForList("ApolloSwitch.listAllSwitches");
	}

	@Override
	public boolean update(Switch c) {
		return this.getSqlMapClientTemplate().update("ApolloSwitch.updateSwitch", c) >= 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Switch> getSince(Date since) {
		return this.getSqlMapClientTemplate().queryForList("ApolloSwitch.listSwitchesSince", since);
	}

    @Override
    public int getSwitchCount(Map map) {
        return (Integer) this.getSqlMapClientTemplate().queryForObject("ApolloSwitch.getSwitchesCount",map);
    }

    @Override
    public List<Switch> getSwitchPage(Map map) {
        return this.getSqlMapClientTemplate().queryForList("ApolloSwitch.getSwitchesPage",map);
    }

}
