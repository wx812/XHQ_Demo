package com.tencent.liteav.ugsv.videorecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.liteav.ugsv.R;
import com.tencent.liteav.ugsv.common.widget.AutoDragHorizontalView;

import java.util.List;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/4/1.
 *     Desc  : the recyclerView adapter of switch camera mode.
 *     Updt  : Description.
 * </pre>
 */
public class RVCameraTypeAdapter extends RecyclerView.Adapter<RVCameraTypeAdapter.CameraTypeViewHolder> implements
        AutoDragHorizontalView.IAutoLocateHorizontalView{
    private Context mContext;
    private View view;
    private List<String> mListData;
//    private OnItemClickListener mOnItemClickListener;


    public RVCameraTypeAdapter(Context context, List<String> listData){
        this.mContext = context;
        this.mListData = listData;
    }

    @Override
    public CameraTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_autolocate, parent, false);
//        view.setOnClickListener(this);
        return new CameraTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CameraTypeViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tv_type.setText(mListData.get(position));
    }

    @Override
    public int getItemCount() {
        return  mListData.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    @Override
    public void onItemSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        if(isSelected) {
            ((CameraTypeViewHolder) holder).tv_type.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }else{
            ((CameraTypeViewHolder) holder).tv_type.setTextColor(mContext.getResources().getColor(R.color.color_percent70_white));
        }
    }


//    @Override
//    public void onClick(View v){
//        if(mOnItemClickListener != null){
//            mOnItemClickListener.onItemClick(view, (Integer)view.getTag());
//        }
//    }


    static class CameraTypeViewHolder extends RecyclerView.ViewHolder{
        TextView tv_type;
        CameraTypeViewHolder(View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }


//    public interface OnItemClickListener{
//        void onItemClick(View view, int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.mOnItemClickListener = listener;
//    }
}
