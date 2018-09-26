//package com.xhq.MyDemo.Adapter.recyclerViewAdapter;
//
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.jjkj.common.constant.apiconfig.ApiUrl;
//import com.jmzw.R;
//import com.jmzw.bean.HomeOrInsideBean;
//
//import java.util.List;
//
///**
// * 作者:zhaoge
// * 时间:2017/8/3.
// */
//
//public class RvCivilAffairsCardAdapter extends BaseQuickAdapter<HomeOrInsideBean, BaseViewHolder>{
//
//    public RvCivilAffairsCardAdapter(int layoutResId, List data) {
//        super(layoutResId, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, HomeOrInsideBean item) {
//
//        helper.setText(R.id.tv_rv_civilaffairscard_item, item.getName());
//
//        Glide.with(mContext)
//                .load(ApiUrl.URL_HOST + item.getHid())
//                .asBitmap()
//                .placeholder(R.mipmap.pic_default)
//                .error(R.mipmap.pic_default)
//                .dontAnimate()
//                .into((ImageView) helper.getView(R.id.iv_rv_civilaffairscard_item));
//
//    }
//}
