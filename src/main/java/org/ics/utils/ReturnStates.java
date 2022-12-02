package org.ics.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ReturnStates
{
    success(0, "", ReturnStatesType.success),
    signupFileError(100101, "图片上传失败", ReturnStatesType.error),
    signupParsePasswordImageError(100102, "密码图像解析失败", ReturnStatesType.error),
    signupDirCreateError(100103, "服务器出错，请联系管理人员", ReturnStatesType.error);

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
