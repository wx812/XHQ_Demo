package com.xhq.demo.tools.dateTimeTools;

import com.xhq.demo.tools.NumberUtil;
import com.xhq.demo.tools.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA. User: AKhh Date: 12-12-20 下午11:43 To change this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("UnusedDeclaration")
public class DateUtil{
    final static int[] SEASON_BEGIN_TABLE = new int[]{
            Calendar.JANUARY, Calendar.JANUARY, Calendar.JANUARY,
            Calendar.APRIL, Calendar.APRIL, Calendar.APRIL,
            Calendar.JULY, Calendar.JULY, Calendar.JULY,
            Calendar.OCTOBER, Calendar.OCTOBER, Calendar.OCTOBER
    };
    private static TimeZone mTimeZone = TimeZone.getTimeZone("Asia/Shanghai");


    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    public static final String PATTERN_yMd = "yyyy-MM-dd";
    public static final String PATTERN_yMdHms = "yyyy-MM-dd HH:mm:ss";

    public static final int FIVE_MINUTES = 5 * 60 * 1000;


    /**
     * Return a Timestamp for right now
     *
     * @return Timestamp for right now
     */
    public static Timestamp nowTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * 获取今日
     *
     * @return 获取今日java.sql.Date类型日期；
     */
    public static java.sql.Date nowDate(){
        return new java.sql.Date(System.currentTimeMillis());
    }


    public static String nowYear(String stddate){
        return stddate.substring(0, 4);
    }


    public static int nowHour(){
        Calendar calendar = getGMT8Calendar();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * return a.minute() + minutes
     */
    public static java.sql.Date getAfterMinute(Date d, int minutes){
        if(d == null) return null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(mTimeZone);
        gc.setTime(d);
        gc.add(GregorianCalendar.MINUTE, minutes);
        return new java.sql.Date(gc.getTimeInMillis());
    }


    /**
     * 得到date + days的日期
     *
     * @param dt 日期
     * @param days 间隔天数，正数往前，负数往后
     * @return String java.sql.Date.toString()
     */
    public static String getAfterDays(Date dt, int days){
        if(dt == null) return "";
        if(days == 0) return dt.toString();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(mTimeZone);
        gc.setTime(dt);
        gc.add(GregorianCalendar.DATE, days);
        java.sql.Date ret = new java.sql.Date(gc.getTimeInMillis());
        return ret.toString();
    }


    /**
     * 得到date + days的日期
     *
     * @param date 日期(yyyy-mm-dd)
     * @param days 间隔天数，正数往前，负数往后
     * @param include67 是否包含星期六和星期日
     */
    public static String getAfterDays(String date, int days, boolean include67){
        if(include67 || days == 0) return getAfterDays(date, days);
        //要剔除信息，需要特殊处理
        int absd = Math.abs(days);
        int step = days > 0 ? 1 : -1;
        java.sql.Date in = toDateDefaultNow(date);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(mTimeZone);
        gc.setTimeInMillis(in.getTime());
        //这里要判断days是正数还是负数，正数是未来，负数是过去
        for(int d = 0; d < absd; ){
            //获取当前是星期几；
            int w = gc.get(GregorianCalendar.DAY_OF_WEEK);
            // [ 星期日 | w | 星期六 ]
            if(w > GregorianCalendar.SUNDAY && w < GregorianCalendar.SATURDAY){
                ++d;
            }
            // 向前或向后逐天循环；
            gc.add(GregorianCalendar.DATE, step);
        }
        java.sql.Date dr = new java.sql.Date(gc.getTimeInMillis());
        return dr.toString();
    }


    public static java.sql.Date getAfterDate(Date dt, int days){
        if(dt == null) return null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);
        gc.add(GregorianCalendar.DATE, days);
        return new java.sql.Date(gc.getTimeInMillis());
    }


    /**
     * 得到date + days的日期
     *
     * @param dt 日期(yyyy-mm-dd)
     * @param days 间隔天数，正数往前，负数往后
     * @return String java.sql.Date.toString()
     */
    public static String getAfterDays(String dt, int days){
        if(days == 0) return dt;
        try{
            return getAfterDays(java.sql.Date.valueOf(dt), days);
        }catch(Exception e){
            return "";
        }
    }


    /**
     * 取与指定日期间隔月的日期(yyyy-mm-dd)
     *
     * @return String java.sql.Date.toString()
     */
    public static String getAfterMonth(Date dt, int months){
        if(dt == null) return "";
        if(months == 0) return dt.toString();
        GregorianCalendar dy = new GregorianCalendar();
        dy.setTime(dt);
        dy.add(GregorianCalendar.MONTH, months);
        java.sql.Date ret = new java.sql.Date(dy.getTimeInMillis());
        return ret.toString();
    }


    /**
     * 取与指定日期间隔月的日期(yyyy-mm-dd)
     *
     * @param dt (yyyy-mm-dd)
     * @param months 间隔月
     * @return java.sql.Date.toString()
     */
    public static String getAfterMonth(String dt, int months){
        return getAfterMonth(java.sql.Date.valueOf(dt), months);
    }


    /**
     * 取与指定日期间隔年的日期
     *
     * @return String java.sql.Date.toString()
     */
    public static String getAfterYear(Date dt, int years){
        if(dt == null) return "";
        if(years == 0) return dt.toString();
        GregorianCalendar dy = new GregorianCalendar();
        dy.setTime(dt);
        dy.add(GregorianCalendar.YEAR, years);
        java.sql.Date ret = new java.sql.Date(dy.getTimeInMillis());
        return ret.toString();
    }


    /**
     * 取与指定日期间隔年的日期
     *
     * @param dt (yyyy-mm-dd)
     * @param years 间隔年         j
     * @return java.sql.Date.toString()
     */
    public static String getAfterYear(String dt, int years){
        return getAfterYear(java.sql.Date.valueOf(dt), years);
    }


    /**
     * get days between fromDate and thruDate
     *
     * @return the days between fromDate and thruDate
     */
    public static int getDifferDays(Date f, Date t) throws IllegalArgumentException{
        return getDifferSeconds(f, t) / (24 * 60 * 60);
    }


    public static int getDifferMinutes(Date f, Date t) throws IllegalArgumentException{
        return getDifferSeconds(f, t) / 60;
    }


    public static int getDifferMinutes(String f, String t) throws IllegalArgumentException{
        return getDifferSeconds(Timestamp.valueOf(f), Timestamp.valueOf(t)) / 60;
    }


    public static int getDifferSeconds(Date f, Date t) throws IllegalArgumentException{
        if(f == null || t == null) return 0;
        if(t.compareTo(f) < 0){
            String msg = "[" + t + "] 比" + "[" + f + "] 早, 应该相反!";
            throw new IllegalArgumentException(msg);
        }
        return (int)((t.getTime() - f.getTime()) / 1000);
    }


    /**
     * 判断strDate 是否是日期格式的字符串(支持如 yyyy-mm-dd)。
     *
     * @param strDate 字符串，判断其是否为日期格式。
     * @return boolean 如果为日期格式则返回=true;
     */
    public static boolean isDateFormat(String strDate){
        return isDateFormat(strDate, "YYYY-MM-DD");
    }


    /**
     * 判断strDate 是否是日期格式的字符串。
     *
     * @param strDate 字符串，判断其是否为日期格式。
     * @param format 字符串，YYYY-MM,YYYYMM,YYYYMMDD,YYYY-MM-DD,YYYY-MM-DD:HH, 空表示所有上述格式,非上述内容将默认为YYYY-MM-DD；
     * @return boolean 如果为日期格式则返回=true;
     */
    public static boolean isDateFormat(String strDate, String format){
        if(StringUtils.isEmpty(strDate)){
            return false;
        }
        if(StringUtils.isEmpty(format)){
            format = "YYYY-MM-DD";
        }
        switch(format){
            case "YYYY-MM":
                return strDate.matches("\\d{4}-\\d{2}") && isDateValue(strDate.substring(0, 4),
                                                                       strDate.substring(5), "01");
            case "YYYYMM":
                return strDate.matches("\\d{6}") && isDateValue(strDate.substring(0, 4), strDate.substring(4),
                                                                "01");
            case "YYYYMMDD":
                return strDate.matches("\\d{8}") && isDateValue(strDate.substring(0, 4), strDate.substring(4, 6),
                                                                strDate.substring(6));
            case "YYYY-MM-DD":
                return strDate.matches("\\d{4}-\\d{2}-\\d{2}") && isDateValue(strDate.substring(0, 4),
                                                                              strDate.substring(5, 7),
                                                                              strDate.substring(8));
            case "YYYY-MM-DD:HH":
                return strDate.matches("\\d{4}-\\d{2}-\\d{2}:[0-5][0-9]") && isDateValue(strDate.substring(0, 4),
                                                                                         strDate.substring(5, 7),
                                                                                         strDate.substring(8, 10));
            case "HHmmss":
                return strDate.matches("[0-5][0-9][0-5][0-9][0-5][0-9]") && isTimeValue(strDate.substring(0, 2),
                                                                                        strDate.substring(2, 4),
                                                                                        strDate.substring(4));
            default:
                return false;
        }
    }


    public static boolean isDateValue(String year, String month, String day){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        s.setLenient(false);//这个的功能是不把1996-13-3 转换为1997-1-3
        try{
            s.parse(year + "-" + month + "-" + day);
            return true;
        }catch(ParseException e){
            return false;
        }
    }


    public static boolean isTimeValue(String hour, String min, String sec){
        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        s.setLenient(false);
        try{
            s.parse(hour + ":" + min + ":" + sec);
            return true;
        }catch(ParseException e){
            return false;
        }
    }


    /**
     * 检查beging + days 是否在end前[包括end];(如:招行开始日期与结束日期之差只能在100以内)
     *
     * @return false 大于了end
     */
    public static boolean isBeforeDate(Date beging, int days, Date end){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(mTimeZone);
        gc.setTime(beging);
        gc.add(GregorianCalendar.DATE, days - 1);
        Date addDate = gc.getTime();
        return (end.compareTo(addDate) <= 0);
    }


    /**
     * 将yyyyMMdd变为yyyy-mm-dd
     *
     * @param s 原日期串，默认用-进行分割
     * @return yyyy-mm-dd
     */
    public static String toStdDateString(String s){
        return toStdDateString(s, "-", false);
    }


    /**
     * 将yyyyMMdd日期串用分隔符进行分割
     *
     * @param s 原日期串，默认用-进行分割
     * @param split 分隔符；
     * @param returnEmptyIfErrDate 如果不是8位的日期串，则控制是反馈“”，还是返回原串；
     * @return 用分隔符，如“-”，“.”进行分割后的日期串；
     */
    public static String toStdDateString(String s, String split, boolean returnEmptyIfErrDate){
        try{
            if(s == null || s.length() < 8){
                return returnEmptyIfErrDate ? "" : s;
            }
            return s.substring(0, 4) + split + s.substring(4, 6) + split + s.substring(6, 8);
        }catch(Exception e){
            return "";
        }
    }


    /**
     * 将yyyy-M-d转成yyyy-MM-dd格式
     */
    public static String toStdDateStringEx(String s){
        try{
            String[] ss = s.split("-");
            if(ss.length < 3) return "";
            return ss[0] + "-" + (ss[1].length() == 2 ? ss[1] : ("0" + ss[1])) + "-" + (ss[2].length() == 2 ?
                    ss[2] : ("0" + ss[2]));
        }catch(Exception e){
            return "";
        }
    }


    /**
     * 将20010101类型数字分割为2001-01-01，默认分隔符为"-";
     *
     * @param l long
     * @return yyyy-mm-dd
     */
    public static String toStdDateString(long l){
        return toStdDateString(String.valueOf(l), "-", false);
    }


    /**
     * 将20010101格式的数据转换成2001?01?01格式的显示，?标识分隔符
     *
     * @param l 日期
     * @param split 分隔符；
     * @param returnEmptyIfErrDate 如果不是8位的日期串，则控制是反馈“”，还是返回原串；
     * @return 2001-01-01
     */
    public static String toStdDateString(long l, String split, boolean returnEmptyIfErrDate){
        return toStdDateString(String.valueOf(l), split, returnEmptyIfErrDate);
    }


    /**
     * 将日期格式：2010-01-01,2011.01.01,2011年01月01日转换成数字；
     *
     * @param ds 日期字符串
     * @return Long, 例如20100101
     */
    public static Long toDateNumber(String ds){
        if(StringUtils.isEmpty(ds) || "-1".equals(ds) || "0".equals(ds)) return -1L;
        try{
            //即去掉串中的非数字字符；
            Pattern pattern = Pattern.compile("\\D", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(ds);
            return Long.valueOf(matcher.replaceAll(""));
        }catch(NumberFormatException e){
            e.printStackTrace();
            return -1L;
        }
    }


    /**
     * 将strDateTime换成Timestamp对象，如果不能转会，则返回当前时间戳。
     *
     * @param datetime 一个时间格式的字符串；
     * @return 如果strDateTime 有效，则返回它的时间戳，否则返回当前系统时间戳.
     */
    public static Timestamp toTimestampDefaultNow(String datetime){
        try{
            return Timestamp.valueOf(datetime);
        }catch(Exception e){
            return nowTimestamp();
        }
    }


    /**
     * 将字符串转换成Java.sql.Date,不能转换，这返回null
     *
     * @param src 源串
     * @return java.sql.Date
     */
    public static java.sql.Date toDate(String src){
        try{
            if(StringUtils.isEmpty(src)) return null;
            return java.sql.Date.valueOf(src);
        }catch(Exception e){
            return null;
        }
    }


    public static java.sql.Date toDate(Timestamp datetime){
        if(datetime == null) return null;
        return new java.sql.Date(datetime.getTime());
    }


    /**
     * 将字符串转换成Java.sql.Date,不能转换，则返回当前日期
     *
     * @param src 源串
     * @return java.sql.Date
     */
    public static java.sql.Date toDateDefaultNow(String src){
        try{
            if(StringUtils.isEmpty(src)) return nowDate();
            return java.sql.Date.valueOf(src);
        }catch(Exception e){
            return nowDate();
        }
    }


    /**
     * 将字符串转换成Java.sql.Date,不能转换，则返回当前日期
     *
     * @param datetime 源串
     * @return java.sql.Date
     */
    public static java.sql.Date toDateDefaultNow(Timestamp datetime){
        if(datetime == null) return nowDate();
        return new java.sql.Date(datetime.getTime());
    }


    /**
     * Makes a Date from separate ints for month, day, year, hour, minute, and second.
     *
     * @param month The month int
     * @param day The day int
     * @param year The year int
     * @param hour The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Date made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.sql.Date toDate(int month, int day, int year, int hour, int minute, int second){
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.set(year, month - 1, day, hour, minute, second);
        }catch(Exception e){
            return null;
        }
        return new java.sql.Date(calendar.getTime().getTime());
    }


    /**
     * Makes a Date from separate Strings for month, day, year, hour, minute, and second.
     *
     * @param monthStr The month String
     * @param dayStr The day String
     * @param yearStr The year String
     * @param hourStr The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Date made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.sql.Date toDate(String monthStr, String dayStr, String yearStr, String hourStr,
                                       String minuteStr, String secondStr){
        int month, day, year, hour, minute, second;
        try{
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        }catch(Exception e){
            return null;
        }
        return toDate(month, day, year, hour, minute, second);
    }


    public static Calendar getGMT8Calendar(){
        return Calendar.getInstance(mTimeZone);
    }


    /**
     * 用中方格式化年月日
     *
     * @param yearMonth YYYYMM
     */
    public static String fmtZHCNYearMonth(String yearMonth){
        if(NumberUtil.toInt(yearMonth) > 190000)
            return String.format("%s-%s", yearMonth.substring(0, 4), yearMonth.substring(4));
        return "";
    }


    /**
     * 用中方格式化年月日
     *
     * @param stdYearMonthDay YYYY-MM-DD
     */
    public static String fmtZHCNYearMonthDay(String stdYearMonthDay){
        return String.format("%s年%s月%s日", stdYearMonthDay.substring(0, 4), stdYearMonthDay.substring(5, 7),
                             stdYearMonthDay.substring(8));
    }


    //季度开始
    public static Date getSeasonBegin(){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int curMonth = now.get(Calendar.MONTH);
        int seasonMonth = SEASON_BEGIN_TABLE[curMonth];

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, seasonMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        return new Timestamp(calendar.getTime().getTime());
    }


    //下个季度开始
    public static Date getNextSeasonBegin(){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int curMonth = now.get(Calendar.MONTH);
        int seasonMonth = SEASON_BEGIN_TABLE[curMonth] + 3;

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, seasonMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        return new Timestamp(calendar.getTime().getTime());
    }


    /**
     * 得到当前日期所在周次的星期一
     *
     * @return 返回的时间戳
     */
    public static Timestamp getWeekStart(){
        return getWeekStart(new Timestamp(System.currentTimeMillis()));
    }


    /**
     * 得到日期所在周次的星期一
     *
     * @param stamp 参数时间戳，如果传入为null,则返回为null
     * @return 返回时间戳
     */
    public static Timestamp getWeekStart(Timestamp stamp){
        if(stamp == null) return null;
        GregorianCalendar tempCal = new GregorianCalendar(mTimeZone);
        tempCal.setTime(stamp);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 0,
                    0, 0);
        if(tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            // AKzz fix bug:
            // 如果今天是星期天, 天数再减1,找到上周(外国人的星期天是本周).
            tempCal.add(Calendar.DATE, -1);                 // 再将天数减一
        }
        tempCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 设为星期一.

        Timestamp retStamp = new Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(0);
        return retStamp;
    }


    /**
     * 当前日期的下周星期一
     */
    public static Timestamp getNextWeekStart(){
        return getNextWeekStart(new Timestamp(System.currentTimeMillis()));
    }


    /**
     * 当前日期的下周星期一,如果传入null,则返回为null
     *
     * @param t 要计算的时间戳
     * @return 返回的时间戳
     */
    public static Timestamp getNextWeekStart(Timestamp t){
        if(t == null) return null;
        Calendar tempCal = new GregorianCalendar(mTimeZone);
        tempCal.setTime(t);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH),
                    // 没有时,分,秒
                    0, 0, 0);
        tempCal.add(Calendar.WEEK_OF_MONTH, 1);             // 周数加1
        tempCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 设为星期一.

        Timestamp retStamp = new Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(0);
        return retStamp;
    }


    //获取上一月，YYYYMM格式
    public static String getPrevYm(String period_id){
        return getNextYm(period_id, -1);
    }


    //获取下一月，YYYYMM格式
    public static String getNextYm(String period_id){
        return getNextYm(period_id, 1);
    }


    public static String getNextYm(String period_id, int add){
        if(StringUtils.isEmptyId(period_id)) return period_id;
        int curYear = Integer.parseInt(period_id.substring(0, 4));
        int curMonth = Integer.parseInt(period_id.substring(4)) + add;
        if(curMonth > 12){
            do{
                curYear++;
                curMonth -= 12;
            }while(curMonth > 12);
        }else if(curMonth <= 0){
            do{
                curYear--;
                curMonth += 12;
            }while(curMonth <= 0);
        }
        return curYear + StringUtils.getRight("00" + curMonth, 2);
    }


    /**
     * Func:为空，返回""
     *
     * @param str 带检查的字符串
     * @return ""
     */
    public static String checkNull(String str){
        if(StringUtils.isEmpty(str))
            return "";
        return str.trim();
    }


    //取小的月份
    public static String min(String p1, String p2){
        if("-1".equals(p1)) return p2;
        if("-1".equals(p2)) return p1;
        p1 = checkNull(p1);
        p2 = checkNull(p2);
        if(p1.compareTo(p2) < 0) return p1;
        return p2;
    }


    //取大的月份
    public static String max(String p1, String p2){
        if("-1".equals(p1)) return p2;
        if("-1".equals(p2)) return p1;
        p1 = checkNull(p1);
        p2 = checkNull(p2);
        if(p1.compareTo(p2) < 0) return p2;
        return p1;
    }


    public static int compareYm(String ym1, String ym2){
        boolean isEmpty1 = StringUtils.isEmptyId(ym1);
        boolean isEmpty2 = StringUtils.isEmptyId(ym2);
        if(isEmpty1 && isEmpty2) return 0;
        if(isEmpty1) return -1;
        if(isEmpty2) return 1;
        return ym1.compareTo(ym2);
    }


    /**
     * @param pattern eg: dd-MM-yyyy, dd.MM
     */
    public static String getNowTimeStrPad(int num, String pattern){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM", Locale.getDefault());
        Date date = new Date();
        long times = date.getTime();
        long add = num * (24 * 60 * 60 * 1000);
        times = times + add;
        date = new Date(times);
        return format.format(date);
    }


    /**
     * @return 返回当前 yyyy-MM-dd 格式的日期
     */
    public static String getToday(){
        return getDate(0);
    }


    /**
     * 获取格式化好的当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss 格式的当前时间
     */
    public static String getFormattedTime(){
        Calendar calendar = Calendar.getInstance();
        return getFormattedTime(calendar.getTimeInMillis());
    }


    /**
     * 获取格式化好的时间
     *
     * @param timeStamp 时间戳
     */
    public static String getFormattedTime(long timeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_yMdHms, Locale.CHINA);
        return sdf.format(timeStamp);
    }


    /**
     * 获取格式化好的时间串对应的时间戳
     *
     * @param formattedTime 格式化好的时间串
     * @return 对应的时间戳
     */
    public static long getTime(String formattedTime){
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_yMdHms, Locale.CHINA);
        try{
            Date date = sdf.parse(formattedTime);
            return date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 返回与当前相差 diff 天的日期
     *
     * @param diff 相差天数，可为正负
     * @return 返回 yyyy-MM-dd 格式的日期
     */
    public static String getDate(int diff){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_yMd, Locale.CHINA);
        if(diff != 0){
            calendar.add(Calendar.DATE, diff);
        }
        return sdf.format(calendar.getTime());
    }


    /**
     * 返回 sourceDate 字符串对应的日期相差 diff 天的日期
     *
     * @param sourceDate 日期字符串，格式为 yyyy-MM-dd
     * @param diff 相差天数
     * @return 返回 yyyy-MM-dd 格式的日期
     */
    public static String getDate(String sourceDate, int diff) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_yMd, Locale.CHINA);
        Date date = sdf.parse(sourceDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(diff != 0){
            calendar.add(Calendar.DATE, diff);
        }
        return sdf.format(calendar.getTime());
    }


    /**
     * 返回当前时间
     *
     * @return 返回格式为 yyyyMMddHHmmss 的当前时间
     */
    public static String getNow(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.CHINA);
        return sdf.format(new Date());
    }


    /*
     * 获取当前时间
     */
    public static String getNowTime(){
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_yMdHms, Locale.CHINA);
        return format.format(Calendar.getInstance().getTime());
    }


    /**
     * 对日期、时间进行加、减操作。
     *
     * <pre>
     * DateTimeUtils.add(date, Calendar.YEAR, -1); // date减一年
     * DateTimeUtils.add(date, Calendar.HOUR, -4); // date减4个小时
     * DateTimeUtils.add(date, Calendar.MONTH, 3); // date加3个月
     * </pre>
     *
     * @param date 日期时间。
     * @param field 执行加减操作的属性，参考{@link Calendar#YEAR}、{@link Calendar#MONTH}、{@link Calendar#HOUR}等。
     * @param amount 加减数量。
     * @return 执行加减操作后的日期、时间。
     */
    public static Date add(Date date, int field, int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, amount);
        return calendar.getTime();
    }


    private static long cha(Date startTime, Date endTime, int type){
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        //获得两个时间的毫秒时间差异

        long cha = 0;

        long diff = endTime.getTime() - startTime.getTime();
        switch(type){
            case Calendar.YEAR:
                cha = endTime.getYear() - startTime.getYear();
                break;
            case Calendar.MONTH:
                cha = (endTime.getYear() - startTime.getYear()) * 12
                        + (endTime.getMonth() - startTime.getMonth());//计算差多少天
                break;
            case Calendar.DATE:
                cha = diff / nd;//计算差多少天
                break;
            case Calendar.HOUR:
                diff = endTime.getTime() - startTime.getTime();
                cha = (diff % nd / nh);//计算差多少小时
                break;
            default:
                break;
        }
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");

        return cha;
    }


    /**
     * 去除日期（年月日）部分，保留时间（时分秒）部分。
     * 去除时间（时分秒）部分，保留日期（年月日）部分。
     *
     * @param date 要操作的日期时间对象。
     * @param dateOrTime true --> 保留日期, false --> 保留时间
     * @return 去除日期部分后的日期时间对象（返回的日期部分为0001-01-01，因为无法用{@link Date}表示0000-00-00）。
     */
    public static Date truncateDate(Date date, boolean dateOrTime) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(dateOrTime){
            calendar.set(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
        }else {
//            calendar.add(Calendar.DATE, dateIndex);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }



    public static void main(String[] args){
        Date cDate = new Date();
        System.out.println(cDate);
        long a;
//        a = cha(Objects.requireNonNull(parse("2015-09-14 10:10:22")), cDate, Calendar.DATE);
//        System.out.println(a);
//        a = cha(Objects.requireNonNull(parse("2015-09-18 10:10:22")), cDate, Calendar.HOUR);
//        System.out.println(a);
//        a = cha(Objects.requireNonNull(parse("2013-09-18 10:10:22")), cDate, Calendar.YEAR);
//        System.out.println(a);
//        a = cha(Objects.requireNonNull(parse("2015-09-18 10:10:22")), cDate, Calendar.MONTH);
//        System.out.println(a);
    }
}
