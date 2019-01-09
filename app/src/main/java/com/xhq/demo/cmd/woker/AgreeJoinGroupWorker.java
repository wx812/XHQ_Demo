package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.cmd.impl.AgrAdminCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

/**
 * Created by zyj on 2017/5/23.
 * 管理员收到同意群管理员邀请加群
 */

public class AgreeJoinGroupWorker extends BaseCmdWorker<AgrAdminCmd> {
    @Override
    protected void localDealCmd(final AgrAdminCmd cmd){
//        String gid = cmd.getGroupId();
//        String uid = cmd.getUserId();
//        AddFriendEntity addFriendEntity = AddFriendBuffer.getInstance().get(gid+uid);
//        if(addFriendEntity == null){
//            LogUtil.e("邀请加群未缓存！");
//            return;
//        }
//        addFriendEntity.setRet(cmd.getRet());
//        AddFriendBuffer.getInstance().add(addFriendEntity);
//        AddFriendDao addFriendDao = new AddFriendDao();
//        addFriendDao.update(addFriendEntity);
//        if(ApiEnum.RetEnum.AGREE.value == cmd.getRet()) {
//            GroupMemberDao groupMemberDao = new GroupMemberDao();
//            GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
//            groupMemberEntity.setMemberId(gid+uid);
//            groupMemberEntity.setGoupId(gid);
//            groupMemberEntity.setUserId(uid);
//            String name = cmd.getName();
//            if (UtilPub.isNotEmpty(name)) {
//                name = UtilEncode.base64DecodeEx(name);
//            }
//            //未设置前用本来的那么
//            groupMemberEntity.setMemberName(name);
//            groupMemberEntity.setGroupMemberType(ApiEnum.GroupMemberType.MEMBER.value);
//            GroupMemberBuffer.getInstance().add(groupMemberEntity);
//            groupMemberDao.insert(groupMemberEntity);
//        }
        ImEventFactory.getInstance().postNewGroupMsg(ApiEnum.Consts.STATU_OK, null, cmd);
    }

    @Override
    public AgrAdminCmd newCmdInstance() {
        return new AgrAdminCmd();
    }

    protected void localErrCmd(AgrAdminCmd cmd, Exception e) {
        e.printStackTrace();
    }

}
