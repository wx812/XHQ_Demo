package com.xhq.demo.tools;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trim;

/**
 * integration of the commonly used string tools Api.<br>
 * java.lang.String,
 * android.text.TextUtils,
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/08/31.
 *     Desc  : Strings util
 *     Updt  : Description
 * </pre>
 */
public class StringUtils{

    public static final char SEPARATOR = '_';
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String SPACE = "";
    public static final String DOT = ".";
    public static final String COMMA = ",";


    //判断Id是否为空id,有可能是"-","-1"
    public static boolean isEmptyId(String id){
        return id == null || id.trim().length() == 0 || "0".equals(id) || "-".equals(id)
                || "-1".equals(id) || "null".equals(id) || "(无)".equals(id) || "".equals(id);
    }


    public static boolean isEmpty(@Nullable CharSequence s){
        return s == null || s.length() == 0;
    }


    /**
     * 判断对象是否为空 , TextUtils.isEmpty判断是并不能去除有空格的字符串:" "
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean isEmpty(Object obj){
        if(obj == null){
            return true;
        }
        if(obj instanceof CharSequence && obj.toString().length() == 0){
            return true;
        }
        if(obj instanceof String && obj.toString().length() == 0){
            return true;
        }
        if(obj.getClass().isArray() && Array.getLength(obj) == 0){
            return true;
        }
        // sub interface (List , Set, Queue)
        if(obj instanceof Collection && ((Collection)obj).isEmpty()){
            return true;
        }
        if(obj instanceof Map && ((Map)obj).isEmpty()){
            return true;
        }
        if(obj instanceof SparseArray && ((SparseArray)obj).size() == 0){
            return true;
        }
        if(obj instanceof SparseBooleanArray && ((SparseBooleanArray)obj).size() == 0){
            return true;
        }
        if(obj instanceof SparseIntArray && ((SparseIntArray)obj).size() == 0){
            return true;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            // SparseLongArray require api greater than or equal 15
            if(obj instanceof SparseLongArray && ((SparseLongArray)obj).size() == 0){
                return true;
            }
        }
        return false;
    }


    public static boolean isNullOrEmpty(String str){
        boolean flag = true;
        if(str != null && !"".equals(str.trim()) && !"null".equalsIgnoreCase(str.trim())){
            flag = false;
        }
        return flag;
    }


    /**
     * deal with the empty string
     *
     * @param str to test string
     * @param defValue default value
     * @return String
     */
    public static String doEmpty(String str, String defValue){
        return isEmpty(str) ? defValue : str;
    }


    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s){
        return (s == null || s.trim().length() == 0);
    }


    public static boolean equals(CharSequence a, CharSequence b){
        if(a == b) return true;
        int length;
        if(a != null && b != null && (length = a.length()) == b.length()){
            if(a instanceof String && b instanceof String){
                return a.equals(b);
            }else{
                for(int i = 0; i < length; i++){
                    if(a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }


    public static boolean equalsIgnoreCase(String a, @NonNull String b){
        boolean region = a.regionMatches(true, 0, b, 0, b.length());
        return (a == b) || (a.length() == b.length()) && region;
    }


    public static String toString(Object object){
        if(object == null){
            return "null";
        }
        if(!object.getClass().isArray()){
            return object.toString();
        }
        if(object instanceof boolean[]){
            return Arrays.toString((boolean[])object);
        }
        if(object instanceof byte[]){
            return Arrays.toString((byte[])object);
        }
        if(object instanceof char[]){
            return Arrays.toString((char[])object);
        }
        if(object instanceof short[]){
            return Arrays.toString((short[])object);
        }
        if(object instanceof int[]){
            return Arrays.toString((int[])object);
        }
        if(object instanceof long[]){
            return Arrays.toString((long[])object);
        }
        if(object instanceof float[]){
            return Arrays.toString((float[])object);
        }
        if(object instanceof double[]){
            return Arrays.toString((double[])object);
        }
        if(object instanceof Object[]){
            return Arrays.deepToString((Object[])object);
        }
        return "Couldn't find a correct type for the object";
    }


    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    static String getStackTraceString(Throwable tr){
        if(tr == null) return "";

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while(t != null){
            if(t instanceof UnknownHostException) return "";
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s){
        return s == null ? "" : s;
    }


    /**
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s){
        return s == null ? 0 : s.length();
    }


    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s){
        if(isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char)(s.charAt(0) - 32)) + s.substring(1);
    }


    /**
     * strSrc中寻找第一个strSe并且返回以其分隔的Left部分，汉字长度也为1
     *
     * @param strSrc 源字符串
     * @param strSe 分割字符
     * @return String 返回
     */
    public static String getLeft(String strSrc, String strSe){
        if(StringUtils.isEmpty(strSrc))
            return "";
        if(StringUtils.isEmpty(strSe))
            strSe = " ";

        String result = "";
        int pos = strSrc.indexOf(strSe);
        if(pos >= 0)
            result = strSrc.substring(0, pos);
        return result;
    }


    /**
     * 返回字符串的右边部分，汉字长度也为1
     *
     * @param strSrc 源串
     * @param count 要获取的右边字符串长度,负数将返回“”，如果count>字符串长度，则返回整个字符串；
     * @return String return
     */
    public static String getRight(String strSrc, int count){
        if(StringUtils.isEmpty(strSrc) || count <= 0) return "";
        int l = strSrc.length();
        if(l <= count) return strSrc;
        return strSrc.substring(l - count);
    }


    /**
     * 左边补齐字符
     *
     * @param src 源串
     * @param pad 补齐字符
     * @param length 最终长度
     * @return 补齐后的字符串
     */
    public static String padLeft(String src, String pad, int length){
        StringBuilder sb = new StringBuilder(repeatString(pad, length));
        sb.append(src);
        return sb.substring(sb.length() - length);
    }


    public static String padRight(String src, String pad, int length){
        StringBuilder sb = new StringBuilder(length * pad.length() + src.length());
        sb.append(src).append(repeatString(pad, length));
        return sb.substring(0, length);
    }


    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s){
        if(isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char)(s.charAt(0) + 32)) + s.substring(1);
    }


    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s){
        int len = length(s);
        if(len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for(int i = 0; i < mid; ++i){
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }


    public static List<String> toList(String[] strArray){
        if(isEmpty(strArray)) return null;
        return new ArrayList<>(Arrays.asList(strArray));
    }


    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s){
        if(isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for(int i = 0, len = chars.length; i < len; i++){
            if(chars[i] == 12288){
                chars[i] = ' ';
            }else if(65281 <= chars[i] && chars[i] <= 65374){
                chars[i] = (char)(chars[i] - 65248);
            }else{
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s){
        if(isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for(int i = 0, len = chars.length; i < len; i++){
            if(chars[i] == ' '){
                chars[i] = (char)12288;
            }else if(33 <= chars[i] && chars[i] <= 126){
                chars[i] = (char)(chars[i] + 65248);
            }else{
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    /**
     * gender number to String
     *
     * @param genderCord gender code
     * @return gender string
     */
    public static String coverGender(String genderCord){
        String gender;
        if("1".equals(genderCord)) gender = "男";
        else gender = "女";
        return gender;
    }


    /**
     * 判断是否都是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if(TextUtils.isEmpty(str)) return false;
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try{
            bigStr = new BigDecimal(str).toString();
        }catch(Exception e){
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        return isNum.matches();
    }


    //函数功能: 正整数
    public static boolean isPureNumber(String s){
        return s.matches("^[1-9]\\d*$");
    }


    //函数功能: 整数
    public static boolean isNumber(String s){
        return s.matches("^[-+][0-9]\\d*$");
    }


    //函数功能: 浮点数
    public static boolean isAmount(String s){
        return s.matches("^[-+]?[\\d,]+(\\.\\d+)?$");
    }


    //函数功能: 带千分号的整数
    public static boolean isFormatNumber(String inputString){
        return inputString.matches("^[-+]?[\\d,]+(\\d+)?$");
    }


    //首字母大写
    public static String upFirst(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


    /**
     * according to the offset hidden string
     *
     * @param sourceStr source string
     * @param startOffsetPos start offset
     * @param endOffsetPos end offset
     * @return hided string
     */
    public static String hideStr(@NonNull String sourceStr, int startOffsetPos, int endOffsetPos){
        int length = sourceStr.length();
        startOffsetPos = startOffsetPos < 0 ? 0 : startOffsetPos;
        endOffsetPos = endOffsetPos > length ? length : endOffsetPos;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sourceStr.length(); i++){
            char c = sourceStr.charAt(i);
            if(i >= startOffsetPos && i <= length - endOffsetPos){
                sb.append('*');
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }




    /**
     * 格式化  保留小数点两位
     */
    public static String decimalFormat(float f){
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(f);//format 返回的是字符串
    }


    /*
     *  输入限制
     */
    public static String stringFilter(String str) throws PatternSyntaxException{
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]"; // 只允许字母、数字和汉字
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 通配符算法。 可以匹配"*"和"?"
     * 如a*b?d可以匹配aAAAbcd
     *
     * @param regex 正则表达式
     * @param str 匹配的字符串
     * @return
     */
    public static boolean matchByWildcard(String regex, String str){
        if(regex == null || str == null){return false;}

        boolean result = false;
        char c; // 当前要匹配的字符串
        boolean beforeStar = false; // 是否遇到通配符*
        int back_i = 0;// 回溯,当遇到通配符时,匹配不成功则回溯
        int back_j = 0;
        int i, j;
        for(i = 0, j = 0; i < str.length(); ){
            if(regex.length() <= j){
                if(back_i != 0){// 有通配符,但是匹配未成功,回溯
                    beforeStar = true;
                    i = back_i;
                    j = back_j;
                    back_i = 0;
                    back_j = 0;
                    continue;
                }
                break;
            }

            if((c = regex.charAt(j)) == '*'){
                if(j == regex.length() - 1){// 通配符已经在末尾,返回true
                    result = true;
                    break;
                }
                beforeStar = true;
                j++;
                continue;
            }

            if(beforeStar){
                if(str.charAt(i) == c){
                    beforeStar = false;
                    back_i = i + 1;
                    back_j = j;
                    j++;
                }
            }else{
                if(c != '?' && c != str.charAt(i)){
                    result = false;
                    if(back_i != 0){// 有通配符,但是匹配未成功,回溯
                        beforeStar = true;
                        i = back_i;
                        j = back_j;
                        back_i = 0;
                        back_j = 0;
                        continue;
                    }
                    break;
                }
                j++;
            }
            i++;
        }

        if(i == str.length() && j == regex.length())// 全部遍历完毕
            result = true;
        return result;
    }


    public static boolean matchIgnoreCase(String str, String regex){
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 将byte数组转换为字符串,
     *
     * @param array byte数组
     * @param length 截取的长度,从0开始,因中文字符等原因,实际截取的长度可能小于length
     * @param charsetName 生成的字符串的字符编码
     * @return str
     */
    public static String byteArray2String(byte[] array, int length, String charsetName){
        String str = "";
        int sig = 1;
        if(length <= array.length){
            for(int i = 0; i < length; i++){
                sig = array[i] * sig >= 0 ? 1 : -1;
            }
            if(sig < 0){
                length -= 1;
            }
            try{
                str = new String(array, 0, length, charsetName);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return str;
    }


    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int,
     * 然后利用Integer.toHexString(int)来转换成16进制字符串。
     * *
     *
     * @param byteArray source byteArray
     * @return hex string
     */
    public static String bytes2HexString(byte[] byteArray){
        if(isEmpty(byteArray)) return null;
        StringBuilder stringBuilder = new StringBuilder("");
        for(byte aByteArray : byteArray){
            int v = aByteArray & 0xFF;
            String hv = Integer.toHexString(v);
            if(hv.length() < 2) stringBuilder.append(0);
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexString2Bytes(String hexString){
        if(TextUtils.isEmpty(hexString)) return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for(int i = 0; i < length; i++){
            int pos = i * 2;
            d[i] = (byte)(char2Byte(hexChars[pos]) << 4 | char2Byte(hexChars[pos + 1]));
        }
        return d;
    }


    public static byte char2Byte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }


    /**
     * charArr to byteArr
     */
    public static byte[] chars2Bytes(char[] chars){
        if(isEmpty(chars)) return null;
        byte[] bytes = new byte[chars.length];
        for(int i = 0; i < chars.length; i++){
            bytes[i] = (byte)(chars[i]);
        }
        return bytes;
    }


    /**
     * bytes to bits
     */
    public static String bytes2Bits(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte aByte : bytes){
            for(int j = 7; j >= 0; --j){
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }


    /**
     * bits to bytes
     */
    public static byte[] bits2Bytes(String bits){
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if(lenMod != 0){
            for(int i = lenMod; i < 8; i++){
                bits = String.format("0%s", bits);
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for(int i = 0; i < byteLen; ++i){
            for(int j = 0; j < 8; ++j){
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }


    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar){
        if(hexChar >= '0' && hexChar <= '9') return hexChar - '0';
        if(hexChar >= 'A' && hexChar <= 'F') return hexChar - 'A' + 10;
        throw new IllegalArgumentException();
    }


    /**
     * 格式化数字,控制显示长度, eg: 1W+, 100W+ etc.
     *
     * @param count
     * @return
     */
    public static String formatMillion(int count){
        if(count / (10000 * 100) > 0){
            return "100W+";
        }else if(count / 10000 > 0){
            return count / 10000 + "W+";
        }else{
            return count + "";
        }
    }


    public static String pinConvert(String pin) throws Exception{
        byte[] bytes = toHexString(pin);
        StringBuffer buffer = new StringBuffer();
        for(byte _byte : bytes){
            String s = Integer.toHexString(_byte);
            if(s.length() > 1){
                buffer.append(s);
            }else{
                buffer.append("0" + s);
            }
        }
        return buffer.toString();
    }


    /**
     * 两位密码长度+密码+00  补足32位
     */
    public static byte[] toHexString(String pin) throws Exception{
        byte[] bytes = new byte[32];
        int len = pin.length();
        if(len > 99){
            throw new Exception("pin too long");
        }
        byte[] lenBytes;
        if(len < 9){
            lenBytes = ("0" + String.valueOf(len)).getBytes();
        }else{
            lenBytes = String.valueOf(len).getBytes();
        }
        byte[] pinBytes = pin.getBytes();
        System.arraycopy(lenBytes, 0, bytes, 0, 2);
        System.arraycopy(pinBytes, 0, bytes, 2, len);
        return bytes;
    }


    //convert string to int
    public static int toIntFromStr(String value, int defaultValue){
        if(value == null || "".equals(value.trim())){
            return defaultValue;
        }
        try{
            return Integer.valueOf(value);
        }catch(Exception e){
            try{
                return Double.valueOf(value).intValue();
            }catch(Exception e1){
                return defaultValue;
            }
        }
    }


    public static String getRandom(){
        Random r = new Random();
        long num = Math.abs(r.nextLong() % 10000000000L);
        String s = String.valueOf(num);
        for(int i = 0; i < 10 - s.length(); i++){
            s = String.format("0%s", s);
        }
        return s;
    }


    public static boolean isChineseName(String name){
        boolean result = true;
        if(TextUtils.isEmpty(name)){
            return false;
        }
        char[] cTemp = name.toCharArray();
        for(int i = 0; i < name.length(); i++){
            if(!isChinese(cTemp[i])){
                result = false;
                break;
            }
        }
        return result;
    }


    public static boolean isChinese(char c){
        boolean result = false;
        if(c >= 19968 && c <= 171941){// 汉字范围 \u4e00-\u9fa5 (中文)
            result = true;
        }
        return result;
    }


    public static int chineseCompare(String s1, String s2){
        Collator chineseCollator = Collator.getInstance(Locale.CHINA);
        return chineseCollator.compare(s1, s2);
    }


    /**
     * 字符串转换成引用的字符串, eg: 中国人 --> "中国人"
     *
     * @param string
     * @return
     */
    static String string2ReferenceStr(String string){
        return "\"" + string + "\"";
    }


    public static String removeDoubleReference(String string){
        int length = string.length();
        if((length > 1) && (string.charAt(0) == '"') && (string.charAt(length - 1) == '"')){
            return string.substring(1, length - 1);
        }
        return string;
    }


    /**
     * 从数组中提取出十六进制的字符串所构成的字节数组
     *
     * @param str 有效的16进制字符串
     * @return byte[]
     */
    public static byte[] fromHexString(String str){
        int stringLength = str.length();
        if((stringLength & 0x1) != 0){
            throw new IllegalArgumentException("不是有效的16进制字符串！");
        }
        byte[] b = new byte[stringLength / 2];

        for(int i = 0, j = 0; i < stringLength; i += 2, j++){
            int high = convertChar(str.charAt(i));
            int low = convertChar(str.charAt(i + 1));
            b[j] = (byte)((high << 4) | low);
        }
        return b;
    }


    private static int convertChar(char c){
        if('0' <= c && c <= '9'){
            return c - '0';
        }else if('a' <= c && c <= 'f'){
            return c - 'a' + 0xa;
        }else if('A' <= c && c <= 'F'){
            return c - 'A' + 0xa;
        }else{
            throw new IllegalArgumentException("Invalid hex Character:" + c);
        }
    }

//    ==================================================================================================


    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] str2Bytes(String str){
        if(str != null){
            try{
                return str.getBytes("UTF-8");
            }catch(UnsupportedEncodingException e){
                return null;
            }
        }else{
            return null;
        }
    }


    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return
     */
    public static String byte2String(byte[] bytes){
        try{
            return new String(bytes, CHARSET_UTF8);
        }catch(UnsupportedEncodingException e){
            return "";
        }
    }


    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val){
        if(val == null) return 0D;
        try{
            return Double.valueOf(trim(val.toString()));
        }catch(Exception e){
            return 0D;
        }
    }


    /**
     * 获取浮点数(有错误默认为0)，可以识别金额中的逗号格式
     *
     * @param str 带转换的字符串
     * @return 浮点数
     */
    public static double toDouble(String str){
        if(str == null){return 0.0;}
        str = str.trim();
        if(str.equals(SPACE)){return 0.0;}
        str = str.replaceAll(",", SPACE).replaceAll("，", SPACE);
        try{
            return Double.valueOf(str);
        }catch(Exception e){
            return 0.0;
        }
    }


    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val){
        return toDouble(val).floatValue();
    }


    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val){
        return toDouble(val).longValue();
    }


    /**
     * 得到Long 获取转换的Long值，有错返回0
     *
     * @param str 带转换的字符串
     * @return long
     */
    public static long toLong(String str){
        return toLong(str, 0L);
    }


    public static long toLong(String str, long defValue){
        if(str == null){return defValue;}
        str = str.trim();
        if(str.equals(SPACE)){return defValue;}
        str = str.replaceAll(COMMA, SPACE).replaceAll("，", SPACE);
        try{
            return Long.valueOf(str);
        }catch(Exception e){
            return defValue;
        }
    }


    /**
     * 转换为boolean
     */
    public static boolean toBoolean(String v, boolean b){
        if(v == null) return b;
        return "1".equals(v) || "true".equalsIgnoreCase(v) || "Y".equalsIgnoreCase(v) || "yes".equalsIgnoreCase(v);
    }


    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val){
        return toLong(val).intValue();
    }



    /**
     * {@link String}转换为{@link Integer}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link Integer}值。
     * @param value 要转换成{@link Integer}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static Integer toInt(String value, Integer defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        // 字符串中包含小数点，Integer.valueOf会报异常，先转换为BigDecimal，返回其整数部分
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return d.intValue();
            } catch (Exception e) {
                return defaultValue;
            }
        }

        // 当做整数字符串处理
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }



    /**
     * 得到int 获取转换的int值，有错返回0
     *
     * @param str 带转换的字符串
     * @return int
     */
    public static int toInt(String str){
        return toInt(str, 0);
    }


    public static int toInt(String str, int defValue){
        if(str == null){return defValue;}
        str = str.trim();
        if(str.equals(SPACE)){return defValue;}
        str = str.replaceAll(COMMA, SPACE).replaceAll("，", SPACE);
        if(str.contains(DOT))
            str = str.substring(0, str.indexOf(DOT.charAt(0)));
        try{
            return Integer.valueOf(str);
        }catch(Exception e){
            return defValue;
        }
    }


    // comma separated number
    public static String toStringFromLong(long number){
        return String.format(Locale.getDefault(), "%,d", number);
    }


    /**
     * 取得重复字串
     *
     * @param repeatString 重复字串
     * @param count 重复次数
     * @return String
     */
    public static String repeatString(String repeatString, int count){
        if(count <= 0) return "";
        StringBuilder ret = new StringBuilder(repeatString.length() * count);
        for(int i = 1; i <= count; i++){
            ret.append(repeatString);
        }
        return ret.toString();
    }


    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html){
        if(isBlank(html)) return "";
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        return m.replaceAll("");
    }


    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html){
        if(html == null){
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }


    /**
     * Removes all spaces from a string
     * 可以替换大部分空白字符， 不限于空格,\s 可以匹配空格、制表符、换页符等空白字符的其中任意一个
     */
    public static String removeSpaces(String str){
        return str.replaceAll("\\s*", "");
    }


    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str 目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length){
        if(str == null) return "";
        try{
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for(char c : replaceHtml(StringEscapeUtils.unescapeHtml(str)).toCharArray()){
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if(currentLength <= length - 3){
                    sb.append(c);
                }else{
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param param 目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr2(String param, int length){
        if(param == null) return "";
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for(int i = 0; i < param.length(); i++){
            temp = param.charAt(i);
            if(temp == '<'){
                isCode = true;
            }else if(temp == '&'){
                isHTML = true;
            }else if(temp == '>' && isCode){
                n = n - 1;
                isCode = false;
            }else if(temp == ';' && isHTML){
                isHTML = false;
            }
            try{
                if(!isCode && !isHTML){
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }

            if(n <= length - 3){
                result.append(temp);
            }else{
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        final String regex = "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML" +
                "|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area" +
                "|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex" +
                "|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>";
        temp_result = temp_result.replaceAll(regex, "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = new ArrayList<>();
        while(m.find()){
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for(int i = endHTML.size() - 1; i >= 0; i--){
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }


    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s){
        if(s == null){
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s){
        if(s == null){
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);

            if(c == SEPARATOR){
                upperCase = true;
            }else if(upperCase){
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }else{
                sb.append(c);
            }
        }

        return sb.toString();
    }


    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s){
        if(s == null){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if(i < (s.length() - 1)){
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if((i > 0) && Character.isUpperCase(c)){
                if(!upperCase || !nextUpperCase){
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            }else{
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 俩整型间随机数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int random(int begin, int end){
        return new Random().nextInt((end - begin + 1)) + begin;
    }


    /**
     * InputStream convert to string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException{
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for(int n; (n = in.read(b)) != -1; ){
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    /**
     * 字符串拼接
     * String 使用+  一次+运算会涉及到4个对象  使用StringBuilder会节省n-1次
     *
     * @param strings
     * @return
     */
    public static String splitJoint(String... strings){
        int capacity = 0;
        for(String string : strings){
            if(string == null) continue;
            capacity += string.length();
        }
        StringBuilder sb = new StringBuilder(capacity);
        for(String string : strings){
            sb.append(string);
        }
        return sb.toString();
    }

//    public static String getFullPinYin(String source){
//        if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) {
//            //Log.e("nulllllll", "tokens-null---------");
//            return source;
//        }
//        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source);
//        if (tokens == null || tokens.size() == 0) {
//            return source;
//        }
//        StringBuffer result = new StringBuffer();
//        for (Token token : tokens) {
//            if (token.type == Token.PINYIN) {
//                result.append(token.target);
//            } else {
//                result.append(token.source);
//            }
//        }
//        return result.toString();
//    }
//
//    public static String getFirstPinYin(String source){
//
//        if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) {
//            //Log.e("nul", "tokens-null---------");
//            return source;
//        }
//        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source);
//        if (tokens == null || tokens.size() == 0) {
//            //Log.e("nullll222", "tokens-null---------");
//            return source;
//        }
//        StringBuffer result = new StringBuffer();
//        for (Token token : tokens) {
//            if (token.type == Token.PINYIN) {
//                result.append(token.target.charAt(0));
//            } else {
//                result.append("#");
//            }
//        }
//        return result.toString();
//    }

}