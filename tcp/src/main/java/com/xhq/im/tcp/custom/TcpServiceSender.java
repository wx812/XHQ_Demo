package com.xhq.im.tcp.custom;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.xhq.common.application.HomeApp;
import com.xhq.common.constant.apiconfig.ApiUrl;
import com.xhq.common.tool.DeviceUtils;
import com.xhq.common.tool.Utils;
import com.xhq.common.tool.spTool.SPKey;
import com.xhq.common.tool.spTool.SPUtils;
import com.xhq.im.BuildConfig;
import com.xhq.im.cmd.BaseCmd;
import com.xhq.im.cmd.CmdFactory;
import com.xhq.im.cmd.ImEnum;
import com.xhq.im.cmd.PKGenerator;
import com.xhq.im.cmd.bean.LoginCmd;
import com.xhq.im.cmd.bean.LogoutCmd;
import com.xhq.im.tcp.client.TCPClientThreadManager;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 此类用于处理Tcp请求,此类与
 */
public class TcpServiceSender extends IntentService {
    public static final Charset CHARSET = Charset.forName("UTF-8");                         //UTF-8
    //   public static final Charset CHARSET = StandardCharsets.ISO_8859_1;                 //ISO_8859_1
    //   public static final Charset CHARSET = Charset.forName("GBK");                      //GBK
    public static final String TAIL = "\n";                                                 //每条消息的尾部，发送和接受都有UTF-8格式的
    public static final long INITIAL_DELAY = 60;                                          //第一次延时执行
    public static final long TIME_INTERVAL = 3 * 60;                                      //心跳包时间间隔
    //public static final long INITIAL_DELAY = 2;                                            //测试用
    //public static final long TIME_INTERVAL = 2;                                            //测试用
    // 所有的action每一个action对应一个操作,作为判断标志
    //链接服务器并创建接受线程的动作
    private static final String ACTION_CONNECT_RECEIVE = BuildConfig.APPLICATION_ID + ".action.CONNECT_RECEIVE";
    private static final String ACTION_LOGIN = BuildConfig.APPLICATION_ID + ".action.LOGIN";
    private static final String ACTION_HEART = BuildConfig.APPLICATION_ID + ".action.HEART";
    private static final String ACTION_LOGOUT = BuildConfig.APPLICATION_ID + ".action.LOGOUT";

    // 这里我们只写参数1 参数2 参数3 不需要写太多的参数
    private static final String EXTRA_PARAM1 = BuildConfig.APPLICATION_ID + ".extra.PARAM1";
    private static final String EXTRA_PARAM2 = BuildConfig.APPLICATION_ID + ".extra.PARAM2";

    private static final Timer TIMER = new Timer();
    static boolean continue_ = true;
    private static ArrayList<TimerTask> timerTasks = new ArrayList<>();

    public TcpServiceSender() {
        super("TcpService");
    }

    /* ***********************************************************封装的TCP接口方法，直接调用就可以在service的handle线程中直接调用***********************************************************/

    /**
     * 重连（断开链接，停止心跳，重新开启socket，重新开启接受请求开始接受，登陆，心跳）
     */
    public static void startActionReconnect() {
        TCPClientThreadManager.getInstance().stopHeart().disconnect();
        String user_account = SPUtils.get(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, "");
        String user_pwd = SPUtils.get(SPKey.USER_CONFIG, SPKey.PWD, "");
        TcpServiceSender.startActionConnectAndReceive(HomeApp.getAppContext());
        TcpServiceSender.startActionLogin(HomeApp.getAppContext(), user_account, user_pwd);
        TcpServiceSender.startActionHeart(HomeApp.getAppContext());
    }

    /**
     * 开启接受请求开始接受，登陆，心跳）
     */
    public static void startActionConnect() {
        String user_account = SPUtils.get(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, "");
        String user_pwd = SPUtils.get(SPKey.USER_CONFIG, SPKey.PWD, "");
        TcpServiceSender.startActionConnectAndReceive(HomeApp.getAppContext());
        TcpServiceSender.startActionLogin(HomeApp.getAppContext(), user_account, user_pwd);
        TcpServiceSender.startActionHeart(HomeApp.getAppContext());
    }

    /**
     * 开始tcp链接，链接后创建接受数据线程，所有的方法都应该调用此方法
     */
    public static void startActionConnectAndReceive(Context context) {
        Intent intent = new Intent(context, TcpServiceSender.class);
        intent.setAction(ACTION_CONNECT_RECEIVE);
        context.startService(intent);
    }

    /**
     * 登陆
     */
    public static void startActionLogin(Context context, String account, String pwd) {
        //没有链接先链接
        if (!TCPClientThreadManager.getInstance().isConnect()) {
            startActionConnectAndReceive(context);
        }
        Intent intent = new Intent(context, TcpServiceSender.class);
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(EXTRA_PARAM1, account);
        intent.putExtra(EXTRA_PARAM2, pwd);
        context.startService(intent);
    }


    /**
     * 心跳   没有登陆之前不能有心跳，掉线之后停止心跳
     */
    public static void startActionHeart(Context context) {
        //没有链接先链接
        Intent intent = new Intent(context, TcpServiceSender.class);
        intent.setAction(ACTION_HEART);
        context.startService(intent);
    }

    /**
     * 退出
     */
    public static void startActionLogout(Context context) {
        Intent intent = new Intent(context, TcpServiceSender.class);
        intent.setAction(ACTION_LOGOUT);
        context.startService(intent);
    }

    /* ************************************************************具体执行***********************************************************/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT_RECEIVE.equals(action)) {
                handleActionConnectReceive();
            } else if (ACTION_LOGIN.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLogin(param1, param2);
            } else if (ACTION_HEART.equals(action)) {
                handleActionHeart();
            } else if (ACTION_LOGOUT.equals(action)) {
                handleActionLogout();
            }
        }
    }


    /**
     * service执行链接并且开启接受线程
     */
    private void handleActionConnectReceive() {
        TCPClientThreadManager.getInstance().setListener(TcpClientReceiver.getInstance());
        TCPClientThreadManager.getInstance().connectAndReceive(ApiUrl.SOCKET_IP, ApiUrl.socketPort, TcpClientReceiver.getInstance());
    }

    /**
     * service执行登陆
     */
    private void handleActionLogin(String account, String pwd) {
        LoginCmd cmd = new LoginCmd();
        cmd.setMsgType(ImEnum.CmdType.LOGIN.value);
        cmd.setUserCode(Utils.encrypt(account));
        cmd.setPassword(Utils.encrypt(pwd));
        cmd.setUs(ImEnum.UserStatuEnum.ONLINE.value);
        cmd.setClientType(ImEnum.Consts.CLIENT_TYPE_APP);
        cmd.setClientInfo("android-" + DeviceUtils.getDeviceName());
        cmd.setImei(DeviceUtils.getDeviceId());
        sendCmd(cmd);
    }

    /**
     * 执行心跳
     */
    public void handleActionHeart() {
        LoginCmd cmd = new LoginCmd();
        cmd.setMsgType(ImEnum.CmdType.PING.value);
        cmd.setMsgId(String.valueOf(Math.random()));
        heartCmd(cmd);
    }

    /**
     * 执行登出
     */
    private void handleActionLogout() {
        LogoutCmd cmd = new LogoutCmd();
        cmd.setMsgType(ImEnum.CmdType.LOGOUT.value);
        cmd.setMsgId(PKGenerator.newId());
        cmd.setTime(Utils.getCurTime());
        SPUtils.put(SPKey.USER_CONFIG, SPKey.AUTO_LOGIN, 0);
        sendCmd(cmd);
    }

    /* **********************************************************************具体的发送封装方法****************************************************************************/

    /**
     * 发送指令
     *
     * @param cmd base指令
     */
    public static void sendCmd(BaseCmd cmd) {
        //  mapCmdQueue.put(cmd.getMsgId(), cmd);
        //startMsgMonitor();
        String msg = CmdFactory.getInstance().encode(cmd);
        send(msg);
    }

    /**
     * 发送消息
     *
     * @param data String类型的数据，默认编码格式UTF-8可以在上面改
     */
    private static void send(String data) {
        String s = data + TAIL;
        byte[] bytes = s.getBytes(CHARSET);
        send(bytes);
    }

    /**
     * 发送byte数组类型的数据
     *
     * @param bytes 数据源
     */
    private static void send(byte[] bytes) {
        TCPClientThreadManager.getInstance().send(bytes);
    }

    /**
     * 发送心跳指令
     *
     * @param cmd base指令
     */
    public static void heartCmd(BaseCmd cmd) {
        String msg = CmdFactory.getInstance().encode(cmd);
        heart(msg);
    }

    /**
     * 发送心跳包
     *
     * @param data String类型的数据
     */
    private static void heart(String data) {
        String s = data + TAIL;
        byte[] bytes = s.getBytes(CHARSET);
        TCPClientThreadManager.getInstance().heart(bytes, INITIAL_DELAY, TIME_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //建议不要在这里写任何东西，这里不是app被干掉的标志
        //com.orhanobut.logger.Logger.d("我被干掉了"); 事实证明service不是单例的，每次执行完所有在队列里面的任务后会销毁，然后重新开启新的service
    }

    //开启监听发送的消息，超时(1s)触发事件
    public static void startMsgMonitor(final byte[] bys) {
        TimerTask currentTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (continue_) {
                    Logger.d("我被调用了");
                    send(bys);
                } else {
                    cancel();
                    timerTasks.remove(this);
                    continue_ = true;
                }
            }
        };
        timerTasks.add(currentTimerTask);
        TIMER.schedule(currentTimerTask, 1000, 1000);//一秒后执行周期为1秒
    }

    public static void stopMsgMonitor() {
        for (TimerTask timerTask : timerTasks) {
            timerTask.cancel();
        }
        timerTasks.clear();
    }
}
