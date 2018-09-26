package com.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Administrator
 * 2018/1/5
 * 11:28
 */
public class RedPacketAttachment extends CustomAttachment {
    private String redPacketId;//  红包id
    private String content;//  消息文本内容
    private String title;// 红包名称

    private static final String KEY_RP_ID = "redPacketId";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TITLE = "title";

    public RedPacketAttachment() {
        super(CustomAttachmentType.RedPacket);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
        redPacketId = data.getString(KEY_RP_ID);
        title = data.getString(KEY_TITLE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_RP_ID, redPacketId);
        data.put(KEY_CONTENT, content);
        data.put(KEY_TITLE, title);
        return data;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
