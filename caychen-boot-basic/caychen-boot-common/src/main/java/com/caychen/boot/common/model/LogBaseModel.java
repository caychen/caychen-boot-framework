package com.caychen.boot.common.model;

import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 15:34
 * @Description:
 */
@Data
public class LogBaseModel {

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求时间
     */
    private String requestDateTime;

    /**
     * 请求参数Json格式，长度不大于500
     */
    private String requestParam;

    /**
     * 该操作的响应参数Json格式，长度不大于500
     **/
    private String responseParam;

    /**
     * 响应时间
     */
    private String responseDateTime;

    /**
     * 执行耗时
     */
    private Long executionTime;

    /**
     * 是否成功
     */
    private Boolean isSuccess;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 请求方式
     */
    private String requestMethod;
}
