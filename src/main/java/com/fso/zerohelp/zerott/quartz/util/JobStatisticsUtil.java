package com.fso.zerohelp.zerott.quartz.util;

import org.json.JSONObject;

import com.fso.zerohelp.zerott.quartz.info.JobStatistics;

public class JobStatisticsUtil {
	public static JSONObject toJsonObject(JobStatistics s) throws Exception{
		JSONObject j = new JSONObject();
		j.put("name", s.getName());
		j.put("runTimes", s.getRunTimes());
		j.put("avgRunTime", s.getAvgRunTime());
		j.put("lastRunTime", s.getLastRunTime());
		j.put("lastJobState", JobStateUtil.toJsonObject(s.getLastJobState()));
		return j;
	}
	public static JobStatistics toJobStatistics(JSONObject o) throws Exception{
		JobStatistics s = new JobStatistics();
		s.setName(o.getString("name"));
		s.setRunTimes(o.getLong("runTimes"));
		s.setAvgRunTime(o.getLong("avgRunTime"));
		s.setLastRunTime(o.getLong("lastRunTime"));
		s.setLastJobState(JobStateUtil.toJobState((JSONObject)o.get("lastJobState")));
		return s;
	}
}
