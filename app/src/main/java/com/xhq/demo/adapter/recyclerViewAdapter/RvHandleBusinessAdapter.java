//package com.xhq.MyDemo.Adapter.recyclerViewAdapter;
//
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.jmzw.R;
//import com.jmzw.bean.HomeOrInsideBean;
//
//import java.util.List;
//
///**
// * 时间:2017/9/15.
// */
//
//public class RvHandleBusinessAdapter extends BaseQuickAdapter<HomeOrInsideBean, BaseViewHolder>{
//
//    public RvHandleBusinessAdapter(int layoutResId, List<HomeOrInsideBean> data) {
//        super(layoutResId, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, HomeItem item) {
//        helper.setText(R.id.text, item.getTitle());
//        helper.setImageResource(R.id.icon, item.getImageResource());
//        // 加载网络图片
//        Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, HomeOrInsideBean item) {
//        helper.setText(R.id.tv_rv_handleBusiness_item, item.getName());
//        helper.setImageResource(R.id.iv_rv_handleBusiness_item, Integer.parseInt(item.getHid()));
//    }
//}
