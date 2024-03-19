package com.caychen.boot.core.config.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author zhe
 * @date 2021/7/19 18:39
 */
@Slf4j
public class ClientHttpRequestConnectionCloseInterceptor implements ClientHttpRequestInterceptor {

    private static final String NO_CONNECTION_UPDATE = "NoConnectionUpdate";

    private static final String CONNECTION_UP = "Connection";

    private static final String CONNECTION_LOW = "connection";

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest,
                                        byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        try {
            HttpHeaders headers = httpRequest.getHeaders();
            if (headers.containsKey(NO_CONNECTION_UPDATE)) {
                log.info("skip connection intercept");
            } else {
                if (!headers.containsKey(CONNECTION_UP) && !headers.containsKey(CONNECTION_LOW)) {
                    headers.set(CONNECTION_LOW, "close");
                }
            }
        } catch (Exception e) {
            log.error("http request intercept fail, {}", e.getMessage(), e);
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

}