package com.caychen.common.utils;

import com.caychen.common.response.R;

import java.util.Objects;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 9:49
 * @Description:
 */
public final class ResponseUtil {

    public static Boolean isSuccess(R r) {
        return Objects.equals(r.getCode(), R.SUCCESS_CODE);
    }
}
