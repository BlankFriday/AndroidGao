package com.example.lenovo.az_qimo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lenovo.az_qimo.bean.TabTest;

import java.util.ArrayList;
import java.util.List;

public class Vp_Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private List<TabTest.DataBean> list;
    public Vp_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public Vp_Adapter(FragmentManager fm, ArrayList<Fragment> fragments, List<TabTest.DataBean> list) {
        super(fm);
        this.fragments = fragments;
        this.list = list;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }
}
