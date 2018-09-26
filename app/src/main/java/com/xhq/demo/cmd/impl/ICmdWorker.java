package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by Akmm at 2016-02-23 15:06
 * 指令构造器
 */
public interface ICmdWorker<T extends BaseCmd> {
    //构建cmd
    T newCmdInstance();

    //处理指令，返回要发送给客户端的指令
    void dealCmd(T cmd);
}
