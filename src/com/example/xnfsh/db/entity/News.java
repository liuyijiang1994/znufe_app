package com.example.xnfsh.db.entity;

import java.io.Serializable;

public class News implements Serializable {
	
	private int id;
	private String title;
	private String text;
	private String date;
	private String type;
	
	public News()
	{
		
	}
	
	public News(String title,String text,String date)
	{
		this.title=title;
		this.text=text;
		this.date=date;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
