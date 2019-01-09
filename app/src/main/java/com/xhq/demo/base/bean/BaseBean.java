package com.xhq.demo.base.bean;

import com.xhq.demo.tools.NumberUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akmm at 2016-02-23 9:31
 * bean基类，基于Map，不实现序列化，需要序列化传递数据，直接传
 */
public abstract class BaseBean{
    protected Map<String, Object> data = new HashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public BaseBean clone() throws CloneNotSupportedException{
        BaseBean bean = (BaseBean) super.clone();
        bean.data = new HashMap<>();
        bean.getData().putAll(this.data);
        return bean;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public String getStr(String fieldName) {
        return getStr(fieldName, "");
    }

    public String getStr(String fieldName, String defValue) {
        Object o = data.get(fieldName);
        return o != null ? o.toString() : defValue;
    }

    public double getDouble(String fieldName) {
        return getDouble(fieldName, 0.0);
    }

    public double getDouble(String fieldName, double defValue) {
        Object o = data.get(fieldName);
        if (o == null) return defValue;
        if (o instanceof Number) return ((Number) o).doubleValue();
        return NumberUtil.toDouble(o.toString());
    }

    public int getInt(String fieldName) {
        return getInt(fieldName, 0);
    }

    public int getInt(String fieldName, int defValue) {
        Object o = data.get(fieldName);
        if (o == null) return defValue;
        if (o instanceof Number) return ((Number) o).intValue();
        return NumberUtil.toInt(o.toString());
    }

    public long getLong(String fieldName) {
        return getLong(fieldName, 0L);
    }

    public long getLong(String fieldName, long defValue) {
        Object o = data.get(fieldName);
        if (o == null) return defValue;
        if (o instanceof Number) return ((Number) o).longValue();
        return NumberUtil.toLong(o.toString());
    }

    public boolean getBool(String fieldName) {
        return getBool(fieldName, false);
    }

    public boolean getBool(String fieldName, boolean defValue) {
        Object o = data.get(fieldName);
        if (o == null) return defValue;
        return NumberUtil.toBool(o.toString(), false);
    }

    public void put(String fieldName, Object value) {
        this.data.put(fieldName, value);
    }

    public void setBool(String fieldName, boolean value) {
        this.data.put(fieldName, value ? 1 : 0);
    }

    public Object get(String fieldName) {
        return this.data.get(fieldName);
    }
}
