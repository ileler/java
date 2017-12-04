package com.mpr.nosql;

import java.util.Date;
import java.util.Map;

/**
 * 
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年8月31日 上午09:27:21
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public interface NoSQLService {

    // <<<<<<<<<<<<<<<<<<<< ALL Data Type <<<<<<<<<<<<<<<<<<<<

    boolean set(String key, Object value);

    boolean set(String key, Object value, long time);

    boolean set(String key, Object value, Date datetime);

    boolean setEx(String key, long expire, Object value);

    boolean expire(String key, long time);

    boolean expire(String key, Date datetime);

    String get(String key);

    <T> T get(String key, Class<T> clazz);

    // >>>>>>>>>>>>>>>>>>>> ALL Data Type >>>>>>>>>>>>>>>>>>>>

    // <<<<<<<<<<<<<<<<<<<< Map Data Type <<<<<<<<<<<<<<<<<<<<

    <T> boolean mapSet(String key, String field, T value);

    <T> boolean mapSet(String key, Map<String, T> map);

    Map<String, String> mapGet(String key);

    String mapGet(String key, String field);

    <T> T mapGet(String key, String field, Class<T> valueClass);

    <T> Map<String, T> mapGet(String key, Class<T> valueClass);

    Long mapDel(String key, String... field);

    Long mapDel(String key);

    // >>>>>>>>>>>>>>>>>>>> Map Data Type >>>>>>>>>>>>>>>>>>>>

}
