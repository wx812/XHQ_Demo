package com.xhq.commonbase.share.platform;

import android.content.Intent;
import android.view.View;

import com.common.R;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

/**
 * Created by ychen on 2017/12/11.
 */

public class SysShare extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "系统分享";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_xitongfgenx;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
        intent.setType("text/plain"); // 分享发送到数据类型
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject"); // 分享的主题
        intent.putExtra(Intent.EXTRA_TEXT, args.targetUrl); // 分享的内容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
        args.activity.startActivity(Intent.createChooser(intent, "分享")); // //目标应用选择对话框的
    }
}
