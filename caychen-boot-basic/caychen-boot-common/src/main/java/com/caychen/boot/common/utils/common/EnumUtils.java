package com.caychen.boot.common.utils.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
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

    public static <T> Boolean isSupport(Class<T> enumClass, String name) {
        return Objects.nonNull(Stream.of(enumClass.getEnumConstants()).filter(enumObj -> {
                            String enumName = null;
                            try {
                                enumName = (String) enumClass.getMethod("name").invoke(enumObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return StringUtils.equalsIgnoreCase(enumName, name);
                        })
                        .findFirst()
                        .orElse(null)
        );

    }

}
