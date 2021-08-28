package com.caychen.boot.file.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 16:55
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(CosProperties.class)
@ConditionalOnProperty(name = "cos.enable", havingValue = "true", matchIfMissing = false)
public class CosConfig {

    @Autowired
    private CosProperties cosProperties;

    @Bean
    public COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getKeyId(), cosProperties.getKeySecret());
        Region region = new Region(cosProperties.getRegion());
        com.qcloud.cos.ClientConfig clientConfig = new com.qcloud.cos.ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
