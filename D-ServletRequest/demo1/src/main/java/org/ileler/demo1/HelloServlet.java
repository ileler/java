package org.ileler.demo1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public HelloServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printRequestArgs(request);
        response.getWriter().write("get response");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("getInputStream:" + getValue(request.getInputStream()));
        printRequestArgs(request);
//        System.out.println("getParameter:arg4:" + getValue(request.getParameter("arg4")));
        response.getWriter().write("post response");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printRequestArgs(request);
        response.getWriter().write("put response");
    }

    private void printRequestArgs(HttpServletRequest request) throws ServletException, IOException {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("getParameter:arg1:" + getValue(request.getParameter("arg1")));
        System.out.println("getParameterNames:" + getValue(request.getParameterNames()));
        System.out.println("getParameterValues:arg1:" + getValue(request.getParameterValues("arg1")));
        System.out.println("getParameterMap:" + getValue(request.getParameterMap()));
        System.out.println("getQueryString:" + getValue(request.getQueryString()));
        System.out.println("getInputStream:" + getValue(request.getInputStream()));
//        System.out.println("getReader:" + getValue(request.getReader()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private String getValue(Object object) {
        return getValue(object, false);
    }

    private String getValue(Object object, boolean isPrint) {
        String result = null;
        if (object == null) {
            result = "null";
        } else if (object instanceof Enumeration) {
            Enumeration e = (Enumeration) object;
            result = "[";
            while (e.hasMoreElements()) {
                result += ((result.endsWith("[") ? "" : ",") + getValue(e.nextElement(), false));
            }
            result += "]";
        } else if (object instanceof String[]) {
            result = "[";
            for (String str : (String[])object) {
                result += ((result.endsWith("[") ? "" : ",") + str);
            }
            result += "]";
        } else if (object instanceof Map) {
            Iterator<Map.Entry<String, Object>> iterator = ((Map<String, Object>) object).entrySet().iterator();
            result = "{";
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                result += ((result.endsWith("{") ? "" : ",") + (key + ":" + getValue(value, false)));
            }
            result += "}";
        } else if (object instanceof InputStream || object instanceof Reader) {
            try {
                BufferedReader bufferedReader = new BufferedReader(object instanceof InputStream ? new InputStreamReader((InputStream)object) : (Reader) object);
                result = "";
                String tmp;
                while ((tmp = bufferedReader.readLine()) != null) {
                    result += tmp;
                }
            } catch (Exception e) {
            }
            if (result == null) result = "null";
        } else {
            result = object.toString();
        }
        if (isPrint) System.out.println(result);
        return result;
    }

}
