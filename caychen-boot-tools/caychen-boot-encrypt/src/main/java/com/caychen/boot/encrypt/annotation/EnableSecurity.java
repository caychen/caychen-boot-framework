package com.caychen.boot.encrypt.annotation;

/**
 * @Author: Caychen
 * @Date: 2024/3/19 16:57
 * @Description:
 */

import com.caychen.boot.encrypt.config.SecretKeyConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({SecretKeyConfig.class})
public @interface EnableSecurity {
}
