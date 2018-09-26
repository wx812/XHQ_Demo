package com.xhq.commonbase.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.R;
import com.common.bean.AccountItemsEntity;

import java.util.List;

/**
 * Created by chenyuan
 * on 2017/1/24.  10.02
 * <p>
 * 月份账单的适配器
 */

public class AccountItmeMonthVPAdapter extends PagerAdapter{
    private Context context;
    private List<AccountItemsEntity.ZXAccountLogsBean> data;
    private LayoutInflater inflater;

    public AccountItmeMonthVPAdapter(Context context, List<AccountItemsEntity.ZXAccountLogsBean> data) {
        this.context = context;
        this.data = data;
        inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        View view = inflater.inflate(R.layout.item_vp_accountmonth, null);

        AccountItemsEntity.ZXAccountLogsBean zbAccountLogsBean = data.get(position);
        TextView tvMoney = (TextView) view.findViewById(R.id.tv_money);
        TextView tvMonth = (TextView) view.findViewById(R.id.tv_month);
        tvMoney.setText(zbAccountLogsBean.getMonthSum() + "");
        tvMonth.setText(zbAccountLogsBean.getDate());
        container.addView(view);
        return view;

    }
}
