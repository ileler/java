package org.ileler.settings.manager.sbs.service.impl;

import org.ileler.settings.manager.sbs.dao.ProfileDAO;
import org.ileler.settings.manager.sbs.model.Profile;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午15:27
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    @Override
    public RespObj add(String env, Profile profile) {
        return new RespObj(profileDAO.add(env, profile));
    }

    @Override
    public RespObj del(String env, String id) {
        return new RespObj(profileDAO.del(env, id));
    }

    @Override
    public RespObj mod(String env, Profile profile) {
        return new RespObj(profileDAO.mod(env, profile));
    }

    @Override
    public RespObj get(String env, String sid) {
        return new RespObj(profileDAO.get(env, sid));
    }
}
