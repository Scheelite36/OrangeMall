package com.orange.orangemall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum OrangeMallExceptionEnum {
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    PASSWORD_TOO_SHORT(10003, "密码长度不少于8位"),
    NAME_EXISTED(10004, "名称重复"),
    NEED_LOGIN(10005, "需要登陆"),
    NOT_MATCH(10006, "用户名或密码错误"),
    UPDATE_FAIL(10007, "更新失败"),
    ADMIN_VERIFY_FAIL(10008, "需要管理员权限"),
    REQUEST_PARAM_ERROR(10009, "请求参数错误"),
    INSERT_FAIL(10010, "插入内容失败"),
    NOT_EXIST(10011, "记录不存在"),
    DELETE_FAIL(10012, "删除失败"),
    CREATE_FAIL(10013, "创建失败"),
    NOT_ENOUGH(10014,"库存不足"),
    UNMATCHED_ORDER(10015,"订单与当前用户不匹配"),
    NO_SUCH_ENUM(10016,"该枚举不存在"),
    STATUS_ERROR(10017,"订单状态异常"),
    SYSTEM_ERROR(20001, "系统异常");
    /**
     * 异常码
     */
    private final Integer code;
    /**
     * 异常信息
     */
    private final String msg;

}
