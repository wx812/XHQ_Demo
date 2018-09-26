package com.xhq.commonbase.share.platform;

import android.content.Intent;
import android.view.View;

import com.common.R;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareEmail extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "邮件";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_youjiang;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        String emailBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + args.targetUrl;
        //邮件主题
        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
        //邮件内容
        email.putExtra(Intent.EXTRA_TEXT, emailBody);
        args.activity.startActivityForResult(Intent.createChooser(email, "请选择邮件发送内容"), 1001);
    }
}
