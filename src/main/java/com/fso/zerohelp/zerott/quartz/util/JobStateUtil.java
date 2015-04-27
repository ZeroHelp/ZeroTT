package com.fso.zerohelp.zerott.quartz.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.fso.zerohelp.zerott.quartz.info.JobState;

public class JobStateUtil {
	public static JSONObject toJsonObject(JobState s) throws Exception{
		JSONObject j = new JSONObject();
		j.put("scheduler", s.getSchedulerName());
		j.put("name", s.getJobName());
		j.put("pid", s.getProcessId());
		j.put("tid", s.getThreadId());

		Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if(s.getLastStartTime() != null) j.put("start", format.format(s.getLastStartTime()));
		if(s.getLastEndTime() != null) j.put("end", format.format(s.getLastEndTime()));
		return j;
	}
	public static JobState toJobState(JSONObject o) throws Exception{
		Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		JobState s = new JobState(
				o.getString("scheduler"),
				o.getString("name"),
				(Date)format.parseObject(o.getString("start")),
				o.getLong("tid"),
				o.getLong("pid")
				);
		if(o.has("end")){
			String endTime = o.getString("end");
			if(endTime != null){
				s.setLastEndTime((Date)format.parseObject(o.getString("end")));
			}
		}
		return s;
	}
}
