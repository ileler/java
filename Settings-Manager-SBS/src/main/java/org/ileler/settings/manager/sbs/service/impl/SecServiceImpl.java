package org.ileler.settings.manager.sbs.service.impl;

import org.ileler.settings.manager.sbs.dao.SecDAO;
import org.ileler.settings.manager.sbs.model.Sec;
import org.ileler.settings.manager.sbs.service.SecService;
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
public class SecServiceImpl implements SecService {

    @Autowired
    private SecDAO secDAO;

    @Override
    public Boolean add(Sec sec) {
        return secDAO.add(sec);
    }

    @Override
    public Boolean login(Sec sec) {
        return secDAO.login(sec);
    }

    @Override
    public Boolean modify(Sec sec) {
        return secDAO.modify(sec);
    }

}
