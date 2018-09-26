package com.xhq.demo.cmd;

/**
 * Created by Akmm at 2017/4/16 21:36
 * 事件监听处理接口
 */

public interface IEventHandler<T extends BaseCmd> {
    void work(ImCmdEvent<T> event);
}
