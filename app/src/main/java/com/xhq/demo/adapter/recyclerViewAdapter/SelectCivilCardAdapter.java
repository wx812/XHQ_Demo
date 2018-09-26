//package com.xhq.MyDemo.Adapter.recyclerViewAdapter;
//
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.jjkj.common.tool.Utils;
//import com.jmzw.R;
//import com.jmzw.bean.ActivateInfoBean;
//
//import java.util.List;
//
///**
// * Created by hskun on 2017/10/25.
// * 说明：
// */
//
//public class SelectCivilCardAdapter extends BaseQuickAdapter<ActivateInfoBean, BaseViewHolder>{
//    public SelectCivilCardAdapter(int layoutResId, @Nullable List<ActivateInfoBean> data) {
//        super(layoutResId, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, ActivateInfoBean item) {
//        ImageView checkBox = helper.getView(R.id.checkBox);
//        helper.setText(R.id.text_bank_card_num, Utils.formatBankCardNum(item.getBank_account_code()))
//                .setText(R.id.text_card_status, item.getCard_statu_desc());
//        if (item.isSelected()) {
//            checkBox.setVisibility(View.VISIBLE);
//        } else {
//            checkBox.setVisibility(View.GONE);
//        }
//    }
//}
