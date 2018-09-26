package com.xhq.commonbase.adapter;

import android.content.Context;

import com.common.R;
import com.common.bean.AccountItemsEntity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by chenyuan
 * on 2017/1/24.  12.22
 */

public class AccountDayItemAdapter extends CommonAdapter<AccountItemsEntity.ZXAccountLogsBean.AccountLogListBean> {


    public AccountDayItemAdapter(Context context, int layoutId, List<AccountItemsEntity.ZXAccountLogsBean.AccountLogListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, AccountItemsEntity.ZXAccountLogsBean.AccountLogListBean accountLogListBean, int i) {
        viewHolder.setText(R.id.tv_category, accountLogListBean.getBillMark() + "");
        viewHolder.setText(R.id.tv_date, accountLogListBean.getBillTime());
        viewHolder.setText(R.id.tv_money, accountLogListBean.getMoney() + "");
    }
}
