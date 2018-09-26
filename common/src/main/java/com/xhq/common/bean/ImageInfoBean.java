package com.xhq.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by hskun on 2017/10/27.
 * 说明：数据库中图片信息的表，
 * 从网络拉取所有的图片，本地图片因为没有数据库做索引支撑，所以存了也不知道是哪张图，以后上传成功的图片必须有数据库做索引，当然清除缓存的图片还是必须删除
 * 这里存图片的字段：身份证ID，图片类型，图片本地路径，图片对应的网络ID（下载路径）（我们清除缓存的时候，如果清除了用户数据，那么应该退出APP）
 * 因为这里下载图片是根据文件id也就是字段attachId来获取的，所以必须建立数据库来存储图片的信息，
 */
@Entity
public class ImageInfoBean {
    /**
     * 跟服务器所对应的id字段
     * id       从服务器返回的字段
     * attachId 从服务器下载图片所需要的参数
     */
    @Id
    private String attachId;
    /**
     * 这个主键不能是自增的
     * 身份证ID
     */
    @NotNull
    private String uid;
    /**
     * 图片类型
     */
    @NotNull
    private String type;
    /**
     * 本地路径,包含名称
     */
    @NotNull
    private String FilePath;
    @Generated(hash = 1053600259)
    public ImageInfoBean(String attachId, @NotNull String uid, @NotNull String type,
            @NotNull String FilePath) {
        this.attachId = attachId;
        this.uid = uid;
        this.type = type;
        this.FilePath = FilePath;
    }
    @Generated(hash = 158010816)
    public ImageInfoBean() {
    }
    public String getAttachId() {
        return this.attachId;
    }
    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getFilePath() {
        return this.FilePath;
    }
    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

}
