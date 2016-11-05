package com.ydc.laundromat.model;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int user_id;
	private String user_account;
	private String user_password;
	private String user_phone;
	private String user_portraitPath;
	private String user_sex;
	private String user_school;
	private String user_status;
	private String user_loginTime;
	private String user_loginIP;
	private String user_wallet;
	
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	
	public String getUser_loginTime() {
		return user_loginTime;
	}
	public void setUser_loginTime(String user_loginTime) {
		this.user_loginTime = user_loginTime;
	}
	public String getUser_loginIP() {
		return user_loginIP;
	}
	public void setUser_loginIP(String user_loginIP) {
		this.user_loginIP = user_loginIP;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_portraitPath() {
		return user_portraitPath;
	}
	public void setUser_portraitPath(String user_portraitPath) {
		this.user_portraitPath = user_portraitPath;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_school() {
		return user_school;
	}
	public void setUser_school(String user_school) {
		this.user_school = user_school;
	}

	public String getUser_wallet() {
		return user_wallet;
	}

	public void setUser_wallet(String user_wallet) {
		this.user_wallet = user_wallet;
	}
}
