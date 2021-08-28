package com.caychen.boot.common.config.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 11:24
 * @Description:
 */
public class IdKeyGenerator {

    private final Snowflake snowflake;

    public IdKeyGenerator(long workerId, long datacenterId) {
        this.snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public Long nextId() {
        return this.snowflake.nextId();
    }

}
