package com.ydc.laundromat.model;

import java.io.Serializable;

public class Admin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int admin_id;
	private String admin_account;
	private String admin_password;
	private String amdin_loginTime;
	private String admin_permission;
	
	
	public int getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}
	public String getAdmin_account() {
		return admin_account;
	}
	public void setAdmin_account(String admin_account) {
		this.admin_account = admin_account;
	}
	public String getAdmin_password() {
		return admin_password;
	}
	public void setAdmin_password(String admin_password) {
		this.admin_password = admin_password;
	}
	public String getAmdin_loginTime() {
		return amdin_loginTime;
	}
	public void setAmdin_loginTime(String amdin_loginTime) {
		this.amdin_loginTime = amdin_loginTime;
	}
	public String getAdmin_permission() {
		return admin_permission;
	}
	public void setAdmin_permission(String admin_permission) {
		this.admin_permission = admin_permission;
	}
	
	
}
