package com.xhq.commonbase.share;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ShareentriesBaseEntiy {
    public String name;
    public int picResId;
    public int type;
    public boolean hasMessage;
    public ShareentriesBaseEntiy(String name, int picResId, int type) {
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
