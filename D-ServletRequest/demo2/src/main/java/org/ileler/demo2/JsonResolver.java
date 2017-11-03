package org.ileler.demo2;

import com.google.common.io.ByteStreams;
import com.jayway.jsonpath.JsonPath;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月02日 上午19:15
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class JsonResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        String body = getRequestBody(webRequest);
        String val = JsonPath.read(body,
                parameter.getMethodAnnotation(JsonParam.class).value());
        return val;
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest
                .getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest
                .getAttribute(JSONBODYATTRIBUTE);
        if (jsonBody == null) {
            try {
                String body = new String(ByteStreams
                        .toByteArray(servletRequest.getInputStream()));
                servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
                return body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

}
