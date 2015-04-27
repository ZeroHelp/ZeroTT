package com.fso.zerohelp.zerott.quartz.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fso.zerohelp.zerott.quartz.Persistent;
import com.fso.zerohelp.zerott.quartz.Switch;
import com.fso.zerohelp.zerott.quartz.dao.SwitchDAO;

public class SwitchSynchronizerImpl extends AbastractSynchronizerImpl<String, Switch>{
	public static Logger logger = LoggerFactory.getLogger(SwitchSynchronizerImpl.class);

	private SwitchDAO switchDAO; //同步数据源
	private SwitchCache cache;

	public void setCache(SwitchCache cache) {
		this.cache = cache;
	}

	public void setSwitchDAO(SwitchDAO switchDAO) {
		this.switchDAO = switchDAO;
	}

	public String getName(){
		return "Switch";
	}

	/**
	 * 实际同步数据的方法，基本流程如下：
	 * 1. 根据lastSyncTime看是增量同步还是全量同步
	 * 2. 如果同步数据出现异常(例如数据库服务器无法连接)，则直接从本地缓存读取配置
	 * 3. 如果数据库配置有更新，逐个更新配置；更新过程中动态增删trigger
	 * 4. 更新成功，将最新的配置数据写入配置文件中
	 * @param context Quartz上下文
	 */
	public void sync(){
		List<Switch> switches = null;

		Persistent<Switch> persistentor = this.getPersistentor("switch");

		boolean readFromDb = false;
		try{
			if(localOnly){
				if(!localLoaded){
					switches = persistentor.readConfig(); //载本地缓存的配置
					if(switches != null && switches.size() != 0)
						localLoaded = true;
				}
			}else if(lastSyncTime == null){
				switches = switchDAO.getAll();
				readFromDb = true;
			}else{
				switches = switchDAO.getSince(lastSyncTime);
				readFromDb = true;
			}
		}catch(Exception e){
			logger.error("Exception on loading config:", e);
			if(!localLoaded){
				switches = persistentor.readConfig(); //载本地缓存的配置
				if(switches != null && switches.size() != 0)
					localLoaded = true;
			}
		}

		if(switches.size() == 0) return;
		for(Switch config : switches){
			cache.set(config.getId(), config);
			logger.debug("config = " + config.toString());

			//更新时间戳，作为下次同步配置的依据
			if(readFromDb){
				if(lastSyncTime == null){
					lastSyncTime = config.getGmtModified();
				}else if(lastSyncTime.compareTo(config.getGmtModified()) < 0){
					lastSyncTime = config.getGmtModified();
				}
			}
		}
		if(readFromDb){
    		List<Switch> configs = switchDAO.getAll();
    		persistentor.writeConfig(configs);
			logger.warn("lastSyncTime = " + lastSyncTime);
		}
	}

	@Override
	protected Persistent<Switch> newPersistentorInstance(String prefix,	String suffix, String dir, int backup) {
		return new SwitchPersistent(prefix, suffix, dir, backup);
	}
}
