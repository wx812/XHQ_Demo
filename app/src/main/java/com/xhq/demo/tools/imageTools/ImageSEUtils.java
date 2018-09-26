package com.xhq.demo.tools.imageTools;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.widget.ImageView;

import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;

import static com.xhq.demo.tools.imageTools.ImageUtils.scaleBitmapBySample;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/6.
 *     Desc  : picture special effect tools.
 *     Updt  : Description.
 * </pre>
 */
public class ImageSEUtils{


    /**
     * judgment bitmap is empty
     *
     * @param bitmap bitmap
     * @return {@code true}: empty <br>{@code false}: non empty
     */
    private static boolean isEmptyBitmap(Bitmap bitmap){
        return null == bitmap || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }


    /**
     * clip picture
     *
     * @param src source bitmap
     * @param x x axis cutting location
     * @param y y axis cutting location
     * @param width cutting width
     * @param height cutting height
     * @param recycle recycle
     * @return after cutting the picture
     */
    public static Bitmap clip(Bitmap src, int x, int y, int width, int height, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    //*********************************************************************************************************//


    /**
     * 旋转图片
     *
     * @param src 源图片
     * @param degrees 旋转角度
     * @param px 旋转点横坐标
     * @param py 旋转点纵坐标
     * @return 旋转后的图片
     */
    public static Bitmap rotate(Bitmap src, int degrees, float px, float py){
        return rotate(src, degrees, px, py, false);
    }


    /**
     * 旋转图片
     *
     * @param src 源图片
     * @param degrees 旋转角度
     * @param px 旋转点横坐标
     * @param py 旋转点纵坐标
     * @param recycle 是否回收
     * @return 旋转后的图片
     */
    public static Bitmap rotate(Bitmap src, int degrees, float px, float py, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        if(degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return null;
    }


    public static String rotateImage(String filePath, String targetPath) {

        int screenW = ScreenUtils.getScreenW();
        int screenH = ScreenUtils.getScreenH();

        Bitmap bm = scaleBitmapBySample(filePath, screenW, screenH);//获取一定尺寸的图片
        int degree = ImageUtils.getRotateDegree(filePath);//获取相片拍摄角度

        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    //*********************************************************************************************************//


    /**
     * 转为圆形图片
     *
     * @param bitmap 源图片
     * @return 圆形图片
     */
    public static Bitmap toRound(Bitmap bitmap){
        return toRound(bitmap, false);
    }


    /**
     * 转为圆形图片
     *
     * @param bitmap 源图片
     * @param recycle 是否回收
     * @return 圆形图片
     */
    public static Bitmap toRound(Bitmap bitmap, boolean recycle){
        if(isEmptyBitmap(bitmap)) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) >> 1;
        Bitmap ret = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(ret);
        Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(width >> 1, height >> 1, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if(recycle && !bitmap.isRecycled()) bitmap.recycle();
        return ret;
    }


    /**
     * 转为圆角图片
     *
     * @param bitmap 源图片
     * @param radius 圆角的度数
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float radius){
        return toRoundCorner(bitmap, radius, false);
    }


    /**
     * 转为圆角图片
     *
     * @param bitmap 源图片
     * @param radius 圆角的度数
     * @param recycle 是否回收
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float radius, boolean recycle){
        if(null == bitmap) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap ret = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(ret);
        Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(new RectF(rect), radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if(recycle && !bitmap.isRecycled()) bitmap.recycle();
        return ret;
    }

    //*********************************************************************************************************//


    /**
     * add frame for picture
     *
     * @param src source bitmap
     * @param borderWidth the width of the frame
     * @param borderColor the color of the frame
     * @param recycle recycle
     * @return with color frame diagram
     */
    public static Bitmap addFrame(Bitmap src, int borderWidth, int borderColor, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        int doubleBorder = borderWidth << 1;
        int newWidth = src.getWidth() + doubleBorder;
        int newHeight = src.getHeight() + doubleBorder;
        Bitmap ret = Bitmap.createBitmap(newWidth, newHeight, src.getConfig());
        Canvas canvas = new Canvas(ret);
        Rect rect = new Rect(0, 0, newWidth, newHeight);
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        // setStrokeWidth是居中画的，所以要两倍的宽度才能画，否则有一半的宽度是空的
        paint.setStrokeWidth(doubleBorder);
        canvas.drawRect(rect, paint);
        canvas.drawBitmap(src, borderWidth, borderWidth, null);
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * add reflection for picture
     *
     * @param src source bitmap
     * @param reflectionHeight reflection height
     * @param recycle recycle
     * @return with reflection image
     */
    public static Bitmap addReflection(Bitmap src, int reflectionHeight, boolean recycle){
        if(isEmptyBitmap(src)) return null;

        final int REFLECTION_GAP = 0;   // 原图与倒影之间的间距
        final int srcWidth = src.getWidth();
        final int srcHeight = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight,
                                                      srcWidth, reflectionHeight, matrix, false);
        Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        LinearGradient shader = new LinearGradient(0, srcHeight,
                                                   0, ret.getHeight() + REFLECTION_GAP,
                                                   0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, srcHeight + REFLECTION_GAP,srcWidth, ret.getHeight(), paint);
        if(!reflectionBitmap.isRecycled()) reflectionBitmap.recycle();
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * add text watermark for picture
     *
     * @param src source bitmap
     * @param content text content
     * @param textSize text font size
     * @param color text font color
     * @param x watermark x axis location
     * @param y watermark y axis location
     * @param recycle recycle
     * @return with text watermarking image
     */
    public static Bitmap addTextWatermark(Bitmap src, String content, float textSize, int color, float x,
                                          float y, boolean recycle){
        if(isEmptyBitmap(src) || content == null) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        canvas.drawText(content, x, y + textSize, paint);
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * see {@link #addImageWatermark(Bitmap, Bitmap, int, int, int, boolean)}
     */
    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha){
        return addImageWatermark(src, watermark, x, y, alpha, false);
    }


    /**
     * to embed the watermark image
     *
     * @param src source image
     * @param watermark watermark image
     * @param x watermark x axis location
     * @param y watermark y axis location
     * @param alpha alpha
     * @param recycle recycle
     * @return a watermark bitmap
     */
    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        if(!isEmptyBitmap(watermark)){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            canvas.drawBitmap(watermark, x, y, paint);
        }
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    //*********************************************************************************************************//


    /**
     * see {@link #skew(Bitmap, float, float, float, float, boolean)}
     */
    public static Bitmap skew(Bitmap src, float kx, float ky){
        return skew(src, kx, ky, 0, 0, false);
    }


    /**
     * see {@link #skew(Bitmap, float, float, float, float, boolean)}
     */
    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py){
        return skew(src, kx, ky, px, py, false);
    }


    /**
     * skew picture
     *
     * @param src source bitmap
     * @param kx 倾斜因子x
     * @param ky 倾斜因子y
     * @param px 平移因子x
     * @param py 平移因子y
     * @param recycle 是否回收
     * @return 倾斜后的图片
     */
    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        Matrix matrix = new Matrix();
        matrix.setSkew(kx, ky, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    //*********************************************************************************************************//


    /**
     * see {@link #toAlpha(Bitmap, Boolean)}
     */
    public static Bitmap toAlpha(Bitmap src){
        return toAlpha(src, false);
    }


    /**
     * to alpha bitmap
     *
     * @param src source bitmap
     * @param recycle recycle
     * @return alpha bitmap
     */
    public static Bitmap toAlpha(Bitmap src, Boolean recycle){
        if(isEmptyBitmap(src)) return null;
        Bitmap ret = src.extractAlpha();
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * see {@link #toGray(Bitmap, boolean)}
     */
    public static Bitmap toGray(Bitmap src){
        return toGray(src, false);
    }


    /**
     * to gray bitmap
     *
     * @param src source bitmap
     * @param recycle recycle
     * @return gray bitmap
     */
    public static Bitmap toGray(Bitmap src, boolean recycle){
        if(isEmptyBitmap(src)) return null;
        Bitmap grayBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(src, 0, 0, paint);
        if(recycle && !src.isRecycled()) src.recycle();
        return grayBitmap;
    }

    //*********************************************************************************************************//


    /**
     * 快速模糊
     * <p>先缩小原图，对小图进行模糊，再放大回原先尺寸</p>
     *
     * @param src 源图片
     * @param scale 缩放比例(0...1)
     * @param radius 模糊半径
     * @return 模糊后的图片
     */
    public static Bitmap fastBlur(Bitmap src,
                                  @FloatRange(from = 0, to = 1, fromInclusive = false) float scale,
                                  @FloatRange(from = 0, to = 25, fromInclusive = false) float radius){
        return fastBlur(src, scale, radius, false);
    }


    /**
     * 快速模糊图片
     * <p>先缩小原图，对小图进行模糊，再放大回原先尺寸</p>
     *
     * @param src 源图片
     * @param scale 缩放比例(0...1)
     * @param radius 模糊半径(0...25)
     * @param recycle 是否回收
     * @return 模糊后的图片
     */
    public static Bitmap fastBlur(Bitmap src,
                                  @FloatRange(from = 0, to = 1, fromInclusive = false) float scale,
                                  @FloatRange(from = 0, to = 25, fromInclusive = false) float radius,
                                  boolean recycle){
        if(isEmptyBitmap(src)) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        int scaleWidth = (int)(width * scale + 0.5f);
        int scaleHeight = (int)(height * scale + 0.5f);
        if(scaleWidth == 0 || scaleHeight == 0) return null;
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(src, scaleWidth, scaleHeight, true);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.scale(scale, scale);
        canvas.drawBitmap(scaleBitmap, 0, 0, paint);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            scaleBitmap = renderScriptBlur(scaleBitmap, radius);
        }else{
            scaleBitmap = stackBlur(scaleBitmap, (int)radius, recycle);
        }
        if(scale == 1) return scaleBitmap;
        Bitmap ret = Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
        if(!scaleBitmap.isRecycled()) scaleBitmap.recycle();
        if(recycle && !src.isRecycled()) src.recycle();
        return ret;
    }


    /**
     * renderScript模糊图片
     * <p>API大于17</p>
     *
     * @param src 源图片
     * @param radius 模糊半径(0...25)
     * @return 模糊后的图片
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap renderScriptBlur(Bitmap src,
                                          @FloatRange(from = 0, to = 25, fromInclusive = false) float radius){
        if(isEmptyBitmap(src)) return null;
        RenderScript rs = null;
        try{
            rs = RenderScript.create(AppUtils.getAppContext());
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input = Allocation.createFromBitmap(rs, src, Allocation.MipmapControl.MIPMAP_NONE,
                                                           Allocation
                                                                   .USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blurScript.setInput(input);
            blurScript.setRadius(radius);
            blurScript.forEach(output);
            output.copyTo(src);
        }finally{
            if(rs != null){
                rs.destroy();
            }
        }
        return src;
    }


    /**
     * stack模糊图片
     *
     * @param src 源图片
     * @param radius 模糊半径
     * @param recycle 是否回收
     * @return stack模糊后的图片
     */
    public static Bitmap stackBlur(Bitmap src, int radius, boolean recycle){
        Bitmap ret;
        if(recycle){
            ret = src;
        }else{
            ret = src.copy(src.getConfig(), true);
        }

        if(radius < 1){
            return null;
        }

        int w = ret.getWidth();
        int h = ret.getHeight();

        int[] pix = new int[w * h];
        ret.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rSum, gSum, bSum, x, y, i, p, yp, yi, yw;
        int vMin[] = new int[Math.max(w, h)];

        int divSum = (div + 1) >> 1;
        divSum *= divSum;
        int dv[] = new int[256 * divSum];
        for(i = 0; i < 256 * divSum; i++){
            dv[i] = (i / divSum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routSum, goutSum, boutSum;
        int rinSum, ginSum, binSum;

        for(y = 0; y < h; y++){
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            for(i = -radius; i <= radius; i++){
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rSum += sir[0] * rbs;
                gSum += sir[1] * rbs;
                bSum += sir[2] * rbs;
                if(i > 0){
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                }else{
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
            }
            stackPointer = radius;

            for(x = 0; x < w; x++){

                r[yi] = dv[rSum];
                g[yi] = dv[gSum];
                b[yi] = dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if(y == 0){
                    vMin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vMin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for(x = 0; x < w; x++){
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            yp = -radius * w;
            for(i = -radius; i <= radius; i++){
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rSum += r[yi] * rbs;
                gSum += g[yi] * rbs;
                bSum += b[yi] * rbs;

                if(i > 0){
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                }else{
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }

                if(i < hm){
                    yp += w;
                }
            }
            yi = x;
            stackPointer = radius;
            for(y = 0; y < h; y++){
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if(x == 0){
                    vMin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vMin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi += w;
            }
        }
        ret.setPixels(pix, 0, w, 0, 0, w, h);
        return ret;
    }

    //*********************************************************************************************************//


    /**
     * 将YUV数据Bitmap byte[]数据转换成Bitmap图片
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public Bitmap yuvToBitmap(byte[] data, int width, int height){
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++){
                int y = (0xff & ((int)data[i * width + j]));
                int u = (0xff & ((int)data[frameSize + (i >> 1) * width + (j & ~1)]));
                int v = (0xff & ((int)data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    //*********************************************************************************************************//


    public static int bitmapByteToInt(byte bytes){
        int heightBit = (bytes >> 4) & 0x0F;
        int lowBit = 0x0F & bytes;
        return heightBit * 16 + lowBit;
    }


    public static int[] bitmapByteArrayToColor(byte[] byteArray){
        int size = byteArray.length;
        if(size == 0){
            return null;
        }

        int arg = 0;
        if(size % 3 != 0){
            arg = 1;
        }

        int[] color = new int[size / 3 + arg];
        int red, green, blue;

        if(arg == 0){
            for(int i = 0; i < color.length; ++i){
                red = bitmapByteToInt(byteArray[i * 3]);
                green = bitmapByteToInt(byteArray[i * 3 + 1]);
                blue = bitmapByteToInt(byteArray[i * 3 + 2]);

                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }
        }else{
            for(int i = 0; i < color.length - 1; ++i){
                red = bitmapByteToInt(byteArray[i * 3]);
                green = bitmapByteToInt(byteArray[i * 3 + 1]);
                blue = bitmapByteToInt(byteArray[i * 3 + 2]);
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }

            color[color.length - 1] = 0xFF000000;
        }

        return color;
    }


    public static Bitmap decodeFrameToBitmap(byte[] frame){
        int[] colors = bitmapByteArrayToColor(frame);
        if(colors == null){
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(colors, 0, 1280, 1280, 720, Bitmap.Config.ARGB_8888);
        return bmp;
    }



//    =======================================================================================================


    /**
     * 使用ColorFilter来改变亮度
     *
     * @param imageview
     * @param brightness
     */
    public static void changeBrightness(ImageView imageview, float brightness) {
        imageview.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }
    public static void changeBrightness(Drawable drawable, float brightness) {
        drawable.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }

    private static ColorMatrixColorFilter getBrightnessMatrixColorFilter(float brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(
                new float[]{
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });
        return new ColorMatrixColorFilter(matrix);
    }

}
