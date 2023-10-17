package com.youth4work.prepapp.util;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.ui.forum.PrepForumDetails;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.home.InviteFriendActivity;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.startup.SignUpActivity;
import com.youth4work.prepapp.ui.startup.SplashActivity;

public class DeeplinkingManager implements GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<AppInviteInvitationResult> {

    private static DeeplinkingManager mInstance = null;
    private final static String TAG = DeeplinkingManager.class.getName();
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity context;
    private DeepLinkListener deepLinkListener;

    public interface DeepLinkListener{
        void onConnectionError(String errorMessage);
    }
    public  DeeplinkingManager(FragmentActivity activity,DeepLinkListener linkListener) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(AppInvite.API)
                .enableAutoManage(activity, this)
                .build();

        this.context = activity;
        this.deepLinkListener = linkListener;
    }
    private DeeplinkingManager()
    {

    }
    public static DeeplinkingManager getInstance(FragmentActivity context) {
        if(mInstance == null) {
            mInstance = new DeeplinkingManager();
        }
        mInstance.context = context;
        return mInstance;
    }
    public void checkForInvites(boolean autoLaunchDeepLink)
    {
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, context, autoLaunchDeepLink)
                .setResultCallback(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }


    @Override
    public void onResult(@NonNull AppInviteInvitationResult result) {
        Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
        if (result.getStatus().isSuccess()) {
            // Extract information from the intent
            Intent intent = result.getInvitationIntent();
            String deepLink = AppInviteReferral.getDeepLink(intent);
            String invitationId = AppInviteReferral.getInvitationId(intent);
            // Because autoLaunchDeepLink = true we don't have to do anything
            // here, but we could set that to false and manually choose
            // an Activity to launch to handle the deep link here.
            handleDeeplinkingSupport(deepLink.toLowerCase());
        }

    }

    public boolean handleDeeplinkingSupport(String mUriPath) {
        Intent intent = new Intent(context, SplashActivity.class);
        UserManager mUserManager = UserManager.getInstance(context);
        boolean flag = false;
        mUriPath=mUriPath.toLowerCase();
        try {

            if (mUriPath != null && mUserManager.isUserLoggedIn()) {
                Log.d(TAG, "Path is: " + mUriPath);

                if (mUriPath.startsWith("app") || mUriPath.startsWith("http") || mUriPath.startsWith("https")) {
                    if (mUriPath.contains("forum/")) {
                        intent = new Intent(context, DashboardActivity.class);
                        String[] arr = mUriPath.split("forum/");
                        intent.putExtra("menuFragment", "2");
                        if (arr.length >= 2) {
                            intent.putExtra("Catid", arr[1]);

                        }
                        flag = true;
                        launchScreen(intent);
                    } else if (mUriPath.contains("forumdetails/")) {
                        String[] arr = mUriPath.split("forumdetails/");
                        if (arr.length >= 2) {
                            intent = new Intent(context, PrepForumDetails.class);
                            intent.putExtra("obj", Integer.parseInt(arr[1]));

                        }
                        flag = true;
                        launchScreen(intent);
                    } else if (mUriPath.contains("starttest/")) {
                        intent = new Intent(context, DashboardActivity.class);
                        String[] arr = mUriPath.split("starttest/");
                        intent.putExtra("menuFragment", "0");
                        if (arr.length >= 2) {
                            intent.putExtra("Catid", arr[1]);

                        }
                        flag = true;
                        launchScreen(intent);
                    } else if (mUriPath.contains("reviewtest/")) {
                        intent = new Intent(context, DashboardActivity.class);
                        String[] arr = mUriPath.split("reviewtest/");
                        intent.putExtra("menuFragment", "1");
                        if (arr.length >= 2) {
                            intent.putExtra("Catid", arr[1]);

                        }
                        flag = true;
                        launchScreen(intent);
                    } else if (mUriPath.contains("upgrade/")) {
                        intent = new Intent(context, UpgradePlanActivity.class);
                        if (mUriPath.contains("/promo/")) {
                            String[] arr = mUriPath.split("/promo/");
                            if (arr.length >= 2) {
                                String promocode = arr[1];
                                intent.putExtra("promocode", promocode);
                            }
                        }
                        flag = true;
                        launchScreen(intent);
                    }
                    else if (mUriPath.contains("refer/")) {
                        intent = new Intent(context, InviteFriendActivity.class);
                        flag = true;
                        launchScreen(intent);
                    }
                }

            } else if (mUriPath != null && mUriPath.contains("signup/")) {
                intent = new Intent(context, SignUpActivity.class);
                flag = true;
                launchScreen(intent);
            } else {
                Log.d(TAG, "No data in intent");

            }


        } catch (Exception e) {
            Log.d(TAG, "Something went wrong");

        }
        return flag;
    }
    private void launchScreen(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if (context != null)
            context.finish();
    }
}