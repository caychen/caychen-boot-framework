package com.caychen.boot.canal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 17:06
 * @Description:
 */
@Data
@ConfigurationProperties("thread-pool")
@Component
public class ThreadPoolProperties {

    private int corePoolSize = 5;

    private int maxPoolSize = 20;

    private int queueCapacity = 100;

    private String threadPrefix = "ThreadPool-";

    private int keepAliveSeconds = 60;

}
