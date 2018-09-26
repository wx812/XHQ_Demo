package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by ychen on 2017/12/9.
 */

public class MobileQQ extends BaseSharePlatform<BaseShareArgs>{
    @Override
    public String getName() {
        return "手机QQ";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_qqshouji;
    }

    @Override
    public void dealClick(BaseShareArgs args,View view) {
        ShareUtil.sharePlatform(args, QQ.NAME);
    }
}
