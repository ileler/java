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

    Server add(String envNmae, Server server);

    Boolean del(String envNmae, String id);

    Server mod(String envNmae, Server server);

    List<Server> get(String envNmae);

    Server get(String envNmae, String id);

    String valid(String envNmae, String id);

    Streams operLogs(String envNmae, String id);

    Streams loginLogs(String envNmae, String id);

}
