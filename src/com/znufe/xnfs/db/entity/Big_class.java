package com.znufe.xnfs.db.entity;

public class Big_class {
	
	private String subject;
	private String name;
	private String brief;
	private String other;
	
	public Big_class(String subject,String name,String brief,String other)
	{
		this.subject=subject;
		this.name=name;
		this.brief=brief;
		this.other=other;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
}
