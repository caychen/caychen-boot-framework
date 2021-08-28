package com.caychen.boot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 11:55
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum CharsetEnum {

    ANSI("ANSI"),
    UNICODE("UNICODE"),
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    UTF_32("UTF-32"),
    GB_2312("GB-2312"),
    GBK("GBK"),
    IOS_8859_1("IOS-8859-1"),

    ;

    private String charset;
}
