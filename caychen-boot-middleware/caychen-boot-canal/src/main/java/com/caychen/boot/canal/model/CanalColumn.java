package com.caychen.boot.canal.model;

import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 15:52
 * @Description:
 */
@Data
public class CanalColumn {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列值（字符串）
     */
    private String columnValue;

    /**
     * 是否更新
     */
    private Boolean updated;

    /**
     * 是否为主键
     */
    private Boolean isKey;

    /**
     * 是否为null
     */
    private Boolean isNull;
}
