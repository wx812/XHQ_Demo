package com.xhq.demo.tools.dateTimeTools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.xhq.demo.tools.appTools.AppUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
 * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
 * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
 * </p>
 * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br>
 * <table border="1" cellspacing="1" cellpadding="1" summary="Chart shows pattern letters, date/time component,
 * presentation, and examples.">
 * <tr>
 * <th align="left">字母</th>
 * <th align="left">日期或时间元素</th>
 * <th align="left">表示</th>
 * <th align="left">示例</th>
 * </tr>
 * <tr>
 * <td><code>G</code></td>
 * <td>Era 标志符</td>
 * <td>Text</td>
 * <td><code>AD</code></td>
 * </tr>
 * <tr>
 * <td><code>y</code> </td>
 * <td>年 </td>
 * <td>Year </td>
 * <td><code>1996</code>; <code>96</code> </td>
 * </tr>
 * <tr>
 * <td><code>M</code> </td>
 * <td>年的月份 </td>
 * <td>Month </td>
 * <td><code>July</code>; <code>Jul</code>; <code>07</code> </td>
 * </tr>
 * <tr>
 * <td><code>w</code> </td>
 * <td>年的周数 </td>
 * <td>Number </td>
 * <td><code>27</code> </td>
 * </tr>
 * <tr>
 * <td><code>W</code> </td>
 * <td>月份的周数 </td>
 * <td>Number </td>
 * <td><code>2</code> </td>
 * </tr>
 * <tr>
 * <td><code>D</code> </td>
 * <td>年的天数 </td>
 * <td>Number </td>
 * <td><code>189</code> </td>
 * </tr>
 * <tr>
 * <td><code>d</code> </td>
 * <td>月份的天数 </td>
 * <td>Number </td>
 * <td><code>10</code> </td>
 * </tr>
 * <tr>
 * <td><code>F</code> </td>
 * <td>月份的星期 </td>
 * <td>Number </td>
 * <td><code>2</code> </td>
 * </tr>
 * <tr>
 * <td><code>E</code> </td>
 * <td>星期的天数 </td>
 * <td>Text </td>
 * <td><code>Tuesday</code>; <code>Tue</code> </td>
 * </tr>
 * <tr>
 * <td><code>a</code> </td>
 * <td>Am/pm 标记 </td>
 * <td>Text </td>
 * <td><code>PM</code> </td>
 * </tr>
 * <tr>
 * <td><code>H</code> </td>
 * <td>一天小时数（0-23） </td>
 * <td>Number </td>
 * <td><code>0</code> </td>
 * </tr>
 * <tr>
 * <td><code>k</code> </td>
 * <td>一天小时数（1-24） </td>
 * <td>Number </td>
 * <td><code>24</code> </td>
 * </tr>
 * <tr>
 * <td><code>K</code> </td>
 * <td>am/pm 小时数（0-11） </td>
 * <td>Number </td>
 * <td><code>0</code> </td>
 * </tr>
 * <tr>
 * <td><code>h</code> </td>
 * <td>am/pm 小时数（1-12） </td>
 * <td>Number </td>
 * <td><code>12</code> </td>
 * </tr>
 * <tr>
 * <td><code>m</code> </td>
 * <td>小时分钟数 </td>
 * <td>Number </td>
 * <td><code>30</code> </td>
 * </tr>
 * <tr>
 * <td><code>s</code> </td>
 * <td>分钟的秒数 </td>
 * <td>Number </td>
 * <td><code>55</code> </td>
 * </tr>
 * <tr>
 * <td><code>S</code> </td>
 * <td>毫秒数 </td>
 * <td>Number </td>
 * <td><code>978</code> </td>
 * </tr>
 * <tr>
 * <td><code>z</code> </td>
 * <td>时区 </td>
 * <td>General time zone </td>
 * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code> </td>
 * </tr>
 * <tr>
 * <td><code>Z</code> </td>
 * <td>时区 </td>
 * <td>RFC 822 time zone </td>
 * <td><code>-0800</code> </td>
 * </tr>
 * </table>
 * <pre>
 *                          HH:mm    15:44
 *                         h:mm a    3:44 下午
 *                        HH:mm z    15:44 CST
 *                        HH:mm Z    15:44 +0800
 *                     HH:mm zzzz    15:44 中国标准时间
 *                       HH:mm:ss    15:44:40
 *                     yyyy-MM-dd    2016-08-12
 *               yyyy-MM-dd HH:mm    2016-08-12 15:44
 *            yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
 *       yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
 *  EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
 *       yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
 *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
 *   yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
 *                         K:mm a    3:44 下午
 *               EEE, MMM d, ''yy    星期五, 八月 12, '16
 *          hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
 *   yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
 *     EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
 *                  yyMMddHHmmssZ    160812154440+0800
 *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
 *
 * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
 * </pre>
 * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
 * <p>
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/10/30.
 *     Desc  : date utils. need the jar of Joda-Time
 *     Updt  : 2017/12/14
 * </pre>
 * <p>IBM的一位工程师开源的 Joda-Time提供了一组Java类包用于处理包括ISO8601标准在内的date和time。
 * 可以利用它把JDK Date和Calendar类完全替换掉，而且仍然能够提供很好的集成.
 */
public class DateTimeUtils{

    public static final int TIME_MSEC = 1;
    public static final int TIME_SEC = 2;
    public static final int TIME_MIN = 3;
    public static final int TIME_HOUR = 4;
    public static final int TIME_DAY = 5;
    public static final int SEC  = 1000; // 秒与毫秒的倍数
    public static final int MIN  = 60000; // 分与毫秒的倍数
    public static final int HOUR = 3600000; // 时与毫秒的倍数
    public static final int DAY  = 86400000; // 天与毫秒的倍数

    public static final String PATTERN_yMd = "yyyy-MM-dd";
    public static final String PATTERN_yMdHms = "yyyy-MM-dd HH:mm:ss";


    /**
     * return DateTime
     *
     * @param obj obj can be, eg: Date, Calendar,
     * <br>String --> date String,
     * <br>long --> ReadableInstant(time millisecond value)
     * @return datetime
     */
    public static DateTime getDateTime(Object obj){
        return new DateTime(obj);
    }


    /**
     * get the object of the current dateTime
     *
     * @return Current DateTime object
     */
    public static DateTime getCurrDateTimeObj(){
        return getDateTime(System.currentTimeMillis());
    }


    public static long format2Long(Date date){
        return getDateTime(date).getMillis();
    }


    /**
     * according to formatPattern format date object
     *
     * @param date date object
     * @param formatPattern {@link #format2String(long, String)}
     * @return string
     */
    public static String format2String(Date date, String formatPattern){
        if(date == null) return null;
//        DateTime dateTime = new DateTime(date).minusMonths(1);
        return getDateTime(date).toString(formatPattern);
    }


    /**
     * According to formatPattern format time millisecond value
     *
     * @param millis Time millisecond value
     * @param formatPattern format pattern of date time. eg: "y/m/d", "ymd", "y-m-d" etc.
     * @return string
     */
    public static String format2String(long millis, String formatPattern){
        DateTime dateTime = getDateTime(millis).minusMonths(1);
        return dateTime.toString(formatPattern);
    }


    /**
     * time millisecond value transform into the date object
     *
     * @param millis millisecond value
     * @return date object
     */
    public static Date format2Date(long millis){
        return getDateTime(millis).toDate();
    }


    /**
     * see {@link #format2String(long, String)}
     */
    @Deprecated
    public static String millis2String(long millis, String formatPattern){
        return new SimpleDateFormat(formatPattern, Locale.getDefault()).format(new Date(millis));
    }


    /**
     * the date string into a date object
     *
     * @param dateStr date string
     * @return Date object
     */
    public static Date format2Date(String dateStr){
        return getDateTime(dateStr).toDate();
    }


    /**
     * use {@link #format2Date(String)} instead
     */
    @Deprecated
    public static Date string2Date(String dateStr, String formatPattern){
        return new Date(string2Millis(dateStr, formatPattern));
    }


    /**
     * parse date string to date object
     *
     * @param dateStr date time string
     * @param formatPattern format pattern of date time. eg: "y/m/d", "ymd", "y-m-d" etc.
     */
    public static Date parse2DateObj(@NonNull String dateStr, @NonNull String formatPattern){
        if(TextUtils.isEmpty(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatPattern);
        DateTime dateTime = DateTime.parse(dateStr, formatter);
        return dateTime.toDate();
    }


    /**
     * the date string into a millisecond value
     *
     * @param dateStr date string
     * @return time millisecond value
     */
    public static long format2Long(String dateStr){
        return getDateTime(dateStr).getMillis();
    }


    /**
     * use {@link #format2Long(String)}
     */
    @Deprecated
    public static long string2Millis(String dateStr, String formatPattern){
        try{
            return new SimpleDateFormat(formatPattern, Locale.getDefault()).parse(dateStr).getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * according to "yMd" mode format date object
     *
     * @param date date object
     * @return {@link #format2yMd}
     */
    public static String format2yMd(Date date){
        return format2String(date, PATTERN_yMd);
    }


    /**
     * according to "yMd" mode format timeMillis value
     *
     * @param millis @see {@link #format2String(long, String)}
     * @return the format eg: 2016-1-16
     */
    public static String format2yMd(long millis){
        return format2String(millis, PATTERN_yMd);
    }


    /**
     * according to "yMdHms" mode format date object
     *
     * @param date date object
     * @return {@link #format2yMdHms(long)}
     */
    public static String format2yMdHms(Date date){
        return format2String(date, PATTERN_yMdHms);
    }


    /**
     * according to "yMdHms" mode format timeMillis value
     *
     * @param millis @see {@link #format2String(long, String)}
     * @return the format eg: 2016-1-16 13:04:11
     */
    public static String format2yMdHms(long millis){
        return format2String(millis, PATTERN_yMdHms);
    }


    /**
     * According to formatting mode get the current time
     *
     * @param formatPattern format pattern
     * @return Corresponding format time
     */
    public static String getCurrDateTime(String formatPattern){
//        SimpleDateFormat format = new SimpleDateFormat(formatPattern, Locale.CHINA);
//        return format.format(new Date(System.currentTimeMillis())); // 月份会增加1
        return getCurrDateTimeObj().toLocalDateTime().toString(formatPattern);
    }


    /**
     * get the current time in "yMd" pattern
     *
     * @return the format eg: 2016-1-16
     */
    public static String getCurrDate_yMd(){
        return getCurrDateTime(PATTERN_yMd);
    }


    /**
     * get the current time in "yMdHms" pattern
     *
     * @return the format eg: 2016-1-16 13:04:11
     */
    public static String getCurrDateTime_yMdHms(){
        return getCurrDateTime(PATTERN_yMdHms);
    }


    //获取某年某月的总天数
    public static int getTotalDaysFromMonth(int year, int month){
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if(isLeapYear(year)){
                    return 29;
                }else{
                    return 28;
                }
        }
        return 100;
    }


    /**
     * for a week in the week
     *
     * @param millis millisecond value
     * @return see {@link org.joda.time.DateTimeConstants}.
     * <br> 1 ~ 7 --> "monday ~ sunday",  eg: sunday(7)
     */
    public static int getWeek(long millis){
        return getDateTime(millis).getDayOfWeek();
    }


    public static String getWeek(Date date){
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }


    public static int getWeekOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_MONTH);
    }


    public static int getWeekOfYear(long millis){
        return getDateTime(millis).getWeekOfWeekyear();
    }


    /**
     * Get the date of the current time for a week --> monday ~ sunday , 7 days
     *
     * @return the format eg: 2016-1-16
     */
    public static String getLastWeekDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format_yMd = new SimpleDateFormat(PATTERN_yMd, Locale.CHINA);
        c.setTime(new Date());
        c.add(Calendar.DATE, -6);
        Date d = c.getTime();
        return format_yMd.format(d);
    }


    /**
     * intercept date string.
     *
     * @param dateStr eg: 20171122
     * @return string array
     */
    public static String[] subDateStr(String dateStr){
        if(TextUtils.isEmpty(dateStr)) return null;
        String y = dateStr.substring(0, 4);
        String m = dateStr.substring(4, 6);
        String d = dateStr.substring(6, dateStr.length());
        return new String[]{y, m, d};
    }


    /**
     * intercept time string
     *
     * @param timeStr eg: 221030
     * @return string array
     */
    public static String[] subTimeStr(String timeStr){
        if(TextUtils.isEmpty(timeStr)) return null;
        String h = timeStr.substring(0, 2);
        String m = timeStr.substring(2, 4);
        String s = timeStr.substring(4, timeStr.length());
        return new String[]{h, m, s};
    }


    //判断是否闰年
    public static boolean isLeapYear(int year){
        return (year % 4 != 0) || (year % 100 == 0) && (year % 400 != 0);
    }


    /**
     * 判断是否是当前时间
     */
    public static boolean isCurrentDay(String date){
        String now;
        try{
            now = getCurrDateTime_yMdHms();
            return !TextUtils.isEmpty(now) && now.substring(0, 8).equals(date.substring(0, 8));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断是否同一天
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(long millis){
        long wee = (System.currentTimeMillis() / DAY) * DAY - 8 * HOUR;
        return millis >= wee && millis < wee + DAY;
    }


    //设置时间
    public static String setTime(long total){
        long hour = total / HOUR;
        long left1 = total % HOUR;
        long minute = left1 / MIN;
        long left2 = left1 % MIN;
        long second = left2 / SEC;
        return hour + "'" + minute + "'" + second + "''";
    }


    /**
     * duration --> eg: long hours:minutes:seconds  --> String "00:00:00"
     */
    public static String getDurationString(long duration){
//        long days = duration / DAY;
        long hours = (duration % DAY) / HOUR;
        long minutes = (duration % HOUR) / MIN;
        long seconds = (duration % MIN) / SEC;

        String hourStr = (hours < 10) ? "0" + hours : hours + "";
        String minuteStr = (minutes < 10) ? "0" + minutes : minutes + "";
        String secondStr = (seconds < 10) ? "0" + seconds : seconds + "";

        if(hours != 0){
            return hourStr + ":" + minuteStr + ":" + secondStr;
        }else{
            return minuteStr + ":" + secondStr;
        }
    }


    /**
     * 根据给定的小时和分钟数 返回 00:00 格式的时间字符串
     *
     * @param hourOfDay hour
     * @param minute minute
     * @return eg "00:00"
     */
    public static String getDurationString(int hourOfDay, int minute){
        String h = hourOfDay + "";
        String m = minute + "";
        if(h.length() == 1){
            h = "0" + h;
        }
        if(m.length() == 1){
            m = "0" + m;
        }
        return h + ":" + m;
    }


    public static String formatShowTime(String time, boolean show){
        String showTime;
        if(time.length() < 12) return time;
        if(isCurrentDay(time)){
//			showTime  = time.substring(11,16);
            showTime = time.substring(8, 10) + ":" + time.substring(10, 12);
        }else{
            if(show){
                showTime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8)
                        + " " + time.substring(8, 10) + ":" + time.substring(10, 12);
            }else{
                showTime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
            }

/*			showTime = time.substring(0, 4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)
					+" "+time.substring(8,10)+":"+time.substring(10,12);*/
        }
/*		if(DateUtil.getTimeDistance(time) == 0){
			showTime  = time.substring(11,16);
		}else {
			showTime = time.substring(0, 16);
		}*/
        return showTime;
    }


    public static String formatDateString(long time) {
        Context ctx = AppUtils.getAppContext();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(ctx);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(ctx);
        Date date = new Date(time);
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }


    /**
     * 获取以unit为单位的两个时间的时间差
     * see {@link #millis2TimeSpan(long, int)}
     */
    public static long getTimeSpan(String time0, String time1, int unit, String pattern) {
        return millis2TimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), unit);
    }

    /**
     * 毫秒时间戳转以unit为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *               <ul>
     *               <li>{@link #TIME_MSEC}: 毫秒</li>
     *               <li>{@link #TIME_SEC }: 秒</li>
     *               <li>{@link #TIME_MIN }: 分</li>
     *               <li>{@link #TIME_HOUR}: 小时</li>
     *               <li>{@link #TIME_DAY }: 天</li>
     *               </ul>
     * @return 以unit为单位的时间长度
     */
    public static long millis2TimeSpan(long millis, final int unit) {
        switch (unit) {
            default:
            case TIME_MSEC:
                return millis;
            case TIME_SEC:
                return millis / SEC;
            case TIME_MIN:
                return millis / MIN;
            case TIME_HOUR:
                return millis / HOUR;
            case TIME_DAY:
                return millis / DAY;
        }
    }

    /**
     * 以unit为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *                 <ul>
     *                 <li>{@link #TIME_MSEC}: 毫秒</li>
     *                 <li>{@link #TIME_SEC }: 秒</li>
     *                 <li>{@link #TIME_MIN }: 分</li>
     *                 <li>{@link #TIME_HOUR}: 小时</li>
     *                 <li>{@link #TIME_DAY }: 天</li>
     *                 </ul>
     * @return 毫秒时间戳
     */
    public static long timeSpan2Millis(long timeSpan, final int unit) {
        switch (unit) {
            default:
            case TIME_MSEC:
                return timeSpan;
            case TIME_SEC:
                return timeSpan * SEC;
            case TIME_MIN:
                return timeSpan * MIN;
            case TIME_HOUR:
                return timeSpan * HOUR;
            case TIME_DAY:
                return timeSpan * DAY;
        }
    }


    /**
     * 获取合两个时间的差,以汉字时间单位方式返回<p>time0和time1格式都为pattern</p>
     * see {@link #millis2FitTimeSpan(long, int)}
     */
    public static String getFitTimeSpan(String time0, String time1, int precision, String pattern) {
        long duration = Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern));
        return millis2FitTimeSpan(duration, precision);
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <ul>
     *                  <li>precision = 0，返回null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) return null;
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {DAY, HOUR, MIN, SEC, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }



    /**
     * 获取时间差
     */
    public static int getTimeDistance(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String now = format.format(new Date());
//        String now = getCurrDateTime_yMdHms();
        try{
            long from = format.parse(date).getTime();
            long to = format.parse(now).getTime();
            int days = (int)((to - from) / (DAY));
            if(days == 0){      //一天以内，以分钟或者小时显示
                return 0;
            }else if(days == 1){
                return 1;
            }else{
                return 1;
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 以汉字显示时间
     */
    public static String getTimesToNow(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String now = format.format(new Date());
        String returnText = null;
        try{
            long from = format.parse(date).getTime();
            long to = format.parse(now).getTime();
            int days = (int)((to - from) / (DAY));
            if(days == 0){      //一天以内，以分钟或者小时显示
                int hours = (int)((to - from) / (HOUR));
                if(hours == 0){
                    int minutes = (int)((to - from) / (MIN));
                    if(minutes == 0){
                        returnText = "刚刚";
                    }else{
                        returnText = minutes + "分钟前";
                    }
                }else{
                    returnText = hours + "小时前";
                }
            }else if(days == 1){
                returnText = "昨天";
            }else{
                returnText = days + "天前";
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return returnText;
    }


    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    public static String getFriendlyTimeSpanByNow(long millis){
        long now = System.currentTimeMillis();
        long span = now - millis;
        if(span < 0)
            // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
            return String.format("%tc", millis);
        if(span < SEC){
            return "刚刚";
        }else if(span < MIN){
            return String.format("%d秒前", span / SEC);
        }else if(span < HOUR){
            return String.format("%d分钟前", span / MIN);
        }
        // 获取当天00:00
        long wee = (now / DAY) * DAY - 8 * HOUR;
        if(millis >= wee){
            return String.format("今天%tR", millis);
        }else if(millis >= wee - DAY){
            return String.format("昨天%tR", millis);
        }else{
            return String.format("%tF", millis);
        }
    }



    /**
     * get zodiac
     *
     * @param date date
     * @return zodiac
     */
    public static String getZodiac(Date date){
        final String[] ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return ZODIAC[cal.get(Calendar.YEAR) % 12];
    }


    /**
     * get Constellation
     *
     * @param millis time millisecond value
     * @return constellation
     */
    public static String getConstellation(long millis) {
        int month = getDateTime(millis).getMonthOfYear();
        int day = getDateTime(millis).getDayOfMonth();
        return getConstellation(month, day);
    }

    public static String getConstellation(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getConstellation(month, day);
    }


    /**
     * get constellation
     *
     * @param month moth
     * @param day day
     * @return constellation
     */
    public static String getConstellation(int month, int day){

        final String[] CONSTELLATION = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
                "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
        return CONSTELLATION[day >= ZODIAC_FLAGS[month - 1] ? month - 1 : (month + 10) % 12];
    }


    public static String getSysTimeZone(){
        TimeZone tz = TimeZone.getDefault();
//        TimeZone tz = TimeZone.getTimeZone("Asia/WuHan");
        return "TimeZone:"+tz.getDisplayName(false, TimeZone.SHORT)+" TimeZone id : " +tz.getID();
    }


    public static void hideDayInDatePicker(DatePicker datePicker){
        try{
            /* 处理android5.0以上的特殊情况 */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if(daySpinnerId != 0){
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if(daySpinner != null) daySpinner.setVisibility(View.GONE);
                }
            }else{
                Field[] datePickerFields = datePicker.getClass().getDeclaredFields();
                for(Field datePickerField : datePickerFields){
                    if("mDaySpinner".equals(datePickerField.getName())
                            || ("mDayPicker").equals(datePickerField.getName())){
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try{
                            dayPicker = datePickerField.get(datePicker);
                        }catch(IllegalAccessException | IllegalArgumentException e){
                            e.printStackTrace();
                        }
                        ((View)dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
