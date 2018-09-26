package com.xhq.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xhq.demo.R;
import com.xhq.demo.bean.ZoningBean;

import java.util.List;

/**
 * Description --> first Area adapter
 * <p>
 * Auth --> Created by ${XHQ} on 2017/8/23.
 */

public class ZoningListAdapter<T> extends BaseAdapter{
    private Context mContext;
    private List<ZoningBean> mListData;


    public ZoningListAdapter(Context context, List<ZoningBean> listData){
        mContext = context;
        mListData = listData;
    }


    @Override
    public int getCount(){
        if(null == mListData) return 0;
        return mListData.size();
    }


    @Override
    public ZoningBean getItem(int i){
        return mListData.get(i);
    }


    @Override
    public long getItemId(int i){
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        ViewHolder holder;
        if(view == null){
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_textview, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        holder.tv_area.setText(mListData.get(i).area_name);

        return view;
    }


    static class ViewHolder{
        private TextView tv_area;
        private View line1;


        ViewHolder(View view){
//            tv_area = view.findViewById(R.id.tv_area);
            line1 = view.findViewById(R.id.line1);
//            line1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * exposure getView method
     *
     * @param holder
     * @param t
     */
//    public abstract void convert(ViewHolder holder, T t, int position);


    /**
     * update
     *
     * @param list
     */
    public void updateListView(List<ZoningBean> list){
        mListData = list;
        notifyDataSetChanged();
    }


    /**
     * update method of the recommend
     *
     * @param listView
     * @param position
     */
    public void notifyDataSetChanged(ListView listView, int position){
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();

        if(position >= firstVisiblePosition && position <= lastVisiblePosition){
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }

    }

}
