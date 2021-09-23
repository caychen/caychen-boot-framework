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

    UNKNOWN_ERROR(100000, "未定义的错误"),
    ALGORITHM_EMPTY_ERROR(100001, "加密算法为空"),
    PASSWORD_EMPTY_ERROR(100002, "原始密码为空"),
    ENCRYPT_ERROR(100003, "加密失败"),
    INQUIRY_AD_ERROR(100004, "查无数据"),
    ID_EMPTY_ERROR(100005, "主键id为空"),
    INQUIRY_WELCOME_PAGE_ERROR(100006, "查无数据"),
    CALL_REMOTE_SERVER_ERROR(100007, "远程调用失败"),
    INVALID_PARAMETER_ERROR(100008, "参数错误"),
    INTERNAL_SERVER_ERROR(100009, "服务器内部错误"),
    NULL_POINTER_ERROR(100010, "空指针异常"),
    REMOTE_SERVER_TIME_OUT_ERROR(100011, "远程服务连接超时"),
    VALIDATION_ERROR(100012, "字段验证异常"),
    REQUEST_METHOD_NOT_SUPPORT_ERROR(100013, "请求方式不支持"),
    SQL_SYNTAX_ERROR(100014, "执行SQL异常"),
    RES_PARAM_IS_NULL_ERROR(100015, "请求参数为空"),
    PARAMETER_INVALID_ERROR(100016, "参数不正确，请检查后再试"),
    FILE_PARSER_ERROR(100017, "上传文件解析失败"),
    FILE_UPLOAD_ERROR(100018, "文件上传失败"),
    TOKEN_NOT_EXISTS_ERROR(100019, "token不存在"),
    TOKEN_HAS_INVALD_ERROR(100020, "token已失效，请重新登录"),
    ACCOUNT_TOKEN_DECRYPT_ERROR(100021, "decryptToken error"),
    ROLE_IS_DISABLE_ERROR(100022, "角色已失效，请切换角色或重新登录"),
    ROLE_NOT_EXISTS_ERROR(100023, "角色不存在，无法跳转"),
    ACCOUNT_LOGIN_OTHER_ERROR(100024, "该账户已在其它地方登录"),
    ACCOUNT_NAME_IS_BLANK_ERROR(100025, "username不存在"),
    FILE_TYPE_ERROR(100026, "文件类型错误"),
    FILE_NAME_MUST_BE_NOT_NULL(100027, "文件名称不可为空"),
    FILE_CAN_NOT_EXCEED_100MB(100028, "文件不可超过100M"),
    CALL_LINUX_SHELL_COMMAND_ERROR(100029, "调用Linux命令异常"),
    COS_KEY_ERROR(100030,"腾讯云文件上传失败"),
    SECRET_KEY_EMPTY_ERROR(100031,"加密串不能为空"),
    SYSTEM_INTERNAL_ERROR(100032, "服务器内部错误"),

    ;

    private Integer code;

    private String desc;
}
