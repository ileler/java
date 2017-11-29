package org.ileler.settings.manager.sbs.model;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月26日 上午4:04 PM
 * Author:      o0o
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class RespObj {

    private String code;

    private String mesg;

    private Object data;

    public RespObj(Object data) {
        this("00000000", "success", data);
    }

    public RespObj(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

    public RespObj(String code, String mesg, Object data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
