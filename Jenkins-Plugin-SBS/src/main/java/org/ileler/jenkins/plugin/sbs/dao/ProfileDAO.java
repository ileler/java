package org.ileler.jenkins.plugin.sbs.dao;

import org.apache.commons.lang.StringUtils;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.ileler.jenkins.plugin.sbs.model.Profile;
import org.ileler.jenkins.plugin.sbs.model.Server;
import org.ileler.jenkins.plugin.sbs.util.JschUtil;
import org.ileler.jenkins.plugin.sbs.util.SettingsDB;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午15:36
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class ProfileDAO {

    private EnvDAO envDAO;

    private ServerDAO serverDAO;
    
    public ProfileDAO(EnvDAO envDAO, ServerDAO serverDAO) {
        this.envDAO = envDAO;
        this.serverDAO = serverDAO;
    }

    private void mkdir(Env env, Profile profile) {
        Server server = serverDAO.get(env.getName(), profile.getSid());
        if (server == null)     return;
        String dir = profile.getDir();
        if (StringUtils.isEmpty(dir))   return;
        String dest;
        if (dir.startsWith("/")) {
            dest = dir;
        } else {
            dest = server.getSpath() + "/" + dir;
        }
        JschUtil.exec(server, "mkdir -p " + dest);
    }

    public Boolean add(String envNmae, Profile profile) {
        Env env = envDAO.get(envNmae);
        if (env == null) return false;
        try {
            new SettingsDB(env).addProfile(profile).save();
            mkdir(env, profile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean del(String envNmae, String id) {
        Env env = envDAO.get(envNmae);
        if (env == null) return false;
        try {
            new SettingsDB(env).delProfile(id).save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean mod(String envNmae, Profile profile) {
        Env env = envDAO.get(envNmae);
        if (env == null) return false;
        try {
            new SettingsDB(env).modProfile(profile).save();
            mkdir(env, profile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Profile> get(String envNmae, String sid) {
        Env env = envDAO.get(envNmae);
        if (env == null) return null;
        try {
            List<Profile> profiles = new SettingsDB(env).getProfiles();
            if (profiles == null || profiles.size() < 1 || StringUtils.isEmpty(sid))    return profiles;
            List<Profile> result = new ArrayList<>(0);
            for (Profile profile : profiles) {
                if (profile != null && sid.equals(profile.getSid())) result.add(profile);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
