package com.androiderstack.item;

import android.support.v4.app.Fragment;

/**
 * Created by gst-10064 on 6/6/16.
 */
public class TaskTabPagerDataItem {
    private String tabTitle, tabId;
    private Fragment tabFragment;

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public Fragment getTabFragment() {
        return tabFragment;
    }

    public void setTabFragment(Fragment tabFragment) {
        this.tabFragment = tabFragment;
    }

}
