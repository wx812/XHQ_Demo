package com.xhq.demo.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.xhq.demo.HomeApp;
import com.xhq.demo.cmd.IEventHandler;
import com.xhq.demo.cmd.ImCmdEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



/**
 * Created by xyy on 2016/8/30 0030.
 */
public abstract class BaseFragment extends Fragment {
    private TextView title_text;
    private static final String ARG_SECTION_NUMBER = "section_number";
//    private Unbinder unbinder;

    //设置LayoutId
    private int layoutId = 0;
    private View view;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 网络类型
     */
    private int netMobile;
    protected SparseArray<IEventHandler> mapEventListener = new SparseArray<>();


//    protected HomeActivity getHomeActivity() {
//        return (HomeActivity) getActivity();
//    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    private void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    private void onInvisible() {
        viewClose();
    }


    /**
     * 切换界面
     * 子类必须重写此方法
     */
    protected abstract void viewClose();

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();


    /**
     * 初始化布局：首行需要setContentView()
     */
    protected void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }


    /**
     * 初始化标题
     */
    protected void initTitle(int title) {
        title_text.setText(title);
    }


    protected void initTitle(String title) {
        title_text.setText(title);
    }


    protected Context getActivityContext() {
        if (getActivity() == null) {
            return HomeApp.getAppContext();
        }
        return getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layoutId, null);
//        title_text = (TextView) view.findViewById(R.id.txt_title);
//        unbinder = ButterKnife.bind(this, view);//初始化View
        initView();

        initEventListener();
        EventBus.getDefault().register(this);
        return view;
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
//		addListener(ImEnum.Consts.EVENT_TYPE_NET_STATU, new IEventHandler() {
//			@Override
//			public void work(ImCmdEvent event) {
//				onNetChange(event.getStatu());
//			}
//		});
    }


    protected View getUIView() {
        return view;
    }


    protected void initView() {

    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
//        if (unbinder != null) {
//            unbinder.unbind();
//        }
        if (OkGo.getInstance() != null) {
            OkGo.getInstance().cancelAll();
        }
        super.onDestroyView();
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
     * 更新view
     */
    public void updateView() {

    }


    public static BaseFragment newInstance(int sectionNumber) {
        BaseFragment fragment = getDestFragment(sectionNumber);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        if (fragment != null) {
            fragment.setArguments(args);
        }

        return fragment;
    }


    private static BaseFragment getDestFragment(int sectionNumber) {
        switch (sectionNumber) {
//            case 1:
//                return new MessageFragment();
//            case 2:
//                return new BookFragment();
//            case 3:
//                return new HomeFragment();
//            case 4:
//                return new FindFragment();
//            case 5:
//                return new OwnFragment();
        }
        return null;
    }
}
