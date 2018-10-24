package com.xhq.demo.base.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * BaseLVHolder Wrap the ItemView, a ViewHolder represents an ItemView
 */
public class BaseLVHolder{

    private SparseArray<View> mViewList;
    private View mRootView;
    private int mPosition;
//	private static LoadImage mImageLoader  = new LoadImage();


    public BaseLVHolder(Context ctx, ViewGroup parent, @LayoutRes int layoutId, int position){
        this.mPosition = position;
        mViewList = new SparseArray<>();
        mRootView = LayoutInflater.from(ctx).inflate(layoutId, parent, false);
        mRootView.setTag(this);
    }


    public BaseLVHolder(Context ctx, ViewGroup parent, View view, int position){
        this.mPosition = position;
        mViewList = new SparseArray<>();
        mRootView = view;
        mRootView.setTag(this);
    }


    public static BaseLVHolder getViewHolder(Context ctx, ViewGroup parent, View convertView, int layoutId, int position){
        if(convertView == null){
            return new BaseLVHolder(ctx, parent, layoutId, position);
        }else{
            BaseLVHolder holder = (BaseLVHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }


    public static BaseLVHolder getViewHolder(Context ctx, ViewGroup parent, View convertView, View codeLayoutView, int position){
        if(convertView == null){
            return new BaseLVHolder(ctx, parent, codeLayoutView, position);
        }else{
            BaseLVHolder holder = (BaseLVHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }


    public View getConvertView(){
        return mRootView;
    }


    public <T extends View> T getView(@IdRes int viewId){
        View view = mViewList.get(viewId);
        if(view == null){
            view = mRootView.findViewById(viewId);
            mViewList.put(viewId, view);
        }
        return (T)view;
    }


    public BaseLVHolder setVisibility(int viewId, int visibility){
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }


    /**
     * 为TextView设置文字
     */
    public BaseLVHolder setText(int textViewId, CharSequence text){
        TextView tv = getView(textViewId);
        tv.setText(text);
        return this;
    }


    public BaseLVHolder setText(int textViewId, SpannableStringBuilder ssb){
        TextView tv = getView(textViewId);
        tv.setText(ssb);
        return this;
    }


    /**
     * 为imageView设置图片、通过resource查找资源
     */
    public BaseLVHolder setImageResource(int imageViewId, int resId){
        ImageView iv = getView(imageViewId);
        iv.setImageResource(resId);
        return this;
    }


    /**
     * 通过bitmap对象为imageView设置背景
     */
    public BaseLVHolder setImageBitmap(int imageViewId, Bitmap bitmap){
        ImageView iv = getView(imageViewId);
        iv.setImageBitmap(bitmap);
        return this;
    }


    /**
     * 在网络上获取图片并设置给imageView
     * @param imageViewId
     * @param url
     * @param resId 图片加载的时候默认显示的资源
     * @return
     */
    public BaseLVHolder setImageForURL(int imageViewId, String url, int resId) {
        ImageView iv = getView(imageViewId);
        iv.setTag(url);
        iv.setImageResource(resId);
//		mImageLoader.getBitmapFromAsyncTask(iv, url);
        return this;
    }


    /**
     * 设置CheckBox选中状态
     */
    public BaseLVHolder setCheckBoxState(int checkBoxId, boolean isChecked){
        CheckBox cb = getView(checkBoxId);
        cb.setChecked(isChecked);
        return this;
    }


    public Boolean getCheckBoxState(int checkBoxId){
        CheckBox cb = getView(checkBoxId);
        return cb.isChecked();
    }


    public BaseLVHolder setProgres(int progressBarId, int diff){
        ProgressBar bar = getView(progressBarId);
        bar.setProgress(diff);//指定增加的进度
        return this;
    }
}
