package com.ydc.laundromat.model;

import java.io.Serializable;

public class Point implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int point_id;
	private String point_name;
	private double point_latitude;//
	private double point_longitude;//
	private String point_address;
	private int point_parentId;
	private int point_no;
	
	public int getPoint_id() {
		return point_id;
	}
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
	public String getPoint_name() {
		return point_name;
	}
	public void setPoint_name(String point_name) {
		this.point_name = point_name;
	}
	
	public String getPoint_address() {
		return point_address;
	}
	public void setPoint_address(String point_address) {
		this.point_address = point_address;
	}
	
	public int getPoint_parentId() {
		return point_parentId;
	}
	public void setPoint_parentId(int point_parentId) {
		this.point_parentId = point_parentId;
	}
	public int getPoint_no() {
		return point_no;
	}
	public void setPoint_no(int point_no) {
		this.point_no = point_no;
	}
	public double getPoint_latitude() {
		return point_latitude;
	}
	public void setPoint_latitude(double point_latitude) {
		this.point_latitude = point_latitude;
	}
	public double getPoint_longitude() {
		return point_longitude;
	}
	public void setPoint_longitude(double point_longitude) {
		this.point_longitude = point_longitude;
	}
}
