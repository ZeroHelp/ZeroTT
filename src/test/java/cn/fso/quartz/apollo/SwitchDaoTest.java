package cn.fso.quartz.apollo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.fso.zerohelp.zerott.quartz.Switch;
import com.fso.zerohelp.zerott.quartz.dao.SwitchDAO;
import com.fso.zerohelp.zerott.quartz.dao.impl.SwitchDAOImpl;
import com.fso.zerohelp.zerott.quartz.enumeration.SwitchStatus;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/lottery-spring.xml" })
public class SwitchDaoTest  extends org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests {
	private SwitchDAO dao;
	private SwitchDAO getDao() {
		if(this.dao == null){
			try {
				DataSource dataSource = this.applicationContext.getBean("quartzDataSource", DataSource.class);
				SwitchDAOImpl localDao = new SwitchDAOImpl();
				localDao.setDataSource(dataSource);
				
				SqlMapClientFactoryBean sqlMapClientFactory = new SqlMapClientFactoryBean();
				sqlMapClientFactory.setConfigLocation(new ClassPathResource("com/taobao/nb/quartz/resources/switch-sqlmap.xml"));
				sqlMapClientFactory.setDataSource(dataSource);
				sqlMapClientFactory.afterPropertiesSet();
				
				localDao.setSqlMapClient((SqlMapClient)sqlMapClientFactory.getObject());
				
				localDao.setSqlMapClientTemplate(new SqlMapClientTemplate(localDao.getSqlMapClient()));
				this.dao = localDao;			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return this.dao;
	}
	
	private Switch getDo(){
		Switch sw = new Switch();
		sw.setId("SSQ_DJPH_SW");
		sw.setName("双色球大奖排行榜开关");
		sw.setStatus(SwitchStatus.ON.getStatus());		
		return sw;
	}
	
	@Test
	public void testInsert(){		
		Assert.isTrue(this.getDao().add(this.getDo()));
	}
	//@Test
	public void testUpdate(){
		Switch sw = this.getDo();
		sw.setName("不知道");
		if(sw.isOff()){
			sw.setStatus(SwitchStatus.ON.getStatus());
		}else{
			sw.setStatus(SwitchStatus.OFF.getStatus());
		}
		this.getDao().update(sw);
		
		Switch newSw = this.getDao().get(sw.getId());
		Assert.isTrue(newSw != null);
		Assert.isTrue(newSw.getName().equals("不知道"));
	}
	//@Test
	public void testDelete(){
		Assert.isTrue(this.getDao().delete(this.getDo().getId()));
	}
	
	//@Test
	public void testListSwitchesSince(){
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			List<Switch> switchList = this.getDao().getSince(dateFmt.parse("2011-01-24"));
			Assert.isTrue(switchList != null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
