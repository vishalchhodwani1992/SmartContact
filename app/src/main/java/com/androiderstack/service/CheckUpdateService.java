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
import com.androiderstack.utility.LogUtils;

public class CheckUpdateService extends IntentService {

    public static final String ACTION_CHECK_UPDATE = "com.androiderstack.service.CheckUpdateService$ACTION_CHECK_UPDATE";
    private final String TAG = "CheckUpdateService";

    public CheckUpdateService()
    {
        super("CheckUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        checkForUpdate();
    }

    private void checkForUpdate()
    {
        try
        {
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    ConstantsLib.CHECK_UPDATE_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    LogUtils.d(TAG, response);

                    Intent intent = new Intent();
                    intent.setAction(ACTION_CHECK_UPDATE);
                    intent.putExtra("data", response);

                    sendBroadcast(intent);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtils.e(TAG, "Error: " + error.getMessage());
                }
            });

            Volley.newRequestQueue(getApplicationContext()).add(strReq);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
