package com.androiderstack.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androiderstack.listner.ConstantsLib;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.BuildConfig;
import com.androiderstack.smartcontacts.HomeActivity;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

import org.json.JSONObject;

public class CheckUpdateService extends IntentService {

    public static final String ACTION_CHECK_UPDATE = "com.androiderstack.service.CheckUpdateService$ACTION_CHECK_UPDATE";
    private final String TAG = "CheckUpdateService";

    String calledFrom = "";

    public CheckUpdateService()
    {
        super("CheckUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        checkForUpdate(intent);
    }

    private void checkForUpdate(Intent intent)
    {
        try
        {
            if (intent != null && intent.getExtras() != null)
            {
                calledFrom = intent.getStringExtra(ConstantsLib.CALLED_FROM);
            }

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    ConstantsLib.CHECK_UPDATE_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    LogUtils.d(TAG, response);

                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        String title = jsonObject.has("title") ? jsonObject.getString("title") : "Update Available";
                        String releaseNote = jsonObject.has("release_note") ? jsonObject.getString("release_note") : "Update available, please update";
                        String notificationReleaseNote = jsonObject.has("notification_release_note") ? jsonObject.getString("notification_release_note") : "Update available, please update";
                        int currentVersionCode = jsonObject.has("version_code_current") ? jsonObject.getInt("version_code_current") : BuildConfig.VERSION_CODE;
                        int minVersionCodeMin = jsonObject.has("version_code_min") ? jsonObject.getInt("version_code_min") : BuildConfig.VERSION_CODE;

                        AppSharedPrefs.getInstance().setUpdateTitle(title);
                        AppSharedPrefs.getInstance().setUpdateReleaseNot(releaseNote);
                        AppSharedPrefs.getInstance().setUpdateCurrentVersion(currentVersionCode);
                        AppSharedPrefs.getInstance().setUpdateMinVersion(minVersionCodeMin);
                        AppSharedPrefs.getInstance().setUpdateNotificationReleaseNot(notificationReleaseNote);

                        notifyUser();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    notifyUser();
                }
            });

            Volley.newRequestQueue(getApplicationContext()).add(strReq);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void notifyUser()
    {
        try
        {
            if (calledFrom.equalsIgnoreCase(HomeActivity.class.getName()) && HomeActivity.isRunning)
            {
                Intent intent = new Intent();
                intent.setAction(ACTION_CHECK_UPDATE);

                sendBroadcast(intent);
            }
            else
            {
                Utils.showUpdateNotification(getApplicationContext());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
