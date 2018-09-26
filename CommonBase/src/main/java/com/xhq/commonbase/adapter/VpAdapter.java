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
public class VpAdapter extends FragmentPagerAdapter{
    private List<Fragment> list;

    public VpAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
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

