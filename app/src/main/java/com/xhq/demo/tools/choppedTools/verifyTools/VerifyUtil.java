package com.xhq.demo.tools.choppedTools.verifyTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/10/12.
 *     Desc  : Description.
 * </pre>
 */

public class VerifyUtil{

    /**
     * 验证邮箱
     *
     * @param emailStr Email string
     * @return true or false
     */
    public static boolean isEmail(final String emailStr){
//        String regex = "^[a-zA-Z0-9]{1,}[a-zA-Z0-9\\_\\.\\-]{0,}@(([a-zA-Z0-9]){1,}\\.){1,3}[a-zA-Z0-9]{0,}[a-zA-Z]{1,}$";
        String regex = "^[a-zA-Z0-9]+[a-zA-Z0-9_.\\-]*@(([a-zA-Z0-9])+\\.){1,3}[a-zA-Z0-9]*[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailStr);
        return matcher.matches();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
/*    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }*/


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles){
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、178、187、188（147）
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、181、177 173（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }

    /**
     * 验证序列号
     *
     * @param active
     * @return
     */
    public static boolean isVilidActive(String active){
        Pattern pattern = Pattern
                .compile("([a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4})");
        Matcher matcher = pattern.matcher(active);
        return matcher.matches();
    }
}
