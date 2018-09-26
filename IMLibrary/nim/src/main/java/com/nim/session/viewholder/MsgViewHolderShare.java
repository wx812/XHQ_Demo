package com.nim.session.viewholder;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.FontConstant;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderText;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.nim.R;
import com.nim.session.FuncIntentHelper;
import com.nim.session.extension.ShareAttachment;

/**
 * Administrator
 * 2017/7/18
 * 9:16
 */
public class MsgViewHolderShare extends MsgViewHolderText {
    private ShareAttachment attachment;
    private LinearLayout layout;
    private ImageView image;
    private TextView tvTitle;
    private TextView tvContent;

    public MsgViewHolderShare(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_share;
    }

    @Override
    protected void inflateContentView() {
        layout = findViewById(R.id.message_item_query_layout);
        image = findViewById(R.id.message_item_query_img);
        tvTitle = findViewById(R.id.message_item_query_title);
        tvContent = findViewById(R.id.message_item_query_content);
        tvTitle.setTextSize(16.0f + FontConstant.TEXT_SIZE * 3);
        tvContent.setTextSize(12.0f + FontConstant.TEXT_SIZE * 3);
    }

    @Override
    protected void bindContentView() {
        attachment = (ShareAttachment) message.getAttachment();
        doLoadImage(attachment.getGameIcon(), image);
        tvTitle.setText(attachment.getTitle());
        tvContent.setText(attachment.getContent());
    }

    @Override
    protected void onItemClick() {
        String uri = attachment.getGameUrl();
        if (!FuncIntentHelper.parseUrl(context, uri)) {
            Intent intent = FuncIntentHelper.getIntent(attachment.getGameUrl());
            context.startActivity(intent);
        }
    }

    private void doLoadImage(String url, ImageView targetImageView) {
        Glide.with(context).load(url).centerCrop()
                .placeholder(R.drawable.sdk_share_dialog_thumb_default)
                .error(R.drawable.sdk_share_dialog_thumb_default).dontAnimate().into(targetImageView);
    }

    @Override
    protected int rightBackground() {
        return com.netease.nim.uikit.R.drawable.nim_message_item_right_white_selector;
    }

    @Override
    protected int leftBackground() {
        return com.netease.nim.uikit.R.drawable.nim_message_item_left_selector;
    }

}
