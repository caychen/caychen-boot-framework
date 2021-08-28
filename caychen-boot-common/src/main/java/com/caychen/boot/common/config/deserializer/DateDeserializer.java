package com.caychen.boot.common.config.deserializer;

import com.caychen.boot.common.utils.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/28 11:24
 * @Description:
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String dateString = p.getText();

        if (StringUtils.isNotBlank(dateString)) {
            return DateUtil.parseDateTime(dateString);
        }
        return null;
    }
}
