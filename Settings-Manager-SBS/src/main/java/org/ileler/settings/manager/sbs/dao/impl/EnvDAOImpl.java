package org.ileler.settings.manager.sbs.dao.impl;

import org.ileler.settings.manager.sbs.dao.EnvDAO;
import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.util.JsonDB;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:01
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Repository
public class EnvDAOImpl implements EnvDAO {

    private JsonDB<Env> jsonDB;

    public EnvDAOImpl() {
        jsonDB = new JsonDB<>("env");
    }

    @Override
    public Boolean add(Env env) {
        jsonDB.addData(env);
        return true;
    }

    @Override
    public Boolean del(String name) {
        if (StringUtils.isEmpty(name)) return false;
        Env _env = get(name);
        if (_env != null) jsonDB.delData(_env);
        return true;
    }

    @Override
    public Boolean mod(Env env) {
        if (env == null) return false;
        del(env.getName());
        jsonDB.addData(env);
        return true;
    }

    @Override
    public List<Env> get() {
        return jsonDB.getData();
    }

    @Override
    public Env get(String name) {
        if (StringUtils.isEmpty(name)) return null;
        List<Env> envs = get();
        if (envs == null) return null;
        for (Env _env : envs) {
            if (_env != null && name.equals(_env.getName())) {
                return _env;
            }
        }
        return null;
    }

    @Override
    public Boolean reset(String name) {
        Env env = StringUtils.isEmpty(name) ? null : get(name);
        if (env == null)    return false;
        try {new File(env.getPath()).delete();}catch(Exception e){}
        return false;
    }

}
