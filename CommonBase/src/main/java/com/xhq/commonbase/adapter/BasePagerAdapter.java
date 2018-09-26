package com.xhq.commonbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yancy  2016/9/8.
 */
public abstract class BasePagerAdapter extends FragmentPagerAdapter{

    String[] titles;

    public BasePagerAdapter(FragmentManager fm){
        super(fm);
        this.titles = setTitles();

    }

    @Override
    public Fragment getItem(int position) {

        return setFragment(position);
    }

    protected abstract Fragment setFragment(int position);


    @Override
    public int getCount() {
        return titles.length;
    }

    protected abstract String[] setTitles();

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
