package com.caychen.boot.common.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caychen.boot.common.exception.BusinessException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:52
 * @Description:
 */
public class FastjsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(FastjsonUtil.class);

    private static final SerializeConfig CONFIG;
    private static final SerializerFeature[] FEATURES = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullStringAsEmpty
    };

    static {
        CONFIG = new SerializeConfig();
        CONFIG.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        CONFIG.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    public static String writeValueAsString(Object value) {
        if (value == null) {
            return null;
        }
        return JSON.toJSONString(value, CONFIG, FEATURES);
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        return JSON.parseObject(content, valueType);
    }

    public static <T> T readValue(String json, String jsonPath) {
        try {
            return JsonPath.read(json, jsonPath);
        } catch (PathNotFoundException e) {
            logger.debug(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public static <T> T readValue(String json, String jsonPath, Predicate... filters) {
        try {
            return JsonPath.read(json, jsonPath, filters);
        } catch (PathNotFoundException e) {
            logger.debug(e.getMessage());
            return null;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public static <T> List<T> readValueAsArray(String json, Class<T> valueType) {
        return JSON.parseArray(json, valueType);
    }

    public static <T> List<T> readValueAsArray(String json, String jsonPath, Class<T> valueType) {
        Object content = JSONPath.read(json, jsonPath);
        if (content == null) {
            logger.debug("path not found:{}", jsonPath);
            return Collections.emptyList();
        }
        return JSONArray.parseArray(content.toString(), valueType);
    }
}
