package com.xhq.commonbase.share.platform;

import android.content.Intent;
import android.view.View;

import com.common.R;
import com.common.bean.ShareToCircleEntity;
import com.common.utils.share.base.BaseShareArgs;
import com.common.utils.share.base.BaseSharePlatform;
import com.common.utils.share.base.ShareCallback;

/**
 * 泡圈朋友圈
 * Created by ychen on 2017/12/11.
 */

public class ChatFriendCircle extends BaseSharePlatform<BaseShareArgs>{

    @Override
    public String getName() {
        return "泡圈朋友圈";
    }

    @Override
    public int getPicResId() {
        return R.drawable.icon_zhixiang;
    }

    @Override
    public void dealClick(BaseShareArgs args,View view) {
        shareToChatFriendCircle(args);
    }

    private void shareToChatFriendCircle(BaseShareArgs args) {
        ShareToCircleEntity entity = new ShareToCircleEntity();
        entity.setGameTitle(args.title);
        entity.setGameContent(args.message);
        entity.setGameIcon(args.imageUrl);
        entity.setGameUrl(args.targetUrl);
        try {
            Intent intent = new Intent("com.chat.activity.ShareToFriendCircleActivity");
            intent.putExtra("share2circle", entity);
            args.activity.startActivityForResult(intent, ShareCallback.REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
