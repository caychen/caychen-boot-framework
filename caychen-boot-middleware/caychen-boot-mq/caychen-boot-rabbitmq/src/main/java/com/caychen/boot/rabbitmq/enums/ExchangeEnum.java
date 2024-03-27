package com.caychen.boot.rabbitmq.enums;

import lombok.Getter;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.TopicExchange;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 18:07
 * @Description:
 */
@Getter
public enum ExchangeEnum {
    FANOUT("fanout", FanoutExchange.class),
    DIRECT("direct", DirectExchange.class),
    TOPIC("topic", TopicExchange.class),
    HEADER("headers", HeadersExchange.class),

    // 延迟队列插件
    X_DELAYED_MESSAGE("x-delayed-message", CustomExchange.class),
    ;

    private final String exchangeType;
    private final Class<? extends Exchange> exchangeClass;

    ExchangeEnum(String exchangeType, Class<? extends Exchange> exchangeClass) {
        this.exchangeType = exchangeType;
        this.exchangeClass = exchangeClass;
    }

    public static ExchangeEnum getExchangeEnum(String exchangeType) {
        for (ExchangeEnum exchangeEnum : values()) {
            if (exchangeEnum.getExchangeType().equals(exchangeType)) {
                return exchangeEnum;
            }
        }
        return null;
    }
}