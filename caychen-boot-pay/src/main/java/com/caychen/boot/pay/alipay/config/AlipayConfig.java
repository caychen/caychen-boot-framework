package com.caychen.boot.pay.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.caychen.boot.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Caychen
 * @Date: 2024/2/13 15:27
 * @Description:
 */
@Slf4j
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfig {

    @Bean
    public AlipayClient alipayClient(AlipayProperties alipayProperties) {
        AlipayClient alipayClient = null;
        /**
         * String serverUrl: 网关地址
         * String appId：app应用id
         * String privateKey：应用私钥
         * String format：格式
         * String charset：字符集
         * String alipayPublicKey：支付宝公钥
         * String signType：签名类型
         */
        try {
            alipayClient = new DefaultAlipayClient(
                    alipayProperties.getGateway(),
                    alipayProperties.getAppId(),
                    alipayProperties.getAppPrivateKey(),
                    alipayProperties.getFormat(),
                    alipayProperties.getCharset(),
                    alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getSignType());
        } catch (Exception e) {
            log.error("AlipayClient创建失败", e);
            throw new SystemException();
        }
        return alipayClient;
    }
}
