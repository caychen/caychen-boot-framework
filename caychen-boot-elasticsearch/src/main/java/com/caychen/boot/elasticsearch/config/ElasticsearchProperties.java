package com.caychen.boot.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Caychen
 * @Date: 2021/9/27 11:25
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchProperties {

    /**
     * elasticsearch的ip地址，如有多个则以逗号分隔
     */
    private String hosts;

    /**
     * elasticsearch的http对外端口，默认为9200
     */
    private int port = 9200;

    /**
     * scheme名称，默认为http
     */
    private String scheme = "http";

    /**
     * elasticsearch的连接用户名
     */
    private String username;

    /**
     * elasticsearch的连接密码
     */
    private String password;

    /**
     * 设置客户端和服务器建立连接的超时时间，单位毫秒，超时后会ConnectionTimeOutException
     */
    private int connectTimeout = 1000;

    /**
     * 客户端从服务器读取数据的超时时间，单位毫秒，超出后会抛出SocketTimeOutException
     */
    private int socketTimeout = 30000;

    /**
     * 连接池获取连接的超时时间，单位毫秒
     */
    private int connectionRequestTimeout = 500;

    /**
     * 最大连接数
     */
    private int maxConnTotal = 30;

    /**
     * 每服务每次能并行接收的请求数量
     */
    private int maxConnPerRoute = 10;
}
