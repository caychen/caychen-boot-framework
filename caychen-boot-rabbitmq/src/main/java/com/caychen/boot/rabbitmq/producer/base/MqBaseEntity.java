package com.caychen.boot.rabbitmq.producer.base;

import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2023/5/5 10:09
 * @Description:
 */
@Data
public class MqBaseEntity {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 是否重置msgid
     */
    private Boolean restore = Boolean.FALSE;

}
