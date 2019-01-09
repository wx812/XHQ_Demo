package com.xhq.demo.tools;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2019/1/8.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class NumberUtil{


    public static final String NO_SPACE = "";
    public static final String DOT = ".";
    public static final String COMMA = ",";


    private static boolean isEmpty(String value){
        return value == null || value.trim().length() <= 0;
    }


    /**
     * 1) 字符串的"true"、"false"值（忽略大小写）会转化为boolean类型的true、false值；
     * <br /> 2) 字符串的"1"、"0"值会转化为boolean类型的true、fasle值；
     * <br /> 3) value为null或者空字符串，会返回defV；
     * <br /> 4) 除以上情况外，均返回true值；
     */
    private static Boolean toBool(String value, Boolean defV) {
        if (isEmpty(value)) return defV;
        value = value.trim().toLowerCase();
        if (value.length() <= 0) return defV;
        if ("1".equals(value) || "true".equals(value) || "Y".equals(value) || "yes".equals(value)) return true;
        return !"false".equals(value) && !"0".equals(value);
    }


    /**
     * {@link Object}转换为{@link Boolean}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时，返回defV；
     * <br /> 2) value为{@link Boolean}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toBool(String, Boolean)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link Double}
     *          、{@link BigDecimal}、{@link BigInteger}等类型时，值为0返回false，否则返回true；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成boolean值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return boolean
     */
    public static Boolean toBool(Object value, Boolean defV) {
        if (value == null) return defV;
        Class<?> clazz = value.getClass();

        // 最可能出现的类型放在最前面判断
        if (Boolean.class.equals(clazz)) return (Boolean) value;
        if (String.class.equals(clazz)) return toBool(String.valueOf(value), defV);

//        if(value instanceof Comparable && clazz.isAssignableFrom(Number.class)) return (Number)value != 0;

        if (Integer.class.equals(clazz)) return (Integer) value != 0;
        if (Byte.class.equals(clazz)) return (Byte) value != 0;
        if (Short.class.equals(clazz)) return (Short) value != 0;
        if (Long.class.equals(clazz)) return (Long) value != 0;
        if (Double.class.equals(clazz)) return (Double) value != 0;
        if (Float.class.equals(clazz)) return (Float) value != 0;
        if (BigDecimal.class.equals(clazz)) return ((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0;
        if (BigInteger.class.equals(clazz)) return ((BigInteger) value).compareTo(BigInteger.ZERO) != 0;
        return defV;
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


    /**
     * 可以识别金额中的逗号格式
     */
    public static int toInt(String str, int defValue){
        if(str == null) return defValue;
        str = str.trim();
        if(str.equals(NO_SPACE)) return defValue;
        str = str.replaceAll(COMMA, NO_SPACE).replaceAll("，", NO_SPACE);
        if(str.contains(DOT)) str = str.substring(0, str.indexOf(DOT.charAt(0)));
        try{
            return Integer.valueOf(str);
        }catch(Exception e){
            return defValue;
        }
    }


    /**
     * 转换为Integer类型
     */
    public static Integer toInt(Object val){
        return toInt(val, 0);
    }


    /**
     * {@link Object}转换为{@link Integer}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defV；
     * <br /> 2) value为{@link Integer}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toInt(String, Integer)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Long}、{@link Float}、{@link Double}、{@link BigDecimal}
     *          、{@link BigInteger}等类型时，返回其intValue()值；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成{@link Integer}值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return integer
     */
    public static Integer toInt(Object value, Integer defV) {
        if (value == null) return defV;
        Class<?> cls = value.getClass();
        if (Integer.class.equals(cls)) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        if (String.class.equals(cls)) return toInt((String) value, defV);
        return defV;
    }


    /**
     * value等于null或者为空字符串，或者在转换过程中发生异常，均返回defV，否则返回转换后的{@link Integer}值。
     */
    private static Integer toInt(String value, Integer defV) {
        if (isEmpty(value)) return defV;

        // 字符串中包含小数点，Integer.valueOf会报异常，先转换为BigDecimal，返回其整数部分
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return d.intValue();
            } catch (Exception e) {
                return defV;
            }
        }

        // 当做整数字符串处理
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defV;
        }
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


    /**
     * 可以识别金额中的逗号格式
     */
    public static long toLong(String str, long defValue){
        if(str == null){return defValue;}
        str = str.trim();
        if(str.equals(NO_SPACE)){return defValue;}
        str = str.replaceAll(COMMA, NO_SPACE).replaceAll("，", NO_SPACE);
        try{
            return Long.valueOf(str);
        }catch(Exception e){
            return defValue;
        }
    }


    public static Long toLong(Object val){
        return toLong(val, 0L);
    }


    /**
     * {@link Object}转换为{@link Long}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defV；
     * <br /> 2) value为{@link Long}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toLong(String, Long)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Float}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其longValue()值；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成{@link Long}值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     */
    public static Long toLong(Object value, Long defV) {
        if (value == null) return defV;
        Class<?> cls = value.getClass();
        if (Long.class.equals(cls)) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        if (String.class.equals(cls)) return toLong((String) value, defV);
        return defV;
    }


    /**
     * value等于null或者为空字符串，或者在转换过程中发生异常，均返回defV，否则返回转换后的{@link Long}值。
     */
    private static Long toLong(String value, Long defV) {
        if (isEmpty(value)) return defV;

        // 字符串中包含小数点，Long.valueOf会报异常，先转换为BigDecimal，返回其整数部分
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return d.longValue();
            } catch (Exception e) {
                return defV;
            }
        }

        // 当做整数字符串处理
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            return defV;
        }
    }


    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val){
        return toDouble(val).floatValue();
    }


    /**
     * {@link Object}转换为{@link Float}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defV；
     * <br /> 2) value为{@link Float}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toFloat(String, Float)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其floatValue()值；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成{@link Float}值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     */
    public static Float toFloat(Object value, Float defV) {
        if (value == null) return defV;
        Class<?> cls = value.getClass();
        if (Float.class.equals(cls)) return (Float) value;
        if (value instanceof Number) return ((Number) value).floatValue();
        if (String.class.equals(cls)) return toFloat((String) value, defV);
        return defV;
    }


    /**
     * value等于null或者为空字符串，或者在转换过程中发生异常，均返回defV，否则返回转换后的{@link Float}值。
     */
    private static Float toFloat(String value, Float defV) {
        if (isEmpty(value)) return defV;
        try {
            return Float.valueOf(value);
        } catch (Exception e) {
            return defV;
        }
    }


    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val){
        return toDouble(val, 0D);
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
        if(str.equals(NO_SPACE)){return 0.0;}
        str = str.replaceAll(COMMA, NO_SPACE).replaceAll("，", NO_SPACE);
        try{
            return Double.valueOf(str);
        }catch(Exception e){
            return 0.0;
        }
    }


    /**
     * {@link Object}转换为{@link Double}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defV；
     * <br /> 2) value为{@link Double}类型时，不进行类型转换，直接返回value对象；
     * <br /> 3) value为{@link String}类型时，参考{@link #toDouble(String, Double)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其doubleValue()值；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成{@link Double}值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return double
     */
    public static Double toDouble(Object value, Double defV) {
        if (value == null) return defV;
        Class<?> cls = value.getClass();
        if (Double.class.equals(cls)) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (String.class.equals(cls)) return toDouble((String) value, defV);
        return defV;
    }


    /**
     * value等于null或者为空字符串，或者在转换过程中发生异常，均返回defV，否则返回转换后的{@link Double}值。
     */
    private static Double toDouble(String value, Double defV) {
        if (isEmpty(value)) return defV;
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            return defV;
        }
    }


    /**
     * {@link Object}转换为{@link BigDecimal}类型。
     * <p>转换不成功时返回defV并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defV；
     * <br /> 2) value为{@link BigDecimal}类型时，不进行类型转换，直接返回value对象；
     * <br /> 3) value为{@link String}类型时，参考{@link #toDecimal(String, BigDecimal)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link Double}、{@link BigInteger}
     *           等类型时，返回其{@link BigDecimal}值；
     * <br /> 5) 其他情况均返回defV；
     * @param value 要转换成{@link BigDecimal}值的对象。
     * @param defV 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return bigDecimal
     */
    @SuppressWarnings("rawtypes")
    public static BigDecimal toDecimal(Object value, BigDecimal defV) {
        if (value == null) return defV;

        Class clazz = value.getClass();
        // 最可能出现的类型放在最前面判断
        if (BigDecimal.class.equals(clazz)) return ((BigDecimal) value);
        if (Double.class.equals(clazz))
            // Double转BigDecimal，需要使用new Decimal(doubleValue.toString())
            // 如果使用new Decimal(doubleValue)，像89.7会变成89.70000000000000012之类值
            return toDecimal(value.toString(), defV);

        if (Float.class.equals(clazz)) return toDecimal(value.toString(), defV);    // 同double
        if (String.class.equals(clazz)) return toDecimal((String) value, defV);
        if (Integer.class.equals(clazz)) return new BigDecimal((Integer) value);
        if (Short.class.equals(clazz)) return new BigDecimal((Short) value);
        if (Byte.class.equals(clazz)) return new BigDecimal((Byte) value);
        if (Long.class.equals(clazz)) return new BigDecimal((Long) value);
        return defV;
    }


    /**
     * value等于null或者为空字符串，或者在转换过程中发生异常，均返回defV，否则返回转换后的{@link BigDecimal}值。
     */
    private static BigDecimal toDecimal(String value, BigDecimal defV) {
        if (isEmpty(value)) return defV;
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return defV;
        }
    }


    // comma separated number
    public static String toStrFromLong(long number){
        return String.format(Locale.getDefault(), "%,d", number);
    }


}
