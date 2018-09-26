package com.xhq.demo.db.db_greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xyy
 */
@Entity
public class DownloadFile{
    @Id
    private String id;
    private String path;
    private String UserId;
    @Generated(hash = 1166616192)
    public DownloadFile(String id, String path, String UserId) {
        this.id = id;
        this.path = path;
        this.UserId = UserId;
    }
    @Generated(hash = 379234666)
    public DownloadFile() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

}
