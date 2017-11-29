package org.ileler.settings.manager.sbs.dao;

import org.ileler.settings.manager.sbs.model.Profile;

import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:58
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface ProfileDAO {

    Boolean add(String envNmae, Profile profile);

    Boolean del(String envNmae, String id);

    Boolean mod(String envNmae, Profile profile);

    List<Profile> get(String envNmae, String sid);

}
