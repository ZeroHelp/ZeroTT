package com.fso.zerohelp.zerott.quartz.impl;

import org.jdom.DataConversionException;
import org.jdom.Element;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;

public class TimetaskConfigPersistent extends AbstractPersistentImpl<TimetaskConfig>{
	public TimetaskConfigPersistent(String prefix, String suffix, String dir, int backup){
		super(prefix, suffix, dir, backup);
	}

	@Override
	protected TimetaskConfig parseElement(Element item) {
		try {
			TimetaskConfig config = new TimetaskConfig();
			config.setId(item.getAttribute("id").getIntValue());
			config.setTaskId(item.getAttribute("taskid").getValue());
			config.setCategory(item.getAttribute("category").getValue());
			config.setCronExpression(item.getAttribute("cronexp").getValue());
			config.setJobType(item.getAttribute("jobType").getIntValue());
			//config.setName(item.getAttribute("name").getValue());
			config.setTargetIp(item.getAttribute("targetIp").getValue());
			config.setMagic(item.getAttribute("magic").getValue());
			config.setStatus(item.getAttribute("status").getIntValue());
			config.setScheduler(item.getAttribute("scheduler").getValue());
			return config;
		} catch (DataConversionException e) {
			return null;
		}
	}

	@Override
	protected String toElement(TimetaskConfig obj) {
		StringBuffer txt = new StringBuffer();
		txt.append("<config");
		txt.append(" id=\"" + obj.getId() + "\"");
		txt.append(" taskid=\"" + obj.getTaskId() + "\"");
		txt.append(" cronexp=\"" + obj.getCronExpression() + "\"");
		txt.append(" jobType=\"" + obj.getJobType() + "\"");
		txt.append(" targetIp=\"" + obj.getTargetIp() + "\"");
		txt.append(" magic=\"" + (obj.getMagic() == null ? "" : obj.getMagic().replaceAll("\"", "'")) + "\"");
		txt.append(" status=\"" + obj.getStatus() + "\"");
		txt.append(" category=\"" + obj.getCategory() + "\"");
		txt.append(" scheduler=\"" + obj.getScheduler() + "\"");
		txt.append("/>");
		return txt.toString();
	}
}
