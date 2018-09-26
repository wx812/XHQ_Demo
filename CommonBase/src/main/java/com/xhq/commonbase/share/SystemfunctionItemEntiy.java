package com.xhq.commonbase.share;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SystemfunctionItemEntiy implements Serializable{
    public String name;
    public int picResId;
    public int type;
    public boolean hasMessage;
    private String headlines;
    public SystemfunctionItemEntiy(String name, int picResId, int type) {
        this.name = name;
        this.picResId = picResId;
        this.type = type;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public int getType() {

        return type;
    }

    public int getPicResId() {

        return picResId;
    }

    public String getName() {

        return name;
    }

    public void setHeadlines(String headlines) {
        this.headlines = headlines;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    @Override
    public String toString() {
        return "ShareentriesEntiy{" +
                "name='" + name + '\'' +
                ", picResId=" + picResId +
                ", type=" + type +
                ", hasMessage=" + hasMessage +
                '}';
    }
}
