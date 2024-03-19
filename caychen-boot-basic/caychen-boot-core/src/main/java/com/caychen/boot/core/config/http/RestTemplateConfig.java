package com.caychen.boot.core.config.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @Author: Caychen
 * @Date: 2021/8/28 9:28
 * @Description:
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {

        ConnectionKeepAliveStrategy strategy = (response, context) -> {
            Args.notNull(response, "HTTP response");
            final HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                final HeaderElement he = it.nextElement();
                final String param = he.getName();
                final String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (final NumberFormatException ignore) {
                    }
                }
            }
            return 1;
        };

        BufferingClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(
                        new HttpComponentsClientHttpRequestFactory(
                                HttpClientBuilder.create()
                                        .setMaxConnTotal(6000)
                                        .setMaxConnPerRoute(200)
                                        .setKeepAliveStrategy(strategy)
                                        .build()
                        ));

        RestTemplate restTemplate = builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
        restTemplate.getInterceptors().add(new ClientHttpRequestConnectionCloseInterceptor());
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
