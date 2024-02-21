package com.caychen.boot.rabbitmq.config;

import com.caychen.boot.rabbitmq.properties.RabbitQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 18:07
 * @Description:
 */
@Slf4j
//@Configuration
public class RabbitQueueBindingConfig extends AbstractRabbitRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("开始注册rabbit的交换机、队列、绑定...");
        ConfigurationProperties annotation = RabbitQueueProperties.class.getAnnotation(ConfigurationProperties.class);
        Binder binder = Binder.get(environment);
        BindResult<RabbitQueueProperties> bound = binder.bind(annotation.prefix(), RabbitQueueProperties.class);
        if (!bound.isBound()) {
            log.warn("未获取到rabbit交换机、队列、绑定等配置，是否自定义配置？");
            return;
        }

        RabbitQueueProperties queueProperties = bound.get();
        //调用父类的initRegister方法
        super.initRegister(registry, queueProperties);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //ignore
    }


}
