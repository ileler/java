package org.ileler.jenkins.plugin.sbs.model;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.configuration = configuration;
        this.password = password;
        this.username = username;
        this.id = id;
        this.setUrl(url);
    }

    private String id;

    private String url;

    private String username;

    private String password;

    private String configuration;

    private String spath;
    private String shome;
    private String host;
    private Integer port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        if (url == null && host == null && username == null) return null;
        return url != null ? url : (url = "scp://" + host + ":" + getPort() + (spath = (shome == null ? ("/home/" + username + "/") : (shome.startsWith("/") ? shome : ("/home/" + username + "/" + shome)))));
    }

    public void setUrl(String url) {
        if (StringUtils.isEmpty(url))   return;
        this.url = url;
        Matcher matcher = Pattern.compile("(?:scp://)?(\\w+.*?)(?::(\\d+.*?))?(/.*)").matcher(url);
        int count = matcher.find() ? matcher.groupCount() : -1;
        this.host = count > 0 ? matcher.group(1) : null;
        String port = count > 1 ? matcher.group(2) : null;
        this.spath = count > 2 ? matcher.group(3) : null;
        if (!StringUtils.isEmpty(port))  this.port = Integer.valueOf(port);
        if (!StringUtils.isEmpty(spath)) this.shome = spath.replace("/home/" + this.getUsername() + "/", "");
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

    public void setShome(String shome) {
        this.shome = shome;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSpath() {
        return spath;
    }

    public String getShome() {
        return shome;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return (port == null ? (port = 22) : port);
    }
}
