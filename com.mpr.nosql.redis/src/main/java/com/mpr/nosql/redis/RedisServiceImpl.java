package com.mpr.nosql.redis;

import com.mpr.nosql.NoSQLService;
import com.mpr.nosql.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年8月31日 上午09:27:21
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
@Service
public class RedisServiceImpl implements NoSQLService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    // <<<<<<<<<<<<<<<<<<<< ALL Data Type <<<<<<<<<<<<<<<<<<<<

    @Override
    public boolean set(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                connection.set(serializer.serialize(key),
                        serializer.serialize(JsonUtils.toJson(value)));
                return true;
            }
        });
    }

    @Override
    public boolean set(String key, Object value, long time) {
        if (StringUtils.isEmpty(key) || value == null) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                connection.set(serializer.serialize(key),
                        serializer.serialize(JsonUtils.toJson(value)), Expiration.milliseconds(time), RedisStringCommands.SetOption.UPSERT);
                return true;
            }
        });
    }

    @Override
    public boolean set(String key, Object value, Date datetime) {
        if (StringUtils.isEmpty(key) || value == null || datetime == null) {
            return false;
        }
        return set(key, value, datetime.getTime());
    }

    @Override
    public boolean setEx(String key, long expire, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                if (connection.exists(serializer.serialize(key))) return false;
                connection.setEx(serializer.serialize(key), expire,
                        serializer.serialize(JsonUtils.toJson(value)));
                return true;
            }
        });
    }

    @Override
    public boolean expire(String key, long time) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                connection.expire(serializer.serialize(key), time);
                return true;
            }
        });
    }

    @Override
    public boolean expire(String key, Date datetime) {
        if (StringUtils.isEmpty(key) || datetime == null) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                connection.expireAt(serializer.serialize(key), datetime.getTime());
                return true;
            }
        });
    }

    @Override
    public String get(String key) {
        return get(key, String.class);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                byte[] bytes = connection.get(serializer.serialize(key));
                if (bytes == null)  return null;
                return JsonUtils.toObject(serializer.deserialize(bytes), clazz);
            }
        });
    }

    // >>>>>>>>>>>>>>>>>>>> ALL Data Type >>>>>>>>>>>>>>>>>>>>

    // <<<<<<<<<<<<<<<<<<<< Map Data Type <<<<<<<<<<<<<<<<<<<<

    @Override
    public <T> boolean mapSet(final String key, final String field,
            final T value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)
                || value == null) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                if (value instanceof String) {
                    connection.hSet(serializer.serialize(key),
                            serializer.serialize(field),
                            serializer.serialize((String) value));
                } else {
                    connection.hSet(serializer.serialize(key),
                            serializer.serialize(field),
                            serializer.serialize(JsonUtils.toJson(value)));
                }
                return true;
            }
        });
    }

    @Override
    public <T> boolean mapSet(final String key, final Map<String, T> map) {
        if (StringUtils.isEmpty(key) || map == null || map.size() < 1) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                Map<byte[], byte[]> _map = new Hashtable<>(map.size());
                Iterator<Map.Entry<String, T>> iterator = map.entrySet()
                        .iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, T> next = iterator.next();
                    Object value = next.getValue();
                    if (value instanceof String) {
                        _map.put(serializer.serialize(next.getKey()),
                                serializer.serialize((String) value));
                    } else {
                        _map.put(serializer.serialize(next.getKey()),
                                serializer.serialize(JsonUtils.toJson(value)));
                    }
                }
                connection.hMSet(serializer.serialize(key), _map);
                return true;
            }
        });
    }

    @Override
    public Map<String, String> mapGet(final String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                Map<byte[], byte[]> value = connection
                        .hGetAll(serializer.serialize(key));
                Map<String, String> _map = new Hashtable<>();
                Iterator<Map.Entry<byte[], byte[]>> iterator = value.entrySet()
                        .iterator();
                while (iterator.hasNext()) {
                    Map.Entry<byte[], byte[]> next = iterator.next();
                    _map.put(serializer.deserialize(next.getKey()),
                            serializer.deserialize(next.getValue()));
                }
                return _map;
            }
        });
    }

    @Override
    public String mapGet(final String key, final String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                byte[] value = connection.hGet(serializer.serialize(key),
                        serializer.serialize(field));
                return serializer.deserialize(value);
            }
        });
    }

    @Override
    public <T> T mapGet(final String key, final String field,
            final Class<T> valueClass) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)
                || valueClass == null) {
            return null;
        }
        String valueStr = mapGet(key, field);
        return StringUtils.isEmpty(valueStr) ? null
                : JsonUtils.toObject(valueStr, valueClass);
    }

    @Override
    public <T> Map<String, T> mapGet(String key, Class<T> valueClass) {
        if (StringUtils.isEmpty(key) || valueClass == null) {
            return null;
        }
        Map<String, String> valueStrMap = mapGet(key);
        if (valueStrMap != null && valueStrMap.size() > 0) {
            Map<String, T> _map = new Hashtable<>();
            Iterator<Map.Entry<String, String>> iterator = valueStrMap
                    .entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String value = next.getValue();
                _map.put(next.getKey(), JsonUtils.toObject(value, valueClass));
            }
            return _map;
        }
        return null;
    }

    @Override
    public Long mapDel(String key, String... fields) {
        if (StringUtils.isEmpty(key) || fields == null || fields.length < 1) {
            return 0l;
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                byte[][] bytes = new byte[fields.length][];
                for (int i = 0, j = bytes.length; i < j; i++) {
                    bytes[i] = serializer.serialize(fields[i]);
                }
                return connection.hDel(serializer.serialize(key), bytes);
            }
        });
    }

    @Override
    public Long mapDel(String key) {
        if (StringUtils.isEmpty(key)) {
            return 0l;
        }
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate
                        .getStringSerializer();
                return connection.hDel(serializer.serialize(key));
            }
        });
    }

    // >>>>>>>>>>>>>>>>>>>> Map Data Type >>>>>>>>>>>>>>>>>>>>

}