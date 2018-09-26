package com.xhq.commonbase.share.platform;

import android.content.Intent;
import android.view.View;

import com.common.R;
import com.common.bean.ShareToCircleEntity;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;
import com.common.utils.share.base.ShareCallback;

/**
 * Created by ychen on 2017/12/8.
 */

public class ChatTeam extends BaseSharePlatform<BaseShareArgs> {

    @Override
    public String getName() {
        return "泡圈聊天";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_paoquan;
    }

    @Override
    public void dealClick(BaseShareArgs args,View view) {
        shareToChatTeam(args);
    }

    public static void shareToChatTeam(BaseShareArgs args) {
        ShareToCircleEntity entitiy = new ShareToCircleEntity();
        entitiy.setGameTitle(args.title);
        entitiy.setGameContent(args.message);
        entitiy.setGameIcon(args.imageUrl);
        entitiy.setGameUrl(args.targetUrl);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendsActivity");
            intent.putExtra("share2friend", entitiy);
            args.activity.startActivityForResult(intent, ShareCallback.REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
