package com.fso.zerohelp.zerott.quartz.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

	public static String executeTime () {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
}
