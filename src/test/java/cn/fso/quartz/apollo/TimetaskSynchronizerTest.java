package cn.fso.quartz.apollo;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fso.zerohelp.zerott.quartz.info.JobState;
import com.fso.zerohelp.zerott.quartz.info.JobStatistics;
import com.fso.zerohelp.zerott.quartz.info.JobTracker;
import com.fso.zerohelp.zerott.quartz.rmi.ITimetaskControlRMI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/apollo-spring.xml" })
public class TimetaskSynchronizerTest extends org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests {
	@Test
	public void testScheduler(){
		/*this.applicationContext.getBean("quartzTimetaskSynchronizer");
		this.applicationContext.getBean("mainScheduler");
		
		ITimetaskControlRMI timetaskControlRMI = (ITimetaskControlRMI) applicationContext.getBean("timetaskControlRMIProxy");
		System.out.println(timetaskControlRMI.getAllTimetaskConfig());*/
		
		try {
			while(true){
				Thread.sleep(1000);
				
				System.out.println("Probing running jobs below:");
				System.out.println("==============working start===============");
				List<JobState> jobs = JobTracker.listWorkingJobs();//列出所有正在工作的Job信息
				Iterator<JobState> i = jobs.iterator();
				while(i.hasNext()){
					JobState n = i.next();
					System.out.println("MONITOR:" + n.getJobName() + "[" + n.getLastStartTime() + ", " + n.getLastEndTime() + "]");
				}
				System.out.println("==================working end================");
				
				System.out.println("==============finished start===========");
				List<JobStatistics> statistics = JobTracker.listFinishedJobs();//列出所有曾经运行过的工作信息，包括正在工作的
				Iterator<JobStatistics> s = statistics.iterator();
				while(s.hasNext()){
					JobStatistics n = s.next();
					System.out.println("MONITOR:" + n.getName() + "[" + n.getLastJobState().getLastStartTime() + ", " + n.getLastJobState().getLastEndTime() + "]");
					/*System.out.println(timetaskControlRMI.updateTimetaskStatus("2", 0));*/
				}
				System.out.println("================finished end==================");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void stopScheduler(){
		/*this.applicationContext.getBean("quartzTimetaskSynchronizer");
		this.applicationContext.getBean("mainScheduler");*/
		
		ITimetaskControlRMI timetaskControlRMI = (ITimetaskControlRMI) applicationContext.getBean("timetaskControlRMIProxy");
		/*System.out.println(timetaskControlRMI.getAllTimetaskConfig());*/
		/*System.out.println(timetaskControlRMI.updateTimetaskStatus("2", 0));*/
		timetaskControlRMI.updateTimetaskStatus("1", 0);//时间程序状态 0:永久关闭 1:可运行 2:不可运行
	}
}
