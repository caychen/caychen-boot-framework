package com.caychen.boot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 17:48
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorEnum {

    // common
    UNKNOWN_ERROR(100000, "未定义的错误"),
    ENCRYPT_ERROR(100003, "加密失败"),
    CALL_REMOTE_SERVER_ERROR(100007, "远程调用失败"),
    INVALID_PARAMETER_ERROR(100008, "参数错误"),
    INTERNAL_SERVER_ERROR(100009, "服务器内部错误"),
    NULL_POINTER_ERROR(100010, "空指针异常"),
    REQUEST_METHOD_NOT_SUPPORT_ERROR(100013, "请求方式不支持"),
    SQL_SYNTAX_ERROR(100014, "执行SQL异常"),
    FILE_UPLOAD_ERROR(100018, "文件上传失败"),
    TOKEN_HAS_INVALID_ERROR(100020, "token无效"),


    ES_INDEX_HAS_FOUND_ERROR(100033, "ES索引已存在"),
    ES_INDEX_NOT_FOUND_ERROR(100033, "ES索引不存在"),

    ;

    private Integer code;

    private String desc;
}
