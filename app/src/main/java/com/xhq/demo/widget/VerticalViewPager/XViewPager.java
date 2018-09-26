package com.xhq.demo.widget.VerticalViewPager;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Author: YuJunKui
 * Time:2017/11/7 10:49
 * Tips:  下拉更多
 */

public class XViewPager extends VerticalViewPager {

    public static final byte LOAD_MORE_VALVE_VALUE = 0;

    private int valveValue;
    private boolean loadMoreEnabled=true;
    private PagerAdapter adapter;

    public onLoadMoreListener onLoadMoreListener;
    private boolean isLoadMerging = false;

    public XViewPager(Context context) {
        this(context, null);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        valveValue = LOAD_MORE_VALVE_VALUE;

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (loadMoreEnabled && adapter.getCount() - valveValue - 1 <= position && !isLoadMerging) {
                    //只要当前大于阀门值则回调
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoadMerging = true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 设置上啦获取更多阀门值
     *
     * @param valveValue 默认为0即为 下拉到底才出发loadmore事件
     *                   如果需要从生下10个view的时候加载 就设置为10
     */
    public void setLoadMoreValveValue(int valveValue) {
        this.valveValue = valveValue;
    }

    public void setLoadMoreEnabled(boolean enabled) {
        this.loadMoreEnabled = enabled;
    }

    public void setOnLoadMoreListener(XViewPager.onLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loadMoreComplete() {
        isLoadMerging = false;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    public interface onLoadMoreListener {
        void onLoadMore();
    }

}
