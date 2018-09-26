//package com.xhq.MyDemo.Adapter.recyclerViewAdapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.jmzw.R;
//import com.jmzw.bean.ZoningBean;
//
//import java.util.List;
//
///**
// * Description --> second area adapter
// * <p>
// * Auth --> Created by ${XHQ} on 2017/8/23.
// */
//
//public class RVZoningAdapter<T> extends RecyclerView.Adapter<RVZoningAdapter.MyViewHolder> implements View.OnClickListener{
//
//    private Context mContext;
//    private List<T> mListData;
//    private OnItemClickListener mOnItemClickListener;
//
//
//    public RVZoningAdapter(Context context, List<T> listData){
//        mContext = context;
//        mListData = listData;
//    }
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View v;
//        v = LayoutInflater.from(mContext).inflate(R.layout.item_list_textview, parent, false);
//        v.setOnClickListener(this);
//        return new MyViewHolder(v);
//    }
//
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position){
//        holder.itemView.setTag(position);
//
//        T t = mListData.get(position);
//        String areaStr = null;
//        if(t instanceof ZoningBean.ZoningBean2){
//            areaStr = ((ZoningBean.ZoningBean2)t).area_name;
//
//        }else if(t instanceof ZoningBean.ZoningBean3){
//            areaStr = ((ZoningBean.ZoningBean3)t).area_name;
//
//        }else if(t instanceof ZoningBean.ZoningBean4){
//            areaStr = ((ZoningBean.ZoningBean4)t).area_name;
//        }
//
//        holder.tv_area.setText(areaStr);
//
//    }
//
//
//    @Override
//    public int getItemCount(){
//        if(null == mListData) return 0;
//        return mListData.size();
//    }
//
//
//    public List<T> getData(){
//        return mListData;
//    }
//
//
//    public void setData(List<T> mListData){
//        this.mListData = mListData;
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public void onClick(View view){
//        if(mOnItemClickListener != null){
//            mOnItemClickListener.onItemClick(view, (Integer)view.getTag());
//        }
//    }
//
//
//    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
//        this.mOnItemClickListener = mOnItemClickListener;
//    }
//
//
//    static class MyViewHolder extends RecyclerView.ViewHolder{
//        private TextView tv_area;
//
//
//        MyViewHolder(View itemView){
//            super(itemView);
//            tv_area = (TextView)itemView.findViewById(R.id.tv_area);
//        }
//    }
//
//
//}
