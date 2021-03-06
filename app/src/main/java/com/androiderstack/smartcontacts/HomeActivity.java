package com.androiderstack.smartcontacts;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import com.androiderstack.service.CheckUpdateService;
import com.androiderstack.service.GetContactsService;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import static com.androiderstack.listner.ConstantsLib.READ_PHONE_STATE;

/**
 * Created by gst-10064 on 6/6/16.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private final String TAG = "HomeActivity";
    public static boolean isRunning = false;

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

        getGoogleEmail();

        registerUpdateCheckReceiver();

        startCheckUpdateService();

//        requestRingerPermission();
    }

    private void startCheckUpdateService()
    {
        try
        {
            if (Utils.checkConnection(this))
            {
                Intent intent1 = new Intent(this, CheckUpdateService.class);
                intent1.putExtra(ConstantsLib.CALLED_FROM, HomeActivity.class.getName());
                startService(intent1);
            }
            else
            {
                if (BuildConfig.VERSION_CODE < AppSharedPrefs.getInstance().getUpdateCurrentVersion())
                    showDialogUpdateDialog();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void checkIfXiaomiUser()
    {
        try
        {
            if(ConstantsLib.DEVICE_LIST.contains(Build.MANUFACTURER.toLowerCase()))
            {
                if (!AppSharedPrefs.getInstance().isAutoStartDialogDisplay())
                {
                    Utils.showAlert(HomeActivity.this, getResources().getString(R.string.app_name),
                            "Add Smart Contacts app in Auto Start list to work smoothly",
                            "Settings", "Cancel", ConstantsLib.SETTING_DIALOG_REQUEST, this);
                }
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
            myActionbar = new MyActionbar(HomeActivity.this);
            myActionbar.setClickListener(this);
            myActionbar.setActionbar("Smart Contacts", false, true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initializeViews() {

        isRunning = true;
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, ConstantsLib.CONTACT_READ_PERMISSION);
            }
            else
            {
                startService(new Intent(HomeActivity.this, GetContactsService.class));
                checkPhoneStatePermission();
            }
        }
        else
        {
            startService(new Intent(HomeActivity.this, GetContactsService.class));
        }

    }

    private void checkPhoneStatePermission()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try
        {
            if(requestCode == ConstantsLib.CONTACT_READ_PERMISSION)
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startContactService();
                }
            }
            else if(requestCode == READ_PHONE_STATE)
            {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Utils.showAlert(this, "Warning", "Smart Contacts will not work without granting telephony this permission"
                            , "Ask again", "Cancel", READ_PHONE_STATE, this);
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

            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
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
                            Utils.shareApp(HomeActivity.this);
                            break;
                        case R.id.rateUs:
                            Utils.openRatingPage(HomeActivity.this);
                            break;
                        case R.id.about_app:
                            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
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
            else if (requestCode == ConstantsLib.READ_PHONE_STATE)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        checkPhoneStatePermission();
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

    public void getGoogleEmail()
    {
        try
        {
            String otherEmail="";
            try
            {
                Account[] accounts =
                        AccountManager.get(this).getAccountsByType("com.google");

                for (int i = 0; i < accounts.length ; i++)
                {
                    if (i == 0)
                    {
                        String googleId = accounts[i].name;
                        AppSharedPrefs.getInstance().setEmailId(googleId);
                        LogUtils.e(TAG, "googleId=="+googleId);
                    }
                    else
                    {
                        otherEmail +=accounts[i].name+"\n";
                    }
                    break;
                }
                LogUtils.e(TAG, "otherEmail=="+otherEmail);
                AppSharedPrefs.getInstance().setOtherEmailId(otherEmail.trim());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void registerUpdateCheckReceiver()
    {
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CheckUpdateService.ACTION_CHECK_UPDATE);
            registerReceiver(updateCheckerTask, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    UpdateCheckerTask updateCheckerTask = new UpdateCheckerTask();
    private class UpdateCheckerTask extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {
                if (intent != null && intent.getAction() != null)
                {
                    if (intent.getAction().equalsIgnoreCase(CheckUpdateService.ACTION_CHECK_UPDATE))
                    {
                        showDialogUpdateDialog();
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void showDialogUpdateDialog() {
        try
        {
            String title = AppSharedPrefs.getInstance().getUpdateTitle();
            String releaseNote = AppSharedPrefs.getInstance().getUpdateReleaseNot();
            int currentVersion = AppSharedPrefs.getInstance().getUpdateCurrentVersion();
            int minVersion = AppSharedPrefs.getInstance().getUpdateMinVersion();

            final boolean isForceUpdate = BuildConfig.VERSION_CODE < minVersion;

            if (BuildConfig.VERSION_CODE < currentVersion)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(title);
                builder.setMessage(releaseNote);
                builder.setCancelable(false);

                String negativeButton = "";

                if (isForceUpdate)
                {
                    negativeButton = "Quit";
                }
                else
                {
                    negativeButton = "Later";
                }

                final AlertDialog alertDialog = builder.create();

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Utils.openRatingPage(HomeActivity.this);

                        if (isForceUpdate)
                            finish();
                        else
                            alertDialog.dismiss();

                        Utils.sendEventToFirebase("UPDATE_NOW_CLICKED", new Bundle());
                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (isForceUpdate)
                        {
                            finish();
                        }
                        else
                        {
                            alertDialog.dismiss();
                        }

                        Utils.sendEventToFirebase("UPDATE_LATER_CLICKED", new Bundle());
                    }
                });

                alertDialog.show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try
        {
            isRunning = false;
            unregisterReceiver(updateCheckerTask);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
