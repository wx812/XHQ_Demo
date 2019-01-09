package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.cmd.impl.NewGroMemCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

/**
 * Created by zyj on 2017/5/23.
 * 新群成员通知
 */

public class NewGroupMemberWorker extends BaseCmdWorker<NewGroMemCmd>{
    @Override
    protected void localDealCmd(final NewGroMemCmd cmd){
//        GroupMemberDao groupMemberDao = new GroupMemberDao();
//        GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
//		String gid = cmd.getGroupId();
//		String uid = cmd.getUserId();
//		groupMemberEntity.setMemberId(gid+uid);
//        groupMemberEntity.setGoupId(gid);
//        groupMemberEntity.setUserId(uid);
//        groupMemberEntity.setGroupMemberType(cmd.getGmt());
//        String name = cmd.getName();
//        if(UtilPub.isNotEmpty(name)){
//            name = UtilEncode.base64DecodeEx(name);
//        }
//        groupMemberEntity.setMemberName(name);
//        GroupMemberBuffer.getInstance().add(groupMemberEntity);
//        groupMemberDao.insert(groupMemberEntity);
        ImEventFactory.getInstance().postNewGroupMemberJoin(ApiEnum.Consts.STATU_OK, null, cmd);
    }

    @Override
    public NewGroMemCmd newCmdInstance() {
        return new NewGroMemCmd();
    }

    protected void localErrCmd(NewGroMemCmd cmd, Exception e) {
        e.printStackTrace();
    }

}
