package com.androiderstack.smartcontacts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androiderstack.custom_view.MyActionbar;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

/**
 * Created by vishalchhodwani on 18/8/17.
 */
public class AboutActivity extends AppCompatActivity {

    private final String TAG = "AboutActivity";

    WebView bioGraphyWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_layout);

        initializeView();

        addActionBar();

        setData();
    }



    private void addActionBar()
    {
        try
        {
            MyActionbar myActionbar = new MyActionbar(AboutActivity.this);
//            myActionbar.setClickListener(this);
            myActionbar.setActionbar("About Smart Contacts", true, false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setData() {

        try
        {
            bioGraphyWebView.setScrollContainer(false);
            bioGraphyWebView.setBackgroundColor(Color.TRANSPARENT);
            bioGraphyWebView.getSettings().setDomStorageEnabled(true);
            bioGraphyWebView.getSettings().setSupportZoom(true);
            bioGraphyWebView.getSettings().setJavaScriptEnabled(true);
            bioGraphyWebView.getSettings().setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            bioGraphyWebView.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView view, String url)
                {
                    LogUtils.e(TAG, "onPageFinished() called");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    LogUtils.v("N", "URL ::" + url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

            });

            String data = Utils.readHtml(AboutActivity.this);

            bioGraphyWebView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initializeView() {

        try
        {
            bioGraphyWebView = (WebView) findViewById(R.id.aboutUs_webView);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
