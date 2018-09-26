package com.xhq.demo.cmd;

import android.util.SparseArray;

import com.xhq.demo.cmd.impl.ICmdWorker;
import com.xhq.demo.cmd.woker.AddFriendWorker;
import com.xhq.demo.cmd.woker.AgreeJoinGroupWorker;
import com.xhq.demo.cmd.woker.BaseAnswerWorker;
import com.xhq.demo.cmd.woker.BeQuitWorker;
import com.xhq.demo.cmd.woker.ChangeStatuWorker;
import com.xhq.demo.cmd.woker.ConfirmAddFriendWorker;
import com.xhq.demo.cmd.woker.DeleteFriendWorker;
import com.xhq.demo.cmd.woker.InviteGroupMemberWorker;
import com.xhq.demo.cmd.woker.LogresWorker;
import com.xhq.demo.cmd.woker.NewGroupMemberWorker;
import com.xhq.demo.cmd.woker.SendPicMsgWorker;
import com.xhq.demo.cmd.woker.SendTextMsgWorker;
import com.xhq.demo.cmd.woker.SendVoiceMsgWorker;
import com.xhq.demo.cmd.woker.UpdateSelfNotifyWorker;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.tools.encodeTools.EncryptUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akmm at 2015-11-15 17:14
 * 指令处理工厂
 */
public class CmdFactory {
    private static CmdFactory INSTANCE;

    static {
        INSTANCE = new CmdFactory();
    }


    public ICmdWorker getCmdBuilder(Integer cmdType) throws Exception{
        ICmdWorker cb;
        cb = mapCmdBuilder.get(cmdType);
        if(cb == null){
            cb = mapAnswerCmdBuilder.get(cmdType);
        }
        if (cb == null) throw new DfpException("暂不支持此类型的指令（" + cmdType + "）！");
        return cb;
    }

    public static CmdFactory getInstance() {
        return INSTANCE;
    }

    //以交易码作为key，指令处理器
    private static SparseArray<ICmdWorker> mapCmdBuilder;
    //应答确认指令
    private static SparseArray<ICmdWorker> mapAnswerCmdBuilder;

    static {
        mapCmdBuilder = new SparseArray<>();
//        mapCmdBuilder.put(ApiEnum.CmdType.ANSWER.value, new LogresWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.ANSWER.value, new BaseAnswerWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.SENDMSG.value, new SendTextMsgWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.SENDYUYINMSG.value, new SendVoiceMsgWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.SENDPICTUREMSG.value, new SendPicMsgWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.SENDFILEMSG.value, new SendPicMsgWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.ADDFRIEND.value, new AddFriendWorker());//加好友/群
        mapCmdBuilder.put(ApiEnum.CmdType.DELFRI.value, new DeleteFriendWorker());//删好友
        mapCmdBuilder.put(ApiEnum.CmdType.CHANGESTATU.value, new ChangeStatuWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.CONFIRADD.value, new ConfirmAddFriendWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.BESQU.value, new BeQuitWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.UPDATESELFNOTIFYFRI.value, new UpdateSelfNotifyWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.INVITEGROMEMBER.value, new InviteGroupMemberWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.AGRADMIN.value, new AgreeJoinGroupWorker());
        mapCmdBuilder.put(ApiEnum.CmdType.NEWGROMEM.value, new NewGroupMemberWorker());

        mapAnswerCmdBuilder = new SparseArray<>();
        mapAnswerCmdBuilder.put(ApiEnum.CmdType.LOGIN.value, new LogresWorker());

    }

    @SuppressWarnings("unchecked")
    public void dealCmd(JSONObject jo) throws Exception{
        JSONObject data = jo.getJSONObject("data");
        Integer cmdType = data.getInt("mtype");
        ICmdWorker cb = null;
        if (cmdType == ApiEnum.CmdType.ANSWER.value) {
            int ct = data.optInt("omtype");
            cb = mapAnswerCmdBuilder.get(ct);
        }
        if (cb == null) {
            cb = mapCmdBuilder.get(cmdType);
            if (cb == null) {
                return;
//                throw new DfpException("暂不支持此类型的指令（" + cmdType + "）！");
            }
        }
        BaseCmd cmd = cb.newCmdInstance();
        cmd.decode(jo);
        if (cmdType == ApiEnum.CmdType.ANSWER.value) {//响应指令
            SocketManager.getInstance().sendFinish(((BaseAnswerCmd)cmd).getOrgMsgId());
        }
        cb.dealCmd(cmd);
    }

    /**
     * 解析并处理指令
     * @param msg 指令文本
     * @throws Exception
     */

    public void dealCmd(String msg) throws Exception{
        dealCmd(new JSONObject(msg));
    }

    /**
     * 构建发送的指令
     * @param cmd
     * @return
     */
    public String encode(BaseCmd cmd) {
        Map<String, Object> data = cmd.getData();
        String s = cmd.toString();
        String sc = EncryptUtils.getSecurityCode(s);
        Map<String, Object> str = new HashMap<>();
        str.put("sc", sc);
        str.put("data", data);
        return UtilJson.toJson(str);
    }
}
