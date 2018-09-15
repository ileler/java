package org.ileler.jenkins.plugin.sbs.dao;

import org.apache.commons.lang.StringUtils;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.ileler.jenkins.plugin.sbs.model.Server;
import org.ileler.jenkins.plugin.sbs.model.Streams;
import org.ileler.jenkins.plugin.sbs.util.JschUtil;
import org.ileler.jenkins.plugin.sbs.util.SettingsDB;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午15:36
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class ServerDAO {

    private EnvDAO envDAO;

    public ServerDAO(EnvDAO envDAO) {
        this.envDAO = envDAO;
    }

    public Server add(String envName, Server server) {
        Env env = envDAO.get(envName);
        if (env == null) return null;
        try {
            new SettingsDB(env).addServer(server).save();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean del(String envName, String id) {
        Env env = envDAO.get(envName);
        if (env == null) return false;
        try {
            new SettingsDB(env).delServer(id).save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Server mod(String envName, Server server) {
        Env env = envDAO.get(envName);
        if (env == null) return null;
        try {
            new SettingsDB(env).modServer(server).save();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Server> get(String envName) {
        Env env = envDAO.get(envName);
        if (env == null) return null;
        try {
            return new SettingsDB(env).getServers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Server get(String envName, String id) {
        Env env = envDAO.get(envName);
        if (env == null || StringUtils.isEmpty(id)) return null;
        try {
            List<Server> servers = new SettingsDB(env).getServers();
            if (servers != null) {
                for (Server server : servers) {
                    if (server != null && server.getId().equals(id)) return server;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String valid(String envName, String id) {
        Server server = get(envName, id);
        if (server == null) return null;
        return JschUtil.connect(server);
    }

    public Streams services(String envName, String id) {
        Server server = get(envName, id);
        if (server == null) return null;
        return JschUtil.executeCommand(server, "bash --login -c 'jps -mlv'");
    }

    public Streams kill(String envName, String id, Long pid) {
        Server server = get(envName, id);
        if (server == null) return null;
        return JschUtil.executeCommand(server, "kill -9 " + pid);
    }

    public Streams operLogs(String envName, String id) {
        Server server = get(envName, id);
        if (server == null) return null;
        return JschUtil.executeCommand(server, "cd /var/log && cat syslog");
    }

    public Streams loginLogs(String envName, String id) {
        Server server = get(envName, id);
        if (server == null) return null;
        return JschUtil.executeCommand(server, "last -50");
    }

}
