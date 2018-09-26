package com.xhq.commonbase.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by huyuanhao on 2016/7/16.
 */
public abstract class PagerCommonAdapter<T> extends PagerAdapter{

    protected List<T> mData;
    private SparseArray<View> mViews;

    public PagerCommonAdapter(List<T> data) {

        mData = data;
        mViews = new SparseArray<>(data.size());
    }

    @Override
    public int getCount() {

        return null == mData ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = newView(mData.get(position), position);
            mViews.put(position, view);
        }
        container.addView(view);
        return view;
    }

    public abstract View newView(T data, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    private int mChildCount = 0;

    public View getView(int postion) {
        return mViews.get(postion);
    }

    public SparseArray<View> getViews() {
        return mViews;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


    public void addMoreData(List<T> data) {
        if (null != mData) {
            mData.addAll(data);
        } else {
            mData = data;
        }
        notifyDataSetChanged();
    }

    public void removeData(int postion) {
        if (mViews.indexOfKey(postion) > 0) {
            mViews.remove(postion);
        }
        if (null != mData && null != mData.get(postion)) {
            mData.remove(postion);
            notifyDataSetChanged();
        }
    }

    public void setListData(List<T> data) {
        if (null == mData) {
            mData = data;
        } else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setNewData(List<T> data) {
        if (null == mData) {
            mData = data;
        } else {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mData;
    }
}
