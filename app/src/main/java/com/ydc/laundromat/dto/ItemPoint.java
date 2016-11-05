package com.ydc.laundromat.dto;

import java.io.Serializable;

/**
 * Created by ydc on 2016/9/9.
 */
public class ItemPoint implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pointId;
    private double pointLatitude;
    private double pointLongitude;
    private String pointName;
    private int emptyNum;
    private String distance;
    private String address;

   

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

	public double getPointLatitude() {
		return pointLatitude;
	}

	public void setPointLatitude(double pointLatitude) {
		this.pointLatitude = pointLatitude;
	}

	public double getPointLongitude() {
		return pointLongitude;
	}

	public void setPointLongitude(double pointLongitude) {
		this.pointLongitude = pointLongitude;
	}

	public int getEmptyNum() {
		return emptyNum;
	}

	public void setEmptyNum(int emptyNum) {
		this.emptyNum = emptyNum;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}
