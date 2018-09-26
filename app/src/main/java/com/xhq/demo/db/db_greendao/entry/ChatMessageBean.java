package com.xhq.demo.db.db_greendao.entry;

/**
 * Created by xyy on 2016/10/15.
 */

import com.xhq.demo.db.db_greendao.ChatConst;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ChatMessageBean{
    @Id
    private String mid;
    private String uid;
    private String gid;
    private String msg;
    private String time;
    private int type;
    private int UserVoiceTime;
    private String UserVoicePath;
    private String UserVoiceUrl;
    private @ChatConst.SendState int sendState;
    private String imageUrl;
    private String imageLocal;
    private String fileUrl;
    private long fileSize;
    private String fileName;
    private String fileStatus;
    private String UserId;

    @Generated(hash = 1557449535)
    public ChatMessageBean() {
    }

    @Generated(hash = 1710039774)
    public ChatMessageBean(String mid, String uid, String gid, String msg,
                           String time, int type, int UserVoiceTime, String UserVoicePath,
                           String UserVoiceUrl, int sendState, String imageUrl, String imageLocal,
                           String fileUrl, long fileSize, String fileName, String fileStatus,
                           String UserId) {
        this.mid = mid;
        this.uid = uid;
        this.gid = gid;
        this.msg = msg;
        this.time = time;
        this.type = type;
        this.UserVoiceTime = UserVoiceTime;
        this.UserVoicePath = UserVoicePath;
        this.UserVoiceUrl = UserVoiceUrl;
        this.sendState = sendState;
        this.imageUrl = imageUrl;
        this.imageLocal = imageLocal;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileStatus = fileStatus;
        this.UserId = UserId;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }


    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String UserContent) {
        this.msg = UserContent;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getUserVoicePath() {
        return this.UserVoicePath;
    }

    public void setUserVoicePath(String UserVoicePath) {
        this.UserVoicePath = UserVoicePath;
    }

    public String getUserVoiceUrl() {
        return this.UserVoiceUrl;
    }

    public void setUserVoiceUrl(String UserVoiceUrl) {
        this.UserVoiceUrl = UserVoiceUrl;
    }

    public int getSendState() {
        return this.sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLocal() {
        return this.imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return this.gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getUserVoiceTime() {
        return this.UserVoiceTime;
    }

    public void setUserVoiceTime(int UserVoiceTime) {
        this.UserVoiceTime = UserVoiceTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
