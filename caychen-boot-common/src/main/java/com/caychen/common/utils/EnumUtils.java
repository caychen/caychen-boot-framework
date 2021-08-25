package com.caychen.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * <p>
 *
 * @author Jerry
 * @version 1.0
 * @since 2020/11/5
 */
@Slf4j
public abstract class EnumUtils {

    public static <T> T fromCode(Class<T> enumClass, int code) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(enumObj -> {
                    int enumCode = -1;
                    try {
                        enumCode = (int) enumClass.getMethod("getCode").invoke(enumObj);
                    } catch (Exception e) {
                        log.error("无法读取code值", e);
                    }
                    return enumCode == code;
                })
                .findFirst()
                .orElse(null);
    }

}
