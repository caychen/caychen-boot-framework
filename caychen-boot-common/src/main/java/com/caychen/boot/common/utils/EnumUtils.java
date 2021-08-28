package com.caychen.boot.common.utils;

import java.util.stream.Stream;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 11:25
 * @Description:
 */
public abstract class EnumUtils {

    public static <T> T fromCode(Class<T> enumClass, int code) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(enumObj -> {
                    int enumCode = -1;
                    try {
                        enumCode = (int) enumClass.getMethod("getCode").invoke(enumObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return enumCode == code;
                })
                .findFirst()
                .orElse(null);
    }

}
