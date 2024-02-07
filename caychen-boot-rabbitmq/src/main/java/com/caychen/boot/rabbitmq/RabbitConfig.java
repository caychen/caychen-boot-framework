package com.caychen.boot.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2023/5/4 14:15
 * @Description:
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitTemplate.ReturnCallback returnCallback;

    @Autowired
    private RabbitTemplate.ConfirmCallback confirmCallback;

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 定制化amqp模版
     * <p>
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调 即消息发送到exchange ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调 即消息发送不到任何一个队列中 ack
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);

        //数据转换喂json存入队列
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 发送消息确认, yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 消息返回, yml需要配置 publisher-returns: true
        rabbitTemplate.setReturnCallback(returnCallback);

        return rabbitTemplate;
    }

    @Configuration
    protected static class RabbitCallbackConfig {

        @Bean
        @ConditionalOnMissingBean(RabbitTemplate.ConfirmCallback.class)
        public RabbitTemplate.ConfirmCallback confirmCallback() {
            log.info("============> 注册默认的confirmCallback");
            return new DefaultConfirmCallback();
        }

        @Bean
        @ConditionalOnMissingBean(RabbitTemplate.ReturnCallback.class)
        public RabbitTemplate.ReturnCallback returnCallback() {
            log.info("============> 注册默认的returnCallback");
            return new DefaultReturnCallback();
        }
    }

}
