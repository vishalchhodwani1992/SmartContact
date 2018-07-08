package com.androiderstack.smartcontacts;

import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.androiderstack.item.DaoMaster;
import com.androiderstack.item.DaoSession;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.utility.Utils;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.greendao.database.Database;

import io.fabric.sdk.android.Fabric;

/**
 * Created by gst-10064 on 30/8/16.
 */
public class AppController extends MultiDexApplication
{
    public static AppController instance = null;
    public static final String TAG = "AppController";

    private DaoSession daoSession;
    private FirebaseAnalytics firebaseAnalytics;

    public static AppController getInstance()
    {
        if (instance != null)
        {
            return instance;
        }
        return new AppController();
    }

    public void onCreate()
    {
        super.onCreate();
        instance = this;

        Fabric.with(this, new Crashlytics());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        firebaseAnalytics.setMinimumSessionDuration(5000);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "profile_changer-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        sendDataFirebase();
    }

    private void sendDataFirebase()
    {
        try
        {
            Utils.getUserId();

            Bundle bundle = new Bundle();
            bundle.putString("userId", AppSharedPrefs.getInstance().getUserId());
            bundle.putString("emailId", AppSharedPrefs.getInstance().getEmailId());
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
            firebaseAnalytics.setUserId(AppSharedPrefs.getInstance().getUserId());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public FirebaseAnalytics getFirebaseAnalytics()
    {
        return firebaseAnalytics;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}