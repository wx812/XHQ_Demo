package com.nim.redpacket.helper;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.nim.redpacket.GrabRpCallBack;
import com.nim.redpacket.api.CanReceRedPacketHelper;
import com.nim.redpacket.bean.EnvelopeRpBean;
import com.nim.redpacket.bean.ReceRedPacketEntity;
import com.nim.redpacket.constant.RedPacketStatus;
import com.nim.redpacket.ui.ReceiveOrSeeRedPacketActivity;
import com.nim.redpacket.ui.RedPacketExpireActivity;
import com.nim.redpacket.ui.RedPacketFinishedActivity;
import com.nim.session.RedPacketRouter;

/**
 * Administrator
 * 2018/1/24
 * 14:06
 */
public class RedPacketClient {
    private static final String TAG = RedPacketClient.class.getName();

    public static void openSingleRp(Activity activity, String redPacketId, GrabRpCallBack callback) {
        openRedPacket(activity, redPacketId, callback);
    }

    public static void openGroupRp(Activity activity, String redPacketId, GrabRpCallBack callback) {
        openRedPacket(activity, redPacketId, callback);
    }

    //查看红包
    private static void openRedPacket(final Activity activity, final String redPacketId, final GrabRpCallBack callback) {
        CanReceRedPacketHelper.canReceRedPacket(activity, redPacketId, TAG, new CanReceRedPacketHelper.SubscriberNetCallBack<ReceRedPacketEntity>() {
            @Override
            public void onStart() {
                DialogMaker.showProgressDialog(activity, null, false);
            }

            @Override
            protected void onSuccess(ReceRedPacketEntity data) {
                DialogMaker.dismissProgressDialog();
                ReceRedPacketEntity.RedPacketBean redPacketBean = data.getRedPacket();
                //群红包(9:自己已领,8:已经被领完了,7:红包已过期,6:只有指定的人才可以领取,1:可以领)
                //个人红包:(15:自己查看自己发的,14:领取人已领取,13:已被领完了,12:红包已超过24小时了,11:只有指定的人才可以领取,10:可以领这个红包)
                int state = data.getState();
                if (state == 1 || state == 10) {
                    ReceiveOrSeeRedPacketActivity.start(activity, redPacketBean, callback);
                } else if (state == 7 || state == 12) {
                    RedPacketExpireActivity.start(activity, redPacketBean);
                    if (callback != null) {
                        callback.updateRpResult(RedPacketStatus.RP_EXPIRE);
                    }
                } else if (state == 8 || state == 13) {
                    RedPacketFinishedActivity.start(activity, redPacketBean);
                    if (callback != null) {
                        callback.updateRpResult(RedPacketStatus.RP_GET_FINISHED);
                    }
                } else if (state == 9) {
                    RedPacketRouter.startRedPacketDetailActivity(activity, redPacketId, "2");
                } else if (state == 14) {//领取之后的跳转
                    RedPacketRouter.startP2PRedPacketDetailActivity(activity, redPacketId, "2");
                } else if (state == 15) {//查看
                    RedPacketRouter.startP2PRedPacketDetailActivity(activity, redPacketId, "1");
                    ReceRedPacketEntity.RedPacketBean redPacket = data.getRedPacket();
                    if (null != redPacket) {
                        int packetStatus = redPacket.getPacketStatus();
                        if (packetStatus == 1) {
                            if (callback != null) {
                                callback.updateRpResult(RedPacketStatus.RP_GET_FINISHED);
                            }
                        } else if (packetStatus == 2) {
                            if (callback != null) {
                                callback.updateRpResult(RedPacketStatus.RP_EXPIRE);
                            }
                        }
                    }
                }
            }

            @Override
            protected void onFailure(Throwable throwable, String s, int httpResultEntity) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static EnvelopeRpBean getEnvelopeRpInfo(Intent intent) {
        if (intent == null) {
            return null;
        } else {
            EnvelopeRpBean bean = new EnvelopeRpBean();
            bean.setEnvelopesID(intent.getStringExtra("envelopesID"));
            bean.setEnvelopeMessage(intent.getStringExtra("envelopeMessage"));
            bean.setEnvelopeName(intent.getStringExtra("envelopeName"));
            return bean;
        }
    }

}
