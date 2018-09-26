package com.xhq.demo.cmd;


import com.xhq.demo.cmd.impl.AddFriendCmd;
import com.xhq.demo.cmd.impl.AgrAdminCmd;
import com.xhq.demo.cmd.impl.ChangeUsCmd;
import com.xhq.demo.cmd.impl.DelFriCmd;
import com.xhq.demo.cmd.impl.InviteGroMemberCmd;
import com.xhq.demo.cmd.impl.LogresCmd;
import com.xhq.demo.cmd.impl.NewGroMemCmd;
import com.xhq.demo.cmd.impl.SendMsgBaseCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Akmm at 2017/4/16 17:04
 * Event工厂
 */

public class ImEventFactory {
    private static ImEventFactory instance;
    static {
        instance = new ImEventFactory();
    }

    public static ImEventFactory getInstance() {
        return instance;
    }

    //网络状态切
    @SuppressWarnings("unchecked")
    public void postNetStatuEvent(int statu) {
        SocketManager.getInstance().setNetChange(statu);
        ImCmdEvent event = new ImCmdEvent(ApiEnum.Consts.EVENT_TYPE_NET_STATU, statu, null, null);
        EventBus.getDefault().post(event);
    }

    //socket状态切换
    @SuppressWarnings("unchecked")
    public void postSocketStatuEvent(int statu) {
        ImCmdEvent event = new ImCmdEvent(ApiEnum.Consts.EVENT_TYPE_SOCKET_STATU, statu, null, null);
        EventBus.getDefault().post(event);
    }

    //登录状态切换
    public void postLoginStatuEvent(int statu, String info, LogresCmd cmd) {
        SocketManager.getInstance().setLoginStatus(statu);
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_LOGIN_STATU, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新消息，需要刷消息列表的
    public void postSendMsg(int statu, String info, SendMsgBaseCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_MSG, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新群消息，需要刷消息列表的
    public void postNewGroupMsg(int statu, String info, InviteGroMemberCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_GROUPMSG, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新群消息，需要刷消息列表的
    public void postNewGroupMsg(int statu, String info, AgrAdminCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_GROUPMSG, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新群消息，需要刷消息列表的
    public void postNewGroupMsg(int statu, String info, AddFriendCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_GROUPMSG, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新朋友，需要刷消息列表的
    public void postNewFriendMsg(int statu, String info, AddFriendCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_FRIEND, statu, info, cmd);
        EventBus.getDefault().post(event);
    }
    //新消息，需要刷消息列表的
    public void postDeleteFriendMsg(int statu, String info, DelFriCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_DELETE_FRIEND, statu, info, cmd);
        EventBus.getDefault().post(event);
    }
    //应答消息
    public void postAnswerMsg(int statu, String info, BaseAnswerCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_BASE_ANSWER, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //好友状态切换
    public void postUserStatuMsg(int statu, String info, ChangeUsCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_USER_STATU, statu, info, cmd);
        EventBus.getDefault().post(event);
    }

    //新群员加入
    public void postNewGroupMemberJoin(int statu, String info, NewGroMemCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_NEW_GROUPMEMBERJOIN, statu, info, cmd);
        EventBus.getDefault().post(event);
    }


    /*//好友信息修改
    public void postUserInfo(int statu, String info, ChangeUsCmd cmd) {
        ImCmdEvent event = new ImCmdEvent<>(ApiEnum.Consts.EVENT_TYPE_USER_STATU, statu, info, cmd);
        EventBus.getDefault().post(event);
    }*/
}
