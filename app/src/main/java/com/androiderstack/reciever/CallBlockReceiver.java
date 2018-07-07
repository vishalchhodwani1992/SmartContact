package com.androiderstack.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.androiderstack.item.BlockContacts;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gst-10064 on 6/6/16.
 */
public class CallBlockReceiver extends BroadcastReceiver {

    Context context;
    AudioManager audiomanager;
    List<BlockContacts> data;
    public final String TAG = "CallBlockReceiver";
    ITelephony iTelephony;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        LogUtils.e(TAG, "check 1");
        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        data = new ArrayList<>();
        this.context = context;

        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

        String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        number = Utils.removeInvalidSymbolsFromNumber2(context, number);

        disConnectNumber(number, stateStr);

        LogUtils.e("check", "state :" +stateStr);
    }

    private void disConnectNumber(String number, String stateStr)
    {
        try
        {
            if(stateStr.equalsIgnoreCase("RINGING"))
            {
                LogUtils.e(TAG, "check 2");

                AppSharedPrefs.getInstance().setRecentState("RINGING");

                getAllNoFromDatabaseToCheck();

                AppSharedPrefs.getInstance().setLastCall(number);

                for(int i = 0 ; i<data.size() ; i++)
                {
                    LogUtils.e(TAG, "check 3");
                    if(number.equalsIgnoreCase(data.get(i).getNumber()) || (number.endsWith(data.get(i).getNumber()) ||  data.get(i).getNumber().endsWith(number)))
                    {
                        LogUtils.e(TAG, "check 4");
                        // code to cut the call;
                        rejectCall(number);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getAllNoFromDatabaseToCheck()
    {
        try
        {
            data.clear();
            data.addAll(AppController.getInstance().getDaoSession().getBlockContactsDao().loadAll());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void rejectCall(String phoneNumber)
    {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            iTelephony = (ITelephony) m.invoke(tm);
            LogUtils.e("INCOMING", phoneNumber);
            if ((phoneNumber != null)) {
                LogUtils.e(TAG, "check 7");
                iTelephony.endCall();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
