package com.xhq.commonbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by huyuanhao on 2016/11/2.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
    private List<Fragment> list;//
    private List<String>  titles;//标题

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String>  titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
