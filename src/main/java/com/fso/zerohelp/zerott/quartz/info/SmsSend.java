package com.fso.zerohelp.zerott.quartz.info;

public class SmsSend {

	private Integer id;
	private Integer configid;
	private String groups;
	private String category;
	private String content;
	private String adminname;
	private String phonenumber;
	private int sendcount;
	private int maxcount;
	private String lastsendtime;
	private String createtime;
	private String modifytime;

	public SmsSend() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConfigid() {
		return configid;
	}

	public void setConfigid(Integer configid) {
		this.configid = configid;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public int getSendcount() {
		return sendcount;
	}

	public void setSendcount(int sendcount) {
		this.sendcount = sendcount;
	}

	public int getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

	public String getLastsendtime() {
		return lastsendtime;
	}

	public void setLastsendtime(String lastsendtime) {
		this.lastsendtime = lastsendtime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	@Override
	public String toString() {
		return "SmsSend [id=" + id + ", configid=" + configid + ", groups=" + groups + ", category=" + category + ", content=" + content + ", adminname=" + adminname + ", phonenumber=" + phonenumber
				+ ", sendcount=" + sendcount + ", maxcount=" + maxcount + ", lastsendtime=" + lastsendtime + ", createtime=" + createtime + ", modifytime=" + modifytime + "]";
	}

}
