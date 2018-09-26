package com.xhq.commonbase.share.platform;

import android.view.View;

import com.common.R;
import com.common.dialog.ReportDialog;
import com.common.utils.ToastUtils;
import com.common.utils.share.ShareUtil;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;
import com.smartcity.commonbussiness.user.UserProvider;

/**
 * Created by ychen on 2017/12/11.
 */

public class ShareReport extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "举报";
    }

    @Override
    public int getPicResId() {
        return R.drawable.qmx_share_jubao;
    }

    @Override
    public void dealClick(final BaseShareArgs args, View view) {
        if(args.targetUserId <=0 || args.reviewId <= 0){
            ToastUtils.showToast("请检查分享举报参数！");
            return;
        }

        final ReportDialog reportDialog = new ReportDialog(args.activity, view);
        reportDialog.setOnClick(new ReportDialog.OnItemClick() {
            @Override
            public void quxiaoClick() {
                reportDialog.dismiss();
            }

            @Override
            public void quedingClick(StringBuffer reason) {
                ShareUtil.getReport(reason, String.valueOf(args.reviewId), String.valueOf(args.reviewType));
                reportDialog.dismiss();
            }
        });
    }

    /**
     * 显示条件
     * @param args
     * @return
     */
    @Override
    public boolean isShow(BaseShareArgs args) {
        if(args.targetUserId <=0 || args.reviewId <= 0){
            return false;
        }

        boolean isMe = UserProvider.getInstance().isLogin() && (UserProvider.getInstance().getUserIdInt() == args.targetUserId);
        return (!isMe);
    }
}
