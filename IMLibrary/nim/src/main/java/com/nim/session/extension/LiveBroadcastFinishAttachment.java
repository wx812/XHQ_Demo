package com.nim.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nim.session.entity.LiveBroadcastFinishEntity;

import java.io.Serializable;

/**
 * Created by ychen on 2018/1/15.
 */

public class LiveBroadcastFinishAttachment extends CustomAttachment implements Serializable {

    public LiveBroadcastFinishEntity mEntity;


    public LiveBroadcastFinishAttachment() {
        super(CustomAttachmentType.LiveBroadcastFinish);
    }

    @Override
    protected void parseData(JSONObject data) {
        mEntity = data.toJavaObject(LiveBroadcastFinishEntity.class);
    }

    @Override
    protected JSONObject packData() {
        return (JSONObject) JSON.toJSON(mEntity);
    }
}
