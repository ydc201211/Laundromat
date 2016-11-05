package com.ydc.laundromat.model;

import java.io.Serializable;

public class WM implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int wm_id;
	private String wm_name;
	private int wm_status;
	private String wm_address;
	private int wm_parentId;
	private String wm_washingTimes;
	private int wm_no;
	
	public int getWm_no() {
		return wm_no;
	}
	public void setWm_no(int wm_no) {
		this.wm_no = wm_no;
	}
	public int getWm_id() {
		return wm_id;
	}
	public void setWm_id(int wm_id) {
		this.wm_id = wm_id;
	}
	public String getWm_name() {
		return wm_name;
	}
	public void setWm_name(String wm_name) {
		this.wm_name = wm_name;
	}
	
	public String getWm_address() {
		return wm_address;
	}
	public void setWm_address(String wm_address) {
		this.wm_address = wm_address;
	}
	public int getWm_parentId() {
		return wm_parentId;
	}
	public void setWm_parentId(int wm_parentId) {
		this.wm_parentId = wm_parentId;
	}
	public String getWm_washingTimes() {
		return wm_washingTimes;
	}
	public void setWm_washingTimes(String wm_washingTimes) {
		this.wm_washingTimes = wm_washingTimes;
	}
	public int getWm_status() {
		return wm_status;
	}
	public void setWm_status(int wm_status) {
		this.wm_status = wm_status;
	}
}
