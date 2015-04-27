package com.fso.zerohelp.zerott.quartz.impl;

import org.jdom.DataConversionException;
import org.jdom.Element;

import com.fso.zerohelp.zerott.quartz.Switch;

public class SwitchPersistent extends AbstractPersistentImpl<Switch>{
	public SwitchPersistent(String prefix, String suffix, String dir, int backup){
		super(prefix, suffix, dir, backup);
	}
	
	@Override
	protected Switch parseElement(Element item) {
		try {
			Switch sw = new Switch();
			sw.setId(item.getAttribute("id").getValue());
			//sw.setName(item.getAttribute("name").getValue());
			sw.setStatus(item.getAttribute("status").getIntValue());
			return sw;
		} catch (DataConversionException e) {
			return null;
		}
	}

	@Override
	protected String toElement(Switch obj) {
		StringBuffer txt = new StringBuffer();
		txt.append("<config");
		txt.append(" id=\"" + obj.getId() + "\"");
		//txt.append(" name=\"" + obj.getName() + "\"");
		txt.append(" status=\"" + obj.getStatus() + "\"");
		txt.append("/>");
		return txt.toString();
	}

}
