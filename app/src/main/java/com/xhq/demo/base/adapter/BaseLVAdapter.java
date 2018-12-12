package com.xhq.demo.base.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public abstract class BaseLVAdapter<T> extends BaseAdapter{

    protected List<T> mListData;
    private int layoutId = -1;
    private View layoutView;


    public BaseLVAdapter(List<T> listData, @LayoutRes int layoutId){
        super();
        this.mListData = listData;
        this.layoutId = layoutId;
    }


    public BaseLVAdapter(List<T> mListData, View codeLayoutView){
        super();
        this.mListData = mListData;
        this.layoutView = codeLayoutView;
    }


    @Override
    public int getCount(){
        return mListData.size();
    }


    @Override
    public T getItem(int position){
        return mListData.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BaseLVHolder holder;
        if(layoutId == -1){
            holder = BaseLVHolder.getViewHolder(parent, convertView, layoutView, position);
        }else{
            holder = BaseLVHolder.getViewHolder(parent, convertView, layoutId, position);
        }
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }


    /**
     * getView()用户自己实现
     *
     * @param holder
     * @param t
     */
    public abstract void convert(BaseLVHolder holder, T t, int position);


    /**
     * 更新listview
     *
     * @param list
     */
    public void updateListView(List<T> list){
        this.mListData = list;
        notifyDataSetChanged();
    }


    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param listView 要更新的listView
     * @param position 要更新的位置
     */
    public void notifyDataSetChanged(ListView listView, int position){
        // 第一个可见的位置
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        // 最后一个可见的位置
        int lastVisiblePosition = listView.getLastVisiblePosition();
        // 在看见范围内才更新，不可见的滑动后自动会调用getView方法更新
        if(position >= firstVisiblePosition && position <= lastVisiblePosition){
            // 获取指定位置view对象
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }
    }
}
