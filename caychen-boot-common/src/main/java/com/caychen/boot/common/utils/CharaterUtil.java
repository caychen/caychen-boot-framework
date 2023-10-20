package com.caychen.boot.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharaterUtil {

    /**
     * 替换特殊字符
     *
     * @param str
     * @param regex
     * @return
     */
    public static String replaceSpecialChar(String str, String regex) {
        String repl = "";
        if (str != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            repl = matcher.replaceAll("");
        }
        return repl;
    }
}
