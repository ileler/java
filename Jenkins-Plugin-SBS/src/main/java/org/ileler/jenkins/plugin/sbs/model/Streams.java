package org.ileler.jenkins.plugin.sbs.model;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年12月05日 上午8:11
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Streams
{
    private Integer exitCode = 0;

    private String out = "";

    private String err = "";

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public String getOut()
    {
        return out;
    }

    public void setOut( String out )
    {
        this.out = out;
    }

    public String getErr()
    {
        return err;
    }

    public void setErr( String err )
    {
        this.err = err;
    }
}
