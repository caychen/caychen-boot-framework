package com.caychen.common.enums;

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

    SYSTEM_INTERNAL_ERROR(600500, "服务器内部错误"),
    UNKNOWN_ERROR(600000, "未定义的错误"),
    ALGORITHM_EMPTY_ERROR(600001, "加密算法为空"),
    PASSWORD_EMPTY_ERROR(600002, "原始密码为空"),
    ENCRYPT_ERROR(600003, "加密失败"),
    INQUIRY_AD_ERROR(600004, "查无数据"),
    ID_EMPTY_ERROR(600005, "主键id为空"),
    INQUIRY_WELCOME_PAGE_ERROR(600006, "查无数据"),
    CALL_REMOTE_SERVER_ERROR(600007, "远程调用失败"),
    INVALID_PARAMETER_ERROR(600008, "参数错误"),
    INTERNAL_SERVER_ERROR(600009, "服务器内部错误"),
    NULL_POINTER_ERROR(600010, "空指针异常"),
    REMOTE_SERVER_TIME_OUT_ERROR(600011, "远程服务连接超时"),
    VALIDATION_ERROR(600012, "字段验证异常"),
    REQUEST_METHOD_NOT_SUPPORT_ERROR(600013, "请求方式不支持"),
    SQL_SYNTAX_ERROR(600014, "执行SQL异常"),
    RES_PARAM_IS_NULL_ERROR(600015, "请求参数为空"),
    PARAMETER_INVALID_ERROR(600016, "参数不正确，请检查后再试"),
    FILE_PARSER_ERROR(600017, "上传文件解析失败"),
    FILE_UPLOAD_ERROR(600018, "文件上传失败"),
    TOKEN_NOT_EXISTS_ERROR(600019, "token不存在"),
    TOKEN_HAS_INVALD_ERROR(600020, "token已失效，请重新登录"),
    ACCOUNT_TOKEN_DECRYPT_ERROR(600021, "decryptToken error"),
    ROLE_IS_DISABLE_ERROR(600022, "角色已失效，请切换角色或重新登录"),
    ROLE_NOT_EXISTS_ERROR(600023, "角色不存在，无法跳转"),
    ACCOUNT_LOGIN_OTHER_ERROR(600024, "该账户已在其它地方登录"),
    ACCOUNT_NAME_IS_BLANK_ERROR(600025, "username不存在"),
    FILE_TYPE_ERROR(600026, "文件类型错误"),
    FILE_NAME_MUST_BE_NOT_NULL(600026, "文件名称不可为空"),
    FILE_CAN_NOT_EXCEED_100MB(600026, "文件不可超过100M"),
    CALL_LINUX_SHELL_COMMAND_ERROR(600027, "调用Linux命令异常"),
    COS_KEY_ERROR(600028,"腾讯云文件上传失败"),

    ;

    private Integer code;

    private String desc;
}
