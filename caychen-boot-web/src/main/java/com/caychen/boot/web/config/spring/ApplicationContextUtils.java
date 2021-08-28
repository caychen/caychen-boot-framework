package com.caychen.boot.web.config.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
class ApplicationctxUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public static ApplicationContext getApplicationctx() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationctxUtils.ctx = applicationContext;
    }

    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        if (ctx == null) {
            return null;
        }
        return ctx.getBean(name, requiredType);
    }

    /**
     * 读取Bean
     * @Param
     * @Return
     **/
    public static <T> T getBean(Class<T> requiredType) {
        if (ctx == null) {
            return null;
        }
        return ctx.getBean(requiredType);
    }

    /**
     * 是否包含某个Bean
     * @Param
     * @Return
     **/
    public static boolean containsBean(String name) {
        return ctx.containsBean(name);
    }

    /**
     * 判断是否为单例
     * @Param
     * @Return
     **/
    public static boolean isSingleton(String name) {
        return ctx.isSingleton(name);
    }

    /**
     * 通过名字获取Bean类型
     * @Param
     * @Return
     **/
    public static Class<? extends Object> getType(String name) {
        return ctx.getType(name);
    }
}