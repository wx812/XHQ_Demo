package com.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Administrator
 * 2017/7/18
 * 9:12
 */
public class ShareAttachment extends CustomAttachment implements Serializable {
    private String gameIcon;
    private String title;
    private String content;
    private String gameUrl;
    private String code;
    private String params;

    public ShareAttachment() {
        super(CustomAttachmentType.Share);
    }

    public ShareAttachment(String str) {
        this();
    }

    @Override
    protected void parseData(JSONObject data) {
        gameIcon = data.getString("gameIcon");
        title = data.getString("title");
        content = data.getString("content");
        gameUrl = data.getString("gameUrl");
        code = data.getString("code");
        params = data.getString("params");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("gameIcon", gameIcon);
        data.put("title", title);
        data.put("content", content);
        data.put("gameUrl", gameUrl);
        data.put("code", code);
        data.put("params", params);
        return data;
    }

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
