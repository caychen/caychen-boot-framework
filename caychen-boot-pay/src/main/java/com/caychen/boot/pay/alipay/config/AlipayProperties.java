package com.caychen.boot.pay.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2024/2/13 15:02
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.pay.alipay")
public class AlipayProperties {

    /**
     * 正式的支付网关地址
     */
    private final String FORMAL_GATEWAY = "https://openapi.alipay.com/gateway.do";
    /**
     * 沙箱网关地址
     */
    private final String SANDBOX_GATEWAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private Boolean isSandBox = Boolean.FALSE;
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String signType = "RSA2";
    private String charset = "UTF-8";
    private String format = "JSON";
    private String version = "1.0";

    public String getGateway() {
        return isSandBox ? SANDBOX_GATEWAY : FORMAL_GATEWAY;
    }
}
