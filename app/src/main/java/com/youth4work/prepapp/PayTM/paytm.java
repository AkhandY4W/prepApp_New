package com.youth4work.prepapp.PayTM;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.Iterator;


/**
 * Created by jagbros-4(Amit) on 14-Jun-17.
 */

public class paytm {

    protected static synchronized String getURLEncodedStringFromBundle(Bundle inParams) {
        try {
            debugLog("Extracting Strings from Bundle...");
            boolean inEx = true;
            StringBuffer Params = new StringBuffer();
            Iterator i$ = inParams.keySet().iterator();

            while(i$.hasNext()) {
                String Key = (String)i$.next();
                if(inEx) {
                    inEx = false;
                } else {
                    Params.append("&");
                }

                Params.append(URLEncoder.encode(Key, "UTF-8"));
                Params.append("=");
                Params.append(URLEncoder.encode(inParams.getString(Key), "UTF-8"));
            }

            debugLog("URL encoded String is " + Params.toString());
            return Params.toString();
        } catch (Exception var5) {
            printStackTrace(var5);
            return null;
        }
    }


    protected static String getJSONString(Bundle inParams) {
        try {
            JSONObject inEx = null;
            if (inParams != null && inParams.size() > 0) {
                inEx = new JSONObject();
                Iterator i$ = inParams.keySet().iterator();

                while (i$.hasNext()) {
                    String Key = (String) i$.next();
                    inEx.put(Key, inParams.get(Key));
                }
            }

            debugLog("JSON string is " + inEx);
            return inEx.toString();
        } catch (Exception var4) {
            printStackTrace(var4);
            return null;
        }
    }
    protected static synchronized boolean isNetworkAvailable(Context inContext) {
        ConnectivityManager ConnectMgr = (ConnectivityManager)inContext.getSystemService("connectivity");
        if(ConnectMgr == null) {
            return false;
        } else {
            NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
            return NetInfo == null?false:NetInfo.isConnected();
        }
    }
    protected static synchronized void debugLog(String inMsg) {
        Log.d("PGSDK", inMsg);
    }
    protected static synchronized String getStringFromBundle(Bundle inParams) {
        try {
            debugLog("Extracting Strings from Bundle...");
            boolean inEx = true;
            StringBuffer Params = new StringBuffer();
            Iterator i$ = inParams.keySet().iterator();

            while(i$.hasNext()) {
                String Key = (String)i$.next();
                if(inEx) {
                    inEx = false;
                } else {
                    Params.append("&");
                }

                Params.append(Key);
                Params.append("=");
                Params.append(inParams.getString(Key));
            }

            debugLog("Extracted String is " + Params.toString());
            return Params.toString();
        } catch (Exception var5) {
            printStackTrace(var5);
            return null;
        }
    }
    protected static synchronized void printStackTrace(Exception inEx) {
        inEx.printStackTrace();
    }


}
