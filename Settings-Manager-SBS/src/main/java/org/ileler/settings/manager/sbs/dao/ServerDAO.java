package org.ileler.settings.manager.sbs.dao;

import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.model.Streams;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface ServerDAO {

    Server add(String envName, Server server);

    Boolean del(String envName, String id);

    Server mod(String envName, Server server);

    List<Server> get(String envName);

    Server get(String envName, String id);

    String valid(String envName, String id);

    Streams services(String envName, String id);

    Streams kill(String envName, String id, Long pid);

    Streams operLogs(String envName, String id);

    Streams loginLogs(String envName, String id);

}
