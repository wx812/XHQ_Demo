package com.xhq.commonbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 在此写类用途
 *
 * @FileName: VpAdapter.java
 * @author: wudebing
 * @date: 2016-11-02 14:50
 */
public class Vp2Adapter extends FragmentPagerAdapter{
    private List<Fragment> list;
    private List<String> titles;

    public Vp2Adapter(FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

