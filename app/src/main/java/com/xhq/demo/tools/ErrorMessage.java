package com.xhq.demo.tools;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/5.
 *     Desc  : Error Message.
 *     Updt  : Description.
 * </pre>
 */
public class ErrorMessage {
    //异常类型
    public final static String ERROR_RIGHT = "0";//正确
    public final static String ERROR_UNKNOWN = "999";//未知错误
    public final static String ERROR_SERVER = "19202";//服务器错误
    public final static String ERROR_TIMEOUT = "19203";//请求超时
    public final static String ERROR_CMD_ERR = "19204";//语法错误，包解析错误
    public final static String ERROR_PARAMETER_ERR = "19205";//参数错误
    public final static String ERROR_SECURITY_CODE = "19206";//防伪码错误
    public final static String ERROR_FILE_TYPE = "19207";//不合法的文件类型
    public final static String ERROR_USER_PWD = "19208";//用户名或密码错误
    public final static String ERROR_USER_STATUS = "19209";//用户状态错误，停用或锁定
    public final static String ERROR_RESOURCES_NONE = "19210";//资源不存在
    public final static String ERROR_SMS_RECEIPT = "19211";//短信回执信息错误，正确格式是手机号码_0/1

    public final static String ERROR_NO_CODE = "19401";//账号不存在
    public final static String ERROR_EXISTS_CODE = "19402";//该帐号已存在
    public final static String ERROR_YZM_ERROR = "19403";//验证码错误
    public final static String ERROR_YZM_TIMEOUT = "19404";//验证码已过期
    public final static String ERROR_LOGIN_AGAIN = "19405";//未获取到登录信息，请重新登陆
    public final static String ERROR_NO_YZM_CODE = "19406";//未获取到验证码信息

    /**
     * 根据错误类型id 返回错误文字提示
     */
    public static String getErrorService(String errorNo) {
        String errorStr;
        switch (errorNo) {
            case ERROR_UNKNOWN:
                errorStr = "未知错误";
                break;
            case ERROR_SERVER:
                errorStr = "服务器错误";
                break;
            case ERROR_TIMEOUT:
                errorStr = "请求超时";
                break;
            case ERROR_CMD_ERR:
            case ERROR_PARAMETER_ERR:
                errorStr = "参数错误";
                break;
            case ERROR_SECURITY_CODE:
                errorStr = "防伪码错误";
                break;
            case ERROR_FILE_TYPE:
                errorStr = "文件类型错误";
                break;
            case ERROR_USER_PWD:
                errorStr = "用户名或密码错误";
                break;
            case ERROR_USER_STATUS:
                errorStr = "用户状态错误";
                break;
            case ERROR_RESOURCES_NONE:
                errorStr = "资源不存在";
                break;
            case ERROR_SMS_RECEIPT:
                errorStr = "短信回执错误";
                break;
            case ERROR_NO_CODE:
                errorStr = "帐号不存在";
                break;
            case ERROR_EXISTS_CODE:
                errorStr = "该帐号已存在";
                break;
            case ERROR_YZM_ERROR:
                errorStr = "验证码错误";
                break;
            case ERROR_YZM_TIMEOUT:
                errorStr = "验证码已过期";
                break;
            case ERROR_LOGIN_AGAIN:
                errorStr = "未获取到登录信息，请重新登录";
                break;
            case ERROR_NO_YZM_CODE:
                errorStr = "未获取到验证码信息";
                break;
//            case "":
//                errorStr = "isOk = false, 错误信息为空!";
//                break;
            default:
                errorStr = errorNo ;
                break;
        }
        return errorStr;
    }
}