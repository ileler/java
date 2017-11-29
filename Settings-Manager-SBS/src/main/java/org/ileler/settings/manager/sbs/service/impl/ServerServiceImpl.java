package org.ileler.settings.manager.sbs.service.impl;

import org.ileler.settings.manager.sbs.dao.ServerDAO;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午15:28
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    private ServerDAO serverDAO;

    @Override
    public RespObj add(String envName, Server server) {
        return new RespObj(serverDAO.add(envName, server));
    }

    @Override
    public RespObj del(String envName, String id) {
        return new RespObj(serverDAO.del(envName, id));
    }

    @Override
    public RespObj mod(String envName, Server server) {
        return new RespObj(serverDAO.mod(envName, server));
    }

    @Override
    public RespObj get(String envName) {
        return new RespObj(serverDAO.get(envName));
    }

    @Override
    public RespObj valid(String envName, String id) {
        return new RespObj(serverDAO.valid(envName, id));
    }

}
