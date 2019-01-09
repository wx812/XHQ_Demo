package com.xhq.demo.tools.imageTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xhq.demo.HomeApp;
import com.xhq.demo.R;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.constant.apiconfig.ApiUrl;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/6/14.
 *     Desc  : 整合第三方图片工具类.
 *     Updt  : Description.
 * </pre>
 */
public class ImgUtilThdParty{


    /**
     * 生成二维码Bitmap
     *
     * @param data   文本内容
     * @param logoBm 二维码中心的Logo图标（可以为null）
     * @return 合成后的bitmap
     */
    public static Bitmap createQRImage(@NonNull String data, @Nullable Bitmap logoBm){

        int widthPix = ScreenUtils.getScreenW();
        int heightPix = widthPix = widthPix / 5 * 3;

        //配置参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 3); //default is 4

        BitMatrix bitMatrix;    // 图像数据转换，使用了矩阵转换
        try{
            bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
        }catch(WriterException e){
            e.printStackTrace();
            return null;
        }
        int[] pixels = new int[widthPix * heightPix];
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for(int y = 0; y < heightPix; y++){
            for(int x = 0; x < widthPix; x++){
                if(bitMatrix.get(x, y)){
                    pixels[y * widthPix + x] = 0xff000000;
                }else{
                    pixels[y * widthPix + x] = 0xffffffff;
                }
            }
        }

        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

        if(logoBm != null) bitmap = ImageUtil.addLogo(bitmap, logoBm);
        return bitmap;
//        try{
//                //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
//            FileOutputStream fos;
//            fos = new FileOutputStream(filePath);
//            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//            } catch(Exception e){
//                e.printStackTrace();
//                 return null;
//            } finally{
//                CloseUtils.closeIO(fos);
//            }

    }


//	/**
//   * https://github.com/FinalTeam/RxGalleryFinal
//	 * 初始化配置GalleyFinal
//   * RxGalleryFinal是一个android图片/视频文件选择器。
//   *  其支持多选、单选、拍摄和裁剪，主题可自定义，无强制绑定第三方图片加载器。
//	 * @param context
//	 */
//	public static  void initGalleyFinal(Context context,boolean forceCrop,int width,int height){
//		//自定义主题
//		ThemeConfig theme = new ThemeConfig.Builder()
//				.setTitleBarBgColor(Color.rgb(0xFF, 0x57, 0x22))
////				.setTitleBarBgColor(R.color.bg_white)
//				.setTitleBarTextColor(Color.BLACK)
//				.setTitleBarIconColor(Color.BLACK)
//				.setFabNornalColor(Color.RED)
//				.setFabPressedColor(Color.BLUE)
//				.setCheckNornalColor(Color.WHITE)
//				.setCheckSelectedColor(Color.BLACK)
//				.build();
//
//		//配置功能
//		FunctionConfig functionConfig;
//
//		FunctionConfig.Builder builder = new FunctionConfig.Builder()
//				.setEnableCamera(true)
//				.setEnableEdit(true)
//				.setEnableCrop(true)
//				.setEnableRotate(true)
//				.setCropWidth(width)//裁剪宽度
//				.setCropHeight(height)//裁剪高度
////				.setEnablePreview(true)
////				.setRotateReplaceSource(true)//旋转是否覆盖源文件  设置true后每次旋转都会压缩图片质量
//				.setCropReplaceSource(true);//裁剪是否覆盖源文件
//		if(width == height){
//			builder.setCropSquare(true);//裁剪正方形
//		}else {
//			builder.setCropSquare(false);
//		}
//		if(forceCrop){
//			builder.setForceCrop(true);  //强制裁剪
//			builder.setForceCropEdit(true);//强制裁剪后是否可编辑
//		}
//
//		functionConfig = builder.build();
//		//配置imageloader
//		ImageLoader imageloader = new GlideImageLoader();
//		CoreConfig coreConfig = new CoreConfig.Builder(context, imageloader, theme)
//				.setFunctionConfig(functionConfig)
//				.setTakePhotoFolder(new File(FileUtils.FileDir))//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
//				.setEditPhotoCacheFolder(new File(FileUtils.FileDir))
//				.build();
//		GalleryFinal.init(coreConfig);
//	}


    //set the glide display background
    public static SimpleTarget<Bitmap> glideBackground(final View view){
        return new SimpleTarget<Bitmap>(){
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation){
                Drawable drawable = new BitmapDrawable(AppUtils.getResources(),resource);
                view.setBackground(drawable);
            }
        };
    }

    public static void loadPictureById(RequestManager manager, ImageView view, String attach_id) {
        String url = ApiUrl.URL_HOST + ApiUrl.URL_DOWNLOAD + "?" + ApiKey.DownloadImg.ATTACH_ID + "=" + attach_id + "&" +
                ApiKey.CommonUrlKey.sk + "=" + SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, "");
        manager.load(url)
               .into(view);
    }


    public static void displayImg(final Context context, final View img, String url, final int width, final int height) {
        //如果url 或  图片路径attach_path为空
       /* if (StringUtils.isNullOrEmpty(url)|| StringUtils.isNullOrEmpty(url.substring(url.lastIndexOf('/')+1))){
            img.setBackgroundResource(R.mipmap.ic_home_convenient);
            return;
        }*/

        if (img instanceof ImageView) {
            Glide.with(HomeApp.getAppContext())
                 .load(url)
                 .asBitmap()
                 .placeholder(ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                 .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                 .diskCacheStrategy(DiskCacheStrategy.RESULT)   //缓存控件大小的图片
//                 .transform(new GlideRoundTransform(context,10))
                 .into((ImageView) img);
        } else {
            Glide.with(HomeApp.getAppContext())
                 .load(url)
                 .asBitmap()
                 .diskCacheStrategy(DiskCacheStrategy.RESULT)   //缓存控件大小的图片
                 .error(R.mipmap.ic_launcher)
                 .placeholder(ContextCompat.getDrawable(context,R.mipmap.ic_launcher))
//                 .transform(new GlideRoundTransform(context,10))
                 .into(new ViewTarget<View, Bitmap>(img) {

                     @Override
                     public void onResourceReady(Bitmap resource,
                                                 GlideAnimation<? super Bitmap> glideAnimation) {
//                            img.setBackgroundDrawable(new BitmapDrawable(resource));
                         img.setBackground(new BitmapDrawable(context.getResources(), resource));
                     }
                 });
        }
    }

    public static void displayImg(final Context context, final View img, String url,int error, final int width, final int height) {
      /*  if (StringUtils.isNullOrEmpty(url)) {
            img.setBackgroundResource(error);
            return;
        }*/
        if (img instanceof ImageView) {
            Glide.with(HomeApp.getAppContext())
                    .load(url)
                    .asBitmap()
//                    .error(R.mipmap.ic_home_convenient)
//                    .transform(new GlideRoundTransform(context,10))
//                    .placeholder(ContextCompat.getDrawable(context,R.mipmap.ic_home_convenient))
                    .into((ImageView) img);
        } else {
            Glide.with(HomeApp.getAppContext())
                    .load(url)
                    .asBitmap()
//                    .error(R.mipmap.ic_home_convenient)
//                    .transform(new GlideRoundTransform(context,10))
//                    .placeholder(ContextCompat.getDrawable(context,R.mipmap.ic_home_convenient))
                    .into(new ViewTarget<View, Bitmap>(img) {

                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
//                            img.setBackgroundDrawable(new BitmapDrawable(resource));
                            img.setBackground(new BitmapDrawable(context.getResources(),resource));

                        }
                    });
        }
    }


    public static void displayImgNoRound(RequestManager glide, final Context context, final View img, String url){

        if(img instanceof ImageView){
            glide.load(url)
                 .asBitmap()
//                   .placeholder(R.mipmap.default_pic)
                 .dontAnimate()
//                   .error(R.mipmap.app_logo) // will be displayed if the image cannot be loaded
                 .into((ImageView)img);
        }else{
            glide.load(url)
                 .asBitmap()
//                   .placeholder(R.mipmap.default_pic)
//                   .error(R.mipmap.app_logo)
                 .dontAnimate()
                 .into(new ViewTarget<View, Bitmap>(img){
                     @Override
                     public void onResourceReady(Bitmap resource,
                                                 GlideAnimation<? super Bitmap> glideAnimation){
                         img.setBackground(new BitmapDrawable(context.getResources(), resource));
                     }
                 });
        }
    }


    public static void displayLocalImg(RequestManager glide, final Context context, final View img, File file){
        if(img instanceof ImageView){
            glide.load(file)
                 .asBitmap()
//                   .placeholder(R.mipmap.default_pic)
                 .dontAnimate()
//                   .error(R.mipmap.app_logo) // will be displayed if the image cannot be loaded
                 .into((ImageView)img);
        }else{
            glide.load(file)
                 .asBitmap()
//                   .placeholder(R.mipmap.default_pic)
//                   .error(R.mipmap.app_logo)
                 .dontAnimate()
                 .into(new ViewTarget<View, Bitmap>(img){
                     @Override
                     public void onResourceReady(Bitmap resource,
                                                 GlideAnimation<? super Bitmap> glideAnimation){
                         img.setBackground(new BitmapDrawable(context.getResources(), resource));
                     }
                 });
        }
    }



    public static void displayLocalImg(RequestManager glide, final Context context, final View img, Uri uri) {

        if (img instanceof ImageView) {
            glide.load(uri)
                 .asBitmap()
//					.placeholder(R.mipmap.default_pic)
                 .dontAnimate()
//                 .error(R.mipmap.default_pic) // will be displayed if the image cannot be loaded
                    /*.transform(new GlideRoundTransform(context,10))*/
                 .into((ImageView) img);
        } else {
            glide
                    .load(uri)
                    .asBitmap()
//					.placeholder(R.mipmap.default_pic)
//                    .error(R.mipmap.default_pic)
                    .dontAnimate()
//					.transform(new GlideRoundTransform(context, 10))
                    .into(new ViewTarget<View, Bitmap>(img) {

                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            img.setBackground(new BitmapDrawable(context.getResources(), resource));
                        }
                    });
        }
    }


        public static void displayImg(RequestManager glide,final Context context, final View img, String url, final int width, final int height) {
//        if (img instanceof com.smzw.view.CircleImageView) {
//            glide
//                    .load(url)
//                    .asBitmap()
//                    .placeholder(R.mipmap.default_pic)
//                    .error(R.mipmap.default_pic) // will be displayed if the image cannot be loaded
//                    .dontAnimate()
//					/*.transform(new GlideRoundTransform(context,10))*/
//                    .into((com.smzw.view.CircleImageView) img);
//        } else {
            glide
                    .load(url)
                    .asBitmap()
//                    .placeholder(R.mipmap.default_pic)
//                    .error(R.mipmap.default_pic)
                    .dontAnimate()
					/*.transform(new GlideRoundTransform(context,10))*/
                    .into(new ViewTarget<View, Bitmap>(img) {

                        @Override
                        public void onResourceReady(Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            img.setBackground(new BitmapDrawable(context.getResources(), resource));
                        }
                    });
//        }
    }
}
