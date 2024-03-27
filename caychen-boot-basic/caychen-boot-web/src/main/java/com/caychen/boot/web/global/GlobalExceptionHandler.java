package com.caychen.boot.web.global;

import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.common.response.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 18:00
 * @Describe:
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R handleBackendException(BusinessException e) {
        return R.error(e.getError(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return R.error(ErrorEnum.INVALID_PARAMETER_ERROR, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
