package com.androiderstack.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

import com.androiderstack.item.IMPContacts;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class profileChangerReceiver extends BroadcastReceiver {

    public final String TAG = "profileChangerReciever";
    public final String RINGING = "RINGING";
    public final String IDLE = "IDLE";

    Context context;
    AudioManager audiomanager;
    List<IMPContacts> contactList;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;

        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        contactList = new ArrayList<>();

        String phoneState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

        LogUtils.e(TAG, "phoneState : " + phoneState);

        if(AppSharedPrefs.getInstance().isAppEnable())
        {
            switch (phoneState)
            {
                case RINGING:
                    ringingStateCalled(number);
                    break;

                case IDLE:
                    idleStateCalled();
                    break;
            }
        }
        else
        {
            LogUtils.e(TAG, "App is disabled");
        }
    }

    private void ringingStateCalled(String number)
    {
        number = Utils.removeInvalidSymbolsFromNumber2(context, number);

        AppSharedPrefs.getInstance().setRecentState(RINGING);

        getAllNoFromDatabaseToCheck();

        AppSharedPrefs.getInstance().setLastCall(number);

        for (int i = 0; i < contactList.size(); i++)
        {
            if (number.equalsIgnoreCase(contactList.get(i).getNumber()) || (number.endsWith(contactList.get(i).getNumber()) || contactList.get(i).getNumber().endsWith(number)))
            {
                boolean isRingingModeOn = audiomanager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
                boolean isVibrateModeOn = audiomanager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;

                if (isRingingModeOn || isVibrateModeOn)
                {
                    AppSharedPrefs.getInstance().setLastUserCall(number);
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    audiomanager.setStreamVolume(AudioManager.STREAM_RING, audiomanager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                }
            }
        }
    }

    private void idleStateCalled()
    {
        boolean autoSilent = true;

        LogUtils.e(TAG, AppSharedPrefs.getInstance().getRecentState());

        if (AppSharedPrefs.getInstance().getRecentState().equalsIgnoreCase(RINGING))
        {
            AppSharedPrefs.getInstance().setRecentState("");

            getAllNoFromDatabaseToCheck();

            String lastUserCall = AppSharedPrefs.getInstance().getLastUserCall();
            String lastCall = AppSharedPrefs.getInstance().getLastCall();
            //..................AutoChange Profile start.........................//
            if (autoSilent)
            {
                LogUtils.e(TAG, "lastUserCall : " + lastUserCall);

                for (int i = 0; i < contactList.size(); i++)
                {
                    if (lastUserCall.equalsIgnoreCase(contactList.get(i).getNumber()) || (lastUserCall.endsWith(contactList.get(i).getNumber()) || contactList.get(i).getNumber().endsWith(lastUserCall)))
                    {
                        LogUtils.e(TAG, "autoChange Success");
                        audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        AppSharedPrefs.getInstance().setLastUserCall("");
                    }
                }
            }
            //..................AutoChangeProfile Finish.......................//
        }
    }

    private void getAllNoFromDatabaseToCheck()
    {
        try
        {
            contactList.clear();
            contactList.addAll(AppController.getInstance().getDaoSession().getIMPContactsDao().loadAll());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}