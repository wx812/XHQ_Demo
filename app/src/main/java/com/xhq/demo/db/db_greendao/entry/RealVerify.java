package com.xhq.demo.db.db_greendao.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xyy on 2017/4/10.
 */
@Entity
public class RealVerify{
    @Id
    private String userId;
    private String cone;
    private String ctwo;
    private String cthr;
    private String name;
    private String nat;
    private String sex;
    private String tel;
    private String bir;
    private String validF;
	private String validT;
    private String addr;
    private String uid;
    private String auth;
    private String statu;


    @Generated(hash = 1410862268)
    public RealVerify(String userId, String cone, String ctwo, String cthr,
                      String name, String nat, String sex, String tel, String bir,
                      String validF, String validT, String addr, String uid, String auth,
                      String statu) {
        this.userId = userId;
        this.cone = cone;
        this.ctwo = ctwo;
        this.cthr = cthr;
        this.name = name;
        this.nat = nat;
        this.sex = sex;
        this.tel = tel;
        this.bir = bir;
        this.validF = validF;
        this.validT = validT;
        this.addr = addr;
        this.uid = uid;
        this.auth = auth;
        this.statu = statu;
    }

    @Generated(hash = 2053775390)
    public RealVerify() {
    }


    public String getCone() {
        return cone;
    }

    public String getCtwo() {
        return ctwo;
    }

    public String getCthr() {
        return cthr;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getUid() {
        return uid;
    }


    public String getNat() {
        return nat;
    }

    public String getAddr() {
        return addr;
    }

    public String getAuth() {
        return auth;
    }

    public String getStatu() {
        return statu;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCone(String cone) {
        this.cone = cone;
    }

    public void setCtwo(String ctwo) {
        this.ctwo = ctwo;
    }

    public void setCthr(String cthr) {
        this.cthr = cthr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getValidF() {
        return this.validF;
    }

    public void setValidF(String validF) {
        this.validF = validF;
    }

    public String getValidT() {
        return this.validT;
    }

    public void setValidT(String validT) {
        this.validT = validT;
    }

    public String getBir() {
        return this.bir;
    }

    public void setBir(String bir) {
        this.bir = bir;
    }

}
