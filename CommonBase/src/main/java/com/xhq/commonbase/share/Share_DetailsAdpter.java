package com.xhq.commonbase.share;

import android.content.Context;
import android.widget.TextView;

import com.common.R;
import com.common.view.CircleImageView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Share_DetailsAdpter extends MultiItemTypeAdapter<ShareentriesBaseEntiy>{
    public Share_DetailsAdpter(Context context, List<ShareentriesBaseEntiy> list){
        super(context, list);
        addItemViewDelegate(new Share_DetailsAdpter.OneImgDelegate());//  单图样式
    }
    //  单图样式
    public class OneImgDelegate implements ItemViewDelegate<ShareentriesBaseEntiy> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_tfhour_share_list_item;
        }

        @Override
        public boolean isForViewType(ShareentriesBaseEntiy item, int position) {
            return null != item;
        }

        @Override
        public void convert(ViewHolder holder, ShareentriesBaseEntiy item, int position) {
            CircleImageView top_pic = holder.getView(R.id.top_pic);
            TextView top_name = holder.getView(R.id.top_name);
            top_pic.setImageResource(item.getPicResId());
            top_name.setText(item.getName());
        }
    }
}
