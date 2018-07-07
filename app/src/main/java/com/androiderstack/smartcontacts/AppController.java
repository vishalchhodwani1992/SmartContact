package com.androiderstack.smartcontacts;

import android.support.multidex.MultiDexApplication;

import com.androiderstack.item.DaoMaster;
import com.androiderstack.item.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by gst-10064 on 30/8/16.
 */
public class AppController extends MultiDexApplication
{
    public static AppController instance = null;
    public static final String TAG = "AppController";

    private DaoSession daoSession;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "profile_changer-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}