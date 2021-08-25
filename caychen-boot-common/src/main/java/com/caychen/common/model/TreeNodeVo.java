package com.caychen.common.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 19:30
 * @Describe:
 */
@Data
public abstract class TreeNodeVo<T> {

    private Long id;

    private Long parentId;

    private List<T> children = new ArrayList<>();
}
