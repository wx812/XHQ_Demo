//package com.xhq.MyDemo.Adapter.recyclerViewAdapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.jjkj.common.constant.ConstantValue;
//import com.jjkj.common.constant.apiconfig.ApiEnum;
//import com.jmzw.R;
//import com.jmzw.bean.CardStatusBean;
//
//import java.util.ArrayList;
//
///**
// * Description
// * Created by ${XHQ} on 2017/8/16.
// */
//
//public class RVGrantInquireAdapter extends RecyclerView.Adapter<RVGrantInquireAdapter.MyViewHolder> implements
//        View.OnClickListener{
//
//    private Context mContext;
//    private ArrayList<CardStatusBean> mListData;
//    private OnItemClickListener mOnItemClickListener = null;
//
//
//    public RVGrantInquireAdapter(Context context, ArrayList<CardStatusBean> list){
//        mContext = context;
//        mListData = list;
//    }
//
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View itemView;
//        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rv_grant_inquire, parent, false);
//        itemView.setOnClickListener(this);
//        return new MyViewHolder(itemView);
//    }
//
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position){
//        if(mListData.size() == 0) return;
//
//        holder.itemView.setTag(position);
//
//        CardStatusBean cardInfo = mListData.get(position);
//        holder.tv_name.setText(cardInfo.getPerson_name());
//
//        String cardType = cardInfo.getCard_type();
//        if(ConstantValue.PREFERENTIAL_CARD.equals(cardType)){
//            cardType = ApiEnum.CardType.PREFERENTIAL_CARD.name;
//        }
//        if(ConstantValue.YLZC_CARD.equals(cardType)){
//            cardType = ApiEnum.CardType.YLZC_CARD.name;
//        }
//        if(ConstantValue.ORDINARY_CARD.equals(cardType)){
//            cardType = ApiEnum.CardType.REGULAR_CARD.name;
//        }
//        holder.tv_civil_card_type.setText(cardType);
//
//        String cardStatu = cardInfo.getCard_statu();
//        if(ConstantValue.DB_DQS.equals(cardStatu)){
//            cardStatu = ApiEnum.CardStatu.DB_DQS.name;
//        }
//        if(ConstantValue.CARD_WAIT.equals(cardStatu)){
//            cardStatu = ApiEnum.CardStatu.CARD_WAIT.name;
//        }
//        if(ConstantValue.GET_CARD_FAIL.equals(cardStatu)){
//            cardStatu = ApiEnum.CardStatu.GET_CARD_FAIL.name;
//        }
//        if(ConstantValue.CARD.equals(cardStatu)){
//            cardStatu = ApiEnum.CardStatu.CARD.name;
//        }
//        holder.tv_card_status.setText(cardStatu);
//
//        holder.tv_card_area.setText(cardInfo.getArea_name());
//        holder.tv_bjt_num.setText(cardInfo.getBjt_no());
//        holder.tv_IDCard_num.setText(cardInfo.getPerson_uid());
//        holder.tv_bank_card_num.setText(cardInfo.getBank_account_code());
//    }
//
//
//    @Override
//    public int getItemCount(){
//        if(mListData == null){
//            return 0;
//        }
//        return mListData.size();
//    }
//
//
//    @Override
//    public void onClick(View view){
//        if(mOnItemClickListener != null){
//            mOnItemClickListener.onItemClick(view, (Integer)view.getTag());
//        }
//
//    }
//
//    static class MyViewHolder extends RecyclerView.ViewHolder{
//        private TextView tv_name, tv_card_status, tv_civil_card_type, tv_card_area, tv_bjt_num, tv_IDCard_num,
//                tv_bank_card_num;
//
//
//        MyViewHolder(View itemView){
//            super(itemView);
//            tv_name = itemView.findViewById(R.id.tv_name);
//            tv_civil_card_type = itemView.findViewById(R.id.tv_civil_card_type);
//            tv_card_status = itemView.findViewById(R.id.tv_card_status);
//            tv_card_area = itemView.findViewById(R.id.tv_inquire_area);
//            tv_bjt_num = itemView.findViewById(R.id.tv_bjt_num);
//            tv_IDCard_num = itemView.findViewById(R.id.tv_uid_num);
//            tv_bank_card_num = itemView.findViewById(R.id.tv_bank_card_num);
//        }
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//}
