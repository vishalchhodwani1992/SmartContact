package com.androiderstack.smartcontacts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.androiderstack.adapter.UserTaskTabFragmentPagerAdapter;
import com.androiderstack.custom_view.MyActionbar;
import com.androiderstack.fragment.BaseFragment;
import com.androiderstack.fragment.BlockContactFragment;
import com.androiderstack.fragment.IMPContactsFragment;
import com.androiderstack.item.TaskTabPagerDataItem;
import com.androiderstack.listner.ConstantsLib;
import com.androiderstack.listner.DialogClickListener;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.service.GetContactsService;
import com.androiderstack.utility.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gst-10064 on 6/6/16.
 */
public class TabActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private final String TAG = "TabActivity";

    private List<TaskTabPagerDataItem> mTabs;
    ViewPager mViewPager;
    TabLayout mSlidingTabLayout;
    UserTaskTabFragmentPagerAdapter userTaskTabFragmentPagerAdapter;

    IMPContactsFragment impContactsFragment;
    AdView adView;

    MyActionbar myActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        initializeViews();

        addActionBar();

        createTabPagerItem();

        startContactService();

        showBannerAd();

        checkIfXiaomiUser();
    }

    private void checkIfXiaomiUser()
    {
        try
        {
            if(ConstantsLib.DEVICE_LIST.contains(Build.MANUFACTURER))
            {
                Utils.showAlert(TabActivity.this, getResources().getString(R.string.app_name), "Add Smart Contacts application in Auto Start list to work smoothly", "Cancel", "Settings", ConstantsLib.SETTING_DIALOG_REQUEST, this);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void addActionBar()
    {
        try
        {
            myActionbar = new MyActionbar(TabActivity.this);
            myActionbar.setClickListener(this);
            myActionbar.setActionbar("Smart Contacts", false, true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initializeViews() {

        mTabs = new ArrayList<>();
        mSlidingTabLayout = (TabLayout) findViewById(R.id.userTaskTabLayout_tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.userTaskTabLayout_viewPager);

        impContactsFragment = new IMPContactsFragment();
        adView = (AdView) findViewById(R.id.adView);

        userTaskTabFragmentPagerAdapter = new UserTaskTabFragmentPagerAdapter(getSupportFragmentManager(), mTabs);
        mViewPager.setAdapter(userTaskTabFragmentPagerAdapter);

        mSlidingTabLayout.removeAllTabs();
        mTabs.clear();
    }

    public void showBannerAd() {
        try {
            AdRequest adRequest = new AdRequest.Builder()

                    // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("A14DB3C86790D7C504F5F0D1D1F42839")
                    .build();

            // Load ads into Banner Ads
//            adView.setAdSize(AdSize.SMART_BANNER);
            adView.loadAd(adRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        try
        {
            BaseFragment fragment = (BaseFragment) mTabs.get(mViewPager.getCurrentItem()).getTabFragment();

            if (fragment != null)
            {
                fragment.onContextItemSelected(item);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return true;
    }

    private void startContactService() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if(ActivityCompat.checkSelfPermission(TabActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(TabActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1001);
            }
            else
            {
                startService(new Intent(TabActivity.this, GetContactsService.class));
            }
        }
        else
        {
            startService(new Intent(TabActivity.this, GetContactsService.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try
        {
            if(requestCode == 1001)
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startContactService();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void createTabPagerItem()
    {
        TaskTabPagerDataItem dataItem1 = new TaskTabPagerDataItem();
        dataItem1.setTabId("1");
        dataItem1.setTabTitle(getResources().getString(R.string.favorite_contacts));
        dataItem1.setTabFragment(new IMPContactsFragment());
        mTabs.add(dataItem1);

        TaskTabPagerDataItem dataItem2 = new TaskTabPagerDataItem();
        dataItem2.setTabId("2");
        dataItem2.setTabTitle(getResources().getString(R.string.block_contacts));
        dataItem2.setTabFragment(new BlockContactFragment());
        mTabs.add(dataItem2);

        userTaskTabFragmentPagerAdapter.notifyDataSetChanged();

//        mViewPager.setOffscreenPageLimit(mTabs.size());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(15);
        }
        mSlidingTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.actionbarLayout_backButton:
                onBackPressed();
                break;
            case R.id.actionbarLayout_menu:
                openMenu(view);
                break;
        }
    }

    private void openMenu(View view)
    {
        try
        {
            final BaseFragment fragment = (BaseFragment) mTabs.get(mViewPager.getCurrentItem()).getTabFragment();

            PopupMenu popupMenu = new PopupMenu(TabActivity.this, view);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.imp_menu, popupMenu.getMenu());

            fragment.setMenuEnable(popupMenu);

            if (fragment instanceof BlockContactFragment)
                popupMenu.getMenu().getItem(0).setVisible(false);
            else
                popupMenu.getMenu().getItem(0).setVisible(true);

            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId())
                    {
                        case R.id.appEnable:
                            if (fragment != null)
                            {
                                AppSharedPrefs.getInstance().setAppEnable(!AppSharedPrefs.getInstance().isAppEnable());
                            }
                            break;
                        case R.id.clearAll:
                            if (fragment != null)
                                fragment.clearList();
                            break;
                        case R.id.shareApp:
                            Utils.shareApp(TabActivity.this);
                            break;
                        case R.id.rateUs:
                            Utils.openRatingPage(TabActivity.this);
                            break;
                        case R.id.about_app:
                            startActivity(new Intent(TabActivity.this, AboutActivity.class));
                            break;
                    }

                    return false;
                }
            });

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

        try
        {
            if (requestCode == ConstantsLib.SETTING_DIALOG_REQUEST)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        Utils.openAutoStartSettingForAll(this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
