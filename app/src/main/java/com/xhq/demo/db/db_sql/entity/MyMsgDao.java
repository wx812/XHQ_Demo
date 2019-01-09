package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.cmd.BaseAnswerCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.encodeTools.EncryptUtils;

//import com.xhq.demo.db.encode.UtilEncode;

/**
 * Created by zyj on 2017/4/11.
 * 消息指令表实体dao
 */

public class MyMsgDao extends AbsEntityDao<MyMsgEntity>{

    @Override
    public MyMsgEntity getNewBean() {
        return new MyMsgEntity();
    }

    @Override
    public String getEntityName() {
        return MyMsgEntity.ENTITY_NAME;
    }

    //保存指令
    public static void saveMsg(BaseAnswerCmd cmd, int read_type, int send_type){
        int send_sign = ApiEnum.SendSignEnum.SENDDING.value;
        if(ApiEnum.SendTypeEnum.RECEIVE.value == send_type){
            send_sign = ApiEnum.SendSignEnum.SUCCESS.value;
        }
        MyMsgDao myMsgDao = new MyMsgDao();
        MyMsgEntity myMsgEntity = new MyMsgEntity();
        myMsgEntity.setMsgId(cmd.getMsgId());
        myMsgEntity.setMsgType(cmd.getMsgType());
        myMsgEntity.setSendTime(cmd.getTime());
        myMsgEntity.setSendSign(send_sign);
        myMsgEntity.setUserId(cmd.getUserId());
        myMsgEntity.setGroupId(cmd.getGroupId());
        myMsgEntity.setSendType(send_type);
        myMsgEntity.setReadType(read_type);
        String msg = cmd.getMsg();
        if (!StringUtils.isEmpty(msg)) {
//            msg = UtilEncode.base64DecodeEx(msg);
            msg = EncryptUtils.decryptPerson(msg);
        }
        myMsgEntity.setMsg(msg);
        myMsgDao.insert(myMsgEntity);
    }
}
