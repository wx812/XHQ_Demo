package com.nim.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nim.session.entity.FlowerGiftMsgEntity;

import java.io.Serializable;

/**
 * Created by ychen on 2017/9/19.
 */

public class FlowerGiftAttachment extends CustomAttachment implements Serializable {

    public FlowerGiftMsgEntity mFlowerGiftMsgEntity;


    public FlowerGiftAttachment() {
        super(CustomAttachmentType.FlowerGift);
    }

    @Override
    protected void parseData(JSONObject data) {
        mFlowerGiftMsgEntity = data.toJavaObject(FlowerGiftMsgEntity.class);
    }

    @Override
    protected JSONObject packData() {
        return (JSONObject) JSON.toJSON(mFlowerGiftMsgEntity);
    }
}
