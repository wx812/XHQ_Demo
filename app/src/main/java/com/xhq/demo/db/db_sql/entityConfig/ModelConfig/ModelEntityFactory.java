package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;


import com.xhq.demo.db.db_sql.DBHelper;
import com.xhq.demo.db.db_sql.UtilCursor;
import com.xhq.demo.db.db_sql.entity.TableVersionDao;
import com.xhq.demo.db.db_sql.entity.TableVersionEntity;
import com.xhq.demo.db.db_sql.entityConfig.EntityDaoHelper;
import com.xhq.demo.db.db_sql.interf.IDBCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyj on 2017/4/10.
 */
public class ModelEntityFactory {
    private static ModelEntityFactory INSTANCE;
    private Map<String, ModelEntity> entitydefs = new HashMap<>();  //实体定义集合；(key=ENTITY_NAME(大写))


    static {
        INSTANCE = new ModelEntityFactory();
    }

    public static ModelEntityFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 增加IM表定义
     */
    public void init() {
        ModelEntity userEntity = new ModelEntity("TB_IM_USER_BASE", "我的联系人", 1);
        ModelEntity groupEntity = new ModelEntity("TB_IM_GROUP", "我的群", 1);
        ModelEntity groupMemberEntity = new ModelEntity("TB_IM_GROUP_MEMBER", "群成员", 1);
        ModelEntity catalogsEntity = new ModelEntity("TB_IM_CATALOG", "我的分组", 2);
        ModelEntity myMsgEntity = new ModelEntity("TB_IM_MY_MSG", "我的消息", 1);
        ModelEntity recentContactsEntity = new ModelEntity("TB_IM_RECENT_CONTACTS", "最近联系人", 1);
        ModelEntity attachEntity = new ModelEntity("TB_IM_ATTACH", "我的附件", 1);
        ModelEntity addFriendEntity = new ModelEntity("TB_IM_ADD_FRIEND", "添加好友", 1);
        ModelEntity tableVersionEntity = new ModelEntity("TB_IM_TABLE_DBVERSION", "表版本信息表", 1);

        tableVersionEntity.addPkField("table_name");
        tableVersionEntity.addIntField("version", "版本号");
        //我的联系人
        userEntity.addPkField("uid");
        userEntity.addStrField("name", "名称", 20);
        userEntity.addStrField("uc", "居家号", 8);
        userEntity.addStrField("cnote", "昵称", 16);
        userEntity.addStrField("pl", "个性签名", 100);
        userEntity.addIntField("us", "在线状态");
        userEntity.addIntField("ut", "身份类别");
        userEntity.addIntField("mt", "消息屏蔽类别");
        userEntity.addStrField("hid", "头像id", 32);
        userEntity.addStrField("cid", "分组id", 32);
        userEntity.addStrField("tel", "手机号", 11);
        //我的群
        groupEntity.addPkField("gid");
        groupEntity.addStrField("gno", "群号", 20);
        groupEntity.addStrField("gname", "群名称", 16);
        groupEntity.addStrField("hid", "群头像", 20);
        groupEntity.addIntField("gnum", "群人数");
        groupEntity.addIntField("onum", "群在线人数");
        groupEntity.addStrField("gdesc", "群简介", 200);
        groupEntity.addStrField("btime", "群创建时间", 32);
        groupEntity.addIntField("gt", "群类别");
        groupEntity.addIntField("mt", "群屏蔽类型");
        groupEntity.addIntField("gmt", "群创建类型");
        //群成员
        groupMemberEntity.addPkField("member_id");
        groupMemberEntity.addStrField("gid", "群id", 32);
        groupMemberEntity.addStrField("uid", "用户id", 32);
        groupMemberEntity.addIntField("gmt", "群成员类别");//群主 管理员
        groupMemberEntity.addStrField("gnote", "群名片", 16);
        //我的分组
        catalogsEntity.addPkField("cid");
        catalogsEntity.addStrField("cname", "分组名称", 16);
        catalogsEntity.addIntField("cnum", "分组人数");
        catalogsEntity.addIntField("onum", "在线人数");
        catalogsEntity.addIntField("cindex", "分组序号");
        catalogsEntity.addIntField("isopen","是否展开");//1为展开，0为不展开
        //我的消息
        myMsgEntity.addPkField("mid");
        myMsgEntity.addStrField("uid", "用户id", 32);
        myMsgEntity.addStrField("gid", "群id", 32);
        myMsgEntity.addIntField("send_type", "发送类型");
        myMsgEntity.addStrField("st", "发送时间", 32);
        myMsgEntity.addIntField("mtype", "消息类型");
        myMsgEntity.addStrField("msg", "具体消息", 500);//固定一定长度
        myMsgEntity.addIntField("send_sign", "发送成功标记");
        myMsgEntity.addIntField("read_type", "消息是否已读");
        //我的附件
        attachEntity.addPkField("attach_id");
        attachEntity.addStrField("attach_name", "附件名", 32);
        attachEntity.addStrField("attach_suffix", "附件后缀名", 32);
        attachEntity.addStrField("attach_owner_id", "附件拥有者id", 32);
        attachEntity.addStrField("attach_path", "附件路径", 100);
        attachEntity.addDoubleField("attach_size", "附件大小", 6, 2);
        attachEntity.addIntField("attach_type", "附件类型");
        attachEntity.addDoubleField("real_attach_size", "附件实际大小", 6, 2);
        attachEntity.addStrField("file_md5", "附件MD5", 256);
        //最近联系人
        recentContactsEntity.addPkField("id");
        recentContactsEntity.addStrField("uid", "用户id", 32);
        recentContactsEntity.addStrField("gid", "群id", 32);
        recentContactsEntity.addStrField("msg", "具体消息", 30);//固定一定长度+"..."
        recentContactsEntity.addStrField("st", "发送时间", 32);
        recentContactsEntity.addIntField("unread_num", "未读数量");
        //添加好友记录
        addFriendEntity.addPkField("id");
        addFriendEntity.addStrField("uid", "用户id", 32);
        addFriendEntity.addStrField("gid", "群id", 32);
        addFriendEntity.addIntField("ret", "添加结果");
        addFriendEntity.addStrField("st", "发送时间", 32);
        addFriendEntity.addIntField("is_read", "是否已读");
        addFriendEntity.addIntField("add_type", "添加类型");
        addFriendEntity.addStrField("msg", "理由", 32);

        addEntity(addFriendEntity);
        addEntity(userEntity);
        addEntity(groupEntity);
        addEntity(groupMemberEntity);
        addEntity(catalogsEntity);
        addEntity(myMsgEntity);
        addEntity(attachEntity);
        addEntity(recentContactsEntity);
        addEntity(tableVersionEntity);
    }

    public List<String> buildCreateSql() {
        List<String> list = new ArrayList<>();
        for (String key : entitydefs.keySet()) {
            ModelEntity modelEntity = entitydefs.get(key);
            String sql = EntityDaoHelper.buildCreateSql(modelEntity);
            list.add(sql);
        }
        return list;
    }

    //更新数据库表
    public void updateTableVersion() throws Exception{
        String sql = "select table_name,version from TB_IM_TABLE_DBVERSION";
        //获取所有表和它们的版本号
        Map<String, Object> map = DBHelper.query(sql, new IDBCallback() {
            @Override
            public Map<String, Integer> extractData(UtilCursor uc){
                Map<String, Integer> resultMap = new HashMap<>();
                while (uc.next()) {
                    resultMap.put(uc.getStringIgnoreNull("table_name"), uc.getIntNotException("version"));
                }
                return resultMap;
            }
        });

        for (String key : entitydefs.keySet()) {
            final ModelEntity modelEntity = entitydefs.get(key);
            final TableVersionDao tableVersionDao = new TableVersionDao();
            //如果没有，插入
            if (map.get(key) == null) {
                TableVersionEntity tb = new TableVersionEntity();
                tb.setTableName(key);
                tb.setVersion(modelEntity.getVersion());
                tableVersionDao.insert(tb);
                continue;
            }
            int newVersion = modelEntity.getVersion();
            int oldVersion = (int) map.get(key);
            //如果数据库有变化
            if (newVersion > oldVersion) {
                //更新数据库版本表
                final TableVersionEntity tableVersionEntity = tableVersionDao.findOneByPK(key);
                if (!tableVersionEntity.isEmpty()) {
                    tableVersionEntity.setVersion(newVersion);
                    tableVersionDao.update(tableVersionEntity);
                }
                try {
                    updateTable(modelEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //插入表字段
    private void updateTable(final ModelEntity modelEntity) throws Exception{
        String sql = "select * from " + modelEntity.getEntityName() + " limit 1";
        final List<ModelField> modelFields = modelEntity.getFields();
        List<ModelField> listModelField = DBHelper.query(sql, new IDBCallback() {
            @Override
            public List<ModelField> extractData(UtilCursor uc){
                List<ModelField> modelFieldList = new ArrayList<>();
                for (int i = 0; i < modelFields.size(); i++) {
                    String fieldName = modelFields.get(i).name;
                    //已有不管
                    if (!uc.containsField(fieldName)) {
                        modelFieldList.add(modelFields.get(i));
                    }
                }
                return modelFieldList;
            }
        });
        //sqlite不支持添加多列
        for (ModelField modelField : listModelField) {
            String type = ModelConsts.FieldType.getSqlTypeByValue(modelField.type);
            String alterSql = "ALTER TABLE " + modelEntity.getEntityName() + " ADD COLUMN " + modelField.name + " " + type + ";";
            DBHelper.update(alterSql);
        }
    }


    public ModelEntity getModelEntity(String entityName) {
        return entitydefs.get(entityName);
    }

    public void addEntity(ModelEntity modelEntity) {
        modelEntity.addStrField("last_time", "最后修改时间", 32);
        entitydefs.put(modelEntity.getEntityName(), modelEntity);
    }

    /**
     * 增加表
     */
    public ModelEntity addEntity(String name, String title, int version) {
        ModelEntity entity = new ModelEntity(name, title, version);
        addEntity(entity);
        return entity;
    }
}
