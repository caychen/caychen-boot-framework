package com.caychen.common.utils;

import com.caychen.common.constant.CommonConstant;
import com.caychen.common.model.TreeNodeVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 19:30
 * @Describe:
 */
public abstract class TreeUtils {

    /**
     * 构建树形结构的 List
     *
     * @param nodes 节点
     * @param <N>
     * @return
     */
    public static <N extends TreeNodeVo<N>> List<N> buildTree(List<N> nodes) {
        Map<Long, List<N>> subNodeMap = nodes.stream()
                // 找出所有子节点,排除所有顶级节点
                .filter(node -> !node.getParentId().equals(CommonConstant.TOP_FUNCTION_PARENT))
                // 根据父级节点的ID分组
                .collect(Collectors.groupingBy(TreeNodeVo::getParentId));
        return nodes.stream()
                /*
                 如果 node 是父级节点,那么设置子节点集合,
                 如果 node 是树末尾的子节点,那么 subNodeMap.get(n.getId()) 的结果是 null,即 n 不会再有子节点
                 */
                .peek(node -> node.setChildren(subNodeMap.get(node.getId())))
                /*
                    找出所有已经设置了子节点的顶级节点即可,
                    剩余的其它节点不再使用,丢弃
                 */
                .filter(node -> node.getParentId().equals(CommonConstant.TOP_FUNCTION_PARENT))
                .collect(Collectors.toList());
    }
}
