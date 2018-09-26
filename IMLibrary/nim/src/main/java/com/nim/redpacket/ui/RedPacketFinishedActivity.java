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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nim.R;
import com.nim.redpacket.bean.ReceRedPacketEntity;
import com.nim.redpacket.constant.RedPacketType;
import com.nim.session.RedPacketRouter;

/**
 * date: 创建时间 2018/1/16
 * autour: fengliang
 * description: 红包已领完
 * version: 1.1.9
 */
public class RedPacketFinishedActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RedPacketFinishedActivity.class.getName();
    private static final String RED_PACKET = "redPacket";
    private ReceRedPacketEntity.RedPacketBean redPacketBean;
    private ImageView iv_close, iv_head;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_examine;
    private String redPacketId;

    public static void start(Activity activity, ReceRedPacketEntity.RedPacketBean data) {
        Intent intent = new Intent(activity, RedPacketFinishedActivity.class);
        intent.putExtra(RED_PACKET, data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.activity_red_packet_finished);
        redPacketBean = (ReceRedPacketEntity.RedPacketBean) getIntent().getSerializableExtra(RED_PACKET);
        initView();
        initData();
    }

    private void initView() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_close = (ImageView) findViewById(R.id.iv_red_packet_close);
        iv_head = (ImageView) findViewById(R.id.iv_red_packet_user);
        tv_name = (TextView) findViewById(R.id.tv_red_packet_send_name);
        tv_examine = (TextView) findViewById(R.id.tv_examine);
        iv_close.setOnClickListener(this);
        tv_examine.setOnClickListener(this);
    }

    private void initData() {
        if (null != redPacketBean) {
            redPacketId = redPacketBean.getId();
            loadRoundView(this, redPacketBean.getUserPic(), iv_head);
            tv_name.setText(redPacketBean.getUserName());
            Drawable dwRight;
            String rpType = redPacketBean.getPacketType();
            if (rpType.equals(RedPacketType.NORMAL_RED_PACKET)) {
                dwRight = getResources().getDrawable(R.mipmap.putonghongbao_icon);
                iv_icon.setVisibility(View.VISIBLE);
                tv_examine.setVisibility(View.GONE);
            } else {
                dwRight = getResources().getDrawable(R.mipmap.icon_fighting_red);
                iv_icon.setVisibility(View.GONE);
                tv_examine.setVisibility(View.VISIBLE);
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
        } else if (v.getId() == R.id.tv_examine) {
            RedPacketRouter.startRedPacketDetailActivity(this, redPacketId, "1");
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
