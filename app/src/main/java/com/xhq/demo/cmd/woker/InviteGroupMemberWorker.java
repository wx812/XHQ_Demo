package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.impl.InviteGroMemberCmd;
import com.xhq.demo.tools.StringUtils;

/**
 * Created by xyy on 2017/5/19.
 * 邀请群成员
 */

public class InviteGroupMemberWorker extends BaseCmdWorker<InviteGroMemberCmd> {
    @Override
    protected void localDealCmd(final InviteGroMemberCmd cmd) throws Exception{
        if (!StringUtils.isEmpty(cmd.getGroupId())) {
//            AddFriendEntity addFriendEntity = AddFriendBuffer.getInstance().get(cmd.getGroupId()+cmd.getUserId());
//            AddFriendDao addFriendDao = new AddFriendDao();
//            //xyy add
//            RecentContactsDao recentContactsDao = new RecentContactsDao();
//            RecentContactsEntity recentContactsEntity = RecentContactsBuffer.getInstance().get(Constant.RecentMsgGroupNoticeKey);
//            if(recentContactsEntity == null){
//                recentContactsEntity = new RecentContactsEntity();
//                recentContactsEntity.setId(Constant.RecentMsgGroupNoticeKey);   //主键
////                RecentContactsBuffer.getInstance().incAddGroupUnreadNum();   //未读群消息+1
////                RecentContactsBuffer.getInstance().incTotalUnreadMsgNum();   //总数+1
//            }else {
//                //群通知里无记录
////                if(AddFriendBuffer.getInstance().get(cmd.getGroupId()+cmd.getUserId()) == null ){
//
////                }
//            }
//            RecentContactsBuffer.getInstance().incAddGroupUnreadNum();   //未读群消息+1
//            RecentContactsBuffer.getInstance().incTotalUnreadMsgNum();   //总数+1
//
//            recentContactsEntity.setGoupId(cmd.getGroupId());
//            recentContactsEntity.setUserId(cmd.getUserId());
//            recentContactsEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getName())
//                    +" 邀请您加入群聊 "+UtilEncode.base64DecodeEx(cmd.getGname()));
//            recentContactsEntity.setSendTime(cmd.getTime());
//            recentContactsDao.insertOrUpdate(recentContactsEntity,RecentContactsBuffer.getInstance());
//
//            //添加加好友记录
//            if (addFriendEntity == null) {
//                addFriendEntity = new AddFriendEntity();
//                addFriendEntity.setId(cmd.getGroupId()+cmd.getUserId());
//                addFriendEntity.setUserId(cmd.getUserId());
//                addFriendEntity.setGoupId(cmd.getGroupId());
//            }
////			LogUtil.e("UtilEncode.base64DecodeEx(cmd.getName() =" + UtilEncode.base64DecodeEx(cmd.getName()));
////			addFriendEntity.setUserName(UtilEncode.base64DecodeEx(cmd.getName()));
//            addFriendEntity.setSendTime(cmd.getTime());
//            if(cmd.getGmt() != ApiEnum.GroupMemberType.MEMBER.value){
//                addFriendEntity.setAddType(ApiEnum.AddFriendTypeEnum.INVITED_BY_ADMIN.value);
//            }else {
//                addFriendEntity.setAddType(ApiEnum.AddFriendTypeEnum.INVITED_BY_MEMBER.value);
//            }
//            addFriendEntity.setRet(ApiEnum.RetEnum.NOANSWER.value);
//            addFriendDao.insertOrUpdate(addFriendEntity,AddFriendBuffer.getInstance());
//            //判断本地库是否有群记录
//            GroupEntity groupEntity = GroupBuffer.getInstance().get(cmd.getGroupId());
//            if (groupEntity == null) {
//                ImCmdHelper.getChatGroupInfo(HomeApp.getCurrentActivity(), cmd.getGroupId(), new ImCmdHelper.IHttpSuccessHandler() {
//                    @Override
//                    public void success() {
//                        ImEventFactory.getInstance().postNewGroupMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//                    }
//                });
//            } else {
//                ImEventFactory.getInstance().postNewGroupMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//            }
        }
    }

    @Override
    public InviteGroMemberCmd newCmdInstance() {
        return new InviteGroMemberCmd();
    }

    protected void localErrCmd(InviteGroMemberCmd cmd, Exception e) {
        e.printStackTrace();
    }

}
