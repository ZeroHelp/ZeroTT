package cn.fso.quartz.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.StatefulJob;
import org.quartz.impl.StdSchedulerFactory;

public class TestQuartz implements StatefulJob{
	public static void main(String args[]){
		
			
		
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			
			scheduler.start();
			
			JobDetail job = new JobDetail("job1", "group1", TestQuartz.class);
			CronTrigger cronTrigger = new CronTrigger();
			cronTrigger.setName("trigger1");
			cronTrigger.setCronExpression("*/5 * * * * ?");
			cronTrigger.setGroup("group1");
			scheduler.scheduleJob(job, cronTrigger);
			Thread.sleep(5000);
			scheduler.unscheduleJob(cronTrigger.getName(), cronTrigger.getGroup());
			//scheduler.deleteJob("trigger1", "group1");
			System.out.println("job unscheduled");
			//scheduler.standby();
			
			Thread.sleep(100000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	//@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("任务开始........");
		try {
			System.out.println("先睡会吧.....");
			Thread.sleep(1000);
			System.out.println("10s到了, 睡醒了.....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void specialMethod(){
		System.out.println("指定任务执行了........");
	}
}
