package com.caychen.boot.canal.handler;

import com.caychen.boot.canal.model.CanalModel;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 15:04
 * @Description:
 */
public interface CanalDataHandler {

    default void handleInsert(CanalModel canalModel) {
    }

    default void handleUpdate(CanalModel canalModel) {
    }

    default void handleDelete(CanalModel canalModel) {
    }

    default void handleTruncate(CanalModel canalModel) {
    }

}
