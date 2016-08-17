package com.example.xnfsh.db.entity;


public class Account {

	protected int account_num;
	protected String account_name;
	protected String account_id;
	protected String account_pir;
	protected String account_intro;
	protected int account_priority;
	protected String account_category;
	
	public Account()
	{
		
	}
	
	public Account(String id,String name,String pic)
	{
		this.account_id=id;
		this.account_name=name;
		this.account_pir=pic;
	}
	
	public int getAccount_num() {
		return account_num;
	}
	public void setAccount_num(int account_num) {
		this.account_num = account_num;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getAccount_pir() {
		return account_pir;
	}
	public void setAccount_pir(String account_pir) {
		this.account_pir = account_pir;
	}
	public String getAccount_intro() {
		return account_intro;
	}
	public void setAccount_intro(String account_intro) {
		this.account_intro = account_intro;
	}
	public int getAccount_priority() {
		return account_priority;
	}
	public void setAccount_priority(int account_priority) {
		this.account_priority = account_priority;
	}
	public String getAccount_category() {
		return account_category;
	}
	public void setAccount_category(String account_category) {
		this.account_category = account_category;
	}

}