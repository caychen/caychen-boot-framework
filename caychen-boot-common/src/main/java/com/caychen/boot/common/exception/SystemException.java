package com.caychen.boot.common.exception;

import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 18:25
 * @Description:
 */
@Data
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }
}
