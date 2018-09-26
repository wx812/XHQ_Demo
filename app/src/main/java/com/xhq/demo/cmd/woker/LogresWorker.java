package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.ImEventFactory;
import com.xhq.demo.cmd.impl.LogresCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.encodeTools.EncryptUtils;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;

/**
 * Created by zhenggm on 2017/1/10.
 * 登录确认
 */
public class LogresWorker extends BaseAnswerCmdWorker<LogresCmd> {
    @Override
    public LogresCmd newCmdInstance() {
        return new LogresCmd();
    }

    @Override
    protected void localDealCmd(LogresCmd cmd) throws Exception{
        SPUtils.put(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, cmd.getSk());//保存sk;
        SPUtils.put(SPKey.USER_CONFIG, SPKey.USER_CODE, cmd.getUserCode());//保存居家号，后续以居家号初始化建表;
        SPUtils.put(SPKey.USER_CONFIG, SPKey.USER_STATE, 1);
        SPUtils.put(SPKey.USER_CONFIG, SPKey.UID, cmd.getUserId());
        SPUtils.put(SPKey.USER_CONFIG, SPKey.USER_TEL, cmd.getTel());
        SPUtils.put(SPKey.USER_CONFIG, SPKey.AUTO_LOGIN, 1);
        String name = "";
        if (!StringUtils.isEmpty(cmd.getName())) {
            name = EncryptUtils.decryptPerson(cmd.getName());
        }
        SPUtils.put(SPKey.USER_CONFIG, SPKey.USER_NAME, name);
        ImEventFactory.getInstance().postLoginStatuEvent(ApiEnum.Consts.STATU_OK, null, cmd);
    }

    @Override
    protected void localErrCmd(LogresCmd cmd, Exception e) {
        String info = e == null ? EncryptUtils.decryptPerson(cmd.getInfo()) : getOrigMsg(e);

        ImEventFactory.getInstance().postLoginStatuEvent(ApiEnum.Consts.STATU_ERROR, info, cmd);
    }


    public static String getOrigMsg(Throwable e) {
        String s = e.getMessage();
        Throwable t = e.getCause();
        while (t != null) {
            s = t.getMessage();
            t = t.getCause();
        }
        return s;
    }
}
