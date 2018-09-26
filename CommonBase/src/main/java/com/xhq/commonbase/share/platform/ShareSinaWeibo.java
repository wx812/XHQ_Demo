package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareSinaWeibo extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "新浪微博";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_xinlangweibo;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        ShareUtil.sharePlatform(args, SinaWeibo.NAME);
    }
}
