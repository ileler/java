package org.ileler.settings.manager.sbs.service;

import org.ileler.settings.manager.sbs.model.Sec;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface SecService {

    Boolean add(Sec sec);

    Boolean login(Sec sec);

    Boolean modify(Sec sec);

}
