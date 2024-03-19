package com.caychen.boot.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 16:48
 * @Description:
 */
@Data
@ConfigurationProperties("oss")
public class OssProperties {

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucket;

    private String callbackUrl;
}
