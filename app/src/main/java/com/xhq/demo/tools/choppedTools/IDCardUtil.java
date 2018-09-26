package com.xhq.demo.tools.choppedTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证号码验证: 公民身份号码是特征组合码，号码的结构如下:
 * 17位数字和1位校验码：6位地址码数字，8位生日数字，3位出生时间顺序号，1位校验码。
 * <p>
 * 1、排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * <p>
 * 2、地址码（前6位）：表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 * 3、出生日期码（第7 至14位）：表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 4、顺序码（第15位至17位）: 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。
 * 5、校验码（第18位）
 * （1）17位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0,...,16 ，先对前17位数字的权求和
 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
 * （2）计算模 Y = mod(S, 11)
 * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 * <p>
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/12/23.
 *     Desc  : IDCard utils.
 *     Updt  : Description.
 * </pre>
 */
public class IDCardUtil{

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     */
    @SuppressWarnings("unchecked")
    public static String isValid(String IDStr){
        String errorInfo = null;// 记录错误信息
        final String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        final String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4",
                "2"};
        String Ai;
        // ================ 校验长度 15位或18位 ================
        if(IDStr.length() != 15 && IDStr.length() != 18){
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }

        // ================ 校验数字 除最后一位都为数字 ================
        if(IDStr.length() == 18){
            Ai = IDStr.substring(0, 17);
        }else{
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if(!isNumeric(Ai)){
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }

        // ================ 校验地区码 ================
        Hashtable h = GetAreaCode();
        if(h.get(Ai.substring(0, 2)) == null){
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        // ================ 校验出生年月 ================
        final String strYear = Ai.substring(6, 10);// 年份
        final String strMonth = Ai.substring(10, 12);// 月份
        final String strDay = Ai.substring(12, 14);// 月份
        if(!isValidDate(strYear + "-" + strMonth + "-" + strDay)){
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try{
            long ymd = gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime();
            if((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || ymd < 0){
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        }catch(NumberFormatException | ParseException e){
            e.printStackTrace();
        }
        if(Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0){
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if(Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0){
            errorInfo = "身份证日份无效";
            return errorInfo;
        }

        // ================ 校验"校验码" ================
        int totalMulAiWi = 0;
        for(int i = 0; i < 17; i++){
            int aiWi = Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
            totalMulAiWi = totalMulAiWi + aiWi;
        }
        int modValue = totalMulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if(IDStr.length() == 18){
            if(!Ai.equalsIgnoreCase(IDStr)) errorInfo = "身份证无效，不是合法的身份证号码";
        }
        return errorInfo;
    }


    /**
     * get date of birth
     *
     * @param idStr IDCard String
     * @return yyyy-MM-dd format birthday string
     */
    public static String getBorn(String idStr){

        String Ai = null;
        if(idStr.length() == 18){
            Ai = idStr.substring(6, 14);
        }else if(idStr.length() == 15){
            Ai = "19" + idStr.substring(6, 12);
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try{
            Date parse = format1.parse(Ai);
            return format2.format(parse);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(String birthday){
        Date date = null;
        try{
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(birthday);
        }catch(ParseException e){
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        if(date != null){
            if(cal.before(date)){
                throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
            }
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if(monthNow <= monthBirth){
            if(monthNow == monthBirth){
                if(dayOfMonthNow < dayOfMonthBirth){
                    age--;
                }
            }else age--;  // monthNow>monthBirth
        }
        return age;
    }


    /**
     * 功能：判断字符串是否为数字
     */
    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate --> eg: 1925-08-12
     */
    public static boolean isValidDate(String strDate){
        Pattern pattern = Pattern.compile(
                new StringBuilder().append(
                        "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?(" +
                                "(0?[1-9])|")
                                   .append("([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|" +
                                                   "([1-2][0-9])|")
                                   .append("(30)))|")
                                   .append("(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(" +
                                                   "([02468][1235679])|")
                                   .append("([13579][01345789]))")
                                   .append("[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|" +
                                                   "([1-2][0-9])|(3[01])")
                                   .append("))|(((0?[469])|(11)")
                                   .append(")[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?(" +
                                                   "(0?[1-9])|")
                                   .append("(1[0-9])|(2[0-8]))))))(\\s")
                                   .append("(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9]))))" +
                                                   ")?$")
                                   .toString());
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }


    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode(){
        final Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
}
