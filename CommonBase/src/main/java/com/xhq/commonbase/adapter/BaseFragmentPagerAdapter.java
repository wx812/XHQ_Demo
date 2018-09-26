package com.xhq.commonbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.common.bean.SelectItem;

import java.util.List;

/**
 * Administrator
 * 2016/11/29
 * 15:56
 */
public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private List<SelectItem> mList;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<SelectItem> mList) {
        super(fm);
        this.mList = mList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getDesc();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void removeAll(List<SelectItem> t) {
        this.mList.removeAll(t);
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
