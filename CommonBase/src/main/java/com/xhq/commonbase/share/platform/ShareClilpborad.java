package com.xhq.commonbase.share.platform;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.common.R;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareClilpborad extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "复制链接";
    }

    @Override
    public int getPicResId() {
        return R.drawable.qmx_share_copy;
    }

    @Override
    public void dealClick(BaseShareArgs args, View view) {
        ClipboardManager cmb = (ClipboardManager) args.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(args.targetUrl);
    }
}
