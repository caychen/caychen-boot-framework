package com.caychen.boot.web.config.logger;

import com.caychen.boot.common.model.LogBaseModel;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:22
 * @Description:
 */
@FunctionalInterface
public interface LogHandler {

    <T extends LogBaseModel> void handleLog(T logModel);
}
