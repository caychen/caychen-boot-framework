package com.caychen.boot.rabbitmq.annotation;

import com.caychen.boot.rabbitmq.config.RabbitRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 11:40
 * @Description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RabbitRegister.class)
//@Import(RabbitQueueBindingConfig.class)
public @interface EnableRabbitProducer {
}
