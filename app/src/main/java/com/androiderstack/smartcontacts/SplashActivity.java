package com.androiderstack.smartcontacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by vishalchhodwani on 6/3/17.
 */
public class SplashActivity extends AppCompatActivity {

    private final String TAG = "SplashActivity";
    private final int SPLASH_TIMER = 3000; // 3 Seconds
    private Handler handler;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        /*| View.SYSTEM_UI_FLAG_FULLSCREEN*/);

        setContentView(R.layout.splash_activity);

        init();

        startSplash();
    }

    private void init()
    {
        context = SplashActivity.this;

        handler = new Handler(getMainLooper());
    }

    private void startSplash() {
        handler.postDelayed(splashRunnable, SPLASH_TIMER);

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(splashRunnable);
        super.onBackPressed();
    }

    Runnable splashRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
