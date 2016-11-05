package com.ydc.laundromat.model;

import java.io.Serializable;

public class School implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int school_id;
	private String school_name;
	private double school_latitude;
	private double school_longitude;
	

	public int getSchool_id() {
		return school_id;
	}
	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}


	public double getSchool_latitude() {
		return school_latitude;
	}

	public void setSchool_latitude(double school_latitude) {
		this.school_latitude = school_latitude;
	}

	public double getSchool_longitude() {
		return school_longitude;
	}

	public void setSchool_longitude(double school_longitude) {
		this.school_longitude = school_longitude;
	}
}
