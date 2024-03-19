package com.caychen.boot.elasticsearch.config;

import com.caychen.boot.common.constant.SymbolConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Caychen
 * @Date: 2021/9/27 11:13
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    public static final RequestOptions DEFAULT_OPTIONS;

    static {
        DEFAULT_OPTIONS = RequestOptions.DEFAULT;
    }

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    private HttpHost[] getHttpHosts(String hosts, int port, String scheme) {
        String[] hostArray = StringUtils.split(hosts, SymbolConstant.COMMA);
        HttpHost[] httpHosts = new HttpHost[hostArray.length];

        if (hostArray.length < 1) {
            throw new RuntimeException("Elasticsearch的地址[elasticsearch.hosts]未配置正确...");
        }

        for (int i = 0; i < hostArray.length; i++) {
            httpHosts[i] = new HttpHost(hostArray[i], port, scheme);
        }
        return httpHosts;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost[] httpHosts = getHttpHosts(
                elasticsearchProperties.getHosts(),
                elasticsearchProperties.getPort(),
                elasticsearchProperties.getScheme());

        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);

        restClientBuilder.setRequestConfigCallback(builder -> {
            builder.setConnectTimeout(elasticsearchProperties.getConnectTimeout());
            builder.setSocketTimeout(elasticsearchProperties.getSocketTimeout());
            builder.setConnectionRequestTimeout(elasticsearchProperties.getConnectionRequestTimeout());
            return builder;
        });

        restClientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            CredentialsProvider credentialsProvider = null;
            if (StringUtils.isNotBlank(elasticsearchProperties.getUsername())
                    && StringUtils.isNotBlank(elasticsearchProperties.getPassword())) {
                credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(elasticsearchProperties.getUsername(),
                                elasticsearchProperties.getPassword()));
            }

            if (credentialsProvider != null) {
                httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            httpAsyncClientBuilder.setMaxConnTotal(elasticsearchProperties.getMaxConnTotal());
            httpAsyncClientBuilder.setMaxConnPerRoute(elasticsearchProperties.getMaxConnPerRoute());
            return httpAsyncClientBuilder;

        });

        return new RestHighLevelClient(restClientBuilder);
    }
}
