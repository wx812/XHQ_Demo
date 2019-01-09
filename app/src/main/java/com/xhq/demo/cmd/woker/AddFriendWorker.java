package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.impl.AddFriendCmd;
import com.xhq.demo.tools.StringUtils;

/**
 * Created by zyj on 2017/4/20.
 * 加好友/群消息
 */

public class AddFriendWorker extends BaseCmdWorker<AddFriendCmd> {
    @Override
    protected void localDealCmd(AddFriendCmd cmd){
        if (StringUtils.isEmpty(cmd.getGroupId())) {
            addFriend(cmd);
        }else {
            addGroup(cmd);
        }
    }

    private void addFriend(final AddFriendCmd cmd){
//        AddFriendDao addFriendDao = new AddFriendDao();
//        AddFriendEntity a = AddFriendBuffer.getInstance().get(cmd.getUserId());
//        //添加加好友记录
//        if (a != null) {
//            UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(cmd.getUserId());
//            if ((userBaseEntity == null || (StringUtils.isEmpty(userBaseEntity.getCatalogId()))&& ApiEnum.RetEnum.NOANSWER.value != a.getRet())){
//                AddFriendBuffer.getInstance().incTotalUnreadFriendsNum();//加好友消息+1
//            }
//            a.setSendTime(cmd.getTime());
//            a.setIsRead(ApiEnum.ReadTypeEnum.UNREAD.value);
//            a.setAddType(ApiEnum.AddFriendTypeEnum.BEADD.value);
//            a.setRet(ApiEnum.RetEnum.NOANSWER.value);
//            a.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//            addFriendDao.update(a);
//            AddFriendBuffer.getInstance().add(a);
//        }else{
//            AddFriendEntity addFriendEntity = new AddFriendEntity();
//            addFriendEntity.setId(cmd.getUserId());
//            addFriendEntity.setUserId(cmd.getUserId());//对方id
//            addFriendEntity.setGoupId(cmd.getGroupId());
//            addFriendEntity.setSendTime(cmd.getTime());
//            addFriendEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//            addFriendEntity.setIsRead(ApiEnum.ReadTypeEnum.UNREAD.value);
//            addFriendEntity.setAddType(ApiEnum.AddFriendTypeEnum.BEADD.value);
//            addFriendEntity.setRet(ApiEnum.RetEnum.NOANSWER.value);
//            if (UtilPub.isNotEmpty(cmd.getMsg())) {
//                addFriendEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//            }
//            AddFriendBuffer.getInstance().incTotalUnreadFriendsNum();//加好友消息+1
//            addFriendDao.insert(addFriendEntity);
//            AddFriendBuffer.getInstance().add(addFriendEntity);
//        }
//        UserBaseEntity u = UserBaseBuffer.getInstance().get(cmd.getUserId());
//        if (u == null) {
//            ImCmdHelper.getUserDetailInfo(HomeApp.getCurrentActivity(), cmd.getUserId(), new ImCmdHelper.IHttpSuccessHandler() {
//                @Override
//                public void success() {
//                    ImEventFactory.getInstance().postNewFriendMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//                }
//            });
//        } else {
//            ImEventFactory.getInstance().postNewFriendMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//        }
    }

    private void addGroup(final AddFriendCmd cmd){
//		LogUtil.e("addGroup  receive 1109");
//        AddFriendDao addFriendDao = new AddFriendDao();
//        AddFriendEntity addFriendEntity = AddFriendBuffer.getInstance().get(cmd.getGroupId()+cmd.getUserId());
//        RecentContactsDao recentContactsDao = new RecentContactsDao();
//        RecentContactsEntity recentContactsEntity = RecentContactsBuffer.getInstance().get(Constant.RecentMsgGroupNoticeKey);
//        String gid = cmd.getGroupId();
//        String uid = cmd.getUserId();
//        String name = cmd.getName();
//        if(UtilPub.isNotEmpty(name)){
//            name = UtilEncode.base64DecodeEx(name);
//        }
//        String gname = cmd.getGname();
//        if(UtilPub.isNotEmpty(gname)){
//            gname = UtilEncode.base64DecodeEx(gname);
//        }
//
//        if(recentContactsEntity == null){
//            recentContactsEntity = new RecentContactsEntity();
//        }
//        RecentContactsBuffer.getInstance().incAddGroupUnreadNum();   //未读群消息+1
//        RecentContactsBuffer.getInstance().incTotalUnreadMsgNum();   //总数+1
//
//        recentContactsEntity.setId(Constant.RecentMsgGroupNoticeKey);   //主键
//        recentContactsEntity.setGoupId(gid);
//        recentContactsEntity.setUserId(uid);
//        recentContactsEntity.setMsg(name+"申请加入群聊"+gname);
//        recentContactsEntity.setSendTime(cmd.getTime());
//        recentContactsDao.insertOrUpdate(recentContactsEntity,RecentContactsBuffer.getInstance());
//
//        //添加加好友记录
//        if (addFriendEntity == null) {
//			addFriendEntity = new AddFriendEntity();
//			addFriendEntity.setId(gid+uid);
//			addFriendEntity.setUserId(uid);
//			addFriendEntity.setGoupId(gid);
//        }
//
//		addFriendEntity.setSendTime(cmd.getTime());
////		addFriendEntity.setIsRead(ApiEnum.ReadTypeEnum.UNREAD.value);
//		addFriendEntity.setAddType(ApiEnum.AddFriendTypeEnum.JOIN_GROUP.value);
//		addFriendEntity.setRet(ApiEnum.RetEnum.NOANSWER.value);
//		if (UtilPub.isNotEmpty(cmd.getMsg())) {
//			addFriendEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//		}
//        addFriendDao.insertOrUpdate(addFriendEntity,AddFriendBuffer.getInstance());
//        UserBaseEntity u = UserBaseBuffer.getInstance().get(cmd.getUserId());
//        if (u == null) {
//            ImCmdHelper.getUserDetailInfo(HomeApp.getCurrentActivity(), cmd.getUserId(), new ImCmdHelper.IHttpSuccessHandler() {
//                @Override
//                public void success() {
//                    //发消息  更新messageFragment  NewGroupActivity
//                    ImEventFactory.getInstance().postNewGroupMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//                }
//            });
//        } else {
//            ImEventFactory.getInstance().postNewGroupMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//        }
    }

    @Override
    public AddFriendCmd newCmdInstance() {
        return new AddFriendCmd();
    }

    protected void localErrCmd(AddFriendCmd cmd, Exception e) {
        e.printStackTrace();
    }

}
