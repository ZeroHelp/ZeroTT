package cn.fso.quartz.apollo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fso.zerohelp.zerott.quartz.MemoryCache;
import com.fso.zerohelp.zerott.quartz.Switch;
import com.fso.zerohelp.zerott.quartz.Synchronizer;
//commit gengxin
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/lottery-spring.xml" })
public class SwitchSynchronizerTest extends org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests {
	@Test
	public void testScheduler(){		
		Synchronizer synchronizer = (Synchronizer)this.applicationContext.getBean("switchSynchronizer");
		MemoryCache<String, Switch> cache = (MemoryCache<String, Switch>)this.applicationContext.getBean("switchCache");
		try {
			while(true){
				Thread.sleep(1000);
				Switch sw = cache.get("ZJHM_sw");
				if(sw == null){
					System.out.println("未找到switch");
				}else{
					System.out.println("开关" + sw.getName() + "状态是：" + sw.getStatus());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
