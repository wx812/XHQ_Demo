package com.xhq.demo.tools.imageTools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.View;

import com.xhq.demo.R;
import com.xhq.demo.tools.CloseUtils;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.fileTools.StorageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/12/23.
 *     Desc  : Image utils.
 *     Updt  : Description.
 * </pre>
 */
public class ImageUtils{

    public static final String DCIM_DIR = StorageUtils.getExStoragePubDir(Environment.DIRECTORY_DCIM).getPath();

    /**
     * judgment bitmap is empty
     *
     * @param bitmap bitmap
     * @return {@code true}: empty <br>{@code false}: non empty
     */
    public static boolean isEmptyBitmap(Bitmap bitmap){
        return null == bitmap || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }


    /**
     * view to Bitmap
     *
     * @param view view
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }

    //*********************************************************************************************************//

    /**
     * bitmap turn to drawable
     *
     * @param bitmap bitmap object
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        if(isEmptyBitmap(bitmap)) return null;
        Resources resources = AppUtils.getAppContext().getResources();
        return new BitmapDrawable(resources, bitmap);
    }


    /**
     * drawable turn to bitmap
     *
     * @param drawable drawable object
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }else if(drawable instanceof NinePatchDrawable){

            final int width = drawable.getIntrinsicWidth();
            final int height = drawable.getIntrinsicHeight();
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);

            // 下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            return bitmap;
        }else{
            return null;
        }
    }

    //*********************************************************************************************************//

    public static Drawable getDrawableFromBytes(byte[] bytes, int reqWPixels, int reqHPixels) {
        return bitmap2Drawable(getBitmapFromBytes(bytes, reqWPixels, reqHPixels));
    }

    @Deprecated
    public static Drawable getDrawableFromBytes(byte[] bytes) {
        return bitmap2Drawable(getBitmapFromBytes(bytes));
    }


    public static byte[] drawable2Bytes(Drawable drawable) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable));
    }


    public static Drawable bytes2Drawable(byte[] bytes) {
        if(StringUtils.isEmpty(bytes)) return null;
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    //*********************************************************************************************************//

    /**
     * an array of bytes into a bitmap image
     *
     * <br>而我现在得到的仅仅是RGBA数据，所以需要先得到一个bitmap实例，再往里填数据。
     *     需要将得到的byteArray再变回buffer，使用buffer的 wrap 方法,包装数组得到buffer.<br>
     *
     * @param bytes byteArray of the picture
     * @param reqWPixels hope the width of the bitmap
     * @param reqHPixels hope the height of the bitmap
     * @return bitmap
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes, int reqWPixels, int reqHPixels) {
        if (bytes.length == 0) return null;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap stitchBmp = Bitmap.createBitmap(reqWPixels, reqHPixels, config);
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(bytes));
        return stitchBmp;
    }


    /**
     * use {@link #getBitmapFromBytes(byte[], int, int)} replace.
     *
     * <br> Returns the decoded Bitmap, or null if the image could not be decoded.<br>
     *     <br> 这个方法生效的前提是，提供的bytes是包含了图像参数的，而非简单的RGBA数据。<br>
     */
    @Deprecated
    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * bitmap turn to bytes array
     *
     * @param bitmap source bitmap
     * @return bytes array of the bitmap
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap){
        int bytes = bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);
        return buf.array();
    }


    /**
     * use {@link #bitmap2Bytes(Bitmap)} replace
     */
    @Deprecated
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }


    /**
     * byteArr转bitmap
     *
     * @param bytes bitmap 的字节数组
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if(StringUtils.isEmpty(bytes))return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    //*********************************************************************************************************//

    public static InputStream Bitmap2InputStream(@NonNull Bitmap bitmap){
        byte[] byteArray = bitmap2Bytes(bitmap);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        return new BufferedInputStream(bais);
    }


    public static Bitmap getBitmapFromStream(InputStream is){
        if (is == null) return null;
        return BitmapFactory.decodeStream(is);
    }

    public static Bitmap getBitmapFromStream(InputStream is, int maxWidth, int maxHeight) {
        if (is == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }


    public InputStream Drawable2InputStream(Drawable drawable){
        byte[] byteArray = drawable2Bytes(drawable);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        return new BufferedInputStream(bais);
    }


    /**
     * using base64 convert picture to string
     *
     * @param bitmap bitmap
     * @return string of the bitmap
     */
    public String bitmap2String(Bitmap bitmap){
        byte[] bytes = bitmap2Bytes(bitmap);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * the string into the bitmap
     *
     * @param bitmapStr string of the bitmap
     * @param reqWPixels hope the width of the bitmap
     * @param reqHPixels hope the height of the bitmap
     * @return bitmap
     */
    public Bitmap getBitmapFromString(String bitmapStr, int reqWPixels, int reqHPixels){
        byte[] bytes = Base64.decode(bitmapStr, 0);
        return getBitmapFromBytes(bytes, reqWPixels, reqHPixels);
    }

    /**
     * see {@link #getBitmapFromString(String, int, int)}
     */
    @Deprecated
    public Bitmap getBitmapFromString(String bitmapStr){
        byte[] bytes = Base64.decode(bitmapStr, 0);
        return getBitmapFromBytes(bytes);
    }




    /**
     * save bitmap into specified image path
     *
     * @param bitmap bitmap
     * @param pathName bitmap si saved as a file path
     * @return bitmap file
     */
    public static File saveBitmap2File(Bitmap bitmap, String pathName){

        FileOutputStream fos;
        File file = new File(pathName);
        try{
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return file;
    }


    public static File saveBitmap2File(Bitmap bitmap, File file){

        FileOutputStream fos;
        try{
//            if(!file.exists()) file.createNewFile();
            fos = new FileOutputStream(file);
            InputStream is = Bitmap2InputStream(bitmap);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }


    /**
     * get bitmap
     *
     * @param file bitmap file
     * @return bitmap
     */
    public static Bitmap getBitmapFromFile(File file){
        if(file == null || !file.exists()) return null;
        InputStream is = null;
        try{
            is = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(is);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }finally{
            CloseUtils.closeIO(is);
        }
    }

    public static Bitmap getBitmapFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return null;
        return BitmapFactory.decodeFile(filePath);
    }


    /**
     * get bitmap
     *
     * @param absPathName absolute path name
     * @param reqWPixels request width px
     * @param reqHPixels request height px
     * @return bitmap
     */
    public static Bitmap getBitmapFromFile(String absPathName, int reqWPixels, int reqHPixels){
        if(TextUtils.isEmpty(absPathName)) return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;  // 只加载图片的宽高等信息
        BitmapFactory.decodeFile(absPathName, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWPixels, reqHPixels);
        opts.inJustDecodeBounds = false; // 加载完整的图片, 并分配内存
        return BitmapFactory.decodeFile(absPathName, opts);
    }


    /**
     * {@link #getBitmapFromFile(String, int, int)}
     *
     * @param file bitmap file
     * @param reqWPixels {@link #getBitmapFromFile(String, int, int)}
     * @param reqHPixels {@link #getBitmapFromFile(String, int, int)}
     * @return bitmap
     */
    public static Bitmap getBitmapFromFile(File file, int reqWPixels, int reqHPixels){
        if(file == null) return null;
        InputStream is = null;
        try{
            is = new BufferedInputStream(new FileInputStream(file));
            is.mark(is.available());
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, opts);
            opts.inSampleSize = calculateInSampleSize(opts, reqWPixels, reqHPixels);
            opts.inJustDecodeBounds = false;
            is.reset();
            return BitmapFactory.decodeStream(is, null, opts);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            CloseUtils.closeIO(is);
        }
    }


    /**
     * see {@link #getBitmapFromFile(String, int, int)}
     */
    public static Bitmap getBitmapFromFile(FileDescriptor fd, int reqWPixels, int reqHPixels) {
        if (fd == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWPixels, reqHPixels);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


    /**
     * {@link #getBitmapFromFile(String, int, int)}
     *
     * @param resDrawableId resource id eg: R.
     * @param reqWPixels {@link #getBitmapFromFile(String, int, int)}
     * @param reqHPixels {@link #getBitmapFromFile(String, int, int)}
     * @return bitmap
     */
    public static Bitmap getBitmapFromRes(@DrawableRes int resDrawableId, int reqWPixels, int reqHPixels){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Resources res = AppUtils.getAppContext().getResources();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resDrawableId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWPixels, reqHPixels);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resDrawableId, options);
    }


    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param picPath source path
     * @param reqWPixels request width px
     * @param reqHPixels request height px
     * @return thumbnail bitmap
     */
    public static Bitmap getBitmapThumbnail(String picPath, int reqWPixels, int reqHPixels) {
        Bitmap scaleBitmap = scaleBitmapBySample(picPath, reqWPixels, reqHPixels);
        Bitmap thumbnailBitmap = ThumbnailUtils.extractThumbnail(scaleBitmap, reqWPixels, reqHPixels,
                                                          ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if(!scaleBitmap.isRecycled()) scaleBitmap.recycle();
        return thumbnailBitmap;
    }


    //*********************************************************************************************************//

    public static void compressScale2File(String sourcePicPath, String destPicPath,
                                          int reqWPixels, int reqHPixels){

        Bitmap bitmap = compressAndScale(sourcePicPath, reqWPixels, reqHPixels);
        InputStream is = Bitmap2InputStream(bitmap);
        BufferedOutputStream bos;
        FileOutputStream fos;
        try{
            fos = new FileOutputStream(destPicPath);
            bos =  new BufferedOutputStream(fos);
            byte data[] = new byte[1024];
            int length;
            while((length = is.read(data)) != -1){
                bos.write(data, 0, length);
            }
            bos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void compressScale2File(Bitmap bmp, File file, @IntRange(from = 1) int ratio) {
        int scaleW = bmp.getWidth() / ratio;
        int scaleH = bmp.getHeight() / ratio;
        Bitmap result = Bitmap.createBitmap(scaleW, scaleH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, scaleW, scaleH);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Bitmap compressAndScale(String picPath, int reqWPixels, int reqHPixels){
        Bitmap scaleBitmap = scaleBitmapBySample(picPath, reqWPixels, reqHPixels);
        return compressBitmap(scaleBitmap);
    }


    public static Bitmap compressBitmap(Bitmap bitmap){
        if(isEmptyBitmap(bitmap)) throw new IllegalArgumentException("bitmap must be not empty");
        Bitmap compressedBitmap = compressBitmapByQuality(bitmap, Bitmap.CompressFormat.PNG, 100);
        if(!bitmap.isRecycled()) bitmap.recycle();
        return compressedBitmap;
    }


    public static Bitmap compressBitmapByQuality(Bitmap bitmap, Bitmap.CompressFormat format,
                                                 @IntRange(from = 0, to = 100) int quality){
        if(isEmptyBitmap(bitmap)) throw new IllegalArgumentException("bitmap must be not empty");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        if(!bitmap.isRecycled()) bitmap.recycle();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }


    /**
     * compress bitmap, Relative to the compression of disk storage,
     * only in the form of file storage compression has effect, suit to upload images.<p>
     * <p>
     * PNG format images can only by changing the image size is compressed, it's to ignore quality Settings.
     *
     * @param bitmap bitmap
     * @param format compressed format
     * @param maxSize specify the compressed size, KB for the unit
     * @param rgbConfig Bitmap.Config -> recommend to use Bitmap.Config.RGB_565,
     * @return compressed bitmap
     */
    public static Bitmap compressBitmapBySize(Bitmap bitmap, Bitmap.CompressFormat format,
                                              int maxSize, @Nullable Bitmap.Config rgbConfig){
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;

        int quality = 100;  // 100 indicates without compression
        try{
            baos = new ByteArrayOutputStream();
            bitmap.compress(format, quality, baos);
            byte[] bytes = baos.toByteArray();
            while(bytes.length / 1024 > maxSize && quality >= 0){
                quality -= 10;  // interval 10
                if(quality < -1) break;
                baos.reset();   // Clean up baos
                bitmap.compress(format, quality, baos);
            }

            bais = new ByteArrayInputStream(baos.toByteArray());
            if(!bitmap.isRecycled()) bitmap.recycle();
            if(null != rgbConfig){
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = rgbConfig;
                return BitmapFactory.decodeStream(bais, null, opts);
            }
            return BitmapFactory.decodeStream(bais);
        }finally{
            CloseUtils.closeIO(bais, baos);
        }
    }


    /**
     * compressed bitmap to the file
     *
     * @param bitmap source bitmap
     * @param destFile destination file
     * @param format compress format
     * @param maxSize generate the file size , unit kb
     * @param recycle recycle
     */
    public static void compressBitmap2File(Bitmap bitmap, File destFile, Bitmap.CompressFormat format,
                                           int maxSize, boolean recycle) {
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        int quality = 100;  // 100 indicates without compression
        try{
            baos = new ByteArrayOutputStream();
            bitmap.compress(format, quality, baos);
            byte[] bytes = baos.toByteArray();
            while(bytes.length / 1024 > maxSize && quality >= 0){
                quality -= 10;  // interval 10
                if(quality < -1) break;
                baos.reset();   // Clean up baos
                bitmap.compress(format, quality, baos);
            }

//            if(null != file && file.exists()){
                fos = new FileOutputStream(destFile);
                fos.write(baos.toByteArray());
                fos.flush();
//            }else {
//                return BitmapFactory.decodeStream(bais);
//            }
            if(recycle && !bitmap.isRecycled()) bitmap.recycle();
//
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            CloseUtils.closeIO(fos,baos);
        }
    }





    /**
     * scale bitmap, can precisely specify the image zooming
     *
     * @param picPath source picture path
     * @param reqWPixels Need to zoom wide
     * @param reqHPixels Need to zoom height
     * @return scaled bitmap
     */
    public static Bitmap scaleBitmapByMatrix(String picPath, int reqWPixels, int reqHPixels){
        if(TextUtils.isEmpty(picPath)) return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap srcBitmap = BitmapFactory.decodeFile(picPath, opts);
        opts.inJustDecodeBounds = false;
        final int bitmapW = opts.outWidth;
        final int bitmapH = opts.outHeight;
        final float scaleWidth;
        final float scaleHeight;
        if(bitmapH > reqHPixels || bitmapW > reqWPixels){
            // different landscape and high vertical screen width
            if(bitmapW <= bitmapH){
                scaleWidth = reqWPixels / bitmapW;
                scaleHeight = reqHPixels / bitmapH;
            }else{
                scaleWidth = reqHPixels / bitmapW;
                scaleHeight = reqWPixels / bitmapH;
            }
        }else{
            scaleWidth = 1;
            scaleHeight = 1;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scaledBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, bitmapW, bitmapH, matrix, true);
        if(!srcBitmap.isRecycled()) srcBitmap.recycle(); // release redundant objects
//        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(picPath, opts));
//        Bitmap.createScaledBitmap(srcBitmap, reqWPixels, reqHPixels, true);
        return scaledBitmap;
    }


    public static Bitmap scaleBitmapByMatrix(Bitmap srcBitmap, int reqWPixels, int reqHPixels){
        if(isEmptyBitmap(srcBitmap)) return null;
        final int bitmapW = srcBitmap.getWidth();
        final int bitmapH = srcBitmap.getHeight();
        final float scaleWidth;
        final float scaleHeight;
        if(bitmapH > reqHPixels || bitmapW > reqWPixels){
            // different landscape and high vertical screen width
            if(bitmapW <= bitmapH){
                scaleWidth = reqWPixels / bitmapW;
                scaleHeight = reqHPixels / bitmapH;
            }else{
                scaleWidth = reqHPixels / bitmapW;
                scaleHeight = reqWPixels / bitmapH;
            }
        }else{
            scaleWidth = 1;
            scaleHeight = 1;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scaledBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, bitmapW, bitmapH, matrix, true);
        if(!srcBitmap.isRecycled()) srcBitmap.recycle(); // release redundant objects
//        Bitmap.createScaledBitmap(srcBitmap, reqWPixels, reqHPixels, true);
        return scaledBitmap;
    }


    /**
     * see {@link #scaleBitmapBySample(String, int, int)}
     */
    public static Drawable scaleDrawableByMatrix(Drawable drawable, int reqWPixels, int reqHPixels) {
        Bitmap srcBitmap = drawable2Bitmap(drawable);
        Bitmap scaleBitmap = scaleBitmapByMatrix(srcBitmap, reqWPixels, reqHPixels);
        if(!srcBitmap.isRecycled()) srcBitmap.recycle();
        return bitmap2Drawable(scaleBitmap);
    }



    /**
     * according to the specified width high zoom pictures
     * <p> by changing the sampling rate to change the zoom pictures, is essentially changes the pixels of the
     * picture show wo change the image size
     * <p> this method is not recommended for precisely specify the size of the picture
     *
     * @param picPath picture path
     * @param reqWPixels specified width
     * @param reqHPixels specified height
     * @return after scaling the bitmap
     */
    public static Bitmap scaleBitmapBySample(String picPath, int reqWPixels, int reqHPixels){
        return scaleBitmapBySample(picPath, reqWPixels, reqHPixels, null);
    }


    /**
     * see {@link #scaleBitmapBySample(String, int, int)}
     *
     * @param picPath see {@link #scaleBitmapBySample(String, int, int)}
     * @param reqWPixels see {@link #scaleBitmapBySample(String, int, int)}
     * @param reqHPixels see {@link #scaleBitmapBySample(String, int, int)}
     * @param rgbConfig Bitmap.Config -> recommend to use RGB_565
     * @return bitmap
     */
    public static Bitmap scaleBitmapBySample(String picPath, int reqWPixels, int reqHPixels,
                                             @Nullable Bitmap.Config rgbConfig){

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap srcBitmap = BitmapFactory.decodeFile(picPath, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWPixels, reqHPixels);
        opts.inJustDecodeBounds = false;
//        opts.inPurgeable = true;          // 5.0 after deprecated
//        opts.inInputShareable = true;     // 5.0 after deprecated
//        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        if(null != rgbConfig) opts.inPreferredConfig = rgbConfig;
        Bitmap scaleBitmap = BitmapFactory.decodeFile(picPath, opts);
        if(!srcBitmap.isRecycled()) srcBitmap.recycle();
        return scaleBitmap;
    }


    //*********************************************************************************************************//
    

    /**
     * according to the specified width-height calculate image sampling rate.<br>
     *     eg: inSampleSize为2，那么采样后图片宽高均为原始图片的1/2，像素为原图的1/4，占有的内存大小为原图的1/4。
     *
     * @param opts BitmapFactory.Options
     * @param reqWPixels specified width
     * @param reqHPixels specified height
     * @return sample rate
     */
    public static int calculateInSampleSize(BitmapFactory.Options opts, int reqWPixels, int reqHPixels){

        int inSampleSize = 1;
        if(reqWPixels == 0 || reqHPixels == 0) return inSampleSize;
        int photoW = opts.outWidth;       // bitmap width
        int photoH = opts.outHeight;      // bitmap height

        if(photoH > reqHPixels || photoW > reqWPixels){

//            final int ratioW = Math.round(photoW / reqWPixels);
//            final int ratioH = Math.round(photoH / reqHPixels);
//            inSampleSize = Math.max(ratioW, ratioH);   //缩放的最大比率

//            while ((photoH >>= 1) >= reqHPixels && (photoW >>= 1) >= reqWPixels) {
//                inSampleSize <<= 1;
//            }

            final int halfH = photoH / 2;
            final int halfW = photoW / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while((halfH / inSampleSize) > reqHPixels && (halfW / inSampleSize) > reqWPixels){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static Bitmap enlargeBitmapByMatrix(String picPath, int reqWPixels, int reqHPixels){

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        Bitmap srcBitmap = BitmapFactory.decodeFile(picPath, opts);
        final int bitmapW = srcBitmap.getWidth();
        final int bitmapH = srcBitmap.getHeight();
        final float scaleWidth;
        final float scaleHeight;
        // different landscape and high vertical screen width
        if(bitmapW <= bitmapH){
            scaleWidth = reqWPixels / bitmapW;
            scaleHeight = reqHPixels / bitmapH;
        }else{
            scaleWidth = reqHPixels / bitmapW;
            scaleHeight = reqWPixels / bitmapH;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scaledBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, bitmapW, bitmapH, matrix, true);
        if(!srcBitmap.isRecycled()) srcBitmap.recycle(); // release redundant objects
//        Bitmap.createScaledBitmap(srcBitmap, reqWPixels, reqHPixels, true);
        return scaledBitmap;
    }


    /**
     * picture rotation Angle
     *
     * @param picPath picture path
     * @return rotation angle
     */
    public static int getRotateDegree(String picPath){
        int degree = 0;
        try{
            ExifInterface exifInterface = new ExifInterface(picPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                            ExifInterface.ORIENTATION_NORMAL);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
        return degree;
    }


    public static Bitmap getBitmapFromMemCache(String key) {
        return BitmapCache.mCache.get(key);
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bmp) {
        if (getBitmapFromMemCache(key) == null) {
            BitmapCache.mCache.put(key, bmp);
        }else {
            BitmapCache.mCache.remove(key);
            BitmapCache.mCache.put(key, bmp);
        }
    }


    //*********************************************************************************************************//


    /**
     * see {@link #isImage(String)}
     *
     * @param file 　file
     * @return see {@link #isImage(String)}
     */
    public static boolean isImage(File file) {
        return file != null && isImage(file.getPath());
    }

    /**
     * according to the file name to determine whether a file for images
     *
     * @param filePath 　file path
     * @return {@code true}: image<br>{@code false}: no
     */
    public static boolean isImage(String filePath) {
        String path = filePath.toUpperCase();
        return path.endsWith(".PNG") || path.endsWith(".JPG")
                || path.endsWith(".JPEG") || path.endsWith(".BMP") || path.endsWith(".GIF");
    }


    /**
     * see {@link #getImageType(byte[])}
     *
     * @param picPath picture file path
     * @return see {@link #getImageType(byte[])}
     */
    public static String getImageType(String picPath){
        if(!TextUtils.isEmpty(picPath)) return getImageType(new File(picPath));
        return null;
    }


    /**
     * see {@link #getImageType(byte[])}
     *
     * @param picFile picture file
     * @return see {@link #getImageType(byte[])}
     */
    public static String getImageType(File picFile){
        if(picFile == null) return null;
        InputStream is = null;
        try{
            is = new FileInputStream(picFile);
            return getImageType(is);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally{
            CloseUtils.closeIO(is);
        }
    }


    /**
     * see {@link #getImageType(byte[])}
     *
     * @param is image input stream
     * @return type of image
     */
    public static String getImageType(InputStream is){
        if(is == null) return null;
        try{
            byte[] bytes = new byte[8];
            return is.read(bytes, 0, 8) != -1 ? getImageType(bytes) : null;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * get type of the image
     *
     * @param bytes bitmap before 8 bytes
     * @return type of image
     */
    public static String getImageType(byte[] bytes){
        if(isJPEG(bytes)) return "JPEG";
        if(isGIF(bytes)) return "GIF";
        if(isPNG(bytes)) return "PNG";
        if(isBMP(bytes)) return "BMP";
        return null;
    }


    private static boolean isJPEG(byte[] b){
        return b.length >= 2 && (b[0] == (byte)0xFF) && (b[1] == (byte)0xD8);
    }


    private static boolean isBMP(byte[] b){
        return b.length >= 2 && (b[0] == 0x42) && (b[1] == 0x4d);
    }


    private static boolean isPNG(byte[] b){
        return b.length >= 8
                && (b[0] == (byte)137 && b[1] == (byte)80
                && b[2] == (byte)78 && b[3] == (byte)71
                && b[4] == (byte)13 && b[5] == (byte)10
                && b[6] == (byte)26 && b[7] == (byte)10);
    }


    private static boolean isGIF(byte[] b){
        return b.length >= 6
                && b[0] == 'G' && b[1] == 'I'
                && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }


    //*********************************************************************************************************//


    public Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap;
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 1;
        try{
//            bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(uri),null,opts);
            bitmap = BitmapFactory.decodeStream(
                    AppUtils.getAppContext().getContentResolver().openInputStream(uri));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    /**
     * 4.4及以上获取图片的方法
     *
     * @param ctx context
     * @param uri local uri
     * @return local picture path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPicPath(final Context ctx, final Uri uri){

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if(isKitKat && DocumentsContract.isDocumentUri(ctx, uri)){
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(ctx, contentUri, null, null);
            }
            // MediaProvider
            else if(isMediaDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(ctx, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())){

            // Return the remote address
            if(isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(ctx, uri, null, null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try{
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()){
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally{
            if(cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



//=====================================================================================================


    //把bitmap转换成base64
    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //把base64转换成bitmap
    public static Bitmap getBitmapFromBase64(String bitmapBase64Str){
        byte[] bitmapArray;
        try{
            bitmapArray = Base64.decode(bitmapBase64Str, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }



    public static Bitmap createCutReflectedImage(View view, int reflectHeight){
        view.buildDrawingCache();
        Bitmap img = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);

        int width = img.getWidth();
        int height = img.getHeight();
        if(height <= reflectHeight) return null;
        Matrix matrix = new Matrix();
        matrix.preScale(1.0F, -1.0F);
        Bitmap bitmap1 = Bitmap.createBitmap(img, 0, height - reflectHeight, width,
                                             reflectHeight, matrix, true);
        Bitmap bitmap2 = Bitmap.createBitmap(width, reflectHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
        LinearGradient lg = new LinearGradient(0.0F, 0.0F, 0.0F,
                                               bitmap2.getHeight(), -2130706433, 16777215, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(lg);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0F, 0.0F, width, bitmap2.getHeight(), paint);
        if(!img.isRecycled()) img.recycle();
        if(!bitmap1.isRecycled()) bitmap1.recycle();
        return bitmap2;
    }



    @SuppressWarnings("deprecation")
    public static ArrayList<String> getGalleryPhotos(Activity act) {
        ArrayList<String> galleryList = new ArrayList<String>();
        try {
            final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imgCursor = act.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                  columns, null, null, orderBy);
            if (imgCursor != null && imgCursor.getCount() > 0) {
                while (imgCursor.moveToNext()) {
                    int dataColumnIndex = imgCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String item = imgCursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
        return galleryList;
    }


    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.setBackgroundColor(Color.WHITE);
                view.draw(canvas);
            }
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;

    }

    public static boolean saveImageToGallery(Context ctx, Bitmap bmp, boolean isPng) {
        if (bmp == null) return false;
        File appDir = new File(StorageUtils.getExStorageDir(), ctx.getString(R.string.app_name));
        if(!appDir.exists() && !appDir.mkdir()) return false;
        String fileName;
        fileName = isPng ? System.currentTimeMillis() + ".png" : System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(isPng ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, fos);
            bmp.recycle();
            fos.flush();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            ContentResolver cr = ctx.getContentResolver();
            MediaStore.Images.Media.insertImage(cr, file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
        return true;
    }


    /**
     * 替换文本为图片
     *
     * @param cs
     * @param regPattern
     * @param drawable
     * @return
     */
    public static SpannableString replaceImageSpan(CharSequence cs, String regPattern, Drawable drawable) {
        SpannableString ss = cs instanceof SpannableString ? (SpannableString) cs : new SpannableString(cs);
        try {
            ImageSpan is = new ImageSpan(drawable);
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                String key = matcher.group();
                ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ss;
    }


    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) return null;
        if (logo == null) return src;

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) return null;
        if (logoWidth == 0 || logoHeight == 0) return src;

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }


}
