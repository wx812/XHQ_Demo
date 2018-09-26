package com.xhq.commonbase.share;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.R;
import com.common.base.application.BaseApplication;
import com.common.utils.share.base.BaseSharePlatform;
import com.smartcity.commonbase.util.RecyclerAdapter;
import com.smartcity.commonbase.util.RvViewHolder;

import java.util.List;

/**
 * Created by ychen on 2017/12/9.
 */

public class ShareAdapter extends RecyclerAdapter<BaseSharePlatform>{
    public ShareAdapter(Context context,int itemId ,List<BaseSharePlatform> list) {
        super(context, itemId, list);
    }

    @Override
    public void convertViewHolder(RvViewHolder holder, BaseSharePlatform entity, int position) {
        ImageView civ_icon = holder.getView(R.id.civ_icon);
        TextView txt_name = holder.getView(R.id.txt_name);
        if(TextUtils.isEmpty(entity.getPicUrl())) {
            Glide.with(BaseApplication.getInstance()).load(entity.getPicResId()).into(civ_icon);
        }else {
            Glide.with(BaseApplication.getInstance()).load(entity.getPicUrl()).placeholder(entity.getPicResId()).
                    error(entity.getPicResId()).crossFade(1000).dontAnimate().into(civ_icon);
        }
        txt_name.setText(entity.getName());
    }
}
