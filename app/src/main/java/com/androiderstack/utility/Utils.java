package com.androiderstack.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.androiderstack.custom_view.StyledEditText;
import com.androiderstack.listner.ConstantsLib;
import com.androiderstack.listner.DialogClickListener;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.smartcontacts.BuildConfig;
import com.androiderstack.smartcontacts.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

/**
 * Created by vishalchhodwani on 8/2/17.
 */
public class Utils {

    private static final String TAG = "Utils";

    public static String removeInvalidSymbolsFromNumber2(Context context, String phoneNumber)
    {
        String formattedNumber = "";

        try
        {
            if (phoneNumber == null || phoneNumber.equalsIgnoreCase(""))
                return formattedNumber;

            TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkCountryIso = telemamanger.getNetworkCountryIso();

            if (networkCountryIso == null || networkCountryIso.equalsIgnoreCase(""))
                networkCountryIso = "IN";

            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, networkCountryIso.toUpperCase());
            formattedNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return formattedNumber;
    }


    public static boolean checkPermissions(Activity activity, String...permission)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                for (int i = 0; i < permission.length ; i++)
                {
                    if(ActivityCompat.checkSelfPermission(activity, permission[i])
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        return false;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static void grantPermission(Activity activity, String[] permissions, int requestCode)
    {
        try
        {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        try
        {
            for (int i = 0; i < grantResults.length; i++)
            {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return true;
    }


    public static void setupUI(final AppCompatActivity activity, View view) {

        try {
            // Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof StyledEditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(activity);
                        return false;
                    }
                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(activity, innerView);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void hideSoftKeyboard(AppCompatActivity activity, StyledEditText editText) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        editText.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showKeyboard(AppCompatActivity activity)
    {
        try
        {
            if (activity.getCurrentFocus() != null)
            {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void showKeyboard(AppCompatActivity activity, StyledEditText editText)
    {
        try
        {
            if (activity.getCurrentFocus() != null)
            {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void writeData(Context context, String data, String fileName) {
        FileOutputStream fout = null;
        try {
            //getting output stream
            fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            //writng data
            fout.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                //closing the output stream
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String readData(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static void shareApp(AppCompatActivity activity)
    {
        try
        {
            try
            {
                final String appPackageName = activity.getPackageName();
                final String playUrl = String.format(ConstantsLib.PLAY_URL,appPackageName);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.share_text, activity.getResources().getString(R.string.app_name), playUrl));
                intent.setType("text/plain");
                activity.startActivity(Intent.createChooser(intent, "Share using"));
            }
            catch (ActivityNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void openRatingPage(AppCompatActivity activity)
    {
        try
        {
            final String appPackageName = activity.getPackageName();
            Uri uri = Uri.parse("market://details?id=" + appPackageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                activity.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static boolean hasIceCreamSandwich() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return true;
        else
            return false;
    }


    public static String readHtml(Context context) throws IOException {

        InputStream is = context.getAssets().open("about_app.html");
        int size = is.available();

        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        String str = new String(buffer);
        str = str.replace("Version", "Version:"+ BuildConfig.VERSION_NAME);

        return str;
    }


    public static void showAlert(Context context, String title, String message,
                                 String positiveText,
                                 String negativeText, final int requestCode,
                                 final DialogClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setCancelable(false);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if (!positiveText.equalsIgnoreCase(""))
            alertDialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        if (!negativeText.equalsIgnoreCase(""))
            alertDialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickListener.onDialogClick(which, requestCode);
                }
            });

        alertDialog.show();
    }

    public static void openAutoStartSettingForAll(AppCompatActivity activity)
    {
        try
        {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            }

            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                activity.startActivity(intent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static void getUserId()
    {
        try
        {
            String userId = AppSharedPrefs.getInstance().getUserId();

            if (userId.equalsIgnoreCase(""))
            {
                AppSharedPrefs.getInstance().setUserId(Utils.getUniqueId());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String getUniqueId()
    {
        try
        {
            return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        try
        {
            return String.valueOf(System.currentTimeMillis());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    public static void sendEventToFirebase(String eventName, Bundle bundle)
    {
        try
        {
            bundle.putString("UserId", AppSharedPrefs.getInstance().getUserId());
            bundle.putString("EmailId", AppSharedPrefs.getInstance().getEmailId());
            AppController.getInstance().getFirebaseAnalytics().logEvent(eventName.toUpperCase(), bundle);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void loadInertialAd(Context context)
    {
        try
        {
            final InterstitialAd interstitial = new InterstitialAd(context);
            AdRequest adRequest = new AdRequest.Builder()

                    // Add a test device to show Test Ads
                    .build();

            // Load ads into Interstitial Ads
            interstitial.loadAd(adRequest);

            // Prepare an Interstitial Ad Listener
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    interstitial.show();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static boolean checkConnection(Context context)
    {
        try {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr == null)
                return false;

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return false;
    }
}
