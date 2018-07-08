package com.androiderstack.listner;

import android.Manifest;

import java.util.ArrayList;

/**
 * Created by vishalchhodwani on 15/3/17.
 */
public interface ConstantsLib {

    int SETTING_DIALOG_REQUEST = 100;
    int CONTACT_READ_PERMISSION = 1001;

    String PLAY_URL = "http://play.google.com/store/apps/details?id=%s";
    String CHECK_UPDATE_URL = "http://androiderstack.com/api/check_update_available.php";
    ArrayList<String> DEVICE_LIST = new ArrayList<String>() {{
        add("xiaomi");
        add("oppo");
        add("vivo");
    }};

    String[] STORAGE_PERMISSIONS  = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] RECORDING_PERMISSIONS  = new String[]{Manifest.permission.RECORD_AUDIO};

}
