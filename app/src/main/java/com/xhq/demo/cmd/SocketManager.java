package com.xhq.demo.cmd;

import android.annotation.SuppressLint;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.xhq.demo.HomeApp;
import com.xhq.demo.cmd.impl.LoginCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.http.SocketClient;
import com.xhq.demo.tools.NumberUtil;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.tools.appTools.DeviceUtils;
import com.xhq.demo.tools.dateTimeTools.DateTimeUtils;
import com.xhq.demo.tools.netTools.NetUtil;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//import com.vilyever.socketclient.helper.SocketClientAddress;
//import com.vilyever.socketclient.helper.SocketClientDelegate;
//import com.vilyever.socketclient.helper.SocketPacket;
//import com.vilyever.socketclient.helper.SocketPacketHelper;
//import com.vilyever.socketclient.helper.SocketResponsePacket;
//import com.vilyever.socketclient.util.CharsetUtil;


/**
 * Created by zhenggm on 2017/4/9.
 * socket通讯管理类
 */
public class SocketManager {
    public final static int INT_DEF = -1; // int初始

    private static SocketManager instance;

    static {
        instance = new SocketManager();
    }

    public static SocketManager getInstance() {
        return instance;
    }

    private SocketManager() {
    }

    //socket客户端
    private SocketClient socketClient;
    private boolean isAutoConnected = false;//是否自动重连
    private boolean isForceDisconnected = false;//是否主动强制退出
    private boolean isLogon = false;//是否登录过，没登录过的，自动重连后不自动登录
    private Timer timerReconnect = null;//自动重连定时器
    private Timer timerMsg = null;//检测发送状态定时器，超过一定时间，认为发送失败
    private int loginStatus = INT_DEF;//登录状态
    //发送的指令队列
    private volatile Map<String, BaseCmd> mapCmdQueue = new HashMap<>();
    /**
     * 网络类型
     */
    private int netMobile;

    //初始化
    public synchronized void init(Context context) {
        this.netMobile = NetUtil.getActiveNetworkType();
        if (socketClient != null) return;
        isAutoConnected = false;
        isForceDisconnected = false;


        //建立socket
//        socketClient = new SocketClient(new SocketClientAddress(ApiUrl.Socket_Ip, 10448, 10000));
//        //读写以换行符结尾
//        socketClient.getSocketPacketHelper().setSendTrailerData(ApiUrl.MSG_TAILER_CHAR);
//        socketClient.getSocketPacketHelper().setReceiveTrailerData(ApiUrl.MSG_TAILER_CHAR);
//        socketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadToTrailer);
//
//        socketClient.setCharsetName(CharsetUtil.UTF_8);           // 设置发送和接收String消息的默认编码
//        socketClient.getSocketPacketHelper().setSendTimeout(30 * 1000); // 设置发送超时时长，单位毫秒
//        socketClient.getSocketPacketHelper().setSendTimeoutEnabled(true); // 设置允许使用发送超时时长，此值默认为false
////120*1000
//        socketClient.getSocketPacketHelper().setReceiveTimeout(10 * 1000); // 设置接收超时时长，单位毫秒。时间范围内无消息达到，自动断开
//        socketClient.getSocketPacketHelper().setReceiveTimeoutEnabled(true); // 设置允许使用接收超时时长，此值默认为false
//
//        String send_str = CmdFactory.getInstance().encode(new PingCmd());
//        socketClient.getHeartBeatHelper().setDefaultSendData(send_str.getBytes());// 设置自动发送心跳包的字符串，若为null则不发送心跳包
//
//        //事件监听
//        socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
//            @Override
//            public void onConnected(SocketClient client) {
//                doConnected();
//            }
//
//            @Override
//            public void onDisconnected(SocketClient client) {
//                doDisconnect();
//            }
//
//            @Override
//            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
//                doReceiveMsg(responsePacket.getMessage());
//            }
//        });
        reconnect();//立即连接
    }

    //关闭socket
    public void close() {
        isForceDisconnected = true;
        closeReconnectTimer();
        timerMsg.cancel();
        timerMsg = null;
//        socketClient.disconnect();
    }

    /**
     * 断线重连
     */
    public void reconnect() {
        if (socketClient == null) return;
        if (timerReconnect != null) {
            timerReconnect.cancel();
            timerReconnect = null;
        }//重连中
        if (!NetUtil.isConnected()) return;//本身没有网络，不用重连

        isAutoConnected = true;
        isForceDisconnected = false;
        socketClient.connect();

        timerReconnect = new Timer();
//        timerReconnect.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (socketClient.isConnected()) return;
//                socketClient.connect();
//            }
//        }, 100, 1000 * 2);//0.1s后开始执行，2s重连一次
    }

    //socket连接后要干的活
    private void doConnected() {
        closeReconnectTimer();
        //自动重连，则需要调用登录，非自动重连，触发事件
        //在登陆页面，就不自动登陆
        if (HomeApp.getCurrentActivity() != null) {

            String className = HomeApp.getCurrentActivity().getComponentName().getClassName();
//            if (className.equals(LoginActivity.class.getName())
//                    || className.equals(RegisterActivity.class.getName())
//                    || className.equals(FixPasswordActivity.class.getName())
//                    || className.equals(InitializationActivity.class.getName())
//                    ) {
//                return;
//            }
        }

        if (isAutoConnected && isLogon) {
            login(HomeApp.getAppContext());
        }

        ImEventFactory.getInstance().postNetStatuEvent(ApiEnum.Consts.STATU_OK);
    }

    /**
     * 关闭自动重连时钟
     */
    private void closeReconnectTimer() {
        if (timerReconnect != null) {
            timerReconnect.cancel();
            timerReconnect = null;
        }
    }

    //socket断开
    private void doDisconnect() {
        loginStatus = ApiEnum.Consts.STATU_ERROR;

        if (!isForceDisconnected) {//如果非主动断开则启动自动重连
            reconnect();//
            ImEventFactory.getInstance().postSocketStatuEvent(ApiEnum.Consts.STATU_ERROR);
        }
    }


    //接收到消息的处理，已经通过lopper处理了，不用再搞一次了
    private void doReceiveMsg(String msg) {
        try {
//            LogUtil.e("msg = "+msg);
            Logger.d("socket:receive---- " + msg);
            CmdFactory.getInstance().dealCmd(msg);
        } catch (Exception e) {
//            LogUtil.e("指令处理错误", e);
        }
    }

    /**
     * 登录
     */
    public void login(Context context) {
        String user_account = SPUtils.get(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, "");
        String pwd = SPUtils.get(SPKey.USER_CONFIG, SPKey.PWD, "");
        login(context, user_account, pwd);
    }

    @SuppressLint("MissingPermission")
    public void login(Context context, String account, String pwd) {
        if (!SocketManager.getInstance().isConnected()) return;
        if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(pwd)) {
//            LogUtil.e("account or pwd is null ");
            return;
        }
        SPUtils.put(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, account);
        SPUtils.put(SPKey.USER_CONFIG, SPKey.PWD, pwd);

        LoginCmd cmd = new LoginCmd();
        cmd.setMsgType(ApiEnum.CmdType.LOGIN.value);
        cmd.setUserCode(encrypt(account));
        cmd.setPassword(encrypt(pwd));
        cmd.setUs(ApiEnum.UserStatuEnum.ONLINE.value);
        cmd.setClientType(ApiEnum.Consts.CLIENT_TYPE_APP);
        cmd.setClientInfo("android-" + DeviceUtils.getDeviceName());
        cmd.setImei(DeviceUtils.getDeviceId());
        sendCmd(cmd);
    }


    private static String encrypt(String txt) {
        if (txt == null || txt.length() == 0) return "";
        StringBuilder src = new StringBuilder(64);
        int i, l = txt.length();
        String s;
        s = Integer.toString(l + 10, 36);
        src.append(Integer.toString(s.length() + 10, 21)).append(s);
        for (i = 0; i < l; i++) {
            s = Integer.toString(txt.charAt(i) + (i + 1) * 10 + l, 36);
            src.append(Integer.toString(s.length() + 10, 21)).append(s);
        }
        return src.toString();
    }


    public void heart(Context context) {
        if (!SocketManager.getInstance().isConnected()) return;

        Logger.d("sk:" + SPUtils.get(SPKey.USER_CONFIG, ApiKey.CommonUrlKey.sk, ""));
        LoginCmd cmd = new LoginCmd();
        cmd.setMsgType(ApiEnum.CmdType.PING.value);
        cmd.setMsgId(String.valueOf(Math.random()));
        sendCmd(cmd);
    }

    /**
     * 发送指令
     */
    public boolean sendCmd(BaseCmd cmd) {
//        if (socketClient.isConnected()) {
//            mapCmdQueue.put(cmd.getMsgId(), cmd);
//            startMsgMonitor();
//            String msg = CmdFactory.getInstance().encode(cmd);
//            Logger.d("socket:send---- " + msg);
//            SocketPacket sp = socketClient.sendString(msg);
//            return sp != null;
//        }
        return false;
    }

    //发送完成，已收到应答
    public void sendFinish(String msgId) {
        mapCmdQueue.remove(msgId);
    }

    //开启监听发送的消息，超时(5s)触发事件
    private void startMsgMonitor() {
        if (timerMsg != null) return;
        timerMsg = new Timer();
        timerMsg.schedule(new TimerTask() {
            @Override
            public void run() {
                Iterator<String> set = mapCmdQueue.keySet().iterator();
                long curTime = NumberUtil.toLong(DateTimeUtils.getCurrDateTime_yMdHms());
                for (; set.hasNext(); ) {
                    String id = set.next();
                    BaseCmd cmd = mapCmdQueue.get(id);
                    if (cmd == null) continue;
                    if (curTime - NumberUtil.toLong(cmd.getTime()) >= 5L) {
                        set.remove();//使用iterator的remove避免出现ConcurrentModificationException异常
//                        mapCmdQueue.remove(id);
                        BaseAnswerCmd bac = new BaseAnswerCmd(cmd);
                        bac.setStatu(ApiEnum.Consts.STATU_ERROR);
                        bac.setInfo("指令超时未应答");
                        ImEventFactory.getInstance().postAnswerMsg(ApiEnum.Consts.STATU_ERROR, null, bac);
                    }
                }
            }
        }, 5, 1000 * 5);//5s后开始执行，5s重连一次
    }

    public boolean isLogin() {
        return loginStatus == ApiEnum.Consts.STATU_OK;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
        if (isLogin()) {
            isLogon = true;
        }
    }

    /**
     * socket是否连接
     */
    public boolean isConnected() {
//        return socketClient != null && socketClient.isConnected();
        return false;
    }

    /**
     * 网络情况变化
     */
    public void setNetChange(int mobileNet) {
        this.netMobile = mobileNet;
    }
}
