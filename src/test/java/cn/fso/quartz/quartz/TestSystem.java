package cn.fso.quartz.quartz;

import java.lang.management.ManagementFactory;
import java.util.Date;

public class TestSystem {
	public static void getPid(){
		System.out.println("pid=" +
				Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testMS(){
		Date start = new Date();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date end = new Date();
		System.out.println("Time elapsed: " + (end.getTime() - start.getTime()));
	}

	public static void main(String args[]){
		TestSystem.testMS();
	}
}
