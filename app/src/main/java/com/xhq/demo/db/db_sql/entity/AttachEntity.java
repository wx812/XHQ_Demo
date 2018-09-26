package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11.
 * 附件实体entity
 */

public class AttachEntity extends AbsEntity{
    public static final String ENTITY_NAME = "TB_IM_ATTACH";
    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //附件id
    public String getAttachId() {
        return getStr("attach_id");
    }

    //附件id
    public void setAttachId(String attach_id) {
        put("attach_id", attach_id);
    }

    //附件名称
    public String getAttachName() {
        return getStr("attach_name");
    }

    //附件名称
    public void setAttachName(String attach_name) {
        put("attach_name", attach_name);
    }

    //附件后缀
    public String getAttachSuffix() {
        return getStr("attach_suffix");
    }

    //附件后缀
    public void setAttachSuffix(String attach_suffix) {
        put("attach_suffix", attach_suffix);
    }

    //ownerId
    public String getAttachOwnerId() {
        return getStr("attach_owner_id");
    }

    //ownerId
    public void setAttachOwnerId(String attach_owner_id) {
        put("attach_owner_id", attach_owner_id);
    }

    //附件路径
    public String getAttachPath() {
        return getStr("attach_path");
    }

    //附件路径
    public void setAttachPath(String attach_path) {
        put("attach_path", attach_path);
    }

    //附件大小
    public double getAttachSize() {
        return getDouble("attach_size");
    }

    //附件大小
    public void etAttachSize(long attach_size) {
        put("attach_size", attach_size);
    }

    //附件实际大小
    public long getRealAttachSize() {
        return getLong("real_attach_size");
    }

    //附件实际大小
    public void setRealAttachSize(double real_attach_size) {
        put("real_attach_size", real_attach_size);
    }

    //附件类型
    public int getAttachType() {
        return getInt("attach_type");
    }

    //附件类型
    public void setAttachTypee(int attach_type) {
        put("attach_type", attach_type);
    }

    //附件Md5
    public String getAttachMd5() {
        return getStr("file_md5");
    }

    //附件Md5
    public void setAttachMd5(String file_md5) {
        put("file_md5", file_md5);
    }


}
