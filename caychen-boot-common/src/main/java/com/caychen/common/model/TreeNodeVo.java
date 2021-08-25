package com.caychen.common.model;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty("子节点")
    private List<T> children = new ArrayList<>();
}
