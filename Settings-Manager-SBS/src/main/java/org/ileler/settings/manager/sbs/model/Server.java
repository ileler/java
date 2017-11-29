package org.ileler.settings.manager.sbs.model;

import java.io.Serializable;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:39
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Server implements Serializable {

    private static final long serialVersionUID = -2114174612355982255L;

    public Server() {}

    public Server(String id, String url, String username, String password, String configuration) {
        this.id = id;
        this.url = url;
        this.username = username;
        this.password = password;
        this.configuration = configuration;
    }

    private String id;

    private String url;

    private String username;

    private String password;

    private String configuration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
