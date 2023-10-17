package com.youth4work.prepapp.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.youth4work.prepapp.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static void logThis(String TAG, @Nullable String message, @Nullable Throwable error) {
        if (message != null) { // validate message
            if (error != null) { // validate throwable error
                Log.e(TAG, message, error);
            } else { // if no error just show a message
                Log.d(TAG, message);
            }
        }
    }

    public static void showToast(Context context, @Nullable String message, int duration, boolean inDebugOnly) {

        // validate duration
        if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG) {
            duration = Toast.LENGTH_SHORT;
        }

        // validate message
        if (message != null) {
            if (inDebugOnly && isDebug()) { // show only in debug config
                Toast.makeText(context, message, duration).show();
            } else if (!inDebugOnly) { // show in debug and release configs
                Toast.makeText(context, message, duration).show();
            }
        }
    }

    public static boolean checkForInternetConnection(@NonNull Context context, String TAG) {

        boolean internetIsAvailable = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            //validate internet connection
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable()) {
                internetIsAvailable = true;
            }
        } catch (Exception e) {
            logThis(TAG, "checkForInternetConnection Exception " + e.getMessage(), e);
        }

        return internetIsAvailable;
    }


    @Nullable
    public static Typeface getTypeFace(@Nullable Context context, String fontName) {
        Typeface typeface = null;
        if (context != null) {
            typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        }
        return typeface;
    }

    @Nullable
    public static TypedArray getTypedArrayResource(@Nullable Context context, int resID) {
        TypedArray typedArray = null;
        try {
            if (context != null) {
                try {
                    typedArray = context.getResources().obtainTypedArray(resID);
                } catch (Resources.NotFoundException e) {
                    logThis(TAG, "getAppResources Resources.NotFoundException", e);
                }
            }
        } catch (Exception e) {
            logThis(TAG, "getTypedArrayResource Exception", e);
        }
        return typedArray;
    }


    public static String[] getStringArray(@NonNull Context context, int arrayResId) {
        return context.getResources().getStringArray(arrayResId);
    }

    public static int[] getIntArray(@NonNull Context context, int arrayResId) {
        return context.getResources().getIntArray(arrayResId);
    }


    @Nullable
    public static ArrayList<String> convertStringArrayToArrayList(@Nullable String[] stringArray) {
        if (stringArray != null && stringArray.length > 0) {
            return new ArrayList<>(Arrays.asList(stringArray));
        } else {
            return null;
        }
    }


    @NonNull
    public static String getContentFromClipboard(@NonNull Context context) {

        // initialize paste data string
        String pasteData = "";

        // get data from clip board
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();
        } catch (Exception e) {
            Utils.logThis(TAG, "getContentFromClipboard Exception", e);
        }

        return pasteData;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestMultiplePermissions(@NonNull Activity activity, @NonNull String[] permissionsList, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // init permissions list
            List<String> permissions = new ArrayList<>();

            // loop through permissions
            for (String permission : permissionsList) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(permission);
                }
            }

            // if permissions list is not empty, request permission
            if (!permissions.isEmpty()) {
                activity.requestPermissions(permissions.toArray(new String[permissions.size()]), requestCode);
            }
        }
    }


    @Nullable
    public static ProgressDialog initProgressDialog(@Nullable Context context, @Nullable String message, boolean isCancelable, boolean isIndeterminate) {
        ProgressDialog loadingDialog = null;
        try {
            if (context != null) {
                if (message == null) {
                    message = "Loading. Please wait...";
                }

                loadingDialog = new ProgressDialog(context);
                loadingDialog.setCancelable(isCancelable);
                loadingDialog.setMessage(message);
                loadingDialog.setIndeterminate(isIndeterminate);
            }
        } catch (Exception e) {
            logThis(TAG, "initProgressDialog Exception", e);
        }
        return loadingDialog;
    }

    public static void dismissProgressDialog(@Nullable ProgressDialog progressDialog) {
        // dismiss the loading dialog
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static Bitmap getBitmapByResourceID(@NonNull Context context, int resID) {
        return BitmapFactory.decodeResource(context.getResources(), resID);
    }


    public static void openShareTextIntent(@NonNull Context context, String stringTitle, String stringContent, String shareDialogTitle) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, stringContent);
            sendIntent.putExtra(Intent.EXTRA_TITLE, stringTitle);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, shareDialogTitle));
        } catch (Exception e) {
            Utils.logThis(TAG, "openShareTextIntent Exception", e);
        }
    }

    public static void openCallIntent(@NonNull Activity activity, String phoneNumber, int codeRequest) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(phoneNumber));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android M+ Check Permission
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    activity.startActivity(callIntent);
                } else {
                    Utils.requestMultiplePermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, codeRequest);
                }
            } else { // Android Pre-M
                activity.startActivity(callIntent);
            }
        } catch (ActivityNotFoundException e) {
            Utils.logThis(TAG, "Calling failed", e);
        }
    }

    public static void openURLIntent(@NonNull Context context, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(intent);
        } catch (Exception e) {
            Utils.logThis(TAG, "Unable to open website!", e);
        }
    }

    public static void openEmailIntent(@NonNull Context context, String emailAddress, String emailSubject) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            emailIntent.setType("message/rfc822");
            context.startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
        } catch (Exception e) {
            Utils.logThis(TAG, "Unable to open email application!", e);
        }
    }


}