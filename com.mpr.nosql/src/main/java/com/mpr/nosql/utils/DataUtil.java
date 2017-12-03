package com.mpr.nosql.utils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年09月07日 上午16:55
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public final class DataUtil {

    public  interface GetKey<T, K> {
        K key(T obj);
    }

    private DataUtil(){}

    public static <T,K> Map<K, T> toMap(List<T> list, GetKey<T, K> getKey) {
        if (list == null || list.size() < 1 || getKey == null) {
            return null;
        }
        Map<K, T> resultMap = new Hashtable<>(list.size());
        for (T t : list) {
            K key = getKey.key(t);
            if (key == null)    continue;
            resultMap.put(key, t);
        }
        return resultMap;
    }

}
