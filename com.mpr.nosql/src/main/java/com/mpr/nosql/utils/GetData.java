package com.mpr.nosql.utils;

import com.mpr.nosql.NoSQLService;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年09月05日 上午11:49
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public final class GetData {

    abstract static class BaseGetter<T> {

        Class<T> getDataClass() {
            Type dataType = getDataType();
            if (dataType instanceof Class)
                return (Class<T>) dataType;
            return (Class<T>) ((ParameterizedTypeImpl) dataType).getRawType();
        }

        Type getDataType() {
            return ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }

    }

    public abstract static class DateGetter<T> extends BaseGetter<T> {

        public abstract T getData();

    }

    public abstract static class DateListGetter<T> extends BaseGetter<T> {

        public abstract Map<String, T> getData(List<String> fields);

    }

    private GetData() {
    }

    // <<<<<<<<<<<<<<<<<<<< ALL Data Type <<<<<<<<<<<<<<<<<<<<

    public static <T> T getData(NoSQLService noSQLService,
                                String key, DateGetter<T> getter) {
        return getData(noSQLService, key, getter, false, null);
    }

    public static <T> T getData(NoSQLService noSQLService,
                                String key, DateGetter<T> getter, long time) {
        return getData(noSQLService, key, getter, false, time);
    }

    public static <T> T getData(NoSQLService noSQLService,
                                String key, DateGetter<T> getter, Date datetime) {
        return getData(noSQLService, key, getter, false, datetime);
    }

    public static <T> T getData(NoSQLService noSQLService,
                                String key, DateGetter<T> getter, boolean isRefresh, long time) {
        return commonGetData(noSQLService, key, getter, isRefresh, Long.valueOf(time));
    }

    public static <T> T getData(NoSQLService noSQLService,
                                String key, DateGetter<T> getter, boolean isRefresh, Date datetime) {
        return commonGetData(noSQLService, key, getter, isRefresh, datetime);
    }

    private static <T> T commonGetData(NoSQLService noSQLService,
                                                String key, DateGetter<T> getter, boolean isRefresh, Object expire) {
        if (StringUtils.isEmpty(key)
                || StringUtils.isEmpty(key) || getter == null) {
            return null;
        }
        if (noSQLService != null && !isRefresh) {
            try {
                T o = noSQLService.get(key, getter.getDataClass());
                if (o != null) {
                    return o;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            final T data = getter.getData();
            if (data != null && noSQLService != null) {
                if (expire != null && expire instanceof Date) {
                    noSQLService.set(key, data, (Date) expire);
                } else if (expire != null && expire instanceof Long) {
                    noSQLService.set(key, data, (Long) expire);
                } else {
                    noSQLService.set(key, data);
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // >>>>>>>>>>>>>>>>>>>> ALL Data Type >>>>>>>>>>>>>>>>>>>>


    // <<<<<<<<<<<<<<<<<<<< Map Data Type <<<<<<<<<<<<<<<<<<<<

    public static <T> Map<String, T> getMapData(NoSQLService noSQLService,
                                                String key, DateGetter<Map<String, T>> getter) {
        return getMapData(noSQLService, key, getter, false);
    }

    public static <T> Map<String, T> getMapData(NoSQLService noSQLService,
                                                String key, DateGetter<Map<String, T>> getter, boolean isRefresh) {
        return getMapData(noSQLService, key, getter, isRefresh, null);
    }

    public static <T> Map<String, T> getMapData(NoSQLService noSQLService,
                                                String key, DateGetter<Map<String, T>> getter, Date expire) {
        return getMapData(noSQLService, key, getter, false, expire);
    }

    public static <T> Map<String, T> getMapData(NoSQLService noSQLService,
                                                String key, DateGetter<Map<String, T>> getter, Long expire) {
        return getMapData(noSQLService, key, getter, false, expire);
    }

    private static <T> Map<String, T> getMapData(NoSQLService noSQLService,
            String key, DateGetter<Map<String, T>> getter, boolean isRefresh, Object expire) {
        if (StringUtils.isEmpty(key)
                || getter == null) {
            return null;
        }
        Map<String, T> stringTMap = null;
        if (noSQLService != null && !isRefresh) {
            try {
                stringTMap = noSQLService.mapGet(key,
                        (Class<T>) (((ParameterizedTypeImpl) getter.getDataType())
                                .getActualTypeArguments()[1]));
                if (stringTMap != null && stringTMap.size() > 0) {
                    return stringTMap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, T> data = null;
        try {
            data = getter.getData();
            if (data != null && data.size() > 0) {
                Map<String, Object> _data = new Hashtable<>(data.size());
                _data.putAll(data);
                if (noSQLService != null) {
                    mapSet(noSQLService, key, null, data, expire);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static <T> T getMapData(NoSQLService noSQLService, String key,
                                   String field, DateGetter<T> getter) {
        return getMapData(noSQLService, key, field, getter, false);
    }

    public static <T> T getMapData(NoSQLService noSQLService, String key,
                                    String field, DateGetter<T> getter, boolean isRefresh) {
        return getMapData(noSQLService, key, field, getter, isRefresh, null);
    }

    public static <T> T getMapData(NoSQLService noSQLService, String key,
                                   String field, DateGetter<T> getter, Date expire) {
        return getMapData(noSQLService, key, field, getter, false, expire);
    }

    public static <T> T getMapData(NoSQLService noSQLService, String key,
                                   String field, DateGetter<T> getter, long expire) {
        return getMapData(noSQLService, key, field, getter, false, Long.valueOf(expire));
    }

    private static <T> T getMapData(NoSQLService noSQLService, String key,
            String field, DateGetter<T> getter, boolean isRefresh, Object expire) {
        if (StringUtils.isEmpty(key)
                || StringUtils.isEmpty(field) || getter == null) {
            return null;
        }
        if (noSQLService != null && !isRefresh) {
            try {
                T o = noSQLService.mapGet(key, field, getter.getDataClass());
                if (o != null) {
                    return o;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            final T data = getter.getData();
            if (data != null && noSQLService != null) {
                mapSet(noSQLService, key, field, data, expire);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getMapData(NoSQLService noSQLService, String key,
                                         List<String> fields, DateListGetter<T> getter) {
        return getMapData(noSQLService, key, fields, getter, false);
    }

    public static <T> List<T> getMapData(NoSQLService noSQLService, String key,
                                         List<String> fields, DateListGetter<T> getter, boolean isRefresh) {
        return getMapData(noSQLService, key, fields, getter, isRefresh, null);
    }

    public static <T> List<T> getMapData(NoSQLService noSQLService, String key,
                                         List<String> fields, DateListGetter<T> getter, Date expire) {
        return getMapData(noSQLService, key, fields, getter, false, expire);
    }

    public static <T> List<T> getMapData(NoSQLService noSQLService, String key,
                                         List<String> fields, DateListGetter<T> getter, long expire) {
        return getMapData(noSQLService, key, fields, getter, false, Long.valueOf(expire));
    }

    private static <T> List<T> getMapData(NoSQLService noSQLService, String key,
            List<String> fields, DateListGetter<T> getter, boolean isRefresh, Object expire) {
        if (StringUtils.isEmpty(key) || fields == null
                || getter == null) {
            return null;
        }
        List<String> noCacheIdList = new ArrayList<>(fields);
        Map<String, T> resultMap = new Hashtable<>(fields.size());
        if (noSQLService != null && !isRefresh) {
            // 先读缓存
            for (String field : fields) {
                try {
                    T goodsListItem = noSQLService.mapGet(key, field,
                            getter.getDataClass());
                    if (goodsListItem != null) {
                        resultMap.put(field, goodsListItem);
                        noCacheIdList.remove(field);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            // 缓存没有的从数据库读
            if (noCacheIdList.size() > 0) {
                Map<String, T> dataMap = getter.getData(noCacheIdList);
                if (dataMap != null) {
                    resultMap.putAll(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 按照顺序输出结果列表
            List<T> resultList = new ArrayList<>(resultMap.size());
            for (String field : fields) {
                T t = resultMap.get(field);
                if (t == null)
                    continue;
                resultList.add(t);
                try {
                    if (noCacheIdList.contains(field) && noSQLService != null) {
                        mapSet(noSQLService, key, field, t, expire);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void mapSet(NoSQLService noSQLService, String key, String field, T t, Object expire) {
        if (noSQLService == null || StringUtils.isEmpty(key) || t == null)  return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(field)) {
                    noSQLService.mapSet(key, (Map) t);
                } else {
                    noSQLService.mapSet(key, field, t);
                }
                Long expireTime = null;
                if (expire != null && expire instanceof Long) {
                    expireTime = (Long)expire;
                } else if (expire != null && expire instanceof Date) {
                    expireTime = ((Date)expire).getTime();
                }
                if (expireTime != null) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (StringUtils.isEmpty(field)) {
                                noSQLService.mapDel(key);
                            } else {
                                noSQLService.mapDel(key, field);
                            }
                        }
                    }, expireTime);
                }
            }
        }).start();
    }

    // >>>>>>>>>>>>>>>>>>>> Map Data Type >>>>>>>>>>>>>>>>>>>>

}
