package com.android.internal.telephony;

/**
 * Created by gst-10064 on 7/6/16.
 */
public interface ITelephony {

    boolean endCall();
    void answerRingingCall();
    void silenceRinger();

}
