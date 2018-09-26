package com.xhq.im.tcp.custom;

import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xhq.common.application.HomeApp;
import com.xhq.im.cmd.CmdFactory;
import com.xhq.im.cmd.IEventHandler;
import com.xhq.im.cmd.ImCmdEvent;
import com.xhq.im.cmd.ImEnum;
import com.xhq.im.tcp.listener.TCPListener;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.xhq.im.tcp.custom.TcpServiceSender.TAIL;


/**
 * Created by hskun on 2017/9/8.
 * 说明专门处理接受消息的类，TCP和Http必定是不一样的接受消息和发送消息不可能是同一个地方来处理
 */

public class TcpClientReceiver implements TCPListener {
    private SparseArray<IEventHandler> mapEventListener = new SparseArray<>();
    private static TcpClientReceiver receiver;
    private String s = "";
    //send异常
    private static final String BROKEN_PIPE = "Broken pipe";
    private static final String SOCKET_IS_CLOSED = "Socket is closed";//向服务器端写操作抛出的异常
    //receive异常
    private static final String SOCKET_CLOSED = "Socket closed";//从服务器端读操作抛出的异常
    private static final String SOFTWARE_CAUSED_CONNECTION_ABORT = "Software caused connection abort";//读取线程在关掉网络后抛出的异常
    //connect异常
    private static final String NETWORK_IS_UNREACHABLE = "Network is unreachable";//failed to connect to /123.56.13.121 (port 10448) from /:: (port 0): connect failed: ENETUNREACH (Network is unreachable)
    private static final String CONNECTION_REFUSED = "Connection refused";//failed to connect to /123.56.13.121 (port 1044) from /:: (port 41215): connect failed: ECONNREFUSED (Connection refused)

    private TcpClientReceiver() {
    }

    synchronized public static TcpClientReceiver getInstance() {
        if (receiver == null) {
            receiver = new TcpClientReceiver();
            EventBus.getDefault().register(receiver);
            receiver.addListener(ImEnum.Consts.EVENT_TYPE_HEART_ANSWER, new IEventHandler() {
                @Override
                public void work(ImCmdEvent event) {
                    int msgType = event.getCmd().getMsgType();
                    if (msgType == ImEnum.CmdType.PING.value) {
                        TcpServiceSender.continue_ = false;
                    }
                }
            });
        }
        return receiver;
    }

    //事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCmdEvent(final ImCmdEvent event) {
        IEventHandler handler = mapEventListener.get(event.getEventType());
        if (handler != null) {
            handler.work(event);
        }
    }

    /**
     * 添加监听
     *
     * @param eventType
     * @param handler
     */
    protected void addListener(int eventType, IEventHandler handler) {
        mapEventListener.put(eventType, handler);
    }

    @Override
    public void onSend(byte[] bys) {

    }

    @Override
    public void onPart(byte[] bys, int len) throws Exception {
        s += new String(bys, 0, len);
        String replace = StringEscapeUtils.escapeJava(TAIL);
        Pattern pattern = Pattern.compile("(.*?)" + replace + "$");
        Matcher matcher = pattern.matcher(s);
        ArrayList<String> strings = new ArrayList<>();
        while (matcher.find()) {
            strings.add(matcher.group());
        }
        for (int i = 0; i < strings.size(); i++) {
            String msg = strings.get(i);
            Logger.d("socket:receive---- " + "time" + System.currentTimeMillis() + msg);
            CmdFactory.getInstance().dealCmd(msg);
        }
        s = "";
    }

    @Override
    public void onInterrupted(InterruptedException e) {
        e.printStackTrace();
    }

    @Override
    public void onConnectIOException(IOException e) {
        Logger.e(e, "链接中抛出异常，一般没有网络，或者服务器没有开机引起的");
        String message = e.getMessage();
        if (!TextUtils.isEmpty(message)) {
            if (message.contains(CONNECTION_REFUSED)) {//一般是服务器没开启
                Observable.just("服务器维护中……")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> Toast.makeText(HomeApp.getAppContext(), s, Toast.LENGTH_SHORT).show());
            } else if (message.contains(NETWORK_IS_UNREACHABLE)) {
                Observable.just("无网络，请检查网络")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> Toast.makeText(HomeApp.getAppContext(), s, Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    public void onReceiveIOException(Exception e) {
        Logger.e(e, "接受数据中抛出异常，一般这个时候需要主动断开链接并提示用户");
        String message = e.getMessage();
        if (!TextUtils.isEmpty(message)) {
            if (message.contains(SOCKET_CLOSED)) {

            } else if (message.contains(SOFTWARE_CAUSED_CONNECTION_ABORT)) {
                HomeApp.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeApp.getCurrentActivity(), "无网络，请检查网络", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        Toast.makeText(HomeApp.getCurrentActivity(), message, Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    @Override
    public void onSendIOException(IOException e) {
        //只要是发送消息抛异常都应该重发消息
        Logger.e(e, "发送数据的时候抛出的异常");
        String message = e.getMessage();
        if (!TextUtils.isEmpty(message)) {
            if (message.contains(SOCKET_IS_CLOSED)) {

            } else if (message.contains(BROKEN_PIPE)) {
                TcpServiceSender.startActionReconnect();
            }
        }
    }

/*    @Override
    public void onHeart(final byte[] bys) {
        TcpServiceSender.startMsgMonitor(bys);
    }*/

}
