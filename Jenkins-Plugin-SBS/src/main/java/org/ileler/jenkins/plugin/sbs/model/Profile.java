package org.ileler.jenkins.plugin.sbs.model;

import java.io.Serializable;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:43
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 5320175101570573623L;

    public Profile() {}

    public Profile(String id, String sid, String dir, String arg, Integer port, Integer dPort) {
        this.id = id;
        this.sid = sid;
        this.dir = dir;
        this.arg = arg;
        this.port = port;
        this.dPort = dPort;
    }

    private String id;

    private String sid;

    private String dir;

    private String arg;

    private Integer port;

    private Integer dPort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getdPort() {
        return dPort;
    }

    public void setdPort(Integer dPort) {
        this.dPort = dPort;
    }
}
