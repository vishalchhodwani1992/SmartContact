package com.androiderstack.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androiderstack.item.TaskTabPagerDataItem;

import java.util.List;


public class UserTaskTabFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<TaskTabPagerDataItem> mTabs;
    public UserTaskTabFragmentPagerAdapter(FragmentManager fragmentManager, List<TaskTabPagerDataItem> tabs) {
        super(fragmentManager);
        this.mTabs = tabs;
    }

    public void setDatasource(List<TaskTabPagerDataItem> datasource){
        mTabs = datasource;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return mTabs.get(i).getTabFragment();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTabTitle();
    }
}