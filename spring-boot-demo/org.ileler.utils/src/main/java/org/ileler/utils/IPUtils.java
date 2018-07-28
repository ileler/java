package org.ileler.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

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
        String clientIP = getClientIP(httpHeaders);
        return StringUtils.isBlank(clientIP) ? request.getRemoteAddr() : clientIP;
    }

    public static String getClientIP(ServerRequest request) {
        return getClientIP(request.headers().asHttpHeaders());
    }

    public static String getClientIP(HttpHeaders headers) {
        if (headers == null)    return null;
        String ip = clientIP(headers, "X-Forwarded-For");
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = clientIP(headers, "Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = clientIP(headers, "WL-Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = clientIP(headers, "HTTP_CLIENT_IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = clientIP(headers, "HTTP_X_FORWARDED_FOR");
        }

        return (StringUtils.isNotBlank(ip) && ip.contains(",")) ? ip.split(",")[0] : ip;
    }

    private static String clientIP(HttpHeaders headers, String key) {
        List<String> strings = headers == null ? null : headers.get(key);
        return (strings == null || strings.size() < 1) ? null : strings.get(0);
    }

    public static String getCurrentClientIP() {
        return getClientIP(getCurrentRequest());
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

}
