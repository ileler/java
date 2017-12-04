package org.ileler.settings.manager.sbs.dao.impl;

import org.ileler.settings.manager.sbs.dao.SecDAO;
import org.ileler.settings.manager.sbs.model.Sec;
import org.ileler.settings.manager.sbs.util.Password;
import org.ileler.settings.manager.sbs.util.PropertiesDB;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:01
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Repository
public class SecDAOImpl implements SecDAO {

    private PropertiesDB propertiesDB;

    public SecDAOImpl() {
        propertiesDB = new PropertiesDB("sec");
    }

    private Boolean del(String username) {
        if (StringUtils.isEmpty(username)) return false;
        Sec _sec = get(username);
        if (_sec != null) propertiesDB.delData(_sec.getUsername());
        return true;
    }

    private List<Sec> get() {
        Map<String, String> data = propertiesDB.getData();
        List<Sec> list = new ArrayList<>(data.size());
        Iterator<String> iterator = data.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            list.add(new Sec(next, data.get(next)));
        }
        return list;
    }

    private Sec get(String username) {
        if (StringUtils.isEmpty(username)) return null;
        List<Sec> secs = get();
        if (secs == null) return null;
        for (Sec _sec : secs) {
            if (_sec != null && username.equals(_sec.getUsername())) {
                return _sec;
            }
        }
        return null;
    }

    @Override
    public Boolean add(Sec sec) {
        propertiesDB.addData(sec.getUsername(), Password.createPassword(sec.getPassword()));
        return true;
    }

    @Override
    public Boolean login(Sec sec) {
        Sec _sec = get(sec.getUsername());
        return _sec == null ? false : Password.authenticatePassword(_sec.getPassword(), sec.getPassword());
    }

    @Override
    public Boolean modify(Sec sec) {
        del(sec.getUsername());
        sec.setPassword(Password.createPassword(sec.getPassword()));
        propertiesDB.addData(sec.getUsername(), sec.getPassword());
        return true;
    }
}
