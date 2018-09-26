package com.xhq.demo.cmd;


import com.xhq.demo.cmd.impl.ICmdWorker;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.tools.ErrorMessage;
import com.xhq.demo.tools.encodeTools.EncryptUtils;

/**
 * Created by Akmm at 2017/1/2 0:54
 * 指令处理基类
 */
public abstract class BaseCmdWorker<T extends BaseCmd> implements ICmdWorker<T>{

    @Override
    public void dealCmd(T cmd) {
        try {
            checkCode(cmd);
            localDealCmd(cmd);
        } catch (Exception e) {
            localErrCmd(cmd, e);
        }
    }

    //校验防伪码
    protected void checkCode(T cmd) throws DfpException {
        String sc = cmd.getStr("sc");
        if (ApiEnum.Consts.DONOT_CHECK_SC.equals(sc)) return;
        String s = EncryptUtils.getSecurityCode(cmd.toString());
        if (!s.equals(sc)) {
            throw new DfpException(Integer.parseInt(ErrorMessage.ERROR_SECURITY_CODE));
        }
    }

    //本地处理指令
    protected abstract void localDealCmd(T cmd) throws Exception;

    /**
     * 处理指令出错
     * @param cmd
     * @param e
     */
    protected void localErrCmd(T cmd, Exception e) {
       e.printStackTrace();
    }
}
