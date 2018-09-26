package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11.
 * 分组表实体entity
 */

public class CatalogEntity extends AbsEntity{
    public static final String ENTITY_NAME = "TB_IM_CATALOG";

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //分组id
    public String getCatalogId() {
        return getStr("cid");
    }

    //分组id
    public void setCatalogId(String cid) {
        put("cid", cid);
    }

    //分组名
    public String getCatalogName() {
        return getStr("cname");
    }

    //分组名
    public void setCatalogName(String cname) {
        put("cname", cname);
    }

    //分组总人数
    public int getCatalogNum() {
        return getInt("cnum");
    }

    //分组总人数
    public void setCatalogNum(int cnum) {
        put("cnum", cnum);
    }

    //分组在线人数
    public int getCatalogOnLineNum() {
        return getInt("onum");
    }

    //分组在线人数
    public void setCatalogOnLineNum(int onum) {
        put("onum", onum);
    }

    //分组序号
    public int getIndex() {
        return getInt("cindex");
    }

    //分组序号
    public void setIndex(int cindex) {
        put("cindex", cindex);
    }

    //是否展开//0为关闭，1为打开
    public int getIsOpen(){
        return getInt("isopen");
    }

    public void setIsOpen(int isOpen){
        put("isopen",isOpen);
    }

}
