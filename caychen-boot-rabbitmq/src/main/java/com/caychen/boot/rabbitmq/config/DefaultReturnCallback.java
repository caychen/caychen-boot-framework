package com.caychen.boot.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 16:20
 * @Description:
 */
@Slf4j
public class DefaultReturnCallback implements RabbitTemplate.ReturnCallback {
    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("ReturnCallback消息发送失败，应答码：[{}], 失败原因：[{}]，交换机名：[{}]，路由键：[{}]，消息: [{}]",
                replyCode, replyText, exchange, routingKey, new String(message.getBody()));
    }
}
