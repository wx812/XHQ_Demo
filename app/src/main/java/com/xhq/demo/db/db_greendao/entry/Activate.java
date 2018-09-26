package com.xhq.demo.db.db_greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xyy on 2017/4/10.
 */
@Entity
public class Activate{
    @Id
    private String userId;
    private String cone;
    private String ctwo;
    private String cthr;
    private String cfour;
    private String name;
    private String tel;
    private String uid;
    private String statu;

	@Generated(hash = 100591211)
	public Activate(String userId, String cone, String ctwo, String cthr,
                    String cfour, String name, String tel, String uid, String statu) {
					this.userId = userId;
					this.cone = cone;
					this.ctwo = ctwo;
					this.cthr = cthr;
					this.cfour = cfour;
					this.name = name;
					this.tel = tel;
					this.uid = uid;
					this.statu = statu;
	}

	@Generated(hash = 147046779)
	public Activate() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCone() {
		return cone;
	}

	public void setCone(String cone) {
		this.cone = cone;
	}

	public String getCtwo() {
		return ctwo;
	}

	public void setCtwo(String ctwo) {
		this.ctwo = ctwo;
	}

	public String getCthr() {
		return cthr;
	}

	public void setCthr(String cthr) {
		this.cthr = cthr;
	}

	public String getCfour() {
		return cfour;
	}

	public void setCfour(String cfour) {
		this.cfour = cfour;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}
}
