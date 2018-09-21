package org.ileler.jenkins.plugin.sbs.controller;

import org.ileler.jenkins.plugin.sbs.dao.ProfileDAO;
import org.ileler.jenkins.plugin.sbs.model.Profile;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年09月13日 上午9:59 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class ProfileController {

    private ProfileDAO profileDAO;

    public ProfileController() {
    }

    public ProfileController(ProfileDAO profileDAO) {
        this();
        this.profileDAO = profileDAO;
    }

    @JavaScriptMethod
    public Boolean add(String env, String id, String sid, String dir, String arg, Integer port, Integer dPort) {
        return profileDAO.add(env, new Profile(id, sid, dir, arg, port, dPort));
    }

    @JavaScriptMethod
    public Boolean del(String env, String id) {
        return profileDAO.del(env, id);
    }

    @JavaScriptMethod
    public Boolean mod(String env, String id, String sid, String dir, String arg, Integer port, Integer dPort) {
        return profileDAO.mod(env, new Profile(id, sid, dir, arg, port, dPort));
    }

    @JavaScriptMethod
    public List<Profile> getBySID(String env, String sid) {
        return profileDAO.get(env, sid);
    }

}