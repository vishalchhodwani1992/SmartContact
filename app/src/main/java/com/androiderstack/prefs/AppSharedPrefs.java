package com.androiderstack.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.smartcontacts.BuildConfig;


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

    public void setAutoStartDialogDisplay(boolean autoStartDialogDisplay)
    {
        this.prefsEditor.putBoolean("autoStartDialogDisplay", autoStartDialogDisplay);
        this.prefsEditor.commit();
    }
    public boolean isAutoStartDialogDisplay()
    {
        return this.appSharedPrefs.getBoolean("autoStartDialogDisplay", false);
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

    public void setUserId(String userId)
    {
        this.prefsEditor.putString("userId", userId);
        this.prefsEditor.commit();
    }
    public String getUserId()
    {
        return this.appSharedPrefs.getString("userId", "");
    }

    public void setEmailId(String emailId)
    {
        this.prefsEditor.putString("emailId", emailId);
        this.prefsEditor.commit();
    }
    public String getEmailId()
    {
        return this.appSharedPrefs.getString("emailId", "");
    }

    public void setOtherEmailId(String otherEmailId)
    {
        this.prefsEditor.putString("otherEmailId", otherEmailId);
        this.prefsEditor.commit();
    }
    public String getOtherEmailId()
    {
        return this.appSharedPrefs.getString("otherEmailId", "");
    }

    public void setUpdateTitle(String updateTitle)
    {
        this.prefsEditor.putString("updateTitle", updateTitle);
        this.prefsEditor.commit();
    }
    public String getUpdateTitle()
    {
        return this.appSharedPrefs.getString("updateTitle", "Update Available");
    }

    public void setUpdateReleaseNot(String updateReleaseNot)
    {
        this.prefsEditor.putString("updateReleaseNot", updateReleaseNot);
        this.prefsEditor.commit();
    }
    public String getUpdateReleaseNot()
    {
        return this.appSharedPrefs.getString("updateReleaseNot", "Update available, Please update");
    }

    public void setUpdateNotificationReleaseNot(String updateNotificationReleaseNot)
    {
        this.prefsEditor.putString("updateNotificationReleaseNot", updateNotificationReleaseNot);
        this.prefsEditor.commit();
    }
    public String getUpdateNotificationReleaseNot()
    {
        return this.appSharedPrefs.getString("updateNotificationReleaseNot", "Update available, Please update");
    }

    public void setUpdateCurrentVersion(int updateCurrentVersion)
    {
        this.prefsEditor.putInt("updateCurrentVersion", updateCurrentVersion);
        this.prefsEditor.commit();
    }
    public int getUpdateCurrentVersion()
    {
        return this.appSharedPrefs.getInt("updateCurrentVersion", BuildConfig.VERSION_CODE);
    }

    public void setUpdateMinVersion(int updateMinVersion)
    {
        this.prefsEditor.putInt("updateMinVersion", updateMinVersion);
        this.prefsEditor.commit();
    }
    public int getUpdateMinVersion()
    {
        return this.appSharedPrefs.getInt("updateMinVersion", BuildConfig.VERSION_CODE);
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

