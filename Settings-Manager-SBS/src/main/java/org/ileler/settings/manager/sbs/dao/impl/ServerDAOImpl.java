package org.ileler.settings.manager.sbs.dao.impl;

import org.ileler.settings.manager.sbs.dao.EnvDAO;
import org.ileler.settings.manager.sbs.dao.ServerDAO;
import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.util.JschUtil;
import org.ileler.settings.manager.sbs.util.SettingsDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午15:36
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Repository
public class ServerDAOImpl implements ServerDAO {

    @Autowired
    private EnvDAO envDAO;

    @Override
    public Server add(String envNmae, Server server) {
        Env env = envDAO.get(envNmae);
        if (env == null) return null;
        try {
            new SettingsDB(env).addServer(server).save();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean del(String envNmae, String id) {
        Env env = envDAO.get(envNmae);
        if (env == null) return false;
        try {
            new SettingsDB(env).delServer(id).save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Server mod(String envNmae, Server server) {
        Env env = envDAO.get(envNmae);
        if (env == null) return null;
        try {
            new SettingsDB(env).modServer(server).save();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Server> get(String envNmae) {
        Env env = envDAO.get(envNmae);
        if (env == null) return null;
        try {
            return new SettingsDB(env).getServers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Server get(String envNmae, String id) {
        Env env = envDAO.get(envNmae);
        if (env == null || StringUtils.isEmpty(id)) return null;
        try {
            List<Server> servers = new SettingsDB(env).getServers();
            if (servers != null) {
                for (Server server : servers) {
                    if (server != null && server.getId().equals(id))    return server;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String valid(String envNmae, String id) {
        Server server = get(envNmae, id);
        if (server == null)     return null;
        return JschUtil.connect(server);
    }

}
