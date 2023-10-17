package com.youth4work.prepapp.ui.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.Tracker;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.util.Constants;

//import com.google.android.gms.analytics.Tracker;

public class SignUp extends AppCompatActivity {

    String SIGNUP_URL = "http://www.youth4work.com/Users/Signup";
    ProgressDialog pd;

private Tracker mTracker;
    WebView webView_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker,"SignUp Activity");

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait Loading...");
        pd.setCancelable(false);
        pd.show();

        setContentView(R.layout.activity_sign_up2);
        webView_signup = (WebView) findViewById(R.id.webView);
        webView_signup.loadUrl(SIGNUP_URL);
        webView_signup.setWebViewClient(new HelpClient());
        WebSettings webSettings = webView_signup.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HelpClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!pd.isShowing()) {
                pd.show();
            }

            if (Uri.parse(url).getHost().equals(SIGNUP_URL)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }

    }
}




