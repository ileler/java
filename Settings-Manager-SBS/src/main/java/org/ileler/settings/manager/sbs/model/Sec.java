package org.ileler.settings.manager.sbs.model;

import java.io.Serializable;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午10:47
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Sec implements Serializable {

    private static final long serialVersionUID = 5687416241919127138L;

    public Sec() {}
    public Sec(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
