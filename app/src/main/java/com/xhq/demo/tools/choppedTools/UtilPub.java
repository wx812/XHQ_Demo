package com.xhq.demo.tools.choppedTools;

import com.xhq.demo.tools.StringUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: AKhh
 * Date: 12-12-26 下午11:55
 * 基础工具类；
 */
@SuppressWarnings("UnusedDeclaration")
public class UtilPub{
    private static SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dLFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat dLFormat2 = new SimpleDateFormat("yyyyMMdd");
    private static DecimalFormat df2Format = new DecimalFormat("###,###,###,###,##0.00");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isEmpty(Object[] o) {
        return o == null || o.length == 0;
    }

    public static boolean isEmptyStr(String str) {
        return str == null || str.trim().length() == 0 || "-".equals(str) || "-1".equals(str);
    }

    //判断Id是否为空id,有可能是"-","-1"
    public static boolean isEmptyId(String id) {
        return id == null || id.trim().length() == 0 || "0".equals(id) || "-".equals(id) || "-1".equals(id) || "null".equals(id) || "(无)".equals(id) ||"".equals(id);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmptyId(String str) {
        return !isEmptyId(str);
    }

    public static boolean isNotEmptyStr(String str) {
        return !isEmptyStr(str);
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Object[] o) {
        return !isEmpty(o);
    }

    /**
     * 获取浮点数(有错误默认为0)，可以识别金额中的逗号格式
     *
     * @param str 带转换的字符串
     * @return 浮点数
     */
    public static double getDoubleIgnoreErr(String str) {
        if (str == null)
            return 0.0;
        str = str.trim();
        if (str.equals(""))
            return 0.0;
        str = str.replaceAll(",", "").replaceAll("，", "");
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * 得到int 获取转换的int值，有错返回0
     *
     * @param str 带转换的字符串
     * @return int
     */
    public static int getIntIgnoreErr(String str) {
        return getIntIgnoreErr(str, 0);
    }

    public static int getIntIgnoreErr(String str, int defValue) {
        if (str == null)
            return defValue;
        str = str.trim();
        if (str.equals(""))
            return defValue;
        str = str.replaceAll(",", "").replaceAll("，", "");
        if (str.contains("."))
            str = str.substring(0, str.indexOf('.'));
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 得到Long 获取转换的Long值，有错返回0
     *
     * @param str 带转换的字符串
     * @return long
     */
    public static long getLongIgnoreErr(String str) {
        return getLongIgnoreErr(str, 0L);
    }

    public static long getLongIgnoreErr(String str, long defValue) {
        if (str == null)
            return defValue;
        str = str.trim();
        if (str.equals(""))
            return defValue;
        str = str.replaceAll(",", "").replaceAll("，", "");
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static String getDateStrIgnoreNull(Timestamp obj) {
        if (obj == null)
            return "";
        return dFormat.format(obj);
    }

    public static String getTimeStrIgnoreNull(Timestamp obj) {
        if (obj == null)
            return "";
        return tFormat.format(obj);
    }

    public static Date getCurDate() {
        return new Date(System.currentTimeMillis());
    }

    public static String getCurDateStr() {
        return dFormat.format(new Date(System.currentTimeMillis()));
    }

    public static Timestamp getCurDatetime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getCurDatetimeStr() {
        return dtFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Func:为空，返回""
     *
     * @param str 带检查的字符串
     * @return ""
     */
    public static String checkNull(String str) {
        if (isEmpty(str))
            return "";
        return str.trim();
    }

    /**
     * 功能：null过滤，返回默认值
     *
     * @param strValue     带检查的字符串
     * @param defaultValue 为空返回的字符串
     * @return str
     */
    public static String checkNull(String strValue, String defaultValue) {
        return strValue == null ? defaultValue: strValue;
    }

    public static String checkNull(Integer n) {
        return checkNull(n, true);
    }

    public static String checkNull(Integer n, boolean zeroToEmpty) {
        int v;
        if (n == null || n == 0)
            v = 0;
        else
            v = n;
        return v != 0 ? String.valueOf(v): zeroToEmpty ? "": "0";
    }

    public static String checkNull(Long n) {
        return checkNull(n, 0L);
    }

    public static String checkNull(Long n, long defValue) {
        return n != null ? String.valueOf(n): String.valueOf(defValue);
    }

    public static String checkNull(Double n) {
        return checkNull(n, 0d);
    }

    public static String checkNull(Double n, double defValue) {
        double d = n != null ? n: defValue;
        return UtilPub.getDoubleStr(d);
    }

    /**
     * Func:取当前年
     *
     * @return ""
     */
    public static String getThisYear() {
        Calendar tlpDate = new GregorianCalendar();
        tlpDate.setTime(getCurDate());
        return String.valueOf(tlpDate.get(Calendar.YEAR));
    }

    /**
     * Func:取当前月
     *
     * @return ""
     */
    public static String getThisMonth() {
        Calendar tlpDate = new GregorianCalendar();
        tlpDate.setTime(getCurDate());
        return String.valueOf(tlpDate.get(Calendar.MONTH));
    }

    /**
     * 功能：空值过滤，返回默认值
     *
     * @param strValue     待检查的字符串
     * @param defaultValue 为空返回的字符串
     * @return str
     */
    public static String checkEmpty(String strValue, String defaultValue) {
        return isEmpty(strValue) ? defaultValue: strValue;
    }

    /**
     * 功能：空值过滤，返回默认值
     *
     * @param strValue     待检查的字符串
     * @return str
     */
    public static String checkEmpty(String strValue) {
        return checkEmpty(strValue, "");
    }

    public static String checkEmptyId(String strValue, String defaultValue) {
        return isEmptyId(strValue) ? defaultValue: strValue;
    }

    public static String getOrigMsg(Throwable e) {
        String s = e.getMessage();
        Throwable t = e.getCause();
        while (t != null) {
            s = t.getMessage();
            t = t.getCause();
        }
        return s;
    }

    //计算一个字符串source的长度(英文和数字占一个长度,其他字符(中文和特殊符号等)占2个长度)
    public static int strLength(String source) {
        int totalLength = source.length();// 总长度
        String otherStr = source.replaceAll("\\d|\\w", "");// 去掉字符串中的数字和英文字符
        int otherLength = otherStr.length();// 占2个长度的字符
        return (totalLength - otherLength) + otherLength * 2;
    }

    /**
     * 格式化最后修改时间
     *
     * @param strTime YYYYMMDDhhmmss
     * @return YYYY-MM-DD hh:mm:ss
     */
    public static String checkLastTime(String strTime) {
        if (isEmpty(strTime))
            return "";
        return checkLastTime(getLongIgnoreErr(strTime));
    }

    public static String checkLastTime(long lngTime) {
        if (0 >= lngTime)
            return "";
        String s1 = String.valueOf(lngTime + "").trim();
        if (s1.length() != 14)
            return "";
        return s1.substring(0, 4).concat("-").concat(s1.substring(4, 6)).concat("-").concat(s1.substring(6, 8)).concat(" ").concat(s1.substring(8, 10)).concat(":").concat(s1.substring(10, 12)).concat(":").concat(s1.substring(12));
    }

    /**
     * 格式化最后修改时间
     *
     * @param strTime YYYYMMDDhhmmss
     * @return YYYY-MM-DD hh:mm:ss
     */
    public static String getTimeFormatValue(String strTime) {
        if (isEmpty(strTime))
            return "";
        return getTimeFormatValue(getLongIgnoreErr(strTime));
    }

    public static String getTimeFormatValue(long lngTime) {
        if (lngTime <= 0L) return "";
        String s1 = String.valueOf(lngTime + "").trim();
        if (s1.length() == 8) {//日期
            return s1.substring(0, 4).concat("-").concat(s1.substring(4, 6)).concat("-").concat(s1.substring(6, 8));
        }
        if (s1.length() == 6) {//时间
            return s1.substring(0, 2).concat(":").concat(s1.substring(2, 4)).concat(":").concat(s1.substring(4, 6));
        }
        if (s1.length() == 14) {//日期
            s1.substring(0, 4).concat("-").concat(s1.substring(4, 6)).concat("-").concat(s1.substring(6, 8)).concat(" ").concat(s1.substring(8, 10)).concat(":").concat(s1.substring(10, 12)).concat(":").concat(s1.substring(12));
        }
        return "";
    }

    //将格式化后的日期转成long
    public static long getTimeDbValue(String strTime) {
        if (StringUtils.isEmptyId(strTime)) return -1L;

        return UtilPub.getLongIgnoreErr(strTime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
    }

    /**
     * 取最后修改时间
     *
     * @return Long
     */
    public static Long getLastTime() {
        return getLongIgnoreErr(dLFormat.format(new Timestamp(System.currentTimeMillis())));
    }

    /**
     * 取最后修改日期
     *
     * @return
     */
    public static Long getLastDate() {
        return Long.valueOf(dLFormat2.format(new Timestamp(System.currentTimeMillis())));
    }

    public static String getAmountStr(double amount) {
        return df2Format.format(amount);
    }

    public static String getAmountStr(double amount, boolean zeroShowEmpty) {
        if (zeroShowEmpty && amount>0.000001 && amount < 0.00001)
            return "";
        else
            return df2Format.format(amount);
    }

    public static String getIntStr(int d) {
        if (d == 0)
            return "";
        else
            return String.valueOf(d);
    }

    /**
     * 截取右边的0串
     *
     * @param codeStr
     * @return
     */
    public static String cutZero(String codeStr) {
        int j = 0;
        int len = codeStr.length() - 1;
        for (int i = len; i > -1; i--) {
            if ('0' == codeStr.charAt(i)) {
                j++;
            } else {
                break;
            }
        }
        return codeStr.substring(0, len - j + 1);
    }

    /**
     * 右边补0串
     *
     * @param codeLen
     * @param cutedCode
     * @return
     */
    public static String fillZero(int codeLen, String cutedCode) {
        StringBuilder codeStr = new StringBuilder(20).append(cutedCode);
        for (int len = cutedCode.length(), i = 0; i < codeLen - len; i++) {
            codeStr.append("0");
        }
        return codeStr.toString();
    }


    public static String getDateStr(Object _dateTime) { //yyyymmddhhMMss
        if (_dateTime == null) return "";
        String dateTime = String.valueOf(_dateTime);
        if (StringUtils.isEmpty(dateTime) || dateTime.length() < 7) {
            return "";
        } else {
            String s1 = String.valueOf(dateTime);
            return s1.substring(0, 4) + "-" + s1.substring(4, 6) + "-" + s1.substring(6, 8);
        }
    }

    public static String getDateTimeStr(Object _dateTime) { //yyyymmddhhMMss
        String dateTime = String.valueOf(_dateTime);
        if (StringUtils.isEmpty(dateTime) || dateTime.length() < 14) {
            return "";
        } else {
            String s1 = String.valueOf(dateTime);
            return s1.substring(0, 4) + "-" + s1.substring(4, 6) + "-" + s1.substring(6, 8);// + " " + s1.substr(8, 10) + ":" + s1.substr(10, 12) + ":" + s1.substr(12);
        }
    }

    //数字转字符串，如无小数，则去掉.00
    public static String getDoubleStr(double d) {
        return getAmountStr(d).replaceAll(",", "").replaceAll("\\.00", "");
    }

    //将List转为sql语句in后面的常量，'XXX','XXX'格式
    public static String getSqlInstr(List<String> list) {
        if (StringUtils.isEmpty(list)) return "";
        StringBuilder sb = new StringBuilder(256);
        for (String s : list) sb.append(",'").append(s).append("'");
        return sb.substring(1);
    }
    /**
     * 判断字符串是否只包含unicode数字。
     * <p/>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p/>
     *
     * <pre>
     * StringUtil.isNumeric(null)   = false
     * StringUtil.isNumeric("")     = true
     * StringUtil.isNumeric("  ")   = false
     * StringUtil.isNumeric("123")  = true
     * StringUtil.isNumeric("12 3") = false
     * StringUtil.isNumeric("ab2c") = false
     * StringUtil.isNumeric("12-3") = false
     * StringUtil.isNumeric("12.3") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode数字组成，则返回<code>true</code>
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

}
