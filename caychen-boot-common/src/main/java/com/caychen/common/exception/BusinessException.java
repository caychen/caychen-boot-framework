package com.caychen.common.exception;

import com.caychen.common.enums.ErrorEnum;
import lombok.Data;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 17:47
 * @Description:
 */
@Data
public class BusinessException extends RuntimeException {

    private ErrorEnum error;

    private String extMsg;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorEnum error, String... extMsg) {
        super(error.getDesc());
        if(extMsg.length == 1) {
            this.extMsg = extMsg[0];
        }
        this.error = error;
    }
}
