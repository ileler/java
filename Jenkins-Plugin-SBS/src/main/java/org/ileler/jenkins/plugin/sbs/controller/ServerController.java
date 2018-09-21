package org.ileler.jenkins.plugin.sbs.controller;

import org.ileler.jenkins.plugin.sbs.dao.ServerDAO;
import org.ileler.jenkins.plugin.sbs.model.Server;
import org.ileler.jenkins.plugin.sbs.model.Streams;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年09月13日 上午9:59 AM
 * Author:      kerwin612
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class ServerController {

    private ServerDAO serverDAO;

    public ServerController() {
    }

    public ServerController(ServerDAO serverDAO) {
        this();
        this.serverDAO = serverDAO;
    }

    @JavaScriptMethod
    public Server add(String env, String id, String host, Integer port, String shome, String username, String password, String configuration) {
        return serverDAO.add(env, new Server(id, host, port, shome, username, password, configuration));
    }

    @JavaScriptMethod
    public Boolean del(String env, String id) {
        return serverDAO.del(env, id);
    }

    @JavaScriptMethod
    public Server mod(String env, String id, String url, String username, String password, String configuration) {
        return serverDAO.mod(env, new Server(id, url, username, password, configuration));
    }

    @JavaScriptMethod
    public List<Server> getByEnv(String env) {
        List<Server> servers = serverDAO.get(env);
//        if (!hasPwd && servers != null) {
//            for (Server server : servers) {
//                server.setPassword("******");
//            }
//        }
        return servers;
    }

    @JavaScriptMethod
    public String valid(String env, String id) {
        return serverDAO.valid(env, id);
    }

    @JavaScriptMethod
    public Streams services(String env, String id) {
        return serverDAO.services(env, id);
    }

    @JavaScriptMethod
    public Streams kill(String env, String id, Long pid) {
        return serverDAO.kill(env, id, pid);
    }

    @JavaScriptMethod
    public Streams loginLogs(String env, String id) {
        return serverDAO.loginLogs(env, id);
    }

    @JavaScriptMethod
    public Streams operLogs(String env, String id) {
        return serverDAO.operLogs(env, id);
    }

}