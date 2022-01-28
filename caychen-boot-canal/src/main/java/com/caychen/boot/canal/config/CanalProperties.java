package com.caychen.boot.canal.config;

import com.caychen.boot.canal.constant.CanalConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 10:35
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "canal")
@Component
public class CanalProperties {

    /**
     * canal服务器地址
     */
    private String serverAddress;

    /**
     * canal监听端口，默认为11111
     */
    private int port = CanalConstant.DEFAULT_CANAL_PORT;

    /**
     * canal服务器实例（即文件夹的名字，默认为example）
     */
    private String instance;

    /**
     * canal订阅数据库表
     * <p>
     * .*\\..*  ：表示所有库所有表
     * <p>
     * AA.BB : 表示AA库的BB表
     */
    private String filter;

    /**
     * 每次抓取的数量
     */
    private Integer batchSize = CanalConstant.DEFAULT_BATCH_SIZE;

    /**
     * 用户名，默认为空串
     */
    private String username = "";

    /**
     * 密码，默认为空串
     */
    private String password = "";

    /**
     * canal监听间隔时间，默认为2s
     */
    private int interval = 2000;
}
