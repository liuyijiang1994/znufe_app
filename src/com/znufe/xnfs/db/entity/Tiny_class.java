package com.znufe.xnfs.db.entity;

public class Tiny_class {
	
	private int id;
	private String t_subject;
	private int selected;
	public String name;
	private String xml;
	private String pic;
	private String other;
	
	public Tiny_class()
	{
		
	}
	
	public Tiny_class(int id,int selected,String name,String xml,String pic)
	{
		this.id=id;
		this.selected=selected;
		this.name=name;
		this.xml=xml;
		this.pic=pic;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getT_subject() {
		return t_subject;
	}
	public void setT_subject(String t_subject) {
		this.t_subject = t_subject;
	}
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
