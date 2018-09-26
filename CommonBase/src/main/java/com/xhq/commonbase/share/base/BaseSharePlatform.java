package com.xhq.commonbase.share.base;

import android.view.View;

/**
 * 分享的Item基础类
 * Created by ychen on 2017/12/8.
 */

public abstract class BaseSharePlatform<T extends BaseShareArgs>{

    public abstract String getName();

    /**
     * 图片资源id，如果是网络图片则为默认图和失败图
     * @return
     */
    public abstract int getPicResId();

    /**
     * 网络图片url
     * @return
     */
    public String getPicUrl(){
        return null;
    }

    /**
     * 处理item点击事件
     * @param args
     * @param view
     */
    public abstract void dealClick(T args,View view);

    /**
     * 是否显示
     * @param args
     * @return
     */
    public boolean isShow(T args){
        return true;
    }


}
