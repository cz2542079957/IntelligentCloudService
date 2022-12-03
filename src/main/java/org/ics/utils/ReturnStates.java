package org.ics.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ReturnStates
{
    // 模块 - 错误错误类型 - 序号
    /*
    错误类型:
    1.参数错误
    2.校验错误
    3.业务逻辑错误
    4.数据库(mysql、redis)错误
    5.系统错误
    9.未知错误（一般错误）


     */

    success(0, "", ReturnStatesType.success),


    authUsernameInvalid(100101, "用户名应为4~24位", ReturnStatesType.warning),
    authUsernameLost(100102, "参数错误", ReturnStatesType.error),

    authTokenError(100201, "登录凭证不正确，请重新登录", ReturnStatesType.error),
    authTokenExpired(100202, "登录凭证已过期，请重新登录", ReturnStatesType.warning),
    authTokenLost(100203, "登录凭证不存在", ReturnStatesType.error),

    authUserExist(100301, "用户已经存在", ReturnStatesType.warning),
    authUserNotExist(100302, "用户不存在", ReturnStatesType.error),
    authPasswordError(100303, "密码错误", ReturnStatesType.error),
    authParsePasswordImageError(100304, "密码图像解析失败", ReturnStatesType.error),

    authAddUserError(100401, "服务器创建用户失败，请联系管理人员", ReturnStatesType.error),

    authDirCreateError(100501, "服务器出错，请联系管理人员", ReturnStatesType.error),
    authIOError(100502, "服务器异常", ReturnStatesType.error),
    ;


    @Setter
    private Integer code;

    @Setter
    private String msg;

    @Setter
    private String type;

}

class ReturnStatesType
{
    public static String empty = "";
    public static String success = "\\success";
    public static String info = "\\info";
    public static String warning = "\\warning";
    public static String error = "\\error";
}
