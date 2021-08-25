package com.caychen.web.controller;

import com.caychen.common.enums.ErrorEnum;
import com.caychen.common.exception.BusinessException;
import com.caychen.common.response.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 16:59
 * @Description:
 */
@RestController
public class TestController {

    @ApiOperation("测试接口")
    @GetMapping("/test")
    public R test() {

        throw new BusinessException(ErrorEnum.INVALID_PARAMETER_ERROR);
    }
}
