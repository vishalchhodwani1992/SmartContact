package com.androiderstack.utility;

import android.util.Log;

import com.androiderstack.smartcontacts.BuildConfig;

/**
 * Created by vishalchhodwani on 6/4/17.
 */
public class LogUtils {

    public static final String TAG = "LogUtils";

    /**
     * DEBUG is true the log will print otherwise not.
     */

    public static boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String msg){
        if(DEBUG)
            Log.i(tag, msg);

    }

    public static void i(String tag, String msg, Throwable tr){
        if(DEBUG)
            Log.i(tag, msg, tr);
    }


    public static void v(String tag, String msg){
        if(DEBUG)
            Log.v(tag, msg);

    }

    public static void d(String tag, String msg){
        if(DEBUG)
            Log.v(tag, msg);

    }

    public static void v(String tag, String msg, Throwable tr){
        if(DEBUG)
            Log.v(tag, msg, tr);
    }

    public static void e(String tag, String msg){
        if(DEBUG)
            Log.e(tag, msg);

    }

    public static void e(String tag, String msg, Throwable tr){
        if(DEBUG)
            Log.e(tag, msg, tr);
    }



}
