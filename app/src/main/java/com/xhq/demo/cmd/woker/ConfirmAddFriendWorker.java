package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.impl.ConFirAddCmd;
import com.xhq.demo.tools.StringUtils;

/**
 * Created by zyj on 2017/4/22.
 * 收到确认加好友
 */

public class ConfirmAddFriendWorker extends BaseCmdWorker<ConFirAddCmd> {

    @Override
    protected void localDealCmd(final ConFirAddCmd cmd){
        if (StringUtils.isEmpty(cmd.getGroupId())) {
            confirmAddFriend(cmd);
        }else {
            confirmAddGroup(cmd);
        }
    }

    @Override
    public ConFirAddCmd newCmdInstance() {
        return new ConFirAddCmd();
    }

    private void confirmAddFriend(final ConFirAddCmd cmd){
//        //添加好友时更新好友表和分组表
//        final CatalogDao catalogDao = new CatalogDao();
//        final CatalogEntity catalogEntity = CatalogBuffer.getInstance().get(SettingLoader.getUserId(HomeApp.getAppContext()));
//        //好友请求表更新
//        AddFriendEntity addFriendEntity = AddFriendBuffer.getInstance().get(cmd.getUserId());
//        if (addFriendEntity != null) {
//            AddFriendDao addFriendDao = new AddFriendDao();
//            addFriendEntity.setRet(cmd.getRet());
//            addFriendEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//            try {
//                addFriendDao.update(addFriendEntity);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            AddFriendBuffer.getInstance().add(addFriendEntity);
//            AddFriendBuffer.getInstance().incTotalUnreadFriendsNum();//加好友消息+1
//        }
//        if (cmd.getRet() == ApiEnum.RetEnum.AGREE.value) {
//            ImCmdHelper.getUserDetailInfo(HomeApp.getCurrentActivity(), cmd.getUserId(), new ImCmdHelper.IHttpSuccessHandler() {
//                @Override
//                public void success() {
//                    final UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(cmd.getUserId());
//                    //更新分组表
//                    //同意的时候,先查一遍好友信息并存库，然后查好友状态，如果在线，分组在线人数+1
//                    if (catalogEntity != null) {
//                        userBaseEntity.setCatalogId(catalogEntity.getCatalogId());
//                        int num = catalogEntity.getCatalogNum();
//                        int onum = catalogEntity.getCatalogOnLineNum();
//                        catalogEntity.setCatalogNum(num + 1);
//                        if (ApiEnum.UserStatuEnum.isOnline(userBaseEntity.getUserStatu())) {
//                            catalogEntity.setCatalogOnLineNum(onum + 1);
//                        }
//                        try {
//                            DbEngine.doTransaction(new AbsDbWorker() {
//                                @Override
//                                public void work() throws Exception {
//                                    new UserBaseDao().update(userBaseEntity);
//                                    UserBaseBuffer.getInstance().add(userBaseEntity);
//                                    catalogDao.update(catalogEntity);
//                                    CatalogBuffer.getInstance().add(catalogEntity);
//                                }
//                            });
//                        } catch (Exception e) {
//                            LogUtil.e("更新分组表失败!");
//                            e.printStackTrace();
//                        }
//                    }
//                    ImEventFactory.getInstance().postNewFriendMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//                }
//            });
//        }
    }

    private void confirmAddGroup(final ConFirAddCmd cmd){
        //群通知更新
//        AddFriendEntity addFriendEntity = AddFriendBuffer.getInstance().get(cmd.getGroupId()+cmd.getUserId());
//        if (addFriendEntity != null) {
//            AddFriendDao addFriendDao = new AddFriendDao();
//            addFriendEntity.setRet(cmd.getRet());
////            addFriendEntity.setUserName(UtilEncode.base64DecodeEx(cmd.getName()));
//            addFriendEntity.setMsg(UtilEncode.base64DecodeEx(cmd.getMsg()));
//            try {
//                addFriendDao.insertOrUpdate(addFriendEntity, AddFriendBuffer.getInstance());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            AddFriendBuffer.getInstance().incAddGroupUnreadNum();//群消息+1
//        }else {
//            LogUtil.e("群通知缓存获取失败");
//            return;
//        }
//        if (cmd.getRet() == ApiEnum.RetEnum.AGREE.value) {
////            ImCmdHelper.getGroupAndMembers(HomeApp.getCurrentActivity());
//            ImCmdHelper.getChatGroupInfo(HomeApp.getAppContext(), cmd.getGroupId(), new ImCmdHelper.IHttpSuccessHandler() {
//                @Override
//                public void success() {
//                    GroupMemberEntity groupMemberEntity = GroupMemberBuffer.getInstance().get(cmd.getGroupId() + cmd.getUserId());
//                    if(groupMemberEntity == null){
//                        groupMemberEntity = new GroupMemberEntity();
//                        groupMemberEntity.setGoupId(cmd.getGroupId());
//                        groupMemberEntity.setUserId(cmd.getUserId());
//                        groupMemberEntity.setGroupMemberType(ApiEnum.GroupMemberType.MEMBER.value);
//                        try {
//                            new GroupMemberDao().insert(groupMemberEntity);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        try {
//                            new GroupMemberDao().update(groupMemberEntity);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
    }
}
