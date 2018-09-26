package com.netease.nim.uikit.face.faceapi;

/**
 * Administrator
 * 2017/7/11
 * 10:57
 */
public class UpdateFriendParams {
    private String userCode;
    private String friendUserCode;
    private int flag;

    public UpdateFriendParams(String userCode, String friendUserCode, int flag) {
        this.userCode = userCode;
        this.friendUserCode = friendUserCode;
        this.flag = flag;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFriendUserCode() {
        return friendUserCode;
    }

    public void setFriendUserCode(String friendUserCode) {
        this.friendUserCode = friendUserCode;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
