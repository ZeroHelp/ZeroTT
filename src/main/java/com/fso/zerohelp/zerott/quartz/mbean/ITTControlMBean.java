package com.fso.zerohelp.zerott.quartz.mbean;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fso.zerohelp.zerott.quartz.info.JobState;
import com.fso.zerohelp.zerott.quartz.info.JobStatistics;
import com.fso.zerohelp.zerott.quartz.info.JobTracker;
import com.fso.zerohelp.zerott.quartz.util.JobStateUtil;
import com.fso.zerohelp.zerott.quartz.util.JobStatisticsUtil;
import com.fso.zerohelp.zerott.quartz.util.OsUtil;

public class ITTControlMBean implements ITTControl{
    private static final Logger logger = LoggerFactory.getLogger(ITTControlMBean.class);
	public String getTTControlInfo(int type, int tid){
		try{
	    	JSONObject response = new JSONObject();
	    	switch(type){
	    		case 0:
	    			int i = 0;
		    		for(JobState s : JobTracker.listWorkingJobs()){
		    			i++;
		    			response.put(String.valueOf(i), JobStateUtil.toJsonObject(s));
		    		}
	    	    	break;
	    		case 1:
	    			if(tid > 0){
	    				StackTraceElement[] threadStackTrace = OsUtil.getThreadStackTrace(tid);
	    				if(threadStackTrace != null){
	    					int j = 0;
	    					for(StackTraceElement e : threadStackTrace){
	    						response.put(String.valueOf(j++), e.toString());
	    					}
	    					response.put("totalNum", j); //标识有多少行记录
	    				}
	    			}
	    			break;
	    		case 2:
	    			List<JobStatistics> allJobs = JobTracker.listFinishedJobs();
	    			int k = 0;
	    			for(JobStatistics s : allJobs){
						response.put(String.valueOf(k++), JobStatisticsUtil.toJsonObject(s));
					}
	    	}
	    	return response.toString();
		}catch(Exception e){
		    logger.error(e.getMessage(), e);
		}
		return "";
	}
}