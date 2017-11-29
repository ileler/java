package org.ileler.settings.manager.sbs.model;

import java.io.Serializable;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:47
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Env implements Serializable {

    private static final long serialVersionUID = 5687416241919127138L;
    private String name;

    private String path;

    private String template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
