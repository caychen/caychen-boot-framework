package com.caychen.boot.rabbitmq.producer;

import com.caychen.boot.common.utils.lang.StringUtils;
import com.caychen.boot.common.utils.random.UUIDUtil;
import com.caychen.boot.rabbitmq.producer.base.MqBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 11:39
 * @Description:
 */
@Component
@Slf4j
public class RabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param exchange          交换机名称
     * @param routingKey        路由key
     * @param object            发送对象
     * @param delayMilliSeconds 延时（毫秒）
     *                          如果delayMilliSeconds大于0，则进行延迟发送，
     *                          如果delayMilliSeconds小于等于0或者为null，则即时发送消息
     */
    public <T extends MqBaseEntity> void sendMessage(
            @NotBlank String exchange,
            @NotBlank String routingKey,
            @NotNull T object,
            @Nullable Long delayMilliSeconds) {

        //设置msgid
        if (object.getRestore() || StringUtils.isBlank(object.getMsgId())) {
            String uuid = UUIDUtil.getUUIDWithReplaceBySpace();
            object.setMsgId(uuid);
            log.info("发送消息唯一标识id: [{}]", uuid);

            object.setRestore(Boolean.FALSE);
        }

        if (delayMilliSeconds != null && delayMilliSeconds > 0) {
            //延迟消息
            this.rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    object,
                    message -> {
                        message.getMessageProperties().setHeader(MessageProperties.X_DELAY, delayMilliSeconds);
                        return message;
                    },
                    new CorrelationData(object.getMsgId())
            );
        } else {
            //正常发送消息
            this.rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    object,
                    new CorrelationData(object.getMsgId())
            );
        }

    }
}
