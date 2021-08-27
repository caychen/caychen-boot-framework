package com.caychen.file.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 16:50
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = false)
public class OssConfig {

    @Autowired
    private OssProperties ossProperties;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getKeyId(),
                ossProperties.getKeySecret());
    }
}
