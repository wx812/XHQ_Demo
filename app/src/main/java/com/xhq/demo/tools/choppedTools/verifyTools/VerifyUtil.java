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
     * @param email Email string
     * @return true or false
     */
    public static boolean isEmail(String email){
        if(null == email || "".equals(email)) return false;
//        String regex = "^[a-zA-Z0-9]{1,}[a-zA-Z0-9\\_\\.\\-]{0,}@(([a-zA-Z0-9]){1,}\\.){1,3}[a-zA-Z0-9]{0,}[a-zA-Z]{1,}$";
//        String regex = "^[a-zA-Z0-9]+[a-zA-Z0-9_.\\-]*@(([a-zA-Z0-9])+\\.){1,3}[a-zA-Z0-9]*[a-zA-Z]+$";
//        String regex = "\\w+@(\\w+.)+[a-z]{2,3}";    //简单匹配
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";    //复杂匹配
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 验证序列号
     */
    public static boolean isVilidActive(String active){
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4})");
        Matcher matcher = pattern.matcher(active);
        return matcher.matches();
    }
}
