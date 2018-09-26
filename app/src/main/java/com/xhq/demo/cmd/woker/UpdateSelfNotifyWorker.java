package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.cmd.impl.UpdateSelfNotifyFricmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

/**
 * Created by zyj on 2017/4/25.
 * 修改个人信息通知联系人
 */

public class UpdateSelfNotifyWorker extends BaseCmdWorker<UpdateSelfNotifyFricmd> {
    @Override
    public UpdateSelfNotifyFricmd newCmdInstance() {
        return new UpdateSelfNotifyFricmd();
    }

    @Override
    protected void localDealCmd(UpdateSelfNotifyFricmd cmd) throws Exception{
//        //更新数据库
//        String uid = cmd.getUserId();
//        String pl = cmd.getSignature();
//        String hid = cmd.getHid();
//        String name = cmd.getName();
//        UserBaseDao userBaseDao = new UserBaseDao();
//        UserBaseEntity userBaseEntity = userBaseDao.findOneByPK(uid);
//        if(!userBaseEntity.isEmpty()){
//            if(UtilPub.isNotEmpty(pl)){
//                userBaseEntity.setSignature(UtilEncode.base64DecodeEx(pl));
//            }
//            if(UtilPub.isNotEmpty(hid)){
//                userBaseEntity.setPhotoId(hid);
//            }
//            if(UtilPub.isNotEmpty(name)){
//                userBaseEntity.setNickName(UtilEncode.base64DecodeEx(name));
//            }
//            userBaseDao.update(userBaseEntity);
//            UserBaseBuffer.getInstance().add(userBaseEntity);
//        }
        //发送更新列表eventype,通讯录页面/聊天页面/最近联系人页面
        ImEventFactory.getInstance().postUserStatuMsg(ApiEnum.Consts.STATU_OK, null, cmd);
    }
}
