package com.xhq.demo.base.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhq.demo.HomeApp;
import com.xhq.demo.cmd.IEventHandler;
import com.xhq.demo.cmd.ImCmdEvent;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.tools.StringUtils;
import com.xhq.demo.widget.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xyy on 2016/10/26.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


//    public <T extends View> T $(int id) {
//        return (T) super.findViewById(id);
//    }
//
//    public <T extends View> T $(View view, int id) {
//        return (T) view.findViewById(id);
//    }


    private Object object = new Object();
    private LoadDialog progressDialog;
    //设置LayoutId
    private int layoutId = 0;

    // titlebar 的view
    private LinearLayout btn_left, btn_right;
    private TextView title_text;

    //titlename 的长度限定
    private static final int LIMIT_LENGTH = 15;

    protected SparseArray<IEventHandler> mapEventListener = new SparseArray<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventListener();
        EventBus.getDefault().register(this);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (layoutId != 0)
            setContentView(layoutId);//设置布局ID
        initTopBar();

//        ButterKnife.bind(this);//初始化View
        HomeApp.getInstance().add(this);//将这个Activity添加到activity栈中
        HomeApp.setCurrentActivity(this);
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


    /**
     * 初始化设置监听事件
     */
    protected void initEventListener() {
        addListener(ApiEnum.Consts.EVENT_TYPE_NET_STATU, new IEventHandler() {
            @Override
            public void work(ImCmdEvent event) {
                onNetChange(event.getStatu());
            }
        });
    }


    public void onNetChange(int netMobile) {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    //事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCmdEvent(final ImCmdEvent event) {
        IEventHandler handler = mapEventListener.get(event.getEventType());
        if (handler != null) {
            handler.work(event);
        }
    }


    private void initTopBar() {
//        btn_left = (LinearLayout) findViewById(R.id.btn_left);
        if (btn_left != null) {
            btn_left.setOnClickListener(this);
        }
//        title_text = (TextView) findViewById(R.id.title_text);
//        btn_right = (LinearLayout) findViewById(R.id.btn_right);
        if (btn_right != null) {
            btn_right.setOnClickListener(this);
        }
    }


    protected void updateRight(int btn_edit) {
        if (btn_edit != 0) {
            (btn_right.getChildAt(0)).setVisibility(View.VISIBLE);
            ((TextView) btn_right.getChildAt(0)).setText(getString(btn_edit));
        }
    }


    protected void onResume() {
        super.onResume();
        HomeApp.getInstance().add(this);//将这个Activity添加到activity栈中
        HomeApp.setCurrentActivity(this);
    }


    protected void onPause() {
        super.onPause();
    }


    protected void updateTitle(String title) {
        if (!StringUtils.isNullOrEmpty(title)) {
            title_text.setText(title);
        }
    }


    /**
     * 初始化标题
     */
    protected void initTitle(int leftDrawable, int leftText, int title, int rightText, int rightDrawable) {
        if (leftText != 0) {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.getChildAt(0).setVisibility(View.GONE);//
            btn_left.getChildAt(1).setVisibility(View.VISIBLE);//
            ((TextView) btn_left.getChildAt(1)).setText(getResources().getString(leftText));
        }
        if (leftDrawable != 0) {
            btn_left.setVisibility(View.VISIBLE);
            ((ImageView) btn_left.getChildAt(0)).setImageResource(leftDrawable); //
            //			((TextView)btn_left.getChildAt(0)).setBackgroundResource(leftDrawable);
            (btn_left.getChildAt(1)).setVisibility(View.GONE);
            (btn_left.getChildAt(0)).setVisibility(View.VISIBLE);
//			((TextView) btn_left.getChildAt(1)).setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
        }
        if (title != 0) {
            title_text.setText(title);
        }
        if (rightText != 0) {
            btn_right.setVisibility(View.VISIBLE);
            (btn_right.getChildAt(0)).setVisibility(View.VISIBLE);
            ((TextView) btn_right.getChildAt(0)).setText(getResources().getString(rightText));
            (btn_right.getChildAt(1)).setVisibility(View.INVISIBLE);
        }
        if (rightDrawable != 0) {
            btn_right.setVisibility(View.VISIBLE);
            (btn_right.getChildAt(0)).setVisibility(View.INVISIBLE);
            (btn_right.getChildAt(1)).setVisibility(View.VISIBLE);
            ((ImageView) btn_right.getChildAt(1)).setImageResource(rightDrawable);
        }
    }


    protected void initTitle(int leftDrawable, int leftText, String title, int rightText, int rightDrawable) {
        if (leftText != 0) {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.getChildAt(0).setVisibility(View.GONE);//
            btn_left.getChildAt(1).setVisibility(View.VISIBLE);//
            ((TextView) btn_left.getChildAt(1)).setText(getResources().getString(leftText));
        }
        if (leftDrawable != 0) {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.getChildAt(0).setVisibility(View.VISIBLE);//
            btn_left.getChildAt(1).setVisibility(View.GONE);//
            ((ImageView) btn_left.getChildAt(0)).setImageResource(leftDrawable); //
//			((TextView) btn_left.getChildAt(1)).setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
        }
        if (!StringUtils.isNullOrEmpty(title)) {
            if (title.length() > LIMIT_LENGTH) {
                title_text.setFilters(new InputFilter[]{
                        new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start,
                                                       int end, Spanned dest, int dstart, int dend) {
                                return source.length() > LIMIT_LENGTH ?
                                        source.subSequence(0, LIMIT_LENGTH) + "..." : source;
                            }
                        }
                });
            }

            title_text.setText(title);
        }

        if (rightText != 0) {
            btn_right.setVisibility(View.VISIBLE);
            (btn_right.getChildAt(0)).setVisibility(View.VISIBLE);
            ((TextView) btn_right.getChildAt(0)).setText(getResources().getString(rightText));
            (btn_right.getChildAt(1)).setVisibility(View.INVISIBLE);
        }
        if (rightDrawable != 0) {
            btn_right.setVisibility(View.VISIBLE);
            (btn_right.getChildAt(0)).setVisibility(View.INVISIBLE);
            (btn_right.getChildAt(1)).setVisibility(View.VISIBLE);
            ((ImageView) btn_right.getChildAt(1)).setImageResource(rightDrawable);
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
//        ButterKnife.bind(this).unbind();
        HomeApp.getInstance().finishActivity(this);
        super.onDestroy();
    }


    /**
     * 初始化布局：首行需要setContentView()
     */
    protected void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_left:
////				onBackPressed();
//                finish();
//                break;
//            case R.id.btn_right:
//                rightClick();
//                break;
//            case R.id.title_text:
//                titleClick();
//                break;

            default:
                break;
        }
    }


    protected void titleClick() {
    }


    protected void rightClick() {
    }


    public void dismissLoading() {
        synchronized (object) {
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                    progressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void showLoading() {
        synchronized (object) {
            if (progressDialog == null) {
                progressDialog = new LoadDialog(this);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }
    }


    public void showLoading(String msg) {
        synchronized (object) {
            if (progressDialog == null) {
                progressDialog = new LoadDialog(this, msg);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

    }
}
