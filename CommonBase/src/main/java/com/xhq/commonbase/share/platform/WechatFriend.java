package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

import cn.sharesdk.wechat.friends.Wechat;

/**
 * 微信好友
 * Created by ychen on 2017/12/9.
 */

public class WechatFriend extends BaseSharePlatform<BaseShareArgs>{
    @Override
    public String getName() {
        return "微信好友";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_weixhy;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        ShareUtil.sharePlatform(args, Wechat.NAME);
    }
}
