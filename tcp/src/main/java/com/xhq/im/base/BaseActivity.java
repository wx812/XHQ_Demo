package com.xhq.im.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.lzy.okgo.OkGo;
import com.xhq.common.application.HomeApp;
import com.xhq.im.cmd.IEventHandler;
import com.xhq.im.cmd.ImCmdEvent;
import com.xhq.im.cmd.ImEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity{
    protected SparseArray<IEventHandler> mapEventListener = new SparseArray<>();

//    private ProgressDialog mProgressDialog;

    //拍照后相片的Uri
    public String imageUrl;

    // time of last click
    private long lastClickTime;

    //switch of limit for click
    private boolean clickLimit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initEventListener();
        EventBus.getDefault().register(this);

        HomeApp.getInstance().add(this);//将这个Activity添加到activity栈中
        HomeApp.setCurrentActivity(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(isFastDoubleClick()){
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * Determine whether rapid double-click;
     *
     * @return false, true
     */
    private boolean isFastDoubleClick(){
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(timeD > 300) lastClickTime = time;
        if(!clickLimit) return timeD < 0;
        return timeD <= 300;
    }


    protected void setClickLimit(boolean clickLimit){
        this.clickLimit = clickLimit;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("imageUrl", imageUrl);
//        Logger.t("xhq").e("outState.... + " + outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = savedInstanceState.getString("imageUrl");
        }
//        Logger.t("xhq").e("outState.... + " + savedInstanceState);
    }


    /**
     * 初始化设置监听事件
     */
    protected void initEventListener(){
        addListener(ImEnum.Consts.EVENT_TYPE_NET_STATU, event -> onNetChange(event.getStatu()));
    }


    /**
     * 添加监听
     *
     * @param eventType
     * @param handler
     */
    protected void addListener(int eventType, IEventHandler handler){
        mapEventListener.put(eventType, handler);
    }


    public void onNetChange(int netMobile){

    }


    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }


    //事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCmdEvent(final ImCmdEvent event){
        IEventHandler handler = mapEventListener.get(event.getEventType());
        if(handler != null){
            handler.work(event);
        }
    }


    protected void onResume(){
        super.onResume();
        HomeApp.getInstance().add(this);//将这个Activity添加到activity栈中
        HomeApp.setCurrentActivity(this);
    }


    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        OkGo.getInstance().cancelTag(this);
    }


    @Override
    protected void onDestroy(){
        EventBus.getDefault().unregister(this);
        HomeApp.getInstance().finishActivity(this);
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }

}
