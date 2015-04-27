package com.fso.zerohelp.zerott.quartz.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fso.zerohelp.zerott.quartz.Persistent;

public abstract class AbstractPersistentImpl<T> implements Persistent<T>{
	public static Logger logger = LoggerFactory.getLogger(AbstractPersistentImpl.class);

	private String prefix;
	private String suffix;
	private String dir;
	private int backup;

	public AbstractPersistentImpl(String prefix, String suffix, String dir, int backup){
		this.prefix = prefix;
		this.suffix = suffix;
		this.dir = dir;
		this.backup = backup;
	}

	private String lastSyncFile;  //如果从配置文件加载配置，最后一次加载的文件名是什么
	private long lastSyncFileModified;  //如果从配置文件加载配置，最后一次加载的文件名的修改时间

	class FileComparator implements Comparator<File>{

		@Override
		public int compare(File f1, File f2) {
			if(f1.lastModified() > f2.lastModified()){
				return -1;
			}else if(f1.lastModified() < f2.lastModified()){
				return 1;
			}else{
				return 0;
			}
		}
	}

	/**
	 * 获取指定目录下按照最后修改时间排序的文件列表
	 * @param path 指定的目录
	 * @return
	 */
	public File[] getSortedFileList(String path){
		FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.startsWith(prefix) && name.endsWith(suffix);
			}
		};
		File dir = new File(path);
		if(!dir.isDirectory()){
			logger.error("文件" + path + "不是有效的目录");
			return null;
		}
		String[] children = dir.list(filter);
		if(children == null){
			return null;
		}else{
			File[] fileList = new File[children.length];
			for(int i =0; i < children.length; i++){
				String absPath = this.dir.endsWith("/") ? (this.dir + children[i]) : (this.dir + "/" + children[i]);
				fileList[i] = new File(absPath);
			}
			Arrays.sort(fileList, new FileComparator());
			return fileList;
		}
	}

	public File getLatestFile(){
		//File[] filelist = this.getSortedFileList(dir);
		//f(filelist != null && filelist.length != 0){
		//	return filelist[0];
		//}else{
		//	return null;
		//}
		return new File(dir + "/" + prefix + suffix);
	}

	/**
	 * 逐个读取现有指定目录下所有文件，并按照修改顺序处理之；如果最新的文件能够正常读取，并顺利解析，则后面
	 * 其他的文件将不会读取，直接返回；解析出的配置信息将保存在lotteryCache中
	 * @param cache 保存的目标cache
	 * @return 成功处理则返回读取的文件File对象，处理失败则返回null
	 */
	private List<T> cachedConfigList;


	protected abstract T parseElement(Element item);
	protected abstract String toElement(T obj);

	public List<T> readConfig(){
		List<T> configList = new LinkedList<T>();

		File latestFile = this.getLatestFile();
		if(latestFile != null){
			if(!latestFile.getName().equals(this.lastSyncFile) || latestFile.lastModified() != this.lastSyncFileModified){
				File[] filelist = getSortedFileList(dir);
				if(filelist == null){
					logger.warn("Config doesn't exist");
					return configList;
				}

				for (File f : filelist) {
					try {
						SAXBuilder builder = new SAXBuilder();
						Document xml = builder.build(f.getAbsoluteFile());
						Element root = xml.getRootElement();
						List<Element> namelist = (List<Element>) root.getChildren("config");
						for (Element item : namelist) {
							T config = this.parseElement(item);
							configList.add(config);
							logger.info("Config from file " + f.getName() + ":" + config.toString());
						}
						break;
					} catch (Exception e) {
						configList.clear();
						logger.error("parse quartz config error", e);
						continue; // 读取错误，尝试读取下一个可能正确的文件
					}
				}
				this.lastSyncFileModified = latestFile.lastModified();
				this.cachedConfigList = configList;
			}else{
				return this.cachedConfigList;
			}
		}else{
			logger.error("LotteryQuartzConfig file doesn't exist");
		}

		return configList;
	}

	/**
	 * 删除过多的旧配置文件，保留文件数目取决于context.getBackup()的数值
	 */
	private synchronized void deleteDeprecatedFiles(){
		File[] filelist = this.getSortedFileList(dir);
		for(int i = 0; i < filelist.length; i++){
			if(i < backup) continue;
			if(filelist[i].getAbsoluteFile().delete()){
				logger.info("QuartzConfig file " + filelist[i].getName() + " deleted");
			}else{
				logger.info("QuartzConfig failed to delete file " + filelist[i].getName());
			}
		}
	}

	private void checkAndCreateDir(String dir){
		StringTokenizer   st=new   StringTokenizer(dir, File.separator);
		String path1;
		if(dir.startsWith(File.separator)){ //UNIX like
			path1= File.separator + st.nextToken() + File.separator;
		}else{
			path1 = st.nextToken() + File.separator;
		}
		String   path2 =path1;
		while(st.hasMoreTokens())
		{
			path1=st.nextToken() + File.separator;
		    path2+=path1;
		    File inbox   =   new File(path2);
		   if(!inbox.exists())
		       inbox.mkdir();
		}
	}
	private void renameFile(String src, String dst){
		File fOld = new File(src);
		File fNew = new File(dst);
		fOld.renameTo(fNew);
	}

	private void renameOldFiles(String baseName, String dir, String suffix, int total){
		String absPath = dir + "/" + baseName + suffix;
		for(int i = total; i > 0; i--){
			File fOld = new File(absPath + ".bak" + i);
			if(i == total){
				fOld.delete();
				continue;
			}else if(fOld.exists()){
				File fNew = new File(absPath + ".bak" + (i + 1));
				fOld.renameTo(fNew);
			}
		}
	}
	/**
	 * 将cache中所有数据写入文件。文件名中包含最新的时间戳，防止覆盖。
	 * @param cache 要输出的cache
	 * @return 写入成功则返回true，写入失败则返回false
	 */
	public boolean writeConfig(String name, List<T> configList){
		boolean success = false;

		try {
			renameOldFiles(name, dir, suffix, this.backup);
			this.checkAndCreateDir(dir);

			String filename = name + suffix;
			String absPath = dir + "/" + filename;

			File f = new File(absPath);
			if(f.exists()){
				if(this.backup > 0)
					renameFile(absPath, absPath + ".bak1");
			}

			BufferedWriter os = new BufferedWriter(new FileWriter(absPath));

			StringBuffer txt = new StringBuffer();
			txt.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>" + "\n");
			txt.append("<root>" + "\n");
			for(T config : configList){
				txt.append(this.toElement(config) + "\n");
			}
			txt.append("</root>");

			os.write(txt.toString());
			os.flush();
			os.close();
			success = true;
		} catch (Exception e) {
			logger.error("wirte config error", e);
			success = false;
		}finally{
			if(success) deleteDeprecatedFiles(); //删除多余的文件
		}
		return success;
	}
	public boolean writeConfig(List<T> configList){
		return writeConfig(prefix, configList);
	}
}
