package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.impl.SendMsgBaseCmd;

/**
 * Created by zyj on 2017/4/17.
 * 消息类父类
 */

public abstract class SendMsgWorker<T extends SendMsgBaseCmd> extends BaseCmdWorker<T> {

    @Override
    protected void localDealCmd(T cmd) throws Exception{
//        recentContact(cmd);    //更新最近联系人表
//        MyMsgDao.saveMsg(cmd, ApiEnum.ReadTypeEnum.UNREAD.value,ApiEnum.SendTypeEnum.RECEIVE.value);  //保存指令
//        ImEventFactory.getInstance().postSendMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//    }
//
//    //更新最近联系人表
//    private RecentContactsEntity recentContact(SendMsgBaseCmd cmd) throws Exception {
//        RecentContactsDao recentContactsDao = new RecentContactsDao();
//        String uid = cmd.getUserId();
//        String gid = cmd.getGroupId();
//        String mid = UtilPub.isNotEmptyId(gid) ? gid : uid;//消息id，取群id或用户id
//
//        RecentContactsEntity recentContactsEntity = RecentContactsBuffer.getInstance().get(mid);
//        boolean isNew = recentContactsEntity == null;
//        if (isNew) {
//            recentContactsEntity = new RecentContactsEntity();
//            recentContactsEntity.init();
//        }
//        recentContactsEntity.setId(mid);
//        recentContactsEntity.setUserId(uid);
//        recentContactsEntity.setGoupId(gid);
//        recentContactsEntity.setSendTime(cmd.getTime());
//
//        String msg = cmd.getMsg();
//        if (UtilPub.isNotEmpty(msg)) {
//            msg = UtilEncode.base64DecodeEx(msg);
//        }
//        recentContactsEntity.setMsg(msg);
//        int unreadNum = recentContactsEntity.getUnreadNum();
//        recentContactsEntity.setUnreadNum(unreadNum + 1);
//
//        RecentContactsBuffer.getInstance().add(recentContactsEntity);//把该条记录放缓存
//        RecentContactsBuffer.getInstance().incTotalUnreadMsgNum();
//        if (isNew) {
//            recentContactsDao.insert(recentContactsEntity);
//        } else {
//            recentContactsDao.update(recentContactsEntity);
//        }
//        return recentContactsEntity;
    }

    //保存指令
//    private void saveMsg(SendMsgBaseCmd cmd) throws Exception {
//        MyMsgDao myMsgDao = new MyMsgDao();
//        MyMsgEntity myMsgEntity = new MyMsgEntity();
//        myMsgEntity.setMsgId(cmd.getMsgId());
//        myMsgEntity.setMsgType(cmd.getMsgType());
//        myMsgEntity.setSendTime(cmd.getTime());
//        myMsgEntity.setSendType(ApiEnum.SendSignEnum.SUCCESS.value);
//        myMsgEntity.setUserId(cmd.getUserId());
//        myMsgEntity.setGroupId(cmd.getGroupId());
//        myMsgEntity.setSendType(ApiEnum.SendTypeEnum.RECEIVE.value);
//        myMsgEntity.setReadType(ApiEnum.ReadTypeEnum.UNREAD.value);
//        String msg = cmd.getMsg();
//        if (UtilPub.isNotEmpty(msg)) {
//            msg = UtilEncode.base64DecodeEx(msg);
//        }
//        myMsgEntity.setMsg(msg);
//        myMsgDao.insert(myMsgEntity);
//    }
}
