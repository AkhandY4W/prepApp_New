package com.youth4work.prepapp.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.home.MyPrepActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.DeeplinkingManager;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.Date;
import java.util.List;

import bolts.AppLinks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity implements
        DeeplinkingManager.DeepLinkListener {


    private static final String IS_APP_FIRST_TIME_LAUNCH = "is_app_first_time_launch1";

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(SplashActivity.this);
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        if(isAppFirstTimeLaunch()){
            mUserManager.setUser(null);
        }
            String token = PreferencesManager.instance(SplashActivity.this).getToken()[0];
            if (token == null) {
                Constants.getAuthToken(SplashActivity.this);
            }
            Intent startingIntent = getIntent();
            Bundle pushData = startingIntent.getBundleExtra("push");
            if (pushData != null) {
                final AppEventsLogger logger = AppEventsLogger.newLogger(SplashActivity.this);
                logger.logPushNotificationOpen(pushData, startingIntent.getAction());
            }
            if (mUserManager.isUserLoggedIn()) {
                if (mUserManager.getEndDate().compareTo(new Date()) <= 0) {
                    mUserManager.getUser().setPrepPlanID(0);
                    mUserManager.setUser(mUserManager.getUser());
                }
                Constants.logLoginEventFb(mUserManager.getUser().getUserId(), mUserManager.getUser().getUserName(), SplashActivity.this);
                PrepApplication application = (PrepApplication) getApplication();
                Tracker mTracker = application.getDefaultTracker();
                mTracker.set("&uid", String.valueOf(mUserManager.getUser().getUserId()));
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("User Sign In")
                        .build());
                startActivity(new Intent(SplashActivity.this, MyPrepActivity.class));
                if (startingIntent.getStringExtra("deeplinkurl") != null) {
                    DeeplinkingManager.getInstance(SplashActivity.this).handleDeeplinkingSupport(getIntent().getStringExtra("deeplinkurl"));
                } else {
                    String appLinkAction = startingIntent.getAction();
                    Uri applinkTargetUrl =
                            AppLinks.getTargetUrlFromInboundIntent(SplashActivity.this, startingIntent);
                    if (applinkTargetUrl == null) {
                        applinkTargetUrl = startingIntent.getData();
                    }
                    if (Intent.ACTION_VIEW.equals(appLinkAction) && applinkTargetUrl != null) {
                        String deepLink = applinkTargetUrl.toString();
                        DeeplinkingManager.getInstance(SplashActivity.this).handleDeeplinkingSupport(deepLink);
                    }
                    DeeplinkingManager deepLinkManager = new DeeplinkingManager(SplashActivity.this, SplashActivity.this);
                    deepLinkManager.checkForInvites(true);
                    SplashActivity.this.finish();
                }

            } else {
                LoginActivity.show(self);
                if (startingIntent.getStringExtra("deeplinkurl") != null) {
                    DeeplinkingManager.getInstance(SplashActivity.this).handleDeeplinkingSupport(getIntent().getStringExtra("deeplinkurl"));
                } else {
                    String appLinkAction = startingIntent.getAction();
                    Uri applinkTargetUrl =
                            AppLinks.getTargetUrlFromInboundIntent(SplashActivity.this, startingIntent);
                    if (applinkTargetUrl != null) {
                    } else {
                        applinkTargetUrl = startingIntent.getData();

                    }
                    if (Intent.ACTION_VIEW.equals(appLinkAction) && applinkTargetUrl != null) {
                        String deepLink = applinkTargetUrl.toString();
                        DeeplinkingManager.getInstance(SplashActivity.this).handleDeeplinkingSupport(deepLink);

                    }
                    DeeplinkingManager deepLinkManager = new DeeplinkingManager(SplashActivity.this, SplashActivity.this);
                    deepLinkManager.checkForInvites(true);
                    SplashActivity.this.finish();
                }

            }

        }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {

    }

    @Override
    public void onAppNotFound() {

    }


    @Override
    public void onConnectionError(String errorMessage) {

    }

    // Custom method to check app is open first time or not
    protected boolean isAppFirstTimeLaunch(){
        if(mSharedPreferences.getBoolean(IS_APP_FIRST_TIME_LAUNCH,true)){
            // App is open/launch for first time
            // Update the preference
            mEditor.putBoolean(IS_APP_FIRST_TIME_LAUNCH,false);
            mEditor.commit();
            mEditor.apply();

            return true;
        }else {
            // App previously opened
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
    }

}
