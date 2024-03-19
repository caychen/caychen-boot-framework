package com.caychen.boot.email.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2022/5/9 18:32
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String from;

    private String to;


}
