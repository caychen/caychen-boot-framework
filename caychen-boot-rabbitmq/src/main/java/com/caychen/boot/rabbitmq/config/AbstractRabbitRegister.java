package com.caychen.boot.rabbitmq.config;

import com.caychen.boot.common.exception.SystemException;
import com.caychen.boot.common.utils.lang.StringUtils;
import com.caychen.boot.rabbitmq.enums.ExchangeEnum;
import com.caychen.boot.rabbitmq.properties.RabbitQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2024/2/8 10:05
 * @Description:
 */
@Slf4j
public abstract class AbstractRabbitRegister {

    protected void initRegister(BeanDefinitionRegistry registry, RabbitQueueProperties queueProperties) {
        //获取交换机
        List<RabbitQueueProperties.ExchangeConfig> exchanges = queueProperties.getExchanges();
        if (CollectionUtils.isEmpty(exchanges)) {
            log.warn("未配置rabbit交换机、队列、绑定等配置");
            return ;
        }

        exchanges.forEach(exchange -> {
            //注册交换机
            registerBeanForExchange(registry, exchange);

            List<RabbitQueueProperties.QueueConfig> queues = exchange.getQueues();
            if (CollectionUtils.isNotEmpty(queues)) {
                queues.forEach(queue -> {
                    //注册队列
                    registerBeanForQueue(registry, exchange, queue);
                });
            }

        });
    }

    /**
     * 注册队列
     *
     * @param registry
     * @param exchange
     * @param queue
     */
    protected void registerBeanForQueue(BeanDefinitionRegistry registry, RabbitQueueProperties.ExchangeConfig exchange, RabbitQueueProperties.QueueConfig queue) {
        //注册队列
        String topicName = queue.getTopicName();
        if (!registry.containsBeanDefinition(topicName)) {
            BeanDefinition bd = BeanDefinitionBuilder
                    .genericBeanDefinition(Queue.class)
                    .addConstructorArgValue(queue.getTopicName())
                    .addConstructorArgValue(queue.getDurable())
                    .addConstructorArgValue(queue.getExclusive())
                    .addConstructorArgValue(queue.getAutoDelete())
                    .getBeanDefinition();

            registry.registerBeanDefinition(topicName, bd);

        }

        //注册队列的绑定关系
        registerQueueBinding(registry, exchange, queue);
    }

    /**
     * 注册绑定
     *
     * @param registry
     * @param exchange
     * @param queue
     */
    protected void registerQueueBinding(BeanDefinitionRegistry registry, RabbitQueueProperties.ExchangeConfig exchange, RabbitQueueProperties.QueueConfig queue) {
        //注册绑定
        RabbitQueueProperties.BindingConfig bindingConfig = queue.getBinding();
        if (bindingConfig != null) {
            String bindingName = bindingConfig.getBindingName();
            if (!registry.containsBeanDefinition(bindingName)) {
                BeanDefinition bbd = BeanDefinitionBuilder
                        .genericBeanDefinition(Binding.class)
                        .addConstructorArgValue(queue.getTopicName())
                        .addConstructorArgValue(Binding.DestinationType.QUEUE)
                        .addConstructorArgValue(exchange.getExchangeName())
                        .addConstructorArgValue(StringUtils.equals(exchange.getExchangeType(), ExchangeEnum.FANOUT.getExchangeType()) ? "" : bindingConfig.getRoutingKey())
                        .addConstructorArgValue(Collections.emptyMap())
                        .getBeanDefinition();

                registry.registerBeanDefinition(bindingName, bbd);
            }
        }
    }

    /**
     * 注册交换机
     *
     * @param registry
     * @param exchange
     */
    protected void registerBeanForExchange(BeanDefinitionRegistry registry, RabbitQueueProperties.ExchangeConfig exchange) {
        String exchangeName = exchange.getExchangeName();

        if (!registry.containsBeanDefinition(exchangeName)) {
            String exchangeType = exchange.getExchangeType();
            Class<? extends Exchange> determineExchangeClazz = determineExchange(exchangeType);

            BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .genericBeanDefinition(determineExchangeClazz)
                    .addConstructorArgValue(exchangeName);


            //注册自定义交换机，主要用于x-delayed-message，延迟交换机
            if (determineExchangeClazz == CustomExchange.class) {
                //添加交换机类型
                builder.addConstructorArgValue(exchange.getExchangeType());
            }

            //补充额外参数
            builder
                    .addConstructorArgValue(exchange.getDurable())
                    .addConstructorArgValue(exchange.getAutoDelete())
                    .addConstructorArgValue(exchange.getArgs());

            //注册bean
            registry.registerBeanDefinition(exchangeName, builder.getBeanDefinition());

        } else {
            registry.getBeanDefinition(exchangeName);
        }
    }


    /**
     * 根据交换机类型，获取对应的交换机类
     *
     * @param exchangeType
     * @return
     */
    private Class<? extends Exchange> determineExchange(String exchangeType) {
        ExchangeEnum exchangeEnum = ExchangeEnum.getExchangeEnum(exchangeType);
        if (exchangeEnum == null) {
            log.error("系统异常，[{}]无法解析成功", exchangeType);
            throw new SystemException("系统异常：无法识别的交换机类型");
        }
        return exchangeEnum.getExchangeClass();
    }
}
