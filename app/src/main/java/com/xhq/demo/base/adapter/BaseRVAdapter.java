package com.xhq.demo.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhq.demo.tools.StringUtils;

import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/23.
 *     Desc  : base recycler view adapter
 *     Updt  : Description
 * </pre>
 */
public abstract class BaseRVAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements View.OnClickListener{

    protected List<T> mListData;
    protected OnItemClickListener<T> mOnItemClickListener = null;


    public BaseRVAdapter(Context context, List<T> listData){
        mListData = listData;
    }


    @Override
    public int getItemCount(){
        if(StringUtils.isEmpty(mListData)) return 0;
        return mListData.size();
    }


    @Override
    public void onClick(View view){
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view, (Integer)view.getTag());
        }
    }


    public List<T> getData(){
        return mListData;
    }


    public void addData(T t){
        this.mListData.add(t);
        notifyDataSetChanged();
    }

    public void addData(T t, int index){
        this.mListData.add(index, t);
        notifyDataSetChanged();
    }

    public void addData(List<T> listData){
        this.mListData.addAll(listData);
        notifyDataSetChanged();
    }


    public void updateData(List<T> listData){
        this.mListData = listData;
        notifyDataSetChanged();
    }


    public void refresh(List<T> listData, int index){
        this.mListData.addAll(index, listData);
        notifyDataSetChanged();
    }


    public void refresh(T data) {
        mListData.clear();
        mListData.add(data);
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener<T> listener){
        this.mOnItemClickListener = listener;
    }
}
