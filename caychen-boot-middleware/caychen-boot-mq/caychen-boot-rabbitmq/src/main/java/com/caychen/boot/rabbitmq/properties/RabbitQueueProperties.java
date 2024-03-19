package com.caychen.boot.rabbitmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @Author: Caychen
 * @Date: 2023/5/4 14:42
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitQueueProperties {

    private List<ExchangeConfig> exchanges;

    @Data
    public static class ExchangeConfig {
        /**
         * 交换机类型
         */
        private String exchangeType;

        /**
         * 交换机名称
         */
        private String exchangeName;

        /**
         * 交换机额外属性，主要用于CustomExchange，作为延迟队列使用
         */
        private Map<String, Object> args;

        /**
         * 交换机是否持久化
         */
        private Boolean durable = Boolean.TRUE;

        /**
         * 换机是否自动删除
         */
        private Boolean autoDelete = Boolean.FALSE;

        private List<QueueConfig> queues;
    }


    @Data
    public static class QueueConfig {
        /**
         * 队列名称
         */
        private String topicName;

        /**
         * 队列是否持久化
         */
        private Boolean durable = Boolean.TRUE;

        /**
         * 队列是否排他
         */
        private Boolean exclusive = Boolean.FALSE;

        /**
         * 队列是否自动删除
         */
        private Boolean autoDelete = Boolean.FALSE;

        /**
         * 队列绑定关系
         */
        private BindingConfig binding;
    }

    @Data
    public static class BindingConfig {

        /**
         * 绑定名称
         */
        private String bindingName;

        /**
         * 绑定路由键
         */
        private String routingKey;
    }


}
