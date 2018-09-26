package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 微信朋友圈
 * Created by ychen on 2017/12/11.
 */

public class WechatFriendCircle extends BaseSharePlatform<BaseShareArgs>{
    @Override
    public String getName() {
        return "微信朋友圈";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_weix;
    }

    @Override
    public void dealClick(BaseShareArgs args,View view) {
        ShareUtil.sharePlatform(args, WechatMoments.NAME);
    }
}
