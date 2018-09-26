package com.xhq.commonbase.share.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.R;
import com.common.utils.ToastUtils;
import com.common.utils.share.ShareAdapter;
import com.common.utils.share.platform.ChatFriendCircle;
import com.common.utils.share.platform.ChatTeam;
import com.common.utils.share.platform.MobileQQ;
import com.common.utils.share.platform.ShareAlipay;
import com.common.utils.share.platform.ShareClilpborad;
import com.common.utils.share.platform.ShareEmail;
import com.common.utils.share.platform.SharePullBlack;
import com.common.utils.share.platform.ShareQQZone;
import com.common.utils.share.platform.ShareReport;
import com.common.utils.share.platform.ShareSinaWeibo;
import com.common.utils.share.platform.ShareSms;
import com.common.utils.share.platform.SysShare;
import com.common.utils.share.platform.WechatFriend;
import com.common.utils.share.platform.WechatFriendCircle;
import com.smartcity.commonbase.util.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享基础工具类
 * @param <T>
 */
public abstract class BaseShareTool<T extends BaseShareArgs> {

    protected T mShareArgs;
    protected List<BaseSharePlatform> mDataUp = new ArrayList<>();
    protected List<BaseSharePlatform> mDataBelow = new ArrayList<>();

    public BaseShareTool(T shareArgs) {
        this.mShareArgs = shareArgs;
    }

    public BaseShareTool() {
    }

    public void setShareArgs(T args){
        this.mShareArgs = args;
    }

    public T getShareArgs() {
        return mShareArgs;
    }

    public void show(){
        if(mShareArgs == null){
            ToastUtils.showToast("ShareArgs不能为null!");
            return;
        }

        mDataUp.clear();
        mDataBelow.clear();
        final Dialog dialog = new Dialog(mShareArgs.activity, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(mShareArgs.activity).inflate(getDialogLayout(), null);
        TextView txt_cancle = dialogView.findViewById(R.id.txt_cancle);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        View center_divider = dialogView.findViewById(R.id.center_divider);

        RecyclerView reyclerUp = dialogView.findViewById(R.id.recycler_up);
        RecyclerView recyclerBelow = dialogView.findViewById(R.id.recycler_below);

        reyclerUp.setLayoutManager(new LinearLayoutManager(mShareArgs.activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerBelow.setLayoutManager(new LinearLayoutManager(mShareArgs.activity, LinearLayoutManager.HORIZONTAL, false));

        addItems(mDataUp,getUpItems());
        if(getBelowItems() != null) {
            addItems(mDataBelow,getBelowItems());
        }

        ShareAdapter adapterUp = new ShareAdapter(mShareArgs.activity,getItemLayout(),mDataUp);
        ShareAdapter adapterBelow = new ShareAdapter(mShareArgs.activity,getItemLayout(),mDataBelow);

        reyclerUp.setAdapter(adapterUp);
        recyclerBelow.setAdapter(adapterBelow);

        if(mDataBelow.isEmpty()){
            center_divider.setVisibility(View.GONE);
            recyclerBelow.setVisibility(View.GONE);
        }

        adapterUp.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                dialog.dismiss();
                mDataUp.get(position).dealClick(mShareArgs,v);
            }

            @Override
            public boolean onItemLongClick(View v, int position) {
                return false;
            }
        });


        adapterBelow.setOnItemShortClickListener(new RecyclerAdapter.OnItemShortClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                dialog.dismiss();
                mDataBelow.get(position).dealClick(mShareArgs,v);
            }
        });

        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.show();

    }

    protected List<BaseSharePlatform> getUpItems(){
        List<BaseSharePlatform> list = new ArrayList<>();
        list.add(new ChatFriendCircle());//泡圈朋友圈
        list.add(new ChatTeam());//泡圈聊天
        list.add(new WechatFriendCircle());//微信朋友圈
        list.add(new WechatFriend());//微信好友
        list.add(new MobileQQ());//手机QQ
        list.add(new ShareQQZone());//QQ空间
        list.add(new ShareSinaWeibo());//新浪微博
        list.add(new ShareAlipay());//支付宝
        return list;
    }

    protected List<BaseSharePlatform> getBelowItems(){
        List<BaseSharePlatform> list = new ArrayList<>();
        list.add(new SysShare());//系统分享
        list.add(new ShareSms());//短信
        list.add(new ShareEmail());//邮件
        list.add(new ShareClilpborad());//复制链接
        list.add(new ShareReport());//举报
        list.add(new SharePullBlack());//拉黑
        return list;
    }

    private void addItems(List<BaseSharePlatform> list,List<BaseSharePlatform> data){
        for(BaseSharePlatform entity : data){
            if(entity.isShow(mShareArgs)){
                list.add(entity);
            }
        }
    }


    protected int getDialogLayout(){
        return R.layout.dialog_common_share;
    }

    protected int getItemLayout(){
        return R.layout.list_item_share_dialog;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ShareCallback.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String platform = data.getStringExtra(ShareCallback.TAG_PLATFORM_NAME);
            if(mShareArgs.callback != null){
                mShareArgs.callback.onComplete(platform);
            }
        }
    }


}
