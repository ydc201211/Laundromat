package com.ydc.laundromat.common;

/**
 * Created by ydc on 2016/9/8.
 */
public class NameValuePair {
    private String name;
    private String value;

    public  NameValuePair(String name,String value){
        this.name = name;
        this.value = value;

    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
