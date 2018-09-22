package org.ileler.jenkins.plugin.sbs.util;


import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:05
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class ObjectDB<T> {

    private static final String DBDIR;

    static {
        String jenkinsHome = System.getenv("JENKINS_HOME");
        DBDIR = (StringUtils.isEmpty(jenkinsHome) ? "." : jenkinsHome) + File.separator + "sbs-settings-db";
        new File(DBDIR).mkdirs();
    }

    private String name;

    private List<T> data;

    public ObjectDB(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
        load();
        if (data == null) {
            data = new ArrayList<>(0);
        }
    }

    public List<T> getData() {
        return data;
    }

    public void addData(T t) {
        addData(t, true);
    }

    private void addData(T t, boolean isSave) {
        data.add(t);
        if (isSave) save();
    }

    public void delData(T t) {
        delData(t, true);
    }

    private void delData(T t, boolean isSave) {
        data.remove(t);
        if (isSave) save();
    }

    private void save() {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(DBDIR + File.separator + this.name));
            objectOutput.writeObject(data);
            objectOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            File file = new File(DBDIR + File.separator + this.name);
            if (!file.exists()) return;
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Object object = objectInputStream.readObject();
            if (object != null) {
                data = (List<T>) object;
            }
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
