package com.caychen.boot.common.exception;

import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2024/2/6 18:25
 * @Description:
 */
@Data
public class SystemException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "系统异常";

    public SystemException() {
        super(DEFAULT_MESSAGE);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message) {
        super(message);
    }
}
