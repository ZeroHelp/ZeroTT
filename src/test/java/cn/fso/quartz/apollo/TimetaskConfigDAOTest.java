package cn.fso.quartz.apollo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.fso.zerohelp.zerott.quartz.TimetaskConfig;
import com.fso.zerohelp.zerott.quartz.dao.TimetaskDAO;
import com.fso.zerohelp.zerott.quartz.enumeration.QuartzJobTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/lottery-spring.xml" })
public class TimetaskConfigDAOTest  extends org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests {
	@Autowired
	private TimetaskDAO timetaskDAO;

	private TimetaskConfig singleConfig;

	@Before
	public void init(){
		this.singleConfig = this.getDo();
		System.out.println("Junit Test initialized");
	}

	private TimetaskConfig getDo(){
		TimetaskConfig config = new TimetaskConfig();
		config.setCategory("");
		config.setCronExpression("*/1 * * * * ?");
		config.setTaskId("TESTEST");
		config.setJobType(QuartzJobTypeEnum.AGENTTYPE.getType());
		config.setLastEndTime(new Date());
		config.setLastStartTime(new Date());
		config.setMagic("magic");
		config.setName("InterruptableJob");
		config.setScheduler("main");
		config.setStatus(1);
		config.setTargetIp("10.32.20.47");

		return config;
	}

	@Test
	public void testInsert(){
		Assert.isTrue(timetaskDAO.addConfig(this.getDo()));
	}

	@Test
	public void testGetAllConfig(){
		Assert.isTrue(timetaskDAO.getAllConfig() != null);
		for(TimetaskConfig config : timetaskDAO.getAllConfig()){
			System.out.println(config.getTaskId());
		}
		for(TimetaskConfig config : timetaskDAO.getAllConfig("main")){
			System.out.println(config.getTaskId());
		}
	}

	@Test
	public void testGetConfigSince(){
		Assert.isTrue(timetaskDAO.getConfigSince(new Date()) != null);
		Assert.isTrue(timetaskDAO.getConfigSince("main", new Date()) != null);
	}
	//@Test
	public void testUpdateNormalColumn(){
		String updateCategory = "updated categeory";

		TimetaskConfig configNew = timetaskDAO.getConfig(singleConfig.getTaskId(), singleConfig.getTargetIp());

		configNew.setCategory(updateCategory);

		timetaskDAO.updateConfig(singleConfig);

		Assert.isTrue(configNew.getCategory().equals(updateCategory));
	}

	//@Test
	public void testUpdateTargetIpColumn(){
		String destIP = "127.0.0.1";
		String destName = "UPDATE TARGET IP COLUMN";

		TimetaskConfig config1 = timetaskDAO.getConfig(singleConfig.getTaskId(), singleConfig.getTargetIp());

		config1.setTargetIp(destIP);
		config1.setName(destName);
		timetaskDAO.updateConfig(config1);

		TimetaskConfig config2 = timetaskDAO.getConfig(config1.getId());

		Assert.isTrue(config2.getTargetIp().equals(destIP));
		Assert.isTrue(config2.getName().equals(destName));

		config2.setTargetIp(singleConfig.getTargetIp());
		timetaskDAO.updateConfig(config2);
	}

	//@Test
	public void testGetAllSchedulerNames(){
		List<String> names = timetaskDAO.getAllSchedulerNames();
		for(String name : names){
			System.out.println("scheduler " + name + " exists");
			Assert.notNull(name);
		}

	}

	@Test
	public void testBatchReplaceIP(){
		List<TimetaskConfig> all = timetaskDAO.getAllConfig();
		List<Integer> ids = new LinkedList<Integer>();
		//将前三条记录更新成本地地址
		int i = 0;
		for(TimetaskConfig c : all){
			ids.add(c.getId());
			if((++i) > 3) break;
		}
		Assert.isTrue(timetaskDAO.batchReplaceIP(ids, "127.0.0.4"));
	}

	@Test
	public void testDelete(){
		TimetaskConfig config1 = timetaskDAO.getConfig(singleConfig.getTaskId(), singleConfig.getTargetIp());

		timetaskDAO.deleteConfig(config1.getId());

		TimetaskConfig configNew = timetaskDAO.getConfig(config1.getId());

		Assert.isTrue(configNew == null);
	}
}
