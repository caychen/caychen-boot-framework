package com.caychen.boot.core.config.config;

import com.caychen.boot.core.config.aop.ComplexLoggerAspect;
import com.caychen.boot.core.config.aop.SimpleLoggerAspect;
import com.caychen.boot.core.config.logger.ConsoleLogHandler;
import com.caychen.boot.core.config.logger.LogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:26
 * @Description:
 */
@Slf4j
@Configuration
public class LogConfigs {

    @Configuration
    public static class SimpleLoggerConfig {

        @Bean
        @ConditionalOnProperty(name = "logging.type", havingValue = "simple", matchIfMissing = false)
        public SimpleLoggerAspect simpleLoggerAspect() {
            return new SimpleLoggerAspect();

        }
    }

    @Configuration
    public static class ComplexLoggerConfig {
        @Bean
        @ConditionalOnProperty(name = "logging.type", havingValue = "complex", matchIfMissing = false)
        public ComplexLoggerAspect complexLoggerAspect() {
            return new ComplexLoggerAspect();
        }

        @Configuration
        public static class LogHandlerConfig {
            @Bean
            @ConditionalOnMissingBean(LogHandler.class)
            public LogHandler logHandler() {
                return new ConsoleLogHandler();
            }
        }
    }

}
