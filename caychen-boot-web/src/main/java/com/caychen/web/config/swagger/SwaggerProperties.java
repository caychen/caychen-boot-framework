package com.caychen.web.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 17:13
 * @Description:
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    private String version = "v1.0.0";

    private String basePackage = "com.caychen";

    private String paths;

    private String author = "caychen";

    private String url = "https://github.com/caychen";

    private String title = "XXX服务";

    private String email = "caychen@aliyun.com";

    private String description = "API 接口文档";

    private String license = "caychen";

    private String termsOfServiceUrl = "https://github.com/caychen";
}
