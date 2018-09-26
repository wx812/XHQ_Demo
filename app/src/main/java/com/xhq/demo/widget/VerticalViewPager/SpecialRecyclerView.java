package com.xhq.demo.widget.VerticalViewPager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.xhq.demo.tools.uiTools.RecyclerViewUtils;

/**
 * Author: YuJunKui
 * Time:2018/2/10 18:03
 * Tips:  特殊的像竖向的viewpage的RecyclerView
 */
public class SpecialRecyclerView extends RecyclerView{

    private int lastPosition = -1;//上一个position
    private boolean isLoadingData = false;//是否正在加载更多
    private boolean loadingMoreEnabled = true;//是否开启加载更多

    private XViewPager.onLoadMoreListener onLoadMoreListener;
    public static final byte LOAD_MORE_VALVE_VALUE = 5;

    private static final String TAG = "SpecialRecyclerView";

    public SpecialRecyclerView(Context context) {
        this(context, null);
    }

    public SpecialRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SpecialRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        new PagerSnapHelper().attachToRecyclerView(this);
    }


    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
    }

    public void setOnLoadMoreListener(XViewPager.onLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loadMoreComplete() {
        isLoadingData = false;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        LayoutManager layoutManager = getLayoutManager();

        if (loadingMoreEnabled && !isLoadingData && onLoadMoreListener != null) {
            int lastVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            if (getLayoutManager().getChildCount() > 0
                    && lastVisibleItemPosition >= getLayoutManager().getItemCount() - LOAD_MORE_VALVE_VALUE
                    && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                isLoadingData = true;
                onLoadMoreListener.onLoadMore();
            }
        }

        //========================================用作监听当前那个页面显示=======================================
        /**
         * 此方法在滑动过程被调用
         * 正常为
         * state值依次为SCROLL_STATE_DRAGGING(1)->SCROLL_STATE_SETTLING(2)->SCROLL_STATE_IDLE(0)
         *            拖拽->停止->稳定
         * 故当SCROLL_STATE_IDLE(0)时检测当前显示最多的是那个view
         */

//        KLog.i(TAG, "onScrollStateChanged() called with: state = [" + state + "]");

        if (onPageChangeListener != null && state == SCROLL_STATE_IDLE) {

            int position = RecyclerViewUtils.getCurrentViewIndex((LinearLayoutManager) layoutManager);

            if (position != lastPosition) {
                //回调出去
                lastPosition = position;
                onPageChangeListener.onPageChange(position);
            }

        }

    }

    private OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageChange(int currentPosition);
    }

}
