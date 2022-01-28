package com.caychen.boot.canal.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 14:40
 * @Description:
 */
@Data
public class CanalModel {

    /**
     * 数据列(改变之前: insert无)
     */
    List<CanalColumn> beforeColumns;
    /**
     * 数据列(改变之后: delete无)
     */
    List<CanalColumn> afterColumns;
    /**
     * schema名
     */
    private String schemaName;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 发生变更的时间
     */
    private Long executeTime;
    /**
     * 执行的ddl sql
     */
    private String sql;
}
