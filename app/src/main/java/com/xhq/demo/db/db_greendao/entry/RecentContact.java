package com.xhq.demo.db.db_greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xyy on 2017/2/14
 * 最近联系人
 */
@Entity
public class RecentContact{

    @Id
    private String mid;
    private String uid;
    private String gid;
    private String hid;                 //头像地址
    private String st;                  //最新消息发送时间
    private String name;                // 发送人名称

    private int count;                  //消息数目
    private int type;                   //消息类型
    private String msg;                 //消息内容
    private String UserId;              // 关联账号
    private int len;                    //语音相关
    private String ids;                 //图片相关
    private String fn;                      //文件相关

    private String generalField;                //给1101,1102,1103,1104一个通用字段


    @Generated(hash = 968618712)
    public RecentContact(String mid, String uid, String gid, String hid, String st,
                         String name, int count, int type, String msg, String UserId, int len,
                         String ids, String fn, String generalField) {
        this.mid = mid;
        this.uid = uid;
        this.gid = gid;
        this.hid = hid;
        this.st = st;
        this.name = name;
        this.count = count;
        this.type = type;
        this.msg = msg;
        this.UserId = UserId;
        this.len = len;
        this.ids = ids;
        this.fn = fn;
        this.generalField = generalField;
    }
    @Generated(hash = 696528904)
    public RecentContact() {
    }


    public String getMid() {
        return this.mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
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
    public String getHid() {
        return this.hid;
    }
    public void setHid(String hid) {
        this.hid = hid;
    }
    public String getSt() {
        return this.st;
    }
    public void setSt(String st) {
        this.st = st;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getLen() {
        return this.len;
    }
    public void setLen(int len) {
        this.len = len;
    }
    public String getIds() {
        return this.ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public String getFn() {
        return this.fn;
    }
    public void setFn(String fn) {
        this.fn = fn;
    }
    public String getGeneralField() {
        return this.generalField;
    }
    public void setGeneralField(String generalField) {
        this.generalField = generalField;
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }


}
