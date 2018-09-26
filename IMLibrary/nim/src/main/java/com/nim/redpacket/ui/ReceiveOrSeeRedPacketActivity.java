package com.nim.redpacket.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.nim.DemoCache;
import com.nim.R;
import com.nim.redpacket.GrabRpCallBack;
import com.nim.redpacket.api.ReceiveRedPacketHelper;
import com.nim.redpacket.bean.ReceRedPacketEntity;
import com.nim.redpacket.constant.RedPacketStatus;
import com.nim.redpacket.constant.RedPacketType;
import com.nim.session.RedPacketRouter;

import common.commonsdk.http.SubscribeManager;

/**
 * description: 红包领取弹窗界面
 * date:2018.01.12
 * version: 1.1.9
 */
public class ReceiveOrSeeRedPacketActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ReceiveOrSeeRedPacketActivity.class.getName();
    private static GrabRpCallBack callBack;
    private static final String RED_PACKET = "redPacket";
    private ReceRedPacketEntity.RedPacketBean redPacketBean;
    private ImageView iv_close, iv_head;
    private TextView tv_name;
    private ImageView iv_icon;
    private TextView tv_examine;
    private TextView packet_content;
    private ImageView open_packet;
    private String redPacketId;

    public static void start(Activity activity, ReceRedPacketEntity.RedPacketBean data, GrabRpCallBack callback) {
        callBack = callback;
        Intent intent = new Intent(activity, ReceiveOrSeeRedPacketActivity.class);
        intent.putExtra(RED_PACKET, data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.activity_receive_or_see_packet);
        redPacketBean = (ReceRedPacketEntity.RedPacketBean) getIntent().getSerializableExtra(RED_PACKET);
        initView();
        initData();
    }

    private void initView() {
        iv_close = (ImageView) findViewById(R.id.iv_red_packet_close);
        iv_head = (ImageView) findViewById(R.id.iv_red_packet_user);
        tv_name = (TextView) findViewById(R.id.tv_red_packet_send_name);
        packet_content = (TextView) findViewById(R.id.tv_red_packet_content);
        open_packet = (ImageView) findViewById(R.id.iv_open_red_packet);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_examine = (TextView) findViewById(R.id.tv_examine);
        iv_close.setOnClickListener(this);
        open_packet.setOnClickListener(this);
        tv_examine.setOnClickListener(this);
    }

    private void initData() {
        if (null != redPacketBean) {
            redPacketId = redPacketBean.getId();
            loadRoundView(this, redPacketBean.getUserPic(), iv_head);
            tv_name.setText(redPacketBean.getUserName());
            packet_content.setText(redPacketBean.getPacketContent());
            Drawable dwRight;
            String userType = redPacketBean.getUserType();//红包发送类型 0 个人/1 群
            if (!userType.equals("0")) {
                String rpType = redPacketBean.getPacketType();
                if (rpType.equals(RedPacketType.NORMAL_RED_PACKET)) {
                    dwRight = getResources().getDrawable(R.mipmap.putonghongbao_icon);
                    tv_examine.setText("查看领取详情");
                    if (!redPacketBean.getUserCode().equals(DemoCache.getAccount())) {
                        iv_icon.setVisibility(View.VISIBLE);
                        tv_examine.setVisibility(View.GONE);
                    } else {
                        iv_icon.setVisibility(View.GONE);
                        tv_examine.setVisibility(View.VISIBLE);
                    }
                } else {
                    tv_examine.setText("查看他人手气");
                    dwRight = getResources().getDrawable(R.mipmap.icon_fighting_red);
                    if (!redPacketBean.getUserCode().equals(DemoCache.getAccount())) {
                        iv_icon.setVisibility(View.VISIBLE);
                        tv_examine.setVisibility(View.GONE);
                    } else {
                        iv_icon.setVisibility(View.GONE);
                        tv_examine.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                dwRight = getResources().getDrawable(R.mipmap.putonghongbao_icon);
                iv_icon.setVisibility(View.VISIBLE);
                tv_examine.setVisibility(View.GONE);
            }
            dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
            tv_name.setCompoundDrawables(null, null, dwRight, null);
        }
    }

    private void loadRoundView(final Context context, String uri, final ImageView id) {
        Glide.with(context).load(uri).asBitmap().placeholder(R.drawable.circle_failure_image).error(R.drawable.circle_failure_image).centerCrop().into(new BitmapImageViewTarget(id) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                id.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_red_packet_close) {
            finish();
        } else if (v.getId() == R.id.iv_open_red_packet) {
            receiveRedPacket(redPacketId);
        } else if (v.getId() == R.id.tv_examine) {
            RedPacketRouter.startRedPacketDetailActivity(this, redPacketId, "1");
            finish();
        }
    }

    //领取红包
    private void receiveRedPacket(final String redPacketId) {
        ReceiveRedPacketHelper.receiveRedPacket(this, redPacketId,
                TAG, new ReceiveRedPacketHelper.SubscriberNetCallBack<ReceRedPacketEntity>() {

                    @Override
                    public void onStart() {
                        DialogMaker.showProgressDialog(ReceiveOrSeeRedPacketActivity.this, null, false);
                    }

                    @Override
                    protected void onSuccess(ReceRedPacketEntity entity) {
                        DialogMaker.dismissProgressDialog();
                        int state = entity.getState();//1:领取成功,2:已经领取过该红包,7:只有指定的人可以领取,8:已领完,6:已过期
                        if (state == 1 || state == 2) {
                            String userType = entity.getRedPacket().getUserType();
                            if ("0".equals(userType)) {
                                //0是个人红包
                                RedPacketRouter.startP2PRedPacketDetailActivity(ReceiveOrSeeRedPacketActivity.this, redPacketId, "2");
                            } else {
                                //1是群红包
                                RedPacketRouter.startRedPacketDetailActivity(ReceiveOrSeeRedPacketActivity.this, redPacketId, "2");
                            }
                            ReceRedPacketEntity.RedPacketBean redPacket = entity.getRedPacket();
                            if (null != redPacket) {
                                int rpStatus = redPacket.getPacketStatus();//红包状态:0进行中/1已正常结束/2:过期
                                if (rpStatus == 0) {
                                    if (callBack != null) {
                                        callBack.updateRpResult(RedPacketStatus.RP_OPEN);
                                    }
                                } else if (rpStatus == 1) {
                                    if (callBack != null) {
                                        callBack.updateRpResult(RedPacketStatus.RP_GET_FINISHED);
                                    }
                                } else {
                                    if (callBack != null) {
                                        callBack.updateRpResult(RedPacketStatus.RP_EXPIRE);
                                    }
                                }
                                if (callBack != null) {
                                    callBack.grabRpResult(rpStatus == 1);
                                }
                            }
                        } else if (state == 8) {
                            RedPacketFinishedActivity.start(ReceiveOrSeeRedPacketActivity.this, redPacketBean);
                            if (callBack != null) {
                                callBack.updateRpResult(RedPacketStatus.RP_GET_FINISHED);
                            }
                        } else if (state == 6) {
                            RedPacketExpireActivity.start(ReceiveOrSeeRedPacketActivity.this, redPacketBean);
                            if (callBack != null) {
                                callBack.updateRpResult(RedPacketStatus.RP_EXPIRE);
                            }
                        }
                        finish();
                    }

                    @Override
                    protected void onFailure(Throwable throwable, String s, int httpResultEntity) {
                        DialogMaker.dismissProgressDialog();
                        Toast.makeText(ReceiveOrSeeRedPacketActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SubscribeManager.getInstance().cance(TAG);
    }

}
