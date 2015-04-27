package com.fso.zerohelp.zerott.quartz.info;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.util.Assert;

public class JobTracker {
	static class JobStateTracker{
		private Queue<JobState> queue;
		private JobState last; //最后一个push的对象
		private static final int MAX_SIZE = 10;
		private int size = 0;
		private long runTimes;

		public long getRunTimes() {
			return runTimes;
		}

		JobStateTracker(){
			queue = new ConcurrentLinkedQueue<JobState>();
		}

		void push(JobState s){
			Assert.notNull(s);
			if(size < MAX_SIZE){
				queue.add(s);
				size++;
			}else{
				queue.poll();
				queue.add(s);
			}
			last = s;
		}

		public void incRuntimes(){
			runTimes++;
		}

		JobState lastState(){
			return last;
		}

		public JobStatistics getStatistics(){
			if(this.lastState() == null) return null;

			JobStatistics st = new JobStatistics();
			Iterator<JobState> sts = queue.iterator();
			//计算平均运行时间
			long totalTime = 0;
			//实际运行次数
			int totalTimes = 0;
			while(sts.hasNext()){
				totalTime += sts.next().runTime();
				totalTimes++;
			}
			st.setName(this.last.getJobName());
			st.setRunTimes(this.runTimes);
			st.setAvgRunTime(totalTime/totalTimes);
			st.setLastRunTime(this.last.runTime());
			st.setLastJobState(this.last);
			return st;
		}
	}

	private static ConcurrentHashMap<String, JobStateTracker> holder = new ConcurrentHashMap<String, JobStateTracker>();

	public static void collect(JobState s){
		JobStateTracker tracker = holder.get(s.getJobName());
		if(tracker == null){
			tracker = new JobTracker.JobStateTracker();
			tracker.push(s);
			holder.put(s.getJobName(), tracker);
		}else{
			tracker.push(s);
		}
	}

	public static void finish(JobState s){
		JobStateTracker tracker = holder.get(s.getJobName());
		if(tracker != null){
			tracker.incRuntimes();
		}
	}
	/**
	 * 列出所有正在工作的Job信息
	 *
	 * @return
	 */
	public static List<JobState> listWorkingJobs(){
		Collection<JobStateTracker> states = holder.values();
		Iterator<JobStateTracker> iter = states.iterator();
		List<JobState> workingStates = new LinkedList<JobState>();
		while(iter.hasNext()){
			JobStateTracker tracker = iter.next();
			JobState state = tracker.lastState();
			if(state != null && state.isWorking()){
				workingStates.add(state);
			}
		}
		return workingStates;
	}
	/**
	 * 列出所有曾经运行过的工作信息，包括正在工作的
	 * @return
	 */
	public static List<JobStatistics> listFinishedJobs(){
		Collection<JobStateTracker> states = holder.values();
		Iterator<JobStateTracker> iter = states.iterator();
		List<JobStatistics> sts = new LinkedList<JobStatistics>();
		while(iter.hasNext()){
			JobStatistics s = iter.next().getStatistics();
			if(s != null) sts.add(s);
		}
		return sts;
	}
}
