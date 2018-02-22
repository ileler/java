package com.mpr.ribbon.debug;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Copyright:   Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2018年02月22日 上午11:53
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public final class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            LOGGER.error("当前线程中不存在 Request 上下文");
            return null;
        }
        return attrs.getRequest();
    }

    public static String getIP(HttpServletRequest request) {
        if (request == null)    return null;
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String getCurrentClientIP() {
        return getIP(getCurrentRequest());
    }

}
