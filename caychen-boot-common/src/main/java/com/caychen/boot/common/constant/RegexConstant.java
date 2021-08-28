package com.caychen.boot.common.constant;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 11:49
 * @Description:
 */
public class RegexConstant {

    /**
     * 缺省替换正则(前2后3)
     */
    public static final String DEFAULT_BLUR_REGEX = "(?<=\\w{2})\\w(?=\\w{3})";
    /**
     * 手机号替换正则
     */
    public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";
    /**
     * 手机号占位符
     */
    public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";
    /**
     * 身份证替换正则
     */
    public static final String ID_CARD_BLUR_REGEX = "(?<=\\w{3})\\w(?=\\w{4})";

}
