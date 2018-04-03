package com.dizzylay.simplev2ex;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/2.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Latest";
            case 1:
                return "Hot";
            default:
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
