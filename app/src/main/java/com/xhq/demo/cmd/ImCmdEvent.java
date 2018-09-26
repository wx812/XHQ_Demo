package com.xhq.demo.cmd;

/**
 * Created by Akmm at 2017/4/16 16:42
 * 消息事件
 */
public class ImCmdEvent<T extends BaseCmd> {
    private int eventType;//事件类别
    private int statu;//处理结果
    private String info; //处理附加信息
    private T cmd;//原始消息指令

    public ImCmdEvent() {
    }

    public ImCmdEvent(int eventType, int statu, String info, T cmd) {
        this.eventType = eventType;
        this.statu = statu;
        this.info = info;
        this.cmd = cmd;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getCmd() {
        return cmd;
    }

    public void setCmd(T cmd) {
        this.cmd = cmd;
    }
}
