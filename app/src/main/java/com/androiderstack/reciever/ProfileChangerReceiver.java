package com.androiderstack.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.androiderstack.item.IMPContacts;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProfileChangerReceiver extends BroadcastReceiver {

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
                    audiomanager.setStreamVolume(AudioManager.STREAM_RING, audiomanager.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        Utils.playSound(context);
                    sendDataToFirebase(contactList.get(i), "RINGER_MODE_NORMAL", RINGING);
                    break;
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
                        {
                            audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            sendDataToFirebase(contactList.get(i), "RINGER_MODE_SILENT", IDLE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                Utils.stopSound(context);
                        }

                        if (AppSharedPrefs.getInstance().wasVibrate())
                        {
                            audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            sendDataToFirebase(contactList.get(i), "RINGER_MODE_VIBRATE", IDLE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                Utils.stopSound(context);
                        }

                        AppSharedPrefs.getInstance().setLastUserCall("");
                        AppSharedPrefs.getInstance().setWasVibrate(false);
                        AppSharedPrefs.getInstance().setWasSilent(false);
                        Utils.loadInertialAd(context);
                        break;
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

    private void sendDataToFirebase(IMPContacts impContacts, String ringerMode, String state) {
        try
        {
            Bundle bundle = new Bundle();
            bundle.putString("ContactName", impContacts.getName());
            bundle.putString("ContactNumber", impContacts.getNumber());
            bundle.putString("State", state);
            bundle.putString("RingerMode", ringerMode);
            Utils.sendEventToFirebase("PROFILE_CHANGED", bundle);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}