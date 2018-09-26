package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

import cn.sharesdk.tencent.qzone.QZone;

/**
 * QQ空间
 * Created by ychen on 2017/12/11.
 */

public class ShareQQZone extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "QQ空间";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_qqkongj;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        ShareUtil.sharePlatform(args, QZone.NAME);
    }
}
