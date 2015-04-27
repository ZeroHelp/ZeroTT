package com.fso.zerohelp.zerott.quartz.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fso.zerohelp.zerott.quartz.Switch;

public interface SwitchDAO {
	public List<Switch> getAll();
	public List<Switch> getSince(Date since);
	public Switch get(String id);
	public boolean add(Switch c);
	public boolean update(Switch c);
	public boolean delete(String s);
	public List<Switch> getSwitchPage(Map map);
	public int getSwitchCount(Map map);
}
