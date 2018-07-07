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
        if (intent != null
                && intent.getAction() != null
                && intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE"))
        {
            this.context = context;

            if(AppSharedPrefs.getInstance().isAppEnable())
            {
                audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                contactList = new ArrayList<>();

                String phoneState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                LogUtils.e(TAG, "phoneState : " + phoneState);


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
        }
    }

    private void ringingStateCalled(String number)
    {
        number = Utils.removeInvalidSymbolsFromNumber2(context, number);

        getAllNoFromDatabaseToCheck();

        for (int i = 0; i < contactList.size(); i++)
        {
            if (number.equalsIgnoreCase(contactList.get(i).getNumber()) || (number.endsWith(contactList.get(i).getNumber()) || contactList.get(i).getNumber().endsWith(number)))
            {
                boolean isDeviceSilent = audiomanager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
                boolean isDeviceVibrate = audiomanager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;

                if (isDeviceSilent || isDeviceVibrate)
                {
                    AppSharedPrefs.getInstance().setLastCall(number);
                    AppSharedPrefs.getInstance().setWasSilent(isDeviceSilent);
                    AppSharedPrefs.getInstance().setWasVibrate(isDeviceVibrate);
                    AppSharedPrefs.getInstance().setRecentState(RINGING);
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
                        if (AppSharedPrefs.getInstance().wasSilent())
                            audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                        if (AppSharedPrefs.getInstance().wasVibrate())
                            audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                        AppSharedPrefs.getInstance().setLastUserCall("");
                        AppSharedPrefs.getInstance().setWasVibrate(false);
                        AppSharedPrefs.getInstance().setWasSilent(false);
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