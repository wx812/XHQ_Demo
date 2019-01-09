package com.xhq.demo.tools.imageTools;

import android.graphics.Bitmap;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/30.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public interface ImageCache{

    Bitmap getBitmapFromFile(String url);
    void putBitmap(String url, Bitmap bitmap);
}
