package com.caychen.boot.web.config.logger;

import com.alibaba.fastjson.JSON;
import com.caychen.boot.common.model.LogBaseModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:55
 * @Description:
 */
@Slf4j
public class ConsoleLogHandler implements LogHandler {
    @Override
    public <T extends LogBaseModel> void handleLog(T logModel) {
        log.info("记录当前日志为：[{}]", JSON.toJSONString(logModel));
    }
}
