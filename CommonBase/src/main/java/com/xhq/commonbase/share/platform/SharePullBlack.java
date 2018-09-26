package com.xhq.commonbase.share.platform;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xhq.commonbase.share.base.BaseShareArgs;
import com.xhq.commonbase.share.base.BaseSharePlatform;
import com.xhq.commonbase.widget.R;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ychen on 2017/12/9.
 */

public class SharePullBlack extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "拉黑";
    }

    @Override
    public int getPicResId() {
        return R.drawable.qmx_share_lahei;
    }

    @Override
    public void dealClick(final BaseShareArgs args, View view) {
        if(args.targetUserId <=0 || args.blackListType < 0 || TextUtils.isEmpty(args.blackUserName)){
            ToastUtils.showToast("请检查拉黑参数！");
            return;
        }

        if(!UserProvider.getInstance().isLogin()){
            //拉黑必须登录
            args.activity.startActivity(new Intent(args.activity, LoginActivity.class));
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(args.activity);
        String title = "确认拉黑%s？";
        if(!TextUtils.isEmpty(args.blackUserName)){
            title = String.format(title,args.blackUserName);
        }else {
            title = String.format(title,"该用户");
        }

        builder.setMessage(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        executePullBlack(args);
                    }
                })
                .setNegativeButton("取消",null)
                .create().show();

    }

    @Override
    public boolean isShow(BaseShareArgs args) {
        if(args.targetUserId <=0 || args.blackListType < 0 || TextUtils.isEmpty(args.blackUserName)){
            return false;
        }

        boolean isMe = UserProvider.getInstance().isLogin() && (UserProvider.getInstance().getUserIdInt() == args.targetUserId);
        return (!isMe);
    }

    public interface PullBlackListener{
        void onPullBlackSuccess(int blackListUserId, String msg);
        void onPullBlackFailure(int blackListUserId, String msg);
    }

    /**
     * 调用拉黑接口
     * @param args
     */
    private void executePullBlack(final BaseShareArgs args){
        new ApiWrapper().getPulltheblack(UserProvider.getInstance().getUserIdInt(), args.targetUserId, args.blackListType, args.blackListAppName)
                .compose(RxUtils.<HttpResultEntity<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return RxUtils.checkLogin(observable);
                    }
                })
                .subscribe(new SubscriberNetCallBack<String>() {
                    @Override
                    protected void onLogin(String msg) {
                    }

                    @Override
                    protected void onSuccess(String msg) {
                        if(null != args.pullBlackListener){
                            args.pullBlackListener.onPullBlackSuccess(args.targetUserId,msg);
                        }
                    }

                    @Override
                    protected void onFinished() {

                    }

                    @Override
                    protected void onFailure(Throwable throwable, String msg) {
                        if(null != args.pullBlackListener) {
                            args.pullBlackListener.onPullBlackFailure(args.targetUserId, msg);
                        }
                    }
                });
    }

}
