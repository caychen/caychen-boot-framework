package com.caychen.boot.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 15:27
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "alibaba.cloud")
public class OssProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private Oss oss;

    @Data
    public static class Oss {
        private String endpoint;
    }
}
