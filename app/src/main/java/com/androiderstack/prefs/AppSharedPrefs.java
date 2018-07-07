package com.androiderstack.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.androiderstack.smartcontacts.AppController;


public class AppSharedPrefs {
	public static final String APP_SHARED_PREFS = "Bukabu";
	public static AppSharedPrefs instance = null;
	public SharedPreferences appSharedPrefs;
	public SharedPreferences.Editor prefsEditor;
    private static boolean isShown = true;

    ConnectivityManager connectivity;

    public AppSharedPrefs(Context paramContext)
    {
        this.appSharedPrefs = paramContext.getSharedPreferences(APP_SHARED_PREFS, 0);
        this.prefsEditor = this.appSharedPrefs.edit();
        connectivity = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static AppSharedPrefs getInstance()
    {
        if ((instance instanceof AppSharedPrefs))
        {
            return instance;
        }
        instance = new AppSharedPrefs(AppController.getInstance().getApplicationContext());
        return instance;
    }

    public SharedPreferences getSharedPreference()
    {
        return this.appSharedPrefs;
    }

    public void setLogin(boolean isLogin)
    {
        this.prefsEditor.putBoolean("isLogin", isLogin);
        this.prefsEditor.commit();
    }
    public boolean isLogin()
    {
        return this.appSharedPrefs.getBoolean("isLogin", false);
    }

    public void setAutoSilent(boolean autoSilent)
    {
        this.prefsEditor.putBoolean("autoSilent", autoSilent);
        this.prefsEditor.commit();
    }
    public boolean isAutoSilent()
    {
        return this.appSharedPrefs.getBoolean("autoSilent", false);
    }

    public void setAppEnable(boolean appEnable)
    {
        this.prefsEditor.putBoolean("appEnable", appEnable);
        this.prefsEditor.commit();
    }
    public boolean isAppEnable()
    {
        return this.appSharedPrefs.getBoolean("appEnable", true);
    }

    public void setNewContactSwitch(boolean newContactSwitch)
    {
        this.prefsEditor.putBoolean("newContactSwitch", newContactSwitch);
        this.prefsEditor.commit();
    }
    public boolean getNewContactSwitch()
    {
        return this.appSharedPrefs.getBoolean("newContactSwitch", false);
    }

    public void setRecentState(String recentState)
    {
        this.prefsEditor.putString("recentState", recentState);
        this.prefsEditor.commit();
    }
    public String getRecentState()
    {
        return this.appSharedPrefs.getString("recentState", "");
    }

    public void setWasSilent(boolean wasSilent)
    {
        this.prefsEditor.putBoolean("wasSilent", wasSilent);
        this.prefsEditor.commit();
    }
    public boolean wasSilent()
    {
        return this.appSharedPrefs.getBoolean("wasSilent", false);
    }

    public void setWasVibrate(boolean wasVibrate)
    {
        this.prefsEditor.putBoolean("wasVibrate", wasVibrate);
        this.prefsEditor.commit();
    }
    public boolean wasVibrate()
    {
        return this.appSharedPrefs.getBoolean("wasVibrate", false);
    }

    public void setLastCall(String lastCall)
    {
        this.prefsEditor.putString("lastCall", lastCall);
        this.prefsEditor.commit();
    }
    public String getLastCall()
    {
        return this.appSharedPrefs.getString("lastCall", "");
    }

    public void setLastUserCall(String lastUserCall)
    {
        this.prefsEditor.putString("lastUserCall", lastUserCall);
        this.prefsEditor.commit();
    }
    public String getLastUserCall()
    {
        return this.appSharedPrefs.getString("lastUserCall", "");
    }

    public static void showLog(String TAG, String message)
    {
        if(isShown)
            Log.d(TAG, message);
    }

  public void clearPreference()
  {
      prefsEditor.clear();
      prefsEditor.commit();
  }

  public  boolean checkInternet()
  {
      if (connectivity != null)
      {
          NetworkInfo[] info = connectivity.getAllNetworkInfo();
          if (info != null)
              for (int i = 0; i < info.length; i++)
                  if (info[i].getState() == NetworkInfo.State.CONNECTED)
                  {
                      return true;
                  }

      }
      return false;
  }
}
