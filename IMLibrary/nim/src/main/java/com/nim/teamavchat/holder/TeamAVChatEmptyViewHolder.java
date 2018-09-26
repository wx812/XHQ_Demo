package com.nim.teamavchat.holder;

import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.nim.teamavchat.module.TeamAVChatItem;

/**
 * Created by huangjun on 2017/5/9.
 */

public class TeamAVChatEmptyViewHolder extends TeamAVChatItemViewHolderBase {

    public TeamAVChatEmptyViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected void inflate(BaseViewHolder holder) {

    }

    @Override
    protected void refresh(TeamAVChatItem data) {

    }


}
