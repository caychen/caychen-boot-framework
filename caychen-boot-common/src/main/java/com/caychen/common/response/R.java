package com.caychen.common.response;

import com.caychen.common.enums.ErrorEnum;
import lombok.Data;

import java.util.Objects;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 15:46
 * @Description:
 */
@Data
public class R<T> {

    public static final Integer SUCCESS_CODE = 0;

    public static final String ERROR_MESSAGE = "error";

    private Integer code = SUCCESS_CODE;

    private String message = "success";

    private String extMsg;

    private T data;

    /**
     * 私有构造函数
     */
    private R() {
    }

    /**
     * 简单的正常返回
     *
     * @return
     */
    public static R ok() {
        return new R();
    }

    /**
     * 携带返回值
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R ok(T data) {
        R r = new R();
        r.setData(data);
        return r;
    }

    /**
     * 异常code
     *
     * @param code
     * @return
     */
    public static R error(Integer code) {
        R r = new R();
        r.setCode(code);
        r.setMessage(ERROR_MESSAGE);
        return r;
    }

    public static R error(ErrorEnum error) {
        R r = new R();
        r.setCode(error.getCode());
        r.setMessage(error.getDesc());
        return r;
    }

    /**
     * 异常code和异常信息
     *
     * @param code
     * @param errorMsg
     * @return
     */
    public static R error(Integer code, String errorMsg) {
        R r = new R();
        r.setCode(code);
        r.setMessage(errorMsg);
        return r;
    }

    public static R error(ErrorEnum error, String extMsg) {
        R r = new R();
        r.setCode(error.getCode());
        r.setMessage(error.getDesc());
        r.setExtMsg(extMsg);
        return r;
    }

    public static <T> R result(Integer code, String message, T data, String extMsg) {
        R r = new R();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        r.setExtMsg(extMsg);

        return r;
    }

    public Boolean isSuccess() {
        return Objects.equals(this.code, R.SUCCESS_CODE);
    }
}
