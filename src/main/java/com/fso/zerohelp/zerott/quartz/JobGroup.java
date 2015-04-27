package com.fso.zerohelp.zerott.quartz;

import java.util.LinkedList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobGroup implements QuartzJob{
	private List<QuartzJob> jobs = new LinkedList<QuartzJob>();
	private String jobName;


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<QuartzJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<QuartzJob> jobs) {
		for(QuartzJob j : jobs){
			jobs.add(j);
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	}

	@Override
	public String getJobName() {
		return this.jobName;
	}

	@Override
	public void run(JobExecutionContext context) {

	}

}
