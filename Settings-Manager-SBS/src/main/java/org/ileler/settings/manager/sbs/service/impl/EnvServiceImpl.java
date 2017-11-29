package org.ileler.settings.manager.sbs.service.impl;

import org.ileler.settings.manager.sbs.dao.EnvDAO;
import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.EnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:01
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Service
public class EnvServiceImpl implements EnvService {

    @Autowired
    private EnvDAO envDAO;

    @Override
    public RespObj add(Env env) {
        return new RespObj(envDAO.add(env));
    }

    @Override
    public RespObj del(String name) {
        return new RespObj(envDAO.del(name));
    }

    @Override
    public RespObj mod(Env env) {
        return new RespObj(envDAO.mod(env));
    }

    @Override
    public RespObj get() {
        return new RespObj(envDAO.get());
    }

    @Override
    public RespObj reset(String name) {
        return new RespObj(envDAO.reset(name));
    }

}
