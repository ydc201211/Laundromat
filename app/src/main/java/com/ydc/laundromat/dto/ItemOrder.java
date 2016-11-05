package com.ydc.laundromat.dto;

import java.io.Serializable;

/**
 * Created by ydc on 2016/9/14.
 */
public class ItemOrder implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int order_id;
    private String order_status;
    private String order_realPay;
    private String order_time;
    private String order_washingType;
    private String order_no;
    private String order_discountWay;
    private String order_discountMoney;
    private String order_wm_name;

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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_realPay() {
        return order_realPay;
    }

    public void setOrder_realPay(String order_realPay) {
        this.order_realPay = order_realPay;
    }

    public String getOrder_washingType() {
        return order_washingType;
    }

    public void setOrder_washingType(String order_washingType) {
        this.order_washingType = order_washingType;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_discountWay() {
        return order_discountWay;
    }

    public void setOrder_discountWay(String order_discountWay) {
        this.order_discountWay = order_discountWay;
    }

    public String getOrder_discountMoney() {
        return order_discountMoney;
    }

    public void setOrder_discountMoney(String order_discountMoney) {
        this.order_discountMoney = order_discountMoney;
    }

    public String getOrder_wm_name() {
        return order_wm_name;
    }

    public void setOrder_wm_name(String order_wm_name) {
        this.order_wm_name = order_wm_name;
    }
}
