package com.androiderstack.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androiderstack.listner.ConstantsLib;
import com.androiderstack.service.CheckUpdateService;
import com.androiderstack.utility.Utils;

public class UpdateCheckerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (intent != null
                    && intent.getAction() != null
                    && intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE"))
            {
                if (Utils.checkConnection(context))
                {
                    Intent intent1 = new Intent(context, CheckUpdateService.class);
                    intent1.putExtra(ConstantsLib.CALLED_FROM, UpdateCheckerReceiver.class.getName());
                    context.startService(intent1);
                }
                else
                {
                    Utils.showUpdateNotification(context);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
