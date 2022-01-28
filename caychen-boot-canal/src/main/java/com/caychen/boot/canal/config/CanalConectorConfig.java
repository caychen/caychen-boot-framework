package com.caychen.boot.canal.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.caychen.boot.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 17:01
 * @Description:
 */
@Configuration
@Slf4j
public class CanalConectorConfig {

    @Autowired
    private CanalProperties canalProperties;

    @Bean
    public CanalConnector initConnector() {
        //目前canal server上的一个instance只能有一个client消费, 当有多个client消费时,会有bug
        // 创建连接
        CanalConnector connector =
                CanalConnectors.newSingleConnector(
                        new InetSocketAddress(
                                //canal服务器地址
                                canalProperties.getServerAddress(),
                                //默认端口11111
                                canalProperties.getPort()
                        ),
                        canalProperties.getInstance(),
                        canalProperties.getUsername(),
                        canalProperties.getPassword());

        //打开连接
        connector.connect();
        log.info("连接成功...");
        if (StringUtils.isNotBlank(canalProperties.getFilter())) {
//            connector.subscribe(".*\\..*");//订阅所有
            //订阅数据库表
            connector.subscribe(canalProperties.getFilter());
        } else {
            //订阅所有，无过滤规则
            connector.subscribe();
        }
        //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
        connector.rollback();

        return connector;
    }
}
