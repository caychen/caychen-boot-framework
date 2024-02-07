package com.caychen.boot.common.annotations;

import com.caychen.boot.common.enums.CheckRuleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:08
 * @Description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRule {

    boolean value() default true;

    CheckRuleEnum check();
}
