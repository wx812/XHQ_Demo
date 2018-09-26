package com.xhq.commonbase.share;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.common.R;
import com.common.utils.ToastUtils;
import com.common.utils.UiUtils;
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

/**
 * ${wudebin} on 2017/9/21.Created
 */
//约吧  玩家挣钱 分享
public class ShareFragmentDialog extends DialogFragment implements View.OnClickListener {
    final List<ShareentriesBaseEntiy> baseEntiyList = new ArrayList<>();
    final List<SystemfunctionItemEntiy> entiyList = new ArrayList<>();
    private String title, message, targetUrl, imageUrl;
    private IShareLsitener listener;

    public static ShareFragmentDialog newNoticeInstance(String title, String message, String targetUrl, String imageUrl) {
        ShareFragmentDialog fragment = new ShareFragmentDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("targetUrl", targetUrl);
        args.putString("iconUrl", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.public_sharing_popups_layout, container, false);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            message = getArguments().getString("message");
            targetUrl = getArguments().getString("targetUrl");
            imageUrl = getArguments().getString("iconUrl");
        }
        baseEntiyList.clear();
        entiyList.clear();
        update(view);
        return view;
    }

    private void update(View view) {
        baseEntiyList.add(new ShareentriesBaseEntiy("泡圈聊天", R.drawable.icon_paoquan, 1));
        baseEntiyList.add(new ShareentriesBaseEntiy("泡圈朋友圈", R.drawable.icon_zhixiang, 2));
        baseEntiyList.add(new ShareentriesBaseEntiy("微信朋友圈", R.drawable.icon_weix, 3));
        baseEntiyList.add(new ShareentriesBaseEntiy("微信好友", R.drawable.icon_weixhy, 4));
        baseEntiyList.add(new ShareentriesBaseEntiy("手机QQ", R.drawable.icon_qqshouji, 5));
        baseEntiyList.add(new ShareentriesBaseEntiy("QQ空间", R.drawable.icon_qqkongj, 6));
        baseEntiyList.add(new ShareentriesBaseEntiy("新浪微博", R.drawable.icon_xinlangweibo, 7));
        baseEntiyList.add(new ShareentriesBaseEntiy("支付宝", R.drawable.icon_zhifubao, 8));

        entiyList.add(new SystemfunctionItemEntiy("系统分享", R.drawable.icon_xitongfgenx, 1));
        entiyList.add(new SystemfunctionItemEntiy("短信", R.drawable.icon_duanxinfenxiang, 2));
        entiyList.add(new SystemfunctionItemEntiy("邮件", R.drawable.icon_youjiang, 3));
        entiyList.add(new SystemfunctionItemEntiy("复制链接", R.drawable.icon_fuzhilianj, 4));
        view.findViewById(R.id.text_quxiao).setOnClickListener(this);
        RecyclerView recycler_view_share = (RecyclerView) view.findViewById(R.id.recycler_view_share);
        RecyclerView recycler_share = (RecyclerView) view.findViewById(R.id.recycler_share);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滚动的RecycleView
        recycler_view_share.setLayoutManager(linearLayoutManager);
        final Share_DetailsAdpter shareDetailsAdpter = new Share_DetailsAdpter(getActivity(), baseEntiyList);
        recycler_view_share.setAdapter(shareDetailsAdpter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滚动的RecycleView
        recycler_share.setLayoutManager(linearLayoutManager1);
        Systmfun_DetailsAdpter detailsAdpter = new Systmfun_DetailsAdpter(getActivity(), entiyList);
        recycler_share.setAdapter(detailsAdpter);
        shareDetailsAdpter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                switch (baseEntiyList.get(i).getType()) {
                    case 1:
                        //泡圈聊天
                        ShareUtil.shareToChatTeam(getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 2:
                        //泡圈朋友圈
                        ShareUtil.shareToChatFriendCircle(getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 3:
                        sharePlatform(WechatMoments.NAME, getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 4:
                        sharePlatform(Wechat.NAME, getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 5:
                        sharePlatform(QQ.NAME, getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 6:
                        sharePlatform(QZone.NAME, getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 7:
                        sharePlatform(SinaWeibo.NAME, getActivity(), title, message, targetUrl, imageUrl);
                        dismiss();
                        break;
                    case 8:
                        IAPApi api = APAPIFactory.createZFBApi(getActivity(), "2016122904705497", false);
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
                        dismiss();
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
                        getActivity().startActivity(Intent.createChooser(intent, "分享")); // //目标应用选择对话框的
                        dismiss();
                        break;
                    case 2:
                        String smsBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + targetUrl;
                        Uri smsToUri = Uri.parse("smsto:");
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
                        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
                        //短信内容
                        sendIntent.putExtra("sms_body", smsBody);
                        getActivity().startActivityForResult(sendIntent, 1002);
                        dismiss();
                        break;
                    case 3:
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("plain/text");
                        String emailBody = "我正在浏览这个,觉得真不错,推荐给你哦~ 地址:" + targetUrl;
                        //邮件主题
                        email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        //邮件内容
                        email.putExtra(Intent.EXTRA_TEXT, emailBody);
                        getActivity().startActivityForResult(Intent.createChooser(email, "请选择邮件发送内容"), 1001);
                        dismiss();
                        break;
                    case 4:
                        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(targetUrl);
                        dismiss();
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
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_quxiao) {
            dismiss();
        }
    }

    private void sharePlatform(String platform, Context context, String title, String message, String targetUrl, String imageUrl) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(targetUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(message);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(iconUrl);//确保SDcard下面存在此张图片
        oks.setImageUrl(imageUrl);
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
        oks.setSite(UiUtils.getString(com.common.R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(targetUrl);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (listener != null) {
                    listener.shareSuccessMsg("分享成功");
                } else {
                    ToastUtils.showToastYB("分享成功");
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (listener != null) {
                    listener.shareErrorMsg("分享失败");
                } else {
                    ToastUtils.showToastYB("分享失败");
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (listener != null) {
                    listener.shareCancelMsg("分享已取消");
                } else {
                    ToastUtils.showToastYB("分享已取消");
                }
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 设置不同分享内容
     */
    private class ShareContentCustomize implements ShareContentCustomizeCallback {
        String text;

        private ShareContentCustomize(String text) {
            this.text = text;
        }

        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
            if (SinaWeibo.NAME.equals(platform.getName())) {
                paramsToShare.setText(text);
            }
        }
    }

    public void setListener(IShareLsitener listener) {
        this.listener = listener;
    }

    public interface IShareLsitener {
        void shareSuccessMsg(String msg);

        void shareErrorMsg(String msg);

        void shareCancelMsg(String msg);
    }
}
