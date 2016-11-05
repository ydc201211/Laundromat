package com.ydc.laundromat.model;

import java.io.Serializable;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int order_id;
	private String order_status;
	private String order_amount;
	private String order_discountWay;
	private String order_discountMoney;
	private String order_realPay;
	private String order_time;
	private int order_userId;
	private int order_wmId;
	private String order_washingType;
	private String order_no;
	private String order_wmName;
	
	public String getOrder_discountWay() {
		return order_discountWay;
	}
	public void setOrder_discountWay(String order_discountWay) {
		this.order_discountWay = order_discountWay;
	}
	
	
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}


	public int getOrder_userId() {
		return order_userId;
	}
	public void setOrder_userId(int order_userId) {
		this.order_userId = order_userId;
	}


	public int getOrder_wmId() {
		return order_wmId;
	}

	public void setOrder_wmId(int order_wmId) {
		this.order_wmId = order_wmId;
	}

	public String getOrder_washingType() {
		return order_washingType;
	}

	public void setOrder_washingType(String order_washingType) {
		this.order_washingType = order_washingType;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getOrder_discountMoney() {
		return order_discountMoney;
	}

	public void setOrder_discountMoney(String order_discountMoney) {
		this.order_discountMoney = order_discountMoney;
	}

	public String getOrder_realPay() {
		return order_realPay;
	}

	public void setOrder_realPay(String order_realPay) {
		this.order_realPay = order_realPay;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_wmName() {
		return order_wmName;
	}

	public void setOrder_wmName(String order_wmName) {
		this.order_wmName = order_wmName;
	}
}
