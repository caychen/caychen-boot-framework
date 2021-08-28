package com.caychen.boot.common.config.serializer;

import com.caychen.boot.common.constant.DateConstant;
import com.caychen.boot.common.utils.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/28 11:17
 * @Description:
 */
public class TimeSerializer extends JsonSerializer<Date> {

    /**
     * json(jackson)序列化时将Date类型序列化成String类型
     * @param value
     * @param gen
     * @param serializers
     * @throws IOException
     */
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String dateString = DateUtil.formatDate(value, DateConstant.DEFAULT_TIME_FORMAT);
        gen.writeString(dateString);
    }
}
