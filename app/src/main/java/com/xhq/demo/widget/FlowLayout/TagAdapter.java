package com.xhq.demo.widget.FlowLayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xhq.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/09/30.
 *     Desc  : FlowLayout adapter.
 *     Updt  : 2018/10/18.
 * </pre>
 */
public class TagAdapter<T> extends BaseAdapter implements FlowLayout.OnInitSelectedPosition{

    private final Context mContext;
    private final List<T> mDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder holder;
        holder = convertView == null ? new MyViewHolder(mContext, 0) : (MyViewHolder)convertView.getTag();

        TextView tv = holder.tv;
        T t = mDataList.get(position);
        if (t instanceof String) tv.setText((String)t);
        return holder.getConvertView();
    }

    @Override
    public boolean isSelectedPosition(int position) {
        return position % 2 == 0;
    }

    public void onlyAddAll(List<T> listData) {
        mDataList.addAll(listData);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> listData) {
        mDataList.clear();
        onlyAddAll(listData);
    }


    private static class MyViewHolder{
        View mConvertView;
        TextView tv;

        MyViewHolder(Context ctx, @LayoutRes int layoutId){
            mConvertView = LayoutInflater.from(ctx).inflate(R.layout.item_tag_flowlayout, null);
            mConvertView.setTag(this);
            tv = mConvertView.findViewById(R.id.tv_tag);
        }

        public View getConvertView(){
            return mConvertView;
        }
    }

}
