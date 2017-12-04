package org.ileler.settings.manager.sbs.util;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:05
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class PropertiesDB {

    private String name;

    private Properties properties;
    private Map<String, String> data;

    public PropertiesDB(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
        load();
        if (properties == null) {
            properties = new Properties();
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addData(String key, String value) {
        addData(key, value, true);
    }

    private void addData(String key, String value, boolean isSave) {
        data.put(key, value);
        if (isSave) save();
    }

    public void delData(String key) {
        delData(key, true);
    }

    private void delData(String key, boolean isSave) {
        data.remove(key);
        if (isSave) save();
    }

    private void save() {
        try {
            Iterator<String> iterator = data.keySet().iterator();
            while(iterator.hasNext()) {
                String next = iterator.next();
                if (next.equals("super")) continue;
                properties.put(next, data.get(next));
            }
            properties.store(new FileOutputStream(new File(this.name)), "Secs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            data = new Hashtable<>(0);
            properties = new Properties();
            data.put("super", "77BB881166AA553388AA66006600BB55");
            File file = new File(this.name);
            if (!file.exists()) return;
            properties.load(new FileInputStream(file));
            Iterator<String> iterator = properties.stringPropertyNames().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                data.put(next, properties.getProperty(next));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
