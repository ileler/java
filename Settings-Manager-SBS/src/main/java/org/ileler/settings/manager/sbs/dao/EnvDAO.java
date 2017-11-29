package org.ileler.settings.manager.sbs.dao;

import org.ileler.settings.manager.sbs.model.Env;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface EnvDAO {

    Boolean add(Env env);

    Boolean del(String name);

    Boolean mod(Env env);

    List<Env> get();

    Env get(String name);

    Boolean reset(String name);

}
