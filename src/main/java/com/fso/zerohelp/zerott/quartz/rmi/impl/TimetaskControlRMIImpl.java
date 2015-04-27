package com.fso.zerohelp.zerott.quartz.rmi.impl;

import java.util.List;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.dao.TimetaskDAO;
import com.fso.zerohelp.zerott.quartz.rmi.ITimetaskControlRMI;

public class TimetaskControlRMIImpl implements ITimetaskControlRMI {
	
	public TimetaskDAO timetaskDAO;

	public void setTimetaskDAO(TimetaskDAO timetaskDAO) {
		this.timetaskDAO = timetaskDAO;
	}

	@Override
	public List<TimetaskConfig> getAllTimetaskConfig() {
		List<TimetaskConfig> list = timetaskDAO.getAllConfig();
		return list;
	}

	@Override
	public boolean updateTimetaskStatus(String id, int toStatus) {
		boolean flag = timetaskDAO.updateTimetaskStatus(id, toStatus);
		return flag;
	}

	@Override
	public boolean batchReplaceIP(List<Integer> ids, String dstIP) {
		boolean flag = timetaskDAO.batchReplaceIP(ids, dstIP);
		return flag;
	}

	@Override
	public boolean batchReplaceIP(String srcIP, String dstIP) {
		boolean flag = timetaskDAO.batchReplaceIP(srcIP, dstIP);
		return flag;
	}
	
	
}
