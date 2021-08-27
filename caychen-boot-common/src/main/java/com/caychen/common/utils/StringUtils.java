package com.caychen.common.utils;

import com.caychen.common.constant.SymbolConstant;

import java.util.UUID;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:48
 * @Description:
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String getUUIDWithoutReplace() {
        return UUID.randomUUID().toString();
    }

    public static String getUUIDWithReplace(String replacement) {
        return getUUIDWithoutReplace().replace(SymbolConstant.HYPHEN, replacement);
    }


}
