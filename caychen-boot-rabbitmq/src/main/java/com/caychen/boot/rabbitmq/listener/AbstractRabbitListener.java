package com.caychen.boot.rabbitmq.listener;

import com.alibaba.fastjson.JSON;
import com.caychen.boot.rabbitmq.producer.base.MqBaseEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 11:39
 * @Description:
 */
@Slf4j
public abstract class AbstractRabbitListener {

    protected <T extends MqBaseEntity> void processBeforeHandleMessage(T msg) {
        log.info("当前正在消费消息，内容为：[{}]", JSON.toJSONString(msg));

        //重试等...
    }

    protected <T extends MqBaseEntity> void processAfterHandleMessage(T msg) {

    }
}
