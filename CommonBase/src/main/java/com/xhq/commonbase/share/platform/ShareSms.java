package com.xhq.commonbase.share.platform;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.common.R;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareSms extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "短信";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_duanxinfenxiang;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        String smsBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + args.targetUrl;
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra("sms_body", smsBody);
        args.activity.startActivityForResult(sendIntent, 1002);
    }
}
