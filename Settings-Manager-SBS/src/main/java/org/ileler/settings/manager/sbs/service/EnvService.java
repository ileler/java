package org.ileler.settings.manager.sbs.service;

import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.RespObj;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface EnvService {

    RespObj add(Env env);

    RespObj del(String name);

    RespObj mod(Env env);

    RespObj get();

    RespObj reset(String name);

}
