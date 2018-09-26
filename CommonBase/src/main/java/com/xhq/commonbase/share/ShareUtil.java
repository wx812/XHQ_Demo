package com.xhq.commonbase.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.common.R;
import com.common.base.application.BaseApplication;
import com.common.base.entity.HttpResultEntity;
import com.common.bean.ShareToCircleEntity;
import com.common.dialog.ReportDialog;
import com.common.http.ApiWrapper;
import com.common.http.callback.SubscriberNetCallBack;
import com.common.utils.RxUtils;
import com.common.utils.ToastUtils;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.ShareCallback;
import com.smartcity.commonbussiness.user.UserProvider;
import com.smartcity.commonbussiness.user.login.LoginActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.common.utils.UiUtils.getString;

/**
 * 分享工具类包含举报和分享泡圈群聊，泡圈朋友圈，微信，qq，新浪微博，支付宝等等
 * Created by ychen on 2017/8/25.
 */

public class ShareUtil {

    @Deprecated
    public static void commonShare(final Activity activity, final String title, final String message,
                                   final String targetUrl, final String imageUrl, int authorUserId, int reviewId, int reviewType, String code, String param) {
        share(activity, title, message, targetUrl, imageUrl, authorUserId, reviewId, reviewType, false, null);
    }

    @Deprecated
    public static void share(final Activity activity, final String title, final String message,
                             final String targetUrl, final String imageUrl, int authorUserId,
                             final int reviewId, final int reviewType, boolean isShowForward, final IShareInSideApp shareInSideApp) {

        final List<ShareentriesBaseEntiy> baseEntiyList = new ArrayList<>();
        final List<SystemfunctionItemEntiy> entiyList = new ArrayList<>();
        //是否是自己发的
        boolean isMe = UserProvider.getInstance().isLogin() && (UserProvider.getInstance().getUserIdInt() == authorUserId);

        if (!isMe && isShowForward) {
            baseEntiyList.add(new ShareentriesBaseEntiy("转发", R.drawable.qmx_huati_quanmx, 1));
        }
        baseEntiyList.add(new ShareentriesBaseEntiy("泡圈聊天", R.drawable.icon_paoquan, 2));
        baseEntiyList.add(new ShareentriesBaseEntiy("泡圈朋友圈", R.drawable.icon_zhixiang, 3));
        baseEntiyList.add(new ShareentriesBaseEntiy("微信朋友圈", R.drawable.icon_weix, 4));
        baseEntiyList.add(new ShareentriesBaseEntiy("微信好友", R.drawable.icon_weixhy, 5));
        baseEntiyList.add(new ShareentriesBaseEntiy("手机QQ", R.drawable.icon_qqshouji, 6));
        baseEntiyList.add(new ShareentriesBaseEntiy("QQ空间", R.drawable.icon_qqkongj, 7));
        baseEntiyList.add(new ShareentriesBaseEntiy("新浪微博", R.drawable.icon_xinlangweibo, 8));
        baseEntiyList.add(new ShareentriesBaseEntiy("支付宝", R.drawable.icon_zhifubao, 9));
        entiyList.add(new SystemfunctionItemEntiy("系统分享", R.drawable.icon_xitongfgenx, 1));
        entiyList.add(new SystemfunctionItemEntiy("短信", R.drawable.icon_duanxinfenxiang, 2));
        entiyList.add(new SystemfunctionItemEntiy("邮件", R.drawable.icon_youjiang, 3));
        entiyList.add(new SystemfunctionItemEntiy("复制链接", R.drawable.icon_fuzhilianj, 4));
        if (!isMe && reviewId > 0) {
            entiyList.add(new SystemfunctionItemEntiy("举报", R.drawable.icon_jubao, 5));
        }
        final Dialog dialog = new Dialog(activity, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.public_sharing_popups_layout, null);
        TextView text_quxiao = (TextView) dialogView.findViewById(R.id.text_quxiao);
        text_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView recycler_view_share = (RecyclerView) dialogView.findViewById(R.id.recycler_view_share);
        RecyclerView recycler_share = (RecyclerView) dialogView.findViewById(R.id.recycler_share);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滚动的RecycleView
        recycler_view_share.setLayoutManager(linearLayoutManager);
        final Share_DetailsAdpter shareDetailsAdpter = new Share_DetailsAdpter(activity, baseEntiyList);
        recycler_view_share.setAdapter(shareDetailsAdpter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滚动的RecycleView
        recycler_share.setLayoutManager(linearLayoutManager1);
        Systmfun_DetailsAdpter detailsAdpter = new Systmfun_DetailsAdpter(activity, entiyList);
        recycler_share.setAdapter(detailsAdpter);
        shareDetailsAdpter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                switch (baseEntiyList.get(i).getType()) {
                    case 1:
                        //转发全名炫点击
                        if (!BaseApplication.getInstance().isLogin()) {
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                        } else {
                            if (shareInSideApp != null) {
                                shareInSideApp.onForwardDazzle(reviewId);
                            }
                        }
                        dialog.dismiss();
                        break;
                    case 2:
                        //泡圈聊天
                        shareToChatTeam(activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 3:
                        //泡圈朋友圈
                        shareToChatFriendCircle(activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 4:
                        sharePlatform(WechatMoments.NAME, activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 5:
                        sharePlatform(Wechat.NAME, activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 6:
                        sharePlatform(QQ.NAME, activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 7:
                        sharePlatform(QZone.NAME, activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 8:
                        sharePlatform(SinaWeibo.NAME, activity, title, message, targetUrl, imageUrl);
                        dialog.dismiss();
                        break;
                    case 9:
                        IAPApi api = APAPIFactory.createZFBApi(activity, "2016122904705497", false);
                        APWebPageObject webPageObject = new APWebPageObject();
                        webPageObject.webpageUrl = targetUrl;
                        ////初始化APMediaMessage ，组装分享消息对象
                        APMediaMessage webMessage = new APMediaMessage();
                        webMessage.mediaObject = webPageObject;
                        webMessage.title = title;
                        webMessage.description = message;
                        webMessage.thumbUrl = imageUrl;
                        SendMessageToZFB.Req webReq = new SendMessageToZFB.Req();
                        webReq.message = webMessage;
                        webReq.transaction = "WebShare" + String.valueOf(System.currentTimeMillis());
                        api.sendReq(webReq);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });
        detailsAdpter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                switch (entiyList.get(i).getType()) {
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送到属性
                        intent.setType("text/plain"); // 分享发送到数据类型
                        intent.putExtra(Intent.EXTRA_SUBJECT, "subject"); // 分享的主题
                        intent.putExtra(Intent.EXTRA_TEXT, targetUrl); // 分享的内容
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
                        activity.startActivity(Intent.createChooser(intent, "分享")); // //目标应用选择对话框的
                        dialog.dismiss();
                        break;
                    case 2:
                        String smsBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + targetUrl;
                        Uri smsToUri = Uri.parse("smsto:");
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
                        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
                        //短信内容
                        sendIntent.putExtra("sms_body", smsBody);
                        activity.startActivityForResult(sendIntent, 1002);
                        dialog.dismiss();
                        break;
                    case 3:
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("plain/text");
                        String emailBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + targetUrl;
                        //邮件主题
                        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        //邮件内容
                        email.putExtra(Intent.EXTRA_TEXT, emailBody);
                        activity.startActivityForResult(Intent.createChooser(email, "请选择邮件发送内容"), 1001);
                        dialog.dismiss();
                        break;
                    case 4:
                        ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(targetUrl);
                        dialog.dismiss();
                        break;
                    case 5:
                        dialog.dismiss();
                        final ReportDialog reportDialog = new ReportDialog(activity, view);
                        reportDialog.setOnClick(new ReportDialog.OnItemClick() {
                            @Override
                            public void quxiaoClick() {
                                reportDialog.dismiss();
                            }

                            @Override
                            public void quedingClick(StringBuffer reason) {
                                getReport(reason, String.valueOf(reviewId), String.valueOf(reviewType));
                                reportDialog.dismiss();
                            }
                        });


                        break;
                    default:
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Deprecated
    public static void sharePlatform(String platform, Context context, String title, String message, String targetUrl, String imageUrl) {
        sharePlatform(platform, context, title, message, targetUrl, imageUrl, false,null);
    }

    public static void sharePlatform(BaseShareArgs args,String platform) {
        sharePlatform(platform, args.activity, args.title, args.message, args.targetUrl, args.imageUrl, args.isLocalImg,args.callback);
    }

    public static void sharePlatform(String platform, Context context, String title, String message, String targetUrl, String imageUrl, boolean isLocalImg, final ShareCallback callback) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(targetUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(message);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(iconUrl);//确保SDcard下面存在此张图片
        if (isLocalImg) {
            oks.setImagePath(imageUrl);
        } else {
            oks.setImageUrl(imageUrl);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(targetUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        if (null != platform) {
            oks.setPlatform(platform);
        }

        //区分设置微博微信和朋友圈中的内容加上链接
        oks.setShareContentCustomizeCallback(new ShareContentCustomize(message + targetUrl));
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(targetUrl);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(callback != null){
                    callback.onComplete(platform.getName());
                }
                ToastUtils.showToast(platform.getName() + "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showToast(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        // 启动分享GUI
        oks.show(context);
    }

    @Deprecated
    public static void shareToChatTeam(Context context, String title, String message, String targetUrl, String imageUrl,String code,String param) {
        shareToChatTeam(context,title,message,targetUrl,imageUrl);
    }

    @Deprecated
    public static void shareToChatFriendCircle(Context context, String title, String message, String targetUrl, String imageUrl,String code,String param) {
        shareToChatFriendCircle(context,title,message,targetUrl,imageUrl);
    }

    /**
     * 分享到泡圈聊天群组
     *
     * @param context
     * @param title
     * @param message
     * @param targetUrl
     * @param imageUrl
     */
    public static void shareToChatTeam(Context context, String title, String message, String targetUrl, String imageUrl) {
        if (!(context instanceof Activity)) {
            return;
        }
        ShareToCircleEntity entitiy = new ShareToCircleEntity();
        entitiy.setGameTitle(title);
        entitiy.setGameContent(message);
        entitiy.setGameIcon(imageUrl);
        entitiy.setGameUrl(targetUrl);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendsActivity");
            intent.putExtra("share2friend", entitiy);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param context
     * @param title
     * @param message
     * @param targetUrl
     * @param imageUrl
     * @param code
     * @param param
     * @param isImg 分享群二维码到泡圈好友或群组
     */
    public static void shareToChatTeam(Context context, String title, String message, String targetUrl, String imageUrl, String code, String param,boolean isImg) {
        if (!(context instanceof Activity)) {
            return;
        }
        ShareToCircleEntity entitiy = new ShareToCircleEntity();
        entitiy.setGameTitle(title);
        entitiy.setGameContent(message);
        entitiy.setGameIcon(imageUrl);
        entitiy.setGameUrl(targetUrl);
        entitiy.setCode(code);
        entitiy.setParams(param);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendsActivity");
            intent.putExtra("share2friend", entitiy);
            intent.putExtra("share_to_friend_isimg",isImg);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享到泡圈朋友圈
     *
     * @param context
     * @param title
     * @param message
     * @param targetUrl
     * @param imageUrl
     */
    public static void shareToChatFriendCircle(Context context, String title, String message, String targetUrl, String imageUrl) {
        if (!(context instanceof Activity)) {
            return;
        }
        ShareToCircleEntity entity = new ShareToCircleEntity();
        entity.setGameTitle(title);
        entity.setGameContent(message);
        entity.setGameIcon(imageUrl);
        entity.setGameUrl(targetUrl);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendCircleActivity");
            intent.putExtra("share2circle", entity);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param targetUrl
     * @param imageUrl
     * @param code
     * @param param
     * @param isImag 分享的图片
     */
    public static void shareToChatFriendCircle(Context context, String title, String message, String targetUrl, String imageUrl, String code, String param, boolean isImag) {
        if (!(context instanceof Activity)) {
            return;
        }
        ShareToCircleEntity entitiy = new ShareToCircleEntity();
        entitiy.setGameTitle(title);
        entitiy.setGameContent(message);
        entitiy.setGameIcon(imageUrl);
        entitiy.setGameUrl(targetUrl);
        entitiy.setCode(code);
        entitiy.setParams(param);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendCircleActivity");
            intent.putExtra("share2circle", entitiy);
            intent.putExtra("share_to_friend_circle_isimg",isImag);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置不同分享内容
     */
    private static class ShareContentCustomize implements ShareContentCustomizeCallback {
        String text;

        public ShareContentCustomize(String text) {
            this.text = text;
        }

        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
            if (SinaWeibo.NAME.equals(platform.getName())) {
                paramsToShare.setText(text);
            }
        }
    }


    /**
     * 举报接口
     *
     * @param reason
     * @param reviewId
     * @param reviewType 类型说明见coommonShare
     */
    public static void getReport(StringBuffer reason, String reviewId, String reviewType) {
        new ApiWrapper().getReport(reason, reviewId, reviewType)
                .compose(RxUtils.<HttpResultEntity<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())

                .subscribe(new SubscriberNetCallBack<String>() {
                    @Override
                    protected void onLogin(String msg) {
                    }

                    @Override
                    protected void onSuccess(String msg) {
                        ToastUtils.showToast("举报成功");
                    }

                    @Override
                    protected void onFinished() {

                    }

                    @Override
                    protected void onFailure(Throwable throwable, String msg) {
                        ToastUtils.showToast("举报失败");
                    }
                });
    }


    public interface IShareInSideApp {
        void onForwardDazzle(int dazzleId);
    }

}
