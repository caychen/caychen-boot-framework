package com.caychen.boot.rabbitmq.config;

import com.caychen.boot.rabbitmq.properties.RabbitQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.annotation.PostConstruct;

/**
 * @Author: Caychen
 * @Date: 2024/2/8 9:57
 * @Description:
 */
@Slf4j
//@Configuration
public class RabbitRegister extends AbstractRabbitRegister implements BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private RabbitQueueProperties queueProperties;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory)beanFactory;
    }

    @PostConstruct
    public void initRegister() {
        //调用父类的initRegister方法
        super.initRegister(beanFactory, queueProperties);
    }
}
