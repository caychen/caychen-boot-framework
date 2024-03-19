package com.caychen.boot.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 16:54
 * @Description:
 */
@Data
@ConfigurationProperties("cos")
public class CosProperties {

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucket;

    private String region;
}
