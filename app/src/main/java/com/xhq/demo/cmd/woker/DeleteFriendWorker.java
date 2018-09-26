package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.impl.DelFriCmd;

/**
 * Created by 闻雅小少 on 2017/4/21.
 */

public class DeleteFriendWorker extends BaseCmdWorker<DelFriCmd> {

    @Override
    protected void localDealCmd(final DelFriCmd cmd) throws Exception{
//        UserBaseEntity a = UserBaseBuffer.getInstance().get(cmd.getUserId());
//        LogUtil.e("DeleteFriendWorker");
//        if (a != null) {
//            saveMsg(cmd);  //保存指令
//        }
    }

    @Override
    public DelFriCmd newCmdInstance() {
        return new DelFriCmd();
    }

    //保存指令
    private void saveMsg(final DelFriCmd cmd) throws Exception{

//        UserBaseDao userBaseDao = new UserBaseDao();
//
//        UserBaseEntity userBaseEntity = UserBaseBuffer.getInstance().get(cmd.getUserId());
//        if (userBaseEntity != null) {
//            userBaseEntity.setCatalogId("");
//            userBaseDao.update(userBaseEntity);
//            UserBaseBuffer.getInstance().add(userBaseEntity);
//        }
//        ImCmdHelper.getUserDetailInfo(HomeApp.getCurrentActivity(), cmd.getUserId(), new ImCmdHelper.IHttpSuccessHandler() {
//            @Override
//            public void success() {
//                ImEventFactory.getInstance().postDeleteFriendMsg(ApiEnum.Consts.STATU_OK, null, cmd);
//            }
//        });
    }
}
