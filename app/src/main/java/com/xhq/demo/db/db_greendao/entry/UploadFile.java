package com.xhq.demo.db.db_greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xyy on 2016/3/24.
 */
@Entity
public class UploadFile{
    @Id
    private String id;
    private String url;
    @Generated(hash = 266528390)
    public UploadFile(String id, String url) {
        this.id = id;
        this.url = url;
    }
    @Generated(hash = 1089490643)
    public UploadFile() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
