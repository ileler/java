package org.ileler.jenkins.plugin.sbs;

import org.ileler.jenkins.plugin.sbs.dao.EnvDAO;
import org.ileler.jenkins.plugin.sbs.dao.ProfileDAO;
import org.ileler.jenkins.plugin.sbs.dao.ServerDAO;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年09月13日 上午9:59 AM
 * Author:      u~u
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Controller {

    private EnvDAO envDAO = new EnvDAO();
    private ServerDAO serverDAO = new ServerDAO(envDAO);
    private ProfileDAO profileDAO = new ProfileDAO(envDAO, serverDAO);

    @JavaScriptMethod
    public List<Env> envs() {
        return envDAO.get();
    }

    @JavaScriptMethod
    public Env env(String envName) {
        return envDAO.get(envName);
    }

    @JavaScriptMethod
    public Boolean addEnv(String name, String path, String template) {
        return envDAO.add(new Env(name, path, template));
    }

    @JavaScriptMethod
    public Boolean delEnv(String name) {
        return envDAO.del(name);
    }

    @JavaScriptMethod
    public Boolean resetEnv(String name) {
        return envDAO.reset(name);
    }

}
