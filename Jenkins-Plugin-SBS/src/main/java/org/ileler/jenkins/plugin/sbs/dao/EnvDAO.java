package org.ileler.jenkins.plugin.sbs.dao;

import org.apache.commons.lang.StringUtils;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.ileler.jenkins.plugin.sbs.util.ObjectDB;

import java.io.File;
import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:01
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class EnvDAO {

    private ObjectDB<Env> jsonDB;

    public EnvDAO() {
        jsonDB = new ObjectDB<>("env");
    }

    public Boolean add(Env env) {
        del(env.getName());
        jsonDB.addData(env);
        return true;
    }

    public Boolean del(String name) {
        if (StringUtils.isEmpty(name)) return false;
        Env _env = get(name);
        if (_env != null) jsonDB.delData(_env);
        return true;
    }

    public Boolean mod(Env env) {
        if (env == null) return false;
        del(env.getName());
        jsonDB.addData(env);
        return true;
    }

    public List<Env> get() {
        return jsonDB.getData();
    }

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

    public Boolean reset(String name) {
        Env env = StringUtils.isEmpty(name) ? null : get(name);
        if (env == null)    return false;
        try {new File(env.getPath()).delete();}catch(Exception e){}
        return false;
    }

}
