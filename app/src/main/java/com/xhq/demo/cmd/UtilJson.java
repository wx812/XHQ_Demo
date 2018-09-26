package com.xhq.demo.cmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.xhq.demo.base.bean.BaseBean;

import java.lang.reflect.Type;

/**
 * Created by Akmm at 14-2-19 上午10:18
 * json工具类，使用Gson
 */
public class UtilJson {
    private static Gson gson;
    static {
        gson = new GsonBuilder().serializeNulls().registerTypeHierarchyAdapter(BaseBean.class, new JsonSerializer<BaseBean>() {
            @Override
            public JsonElement serialize(BaseBean absEntity, Type type, JsonSerializationContext jsonSerializationContext) {
                return jsonSerializationContext.serialize(absEntity.getData());
            }
        }).create();
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }
}
