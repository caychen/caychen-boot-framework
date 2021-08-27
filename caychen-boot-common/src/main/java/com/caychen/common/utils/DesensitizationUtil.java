package com.caychen.common.utils;

import com.caychen.common.constant.RegexConstant;
import com.caychen.common.constant.SymbolConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:51
 * @Description:
 */
public class DesensitizationUtil {

    /**
     * 占位符
     */
    private static final String PLACE_HOLDER = SymbolConstant.MULTIPLICATION;

    /**
     * @Description 手机号脱敏
     * @Param value 待脱敏手机号
     * @Return 脱敏后的值, eg.139****6789
     */
    public static String coverMobile(String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return value;
        }
        if (ValidateUtil.isMobile(value)) {
            return value.replaceAll(RegexConstant.PHONE_BLUR_REGEX, RegexConstant.PHONE_BLUR_REPLACE_REGEX);
        } else {
            return value.replaceAll(RegexConstant.DEFAULT_BLUR_REGEX, PLACE_HOLDER);
        }
    }

    /**
     * @Description 身份证号脱敏
     * @Param value 待脱敏值
     * @Return 脱敏后的值, eg.510***********4567
     */
    public static String coverIdCard(String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return value;
        }
        return value.replaceAll(RegexConstant.ID_CARD_BLUR_REGEX, PLACE_HOLDER);
    }

    /**
     * @Description 通用字符串脱敏
     * @Param value 待脱敏值, startLen 需保留的前部字符数(可为0) endLen需保留的尾部字符数(可为0)
     * @Return 脱敏后的值 eg.abc*****ijk
     */
    public static String coverText(String value, int startLen, int endLen) {
        if (StringUtils.isEmpty(value) || startLen < 0 || endLen < 0) {
            return value;
        }
        String blurRegex = buildGroundBlurRegex(startLen, endLen);
        return value.replaceAll(blurRegex, PLACE_HOLDER);
    }


    /**
     * @Description 生成保留首尾部分字符串替换正则式
     * @Param startLen 需保留的前部字符数 endLen需保留的尾部字符数
     * @Return regex
     */
    private static String buildGroundBlurRegex(int startLen, int endLen) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(?<=\\w{").append(startLen).append("})\\w(?=\\w{").append(endLen).append("})");
        return stringBuilder.toString();
    }
}
