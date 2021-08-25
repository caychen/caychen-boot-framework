package com.caychen.common.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 19:30
 * @Describe:
 */
@Data
public abstract class TreeNodeVo<T> {

    /**
     * 当前节点id
     */
    private Long id;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 子节点集合
     */
    private List<T> children = Lists.newArrayList();
}
