package com.caychen.boot.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 16:10
 * @Description:
 */
@Slf4j
public class DefaultConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        /**
         * Confirmation callback.
         *
         * @param correlationData correlation data for the callback.
         * @param ack             true for ack, false for nack
         * @param cause           An optional cause, for nack, when available, otherwise null.
         */
        log.info("ConfirmCallback消息至交换机回调确认，id: [{}]", correlationData.getId());
        if (ack) {
            log.info("消息成功投递至交换机。");
        } else {
            // 该情况一般出现在交换机不存在等...
            log.error("消息失败投递至交换机，原因: [{}]", cause);
        }


    }
}
