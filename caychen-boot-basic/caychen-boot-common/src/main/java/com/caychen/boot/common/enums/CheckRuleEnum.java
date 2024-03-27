package com.caychen.boot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:09
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum CheckRuleEnum {

    DEBUG("debug", "debug模式"),

    CUSTOM("custom", "自定义");

    private String code;
    private String desc;

}
