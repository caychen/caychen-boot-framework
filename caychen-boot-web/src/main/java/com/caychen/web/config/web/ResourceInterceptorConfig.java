package com.caychen.web.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 17:23
 * @Description:
 */
@Configuration
public class ResourceInterceptorConfig implements WebMvcConfigurer {

    /**
     * 静态资源增加url映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger资源映射
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        //前端web的jar包映射，比如jQuery，AngularJs等...
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
