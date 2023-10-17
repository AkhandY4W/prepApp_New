package com.youth4work.prepapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.workmail.manager.LruBitmapCache;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.security.auth.x500.X500Principal;

public class PrepApplication extends MultiDexApplication {

    ///
    // Constants
    ///

    public static final String TAG = "Prep-";
    private static final String PREFERENCES_KEY = "com.youth4work.prepapp";
    private static final String PACKAGE_NAME = "com.youth4work.prepapp";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    ///
    // Static variables.
    ///
    private static PrepApplication sSingleton;

    ///
    // Data members
    ///

    private BaseActivity mActiveBaseActivity;
    private String mVersion;
    private int mBuildNumber = -1;
    private Tracker mTracker;

    /**
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            //To remove on publish
            if (BuildConfig.DEBUG) {
                // do something for a debug build
                analytics.setDryRun(true);
            }

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);

        }
        return mTracker;
    }

    ///
    // Singleton
    ///
    public static PrepApplication singleton() {
        // NOTE: The instance is created by the system.
        return sSingleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sSingleton = this;
//        Fabric.with(this, new Crashlytics());
        Iconify.with(new FontAwesomeModule());
        //FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build());

//        io.intercom.android.sdk.Intercom.initialize(this, "android_sdk-33ed94e524f0ab539f755e6eb0b4793838281be6", "lw0w9vsu");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    ///
    // Returns the version number.
    ///
    public String getVersionNumber() {
        if (mVersion == null) {
            try {
                PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                mVersion = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // do nothing
            }
        }

        return mVersion;
    }


    ///
    // Returns the build number.
    ///
    public int getBuildNumber() {
        if (mBuildNumber == -1) {
            try {
                PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                mBuildNumber = pi.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                // do nothing
            }
        }

        return mBuildNumber;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "DEFAULT" : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void clearCache(String url){
        if (mRequestQueue != null) {
            mRequestQueue.getCache().clear();
        }
    }

    public void removeCache(String url){
        if (mRequestQueue != null) {
            mRequestQueue.getCache().remove(url);
        }
    }

    public void invalidateCache(String url){
        if (mRequestQueue != null) {
            mRequestQueue.getCache().invalidate(url, true);
        }
    }
    ///
    // Get Device Token
    ///
    public String getDeviceToken() {

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    ///
    // Set DeviceToken from GCM registration
    ///
    public void setDeviceToken(String deviceToken) {
    }

    ///
    // Returns true if this is a debug build.
    ///
    public boolean isDebugBuild() {
        final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

        boolean debuggable = false;
        Context ctx = getContext();

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);

                if (debuggable) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            // debuggable variable will remain false
        } catch (CertificateException e) {
            // debuggable variable will remain false
        }

        return debuggable;
    }


    ///
    // Returns true if only portrait orientation is allowed.
    ///
    public boolean forcePortraitOrientation() {
        return !isDebugBuild();
    }


    ///
    // Get the shared App's context. Use this context when an activity's context
    // won't do (background tasks, to survive screen rotations, activity isn't easily
    // attained etc.)
    ///
    public Context getContext() {
        return getApplicationContext();
    }

    ///
    // Returns the active activity.
    ///
    public BaseActivity getActiveBaseActivity() {
        return mActiveBaseActivity;
    }

    ///
    // Called to track the active BaseActivity.
    ///
    public void setActiveBaseActivity(BaseActivity activity) {
        mActiveBaseActivity = activity;
    }

    ///
    // To get hash key of key store file using which app is signed (either debug or release)
    ///
    @Nullable
    public String getHashKey() {
        PackageInfo info;
        String hashKey = null;

        try {
            info = getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = new String(Base64.encode(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            hashKey = null;
        } catch (NoSuchAlgorithmException e) {
            hashKey = null;
        } catch (Exception e) {
            hashKey = null;
        }
        return hashKey;
    }

}
