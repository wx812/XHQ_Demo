package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.cmd.impl.ChangeUsCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

/**
 * Created by Akmm at 2017/4/22 22:12
 * 好友状态切换
 */

public class ChangeStatuWorker extends BaseCmdWorker<ChangeUsCmd>{
    @Override
    public ChangeUsCmd newCmdInstance() {
        return new ChangeUsCmd();
    }

    @Override
    protected void localDealCmd(ChangeUsCmd cmd){
//        UserBaseEntity user = UserBaseBuffer.getInstance().get(cmd.getUid());
//        if (user == null) return;
//        user.setUserStatu(cmd.getUs());
//        new UserBaseDao().update(user, "us");

        ImEventFactory.getInstance().postUserStatuMsg(ApiEnum.Consts.STATU_OK, null, cmd);
    }
}
