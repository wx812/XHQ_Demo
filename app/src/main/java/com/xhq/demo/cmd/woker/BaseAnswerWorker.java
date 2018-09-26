package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseAnswerCmd;
import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.db.db_sql.entity.MyMsgDao;
import com.xhq.demo.db.db_sql.entity.MyMsgEntity;
import com.xhq.demo.tools.ErrorMessage;

/**
 * Created by zyj on 2017/4/20.
 * 加好友响应
 */

public class BaseAnswerWorker extends BaseAnswerCmdWorker<BaseAnswerCmd> {
    @Override
    protected void localDealCmd(BaseAnswerCmd cmd) throws Exception{
        if (Integer.parseInt(ErrorMessage.ERROR_RIGHT) == cmd.getStatu()) {
            MyMsgDao myMsgDao = new MyMsgDao();
            String mid = cmd.getOrgMsgId();
            MyMsgEntity myMsgEntity = myMsgDao.findOneByPK(mid);
            //更新消息表发送状态
            if(!myMsgEntity.isEmpty()){
                myMsgEntity.setSendSign(ApiEnum.SendSignEnum.SUCCESS.value);
                myMsgDao.update(myMsgEntity);
            }

            ImEventFactory.getInstance().postAnswerMsg(ApiEnum.Consts.STATU_OK, null, cmd);
        } else {
            ImEventFactory.getInstance().postAnswerMsg(ApiEnum.Consts.STATU_ERROR, null, cmd);
        }

    }

    @Override
    public BaseAnswerCmd newCmdInstance() {
        return new BaseAnswerCmd();
    }
}
