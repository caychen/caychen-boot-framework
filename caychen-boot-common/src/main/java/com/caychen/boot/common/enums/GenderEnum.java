package com.caychen.boot.common.enums;

import com.caychen.boot.common.utils.EnumUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 14:50
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女"),

    ;

    private int code;

    private String desc;

    public static GenderEnum ofCode(int code) {
        return EnumUtils.fromCode(GenderEnum.class, code);
    }
}
