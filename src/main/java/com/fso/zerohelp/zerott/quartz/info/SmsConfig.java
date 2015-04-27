package com.fso.zerohelp.zerott.quartz.info;

public class SmsConfig {

	private Integer id;
	private String priority;
	private String organization;
	private String template;
	private String version;
	private String createtime;
	private String modifytime;
	
	public SmsConfig() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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
		return "SmsConfig [id=" + id + ", priority=" + priority + ", organization=" + organization + ", template=" + template + ", version=" + version + ", createtime=" + createtime + ", modifytime="
				+ modifytime + "]";
	}
	
}
