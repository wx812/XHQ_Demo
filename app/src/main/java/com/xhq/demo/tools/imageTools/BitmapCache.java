package com.xhq.demo.tools.imageTools;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/6/30.
 *     Desc  : bitmap cache.
 *     Updt  : Description.
 * </pre>
 */
public class BitmapCache implements ImageCache{

    public static final int maxSize = (int)(Runtime.getRuntime().maxMemory() / 1024);

    // LruCache 是 Android utils包下的
    public static LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(maxSize / 8){

        @Override
        protected int sizeOf(String key, Bitmap bitmap){
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };


    @Override
    public Bitmap getBitmapFromFile(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }



}