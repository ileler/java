package org.ileler.utils;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * Author: kerwin612
 */
public class IPUtils {

    public static String getClientIP(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            httpHeaders.addAll(name, Collections.list(request.getHeaders(name)));
        }
        return getClientIP(httpHeaders);
    }

    public static String getClientIP(ServerRequest request) {
        return getClientIP(request.headers().asHttpHeaders());
    }

    public static String getClientIP(HttpHeaders headers) {
        return null;
    }

    public static String getCurrentClientIP() {
        return null;
    }

}
