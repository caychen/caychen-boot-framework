package com.caychen.boot.web.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 17:04
 * @Description:
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定所有controller的路径
//                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))

                //指定在class上使用RestController注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title(swaggerProperties.getTitle())
                .contact(new Contact(
                        swaggerProperties.getAuthor(),
                        swaggerProperties.getUrl(),
                        swaggerProperties.getEmail()))
                // 设置文档的描述
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                // 设置文档的License信息->1.3 License information
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();
    }
}
