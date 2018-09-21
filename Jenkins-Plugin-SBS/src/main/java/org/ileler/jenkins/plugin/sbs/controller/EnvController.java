package org.ileler.jenkins.plugin.sbs.controller;

import org.ileler.jenkins.plugin.sbs.dao.EnvDAO;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年09月13日 上午9:59 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class EnvController {

    private EnvDAO envDAO;

    public EnvController() {
    }

    public EnvController(EnvDAO envDAO) {
        this();
        this.envDAO = envDAO;
    }

    @JavaScriptMethod
    public List<Env> get() {
        return envDAO.get();
    }

    @JavaScriptMethod
    public Env getByName(String envName) {
        return envDAO.get(envName);
    }

    @JavaScriptMethod
    public Boolean add(String name, String path, String template) {
        return envDAO.add(new Env(name, path, template));
    }

    @JavaScriptMethod
    public Boolean mod(String name, String path, String template) {
        return envDAO.mod(new Env(name, path, template));
    }

    @JavaScriptMethod
    public Boolean del(String name) {
        return envDAO.del(name);
    }

    @JavaScriptMethod
    public Boolean reset(String name) {
        return envDAO.reset(name);
    }

}
