package com.xhq.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;


/**
 * 作者:zhaoge
 * 时间:2017/7/24.
 */

public class UltraPagerAdapter extends PagerAdapter{
    private RelativeLayout mRelativeLayout;
    private ImageView mImageView;
    private Button mButton;
    private Context mContext;

    public UltraPagerAdapter(Context context) {

        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ultrapager, null);
//        mRelativeLayout = view.findViewById(R.id.rl_ultrapager);
//        mImageView = view.findViewById(R.id.iv_ultrapager);
//        mButton = view.findViewById(R.id.btn_enter);
        Button mButtonPass = null;
//        mButtonPass = view.findViewById(R.id.btn_pass);
        switch (position) {
            case 0:
                mButtonPass.setVisibility(View.VISIBLE);
                mButtonPass.setOnClickListener(v -> {
                    Activity activity = (Activity) mContext;

                    //创建缓存,第一次登录
                    SPUtils.put(SPKey.FIRST_USE, SPKey.IS_FIRST, false);

                    Intent it = new Intent();
//                    it.setClass(activity, InitializationActivity.class);
                    activity.startActivity(it);
                    activity.finish();
                });
//                mImageView.setBackgroundResource(R.mipmap.pic_guide_one);
                break;
            case 1:
//                mImageView.setBackgroundResource(R.mipmap.pic_guide_two);
                break;
            case 2:
//                mImageView.setBackgroundResource(R.mipmap.pic_guide_three);
                break;
            case 3:
//                mImageView.setBackgroundResource(R.mipmap.pic_guide_four);
                break;
            case 4:
//                mImageView.setBackgroundResource(R.mipmap.pic_guide_five);
                mButton.setVisibility(View.VISIBLE);
                mButton.setOnClickListener(v -> {
                    Activity activity = (Activity) mContext;

                    //创建缓存,第一次登录
                    SPUtils.put(SPKey.FIRST_USE, SPKey.IS_FIRST, false);

                    Intent it = new Intent();
//                    it.setClass(activity, InitializationActivity.class);
                    activity.startActivity(it);
                    activity.finish();
                });
                break;
        }
        container.addView(mRelativeLayout);
        return mRelativeLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        RelativeLayout view = (RelativeLayout) object;
        container.removeView(view);
    }
}
