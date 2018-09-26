package com.xhq.demo;

import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/11/2.
 *     Desc  : global cache interface.
 *     Updt  : Description
 * </pre>
 */
public interface GlobalCache{

    Map<String, SoftReference<List<Object>>> zoningMap = new ArrayMap<>();

    int maxSize = (int)(Runtime.getRuntime().maxMemory() / 1024);
    LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(maxSize / 8){

        @Override
        protected int sizeOf(String key, Bitmap bitmap){
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

}
