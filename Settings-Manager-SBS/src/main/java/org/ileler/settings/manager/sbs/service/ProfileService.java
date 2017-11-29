package org.ileler.settings.manager.sbs.service;

import org.ileler.settings.manager.sbs.model.Profile;
import org.ileler.settings.manager.sbs.model.RespObj;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface ProfileService {

    RespObj add(String env, Profile profile);

    RespObj del(String env, String id);

    RespObj mod(String env, Profile profile);

    RespObj get(String env, String sid);

}
