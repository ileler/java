package org.ileler.demo2;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月02日 上午19:15
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class JsonResolver implements HandlerMethodArgumentResolver, WebArgumentResolver {

    private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        return resolveArgument(methodParameter, null, webRequest, null);
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        JsonParam parameterAnnotation = parameter.getParameterAnnotation(JsonParam.class);
        if (parameterAnnotation == null) {
            Annotation[] parameterAnnotations = parameter.getParameterAnnotations();
            for (Annotation annotation : parameterAnnotations) {
                if (JsonParam.class.equals(annotation.annotationType())) {
                    parameterAnnotation = (JsonParam) annotation;
                    break;
                }
            }
        }
        String value = parameterAnnotation.value();
        if (StringUtils.isBlank(value)) {
            value = parameter.getParameterName();
        }
        String body = parameterAnnotation == null ? null : getRequestBody(webRequest);
        Object read = null;
        try {
            read = JsonPath.read(body, value);
        } catch (Exception e) {}
        return read;
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest
                .getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest
                .getAttribute(JSONBODYATTRIBUTE);
        if (jsonBody == null) {
            try {
                String body = IOUtils.toString(servletRequest.getInputStream());
                servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
                return body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }

}
