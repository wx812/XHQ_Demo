package com.xhq.demo.cmd.woker;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.xhq.demo.HomeApp;
import com.xhq.demo.R;
import com.xhq.demo.cmd.BaseCmdWorker;
import com.xhq.demo.cmd.SocketManager;
import com.xhq.demo.cmd.impl.BesQuitCmd;


/**
 * Created by zyj on 2017/4/26.
 * 登陆被挤下线
 */

public class BeQuitWorker extends BaseCmdWorker<BesQuitCmd>{
    private static boolean isShowing;

    @Override
    public BesQuitCmd newCmdInstance() {
        return new BesQuitCmd();
    }

    @Override
    protected void localDealCmd(BesQuitCmd cmd){
        if (!isShowing) {
            showLoginSqueezeDialog();
        }

//        ImEventFactory.getInstance().postBeQuitMsg(ApiEnum.Consts.STATU_OK, null, cmd);
    }

    private void showLoginSqueezeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeApp.getCurrentActivity());

        AlertDialog obdDialog = builder.setMessage("您的账号在其他设备上登录，如非本人操作，请及时修改账号密码")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("掉线通知")
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShowing = false;
//                        Intent intent = new Intent(HomeApp.getCurrentActivity(), LoginActivity.class);
//                        HomeApp.getCurrentActivity().startActivity(intent);
                    }
                })
                .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShowing = false;
                        SocketManager.getInstance().login(HomeApp.getAppContext());
                    }
                }).create();

        obdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //禁用返回按键  默认返回 false
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        if (!obdDialog.isShowing())
            obdDialog.show();
    }
}
