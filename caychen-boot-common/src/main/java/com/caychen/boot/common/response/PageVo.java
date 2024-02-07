package com.caychen.boot.common.response;

import lombok.Data;

import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2024/2/7 16:43
 * @Description:
 */
@Data
public class PageVo<T> {

    /**
     * 当前页码
     */
    private int pageIndex;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总条数
     */
    private long total;

    /**
     * 当前页面数据
     */
    private List<T> data;
}
