package com.xhq.commonbase.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.HintView;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyuanhao on 2016/7/16.
 */
public abstract class PagerCommonAdapters<T> extends PagerAdapter{
    private RollPagerView mViewPager;
    protected Context mContext;
    protected List<T> mData;
    private ArrayList<View> mViewList = new ArrayList<>();

    private class LoopHintViewDelegate implements RollPagerView.HintViewDelegate{
        @Override
        public void setCurrentPosition(int position, HintView hintView) {
            if (hintView!=null)
                hintView.setCurrent(position%getRealCount());
        }

        @Override
        public void initView(int length, int gravity, HintView hintView) {
            if (hintView!=null)
                hintView.initView(getRealCount(),gravity);
        }
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mViewList.clear();

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        if (getCount() == 0)return;
        int half = Integer.MAX_VALUE/2;
        int start = half - half%getRealCount();
        mViewPager.getViewPager().setCurrentItem(start,false);
    }

    public PagerCommonAdapters(Context context,RollPagerView viewPager,List<T> data){
        this.mViewPager = viewPager;
        this.mContext = context;
        this.mData = data;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container,int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,mData.get(position),position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container,T data, int position);

    @Deprecated
    @Override
    public final int getCount() {
        return getRealCount()<=1?getRealCount():Integer.MAX_VALUE;
    }

    protected abstract int getRealCount();
}
