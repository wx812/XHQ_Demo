package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.BaseAnswerCmd;
import com.xhq.demo.cmd.BaseCmdWorker;

/**
 * Created by Akmm at 2017/4/13 23:08
 * 通用应答指令处理类
 */
public abstract class BaseAnswerCmdWorker<T extends BaseAnswerCmd> extends BaseCmdWorker<T> {
    @Override
    public void dealCmd(T cmd) {
        try {
            checkCode(cmd);
            if (cmd.getStatu() == 0) {
                localDealCmd(cmd);
            } else {
                localErrCmd(cmd, null);
            }
        } catch (Exception e) {
            localErrCmd(cmd, e);
        }
    }

    /**
     * 处理指令出错
     * @param cmd
     * @param e
     */
    protected void localErrCmd(T cmd, Exception e) {}
}
