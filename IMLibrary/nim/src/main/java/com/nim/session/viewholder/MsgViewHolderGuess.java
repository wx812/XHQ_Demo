package com.nim.session.viewholder;

import android.widget.ImageView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderText;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.nim.R;
import com.nim.session.extension.GuessAttachment;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderGuess extends MsgViewHolderText {
    private GuessAttachment attachment;
    private ImageView nim_message_item_iv_body;

    public MsgViewHolderGuess(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_guess;
    }

    @Override
    protected void inflateContentView() {
        nim_message_item_iv_body = findViewById(R.id.nim_message_item_iv_body);
    }

    @Override
    protected void bindContentView() {
        attachment = (GuessAttachment) message.getAttachment();
        nim_message_item_iv_body.setImageResource(attachment.getValue().getResId());
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected void onItemClick() {
        //ToDo
    }
}
