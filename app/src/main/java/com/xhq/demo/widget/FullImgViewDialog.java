package com.xhq.demo.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xhq.demo.R;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.io.File;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/12/22.
 *     Desc  : Full screen view ImageView dialog.
 *     Updt  : Description.
 * </pre>
 */
public class FullImgViewDialog extends Dialog{

    private Bitmap bitmap;
    private File bitmapFile;
    private Context context;


    public FullImgViewDialog(Context context, Bitmap bm){
        super(context, R.style.dialog_full_screen_view);
        windowDeploy();
        bitmap = bm;
    }


    public FullImgViewDialog(Context context, File file){
        super(context, R.style.dialog_full_screen_view);
        windowDeploy();
        this.context = context;
        bitmapFile = file;
    }


    public FullImgViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener){
        super(context, cancelable, cancelListener);
    }


    public void windowDeploy(){
        Window window = this.getWindow();
        if(window != null){
            window.setWindowAnimations(R.style.Anim_Window_Scale); //设置窗口弹出动画
//            window.setBackgroundDrawableResource();
            WindowManager.LayoutParams wmlp = window.getAttributes();
//            wmlp.alpha = 0.6f;
            wmlp.gravity = Gravity.CENTER;
            window.setAttributes(wmlp);
        }
    }


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_full_screen_view, null);
        ImageView iv = view.findViewById(R.id.iv_full_scree_image);
        int[] screenWH = ScreenUtils.getScreenWH();
//        Bitmap scaleBitmap = ImageUtils.getBitmap(bitmapFile, screenWH[0], screenWH[1]);
//        iv.setImageBitmap(scaleBitmap);
        Glide.with(context).load(bitmapFile).asBitmap().dontAnimate()
             .fitCenter()
//             .diskCacheStrategy(DiskCacheStrategy.SOURCE)
             .into(new SimpleTarget<Bitmap>(screenWH[0], screenWH[1]){
                 @Override
                 public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation){
                     iv.setImageBitmap(resource);
                 }
             });
        setContentView(view);
    }


    public void show(){
        setCanceledOnTouchOutside(true);
        super.show();
    }


    public void dismiss(){
        super.dismiss();
    }
}
