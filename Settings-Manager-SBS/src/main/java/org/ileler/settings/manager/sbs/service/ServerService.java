package org.ileler.settings.manager.sbs.service;

import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.model.Server;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface ServerService {

    RespObj add(String envName, Server server);

    RespObj del(String envName, String id);

    RespObj mod(String envName, Server server);

    RespObj get(String envName, boolean hasPwd);

    RespObj valid(String envName, String id);

}
