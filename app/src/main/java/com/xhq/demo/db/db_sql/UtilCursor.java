package com.xhq.demo.db.db_sql;

import android.database.Cursor;

import com.xhq.demo.tools.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class UtilCursor {
    public Cursor rs;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);


    public static String cutStringRight(String src, String cut) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(cut)) {
            return "";
        }
        while (src.endsWith(cut))
            src = src.substring(0, src.length() - cut.length());
        return src;
    }

    /**
     * 取得重复字串
     *
     * @param repeatString 重复字串
     * @param count        重复次数
     * @return String
     */
    public static String repeatString(String repeatString, int count) {
        if (count <= 0) return "";
        StringBuilder ret = new StringBuilder(repeatString.length() * count);
        for (int i = 1; i <= count; i++) {
            ret.append(repeatString);
        }
        return ret.toString();
    }


    /**
     * Return a Timestamp for right now
     *
     * @return Timestamp for right now
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 取得系统今天的时间串
     *
     * @return current datetime, pattern:"yyyyMMddHHmmss"
     */
    public static String nowDateTimeString() {
        return dateFormat.format(nowTimestamp());
    }

    /**
     * 获取当前时间数字串，例如20100909121359 标识2010年09月09日 12时13分59秒
     *
     * @return Long
     */
    public static Long nowDateTimeNumber() {
        return Long.valueOf(nowDateTimeString());
    }

    /**
     * 取最后修改时间
     *
     * @return Long
     */
    public static Long getLastTime() {
        return toLong(dateFormat.format(nowTimestamp()));
    }

    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static long toLong(String str, long defValue) {
        if (str == null){return defValue;}
        str = str.trim();
        if (str.equals("")){return defValue;}
        str = str.replaceAll(",", "").replaceAll("，", "");
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    public UtilCursor() {
//        this.rs = rs;
    }

    public boolean next(){
        return rs.moveToNext();
    }

    public void close() {
        if (!rs.isClosed())
        rs.close();
    }

    public Boolean isClosed() {
       return rs.isClosed();
    }

    //是否包含字段
    public boolean containsField(String fieldName) {
        return rs.getColumnIndex(fieldName) >= 0;
    }

    /**
     * 获取字段内容,同时去掉内容中的"-"字符
     *
     * @param fieldName 字段名称
     * @return 返回值, null会返回""
     */
    public String getStringRemoveSubSign(String fieldName){
        String str = rs.getString(rs.getColumnIndex(fieldName));
        if (str == null)
            return "";
        if ("-".equals(str))
            return "";
        return str;
    }

    /**
     * 获取字段中的字符串类型值,如果为null,则返回"";
     *
     * @param fieldName 字段名称
     * @return 返回值
     */
    public String getStringIgnoreNull(String fieldName){
        String str = rs.getString(rs.getColumnIndex(fieldName));
        if (str == null)
            return "";
        return str;
    }

    public String getStringIgnoreNull(String fieldName, String defaultvalue){
        String str = rs.getString(rs.getColumnIndex(fieldName));
        if (str == null)
            return defaultvalue;
        return str;
    }

    public double getDouble(String fieldName){
        return rs.getDouble(rs.getColumnIndex(fieldName));
    }

    public double getDouble(int index){
        return rs.getDouble(index);
    }

    public double getDoubleNotException(String fieldName) {
        return rs.getDouble(rs.getColumnIndex(fieldName));
    }


    public int getInt(String fieldName){
        return rs.getInt(rs.getColumnIndex(fieldName));
    }

    public int getInt(int index){
        return rs.getInt(index);
    }


    /**
     * 返回int,如果异常则返回0
     *
     * @param fieldName 字段名称
     * @return 不会抛出异常；
     */
    public int getIntNotException(String fieldName) {
        return rs.getInt(rs.getColumnIndex(fieldName));
    }

    public long getLong(String fieldName){
        return rs.getLong(rs.getColumnIndex(fieldName));
    }

    /**
     * 返回long,如果异常则返回0
     *
     * @param fieldName 字段名称
     * @return 不会抛出异常；
     */
    public long getLongNotException(String fieldName) {
        return rs.getLong(rs.getColumnIndex(fieldName));
    }

    public int getColumnCount(){
        return rs.getColumnCount();
    }


//    public String getColumnName(int index) throws SQLException {
//        if (metaData == null) metaData = rs.;
//        return metaData.getColumnName(index);
//    }
//
//    public ResultSetMetaData getMetaData() throws SQLException {
//        if (metaData == null) metaData = rs;
//        return metaData;
//    }
}
