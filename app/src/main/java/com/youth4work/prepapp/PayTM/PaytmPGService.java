package com.youth4work.prepapp.PayTM;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.URLUtil;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */

public class PaytmPGService {

        private static volatile PaytmPGService mService;
        public volatile PaytmOrder mOrder;
        public volatile PaytmMerchant mMerchant;
        public volatile PaytmClientCertificate mCertificate;
        private volatile boolean mbServiceRunning;
        private volatile String mStatusQueryURL;
        protected volatile String mCASURL;
        protected volatile String mCancelTransactionURL;
        protected volatile String mPGURL;
        private static final String STAGING_STATUS_QUERY_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/TXNSTATUS";
        private static final String PRODUCTION_STATUS_QUERY_URL = "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS";
        private static final String STAGING_CAS_URL = "https://pguat.paytm.com:8448/CAS/ChecksumGenerator";
        private static final String PRODUCTION_CAS_URL = "https://secure.paytm.in/oltp-web/generateChecksum";
        private static final String STAGING_CANCEL_TRANSACTION_URL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/CANCEL_TXN";
        private static final String PRODUCTION_CANCEL_TRANSACTION_URL = "https://secure.paytm.in/oltp/HANDLER_INTERNAL/CANCEL_TXN";
        private static final String STAGING_PG_URL = "https://pguat.paytm.com/oltp-web/processTransaction";
        private static final String PRODUCTION_PG_URL = "https://secure.paytm.in/oltp-web/processTransaction";
        public volatile PaytmPaymentTransactionCallback mPaymentTransactionCallback;

        protected volatile PaytmStatusQueryCallback mStatusQueryCallback;

        public PaytmPGService() {
        }

        public static synchronized PaytmPGService getService() {
            try {
                if(mService == null) {
                    paytm.debugLog("Creating an instance of Paytm PG Service...");
                    mService = new PaytmPGService();
                    paytm.debugLog("Created a new instance of Paytm PG Service.");
                }
            } catch (Exception var1) {
                paytm.printStackTrace(var1);
            }

            return mService;
        }

        public static synchronized PaytmPGService getStagingService() {
            PaytmPGService PGService = getService();
            PGService.mStatusQueryURL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/TXNSTATUS";
            PGService.mCASURL = "https://pguat.paytm.com:8448/CAS/ChecksumGenerator";
            PGService.mCancelTransactionURL = "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/CANCEL_TXN";
            PGService.mPGURL = "https://pguat.paytm.com/oltp-web/processTransaction";
            return PGService;
        }

        public static synchronized PaytmPGService getProductionService() {
            PaytmPGService PGService = getService();
            PGService.mStatusQueryURL = "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS";
            PGService.mCASURL = "https://secure.paytm.in/oltp-web/generateChecksum";
            PGService.mCancelTransactionURL = "https://secure.paytm.in/oltp/HANDLER_INTERNAL/CANCEL_TXN";
            PGService.mPGURL = "https://secure.paytm.in/oltp-web/processTransaction";
            return PGService;
        }

        public synchronized void initialize(PaytmOrder inOrder, PaytmMerchant inMerchant, PaytmClientCertificate inCertificate) {
            this.mOrder = inOrder;
            this.mMerchant = inMerchant;
            this.mCertificate = inCertificate;
        }

        public void enableLog(Context ctx) {
            ApplicationInfo appInfoo = this.getApplicationinfo(ctx);
            if(appInfoo != null) {
                boolean isDebuggable = 0 != (appInfoo.flags &= 2);
                Log.setEnableDebugLog(isDebuggable);
            } else {
                Log.setEnableDebugLog(false);
            }

        }

        public synchronized void startPaymentTransaction(Context inCtxt, boolean inbHideHeader, boolean inbSendAllChecksumResponseParametersToPGServer, PaytmPaymentTransactionCallback inPaymentTransactionCallback) {
            try {
                this.enableLog(inCtxt);
                if(paytm.isNetworkAvailable(inCtxt)) {
                    if(!this.mbServiceRunning) {
                        Bundle var10 = new Bundle();
                        if(this.mOrder != null) {
                            Iterator PG1 = this.mOrder.getRequestParamMap().entrySet().iterator();

                            while(PG1.hasNext()) {
                                Map.Entry PG = (Map.Entry)PG1.next();
                                String paramName = (String)PG.getKey();
                                String paramValue = (String)PG.getValue();
                                paytm.debugLog(paramName + " = " + paramValue);
                                var10.putString((String)PG.getKey(), (String)PG.getValue());
                            }
                        }

                        if(this.mMerchant != null && this.mMerchant.mChecksumGenerationURL != null && this.mMerchant.mChecksumGenerationURL.length() > 0) {
                            this.mCASURL = this.mMerchant.mChecksumGenerationURL;
                        }

                        paytm.debugLog("Starting the Service...");
                        Intent PG11 = new Intent(inCtxt, PaytmPGActivity.class);
                        PG11.putExtra("Parameters", var10);
                        PG11.putExtra("HIDE_HEADER", inbHideHeader);
                        PG11.putExtra("SEND_ALL_CHECKSUM_RESPONSE_PARAMETERS_TO_PG_SERVER", inbSendAllChecksumResponseParametersToPGServer);
                        this.mbServiceRunning = true;
                        this.mPaymentTransactionCallback = inPaymentTransactionCallback;
                        ((Activity)inCtxt).startActivity(PG11);
                        paytm.debugLog("Service Started.");
                    } else {
                        paytm.debugLog("Service is already running.");
                    }
                } else {
                    this.stopService();
                    inPaymentTransactionCallback.networkNotAvailable();
                }
            } catch (Exception var101) {
                this.stopService();
                paytm.printStackTrace(var101);
            }

        }

        protected synchronized void stopService() {
            mService = null;
            paytm.debugLog("Service Stopped.");
        }

    public synchronized void queryStatus(Context inCtxt, PaytmPGService ser, String StatusQueryURL, PaytmStatusQuery inStatusQuery, PaytmClientCertificate inCertificate, PaytmStatusQueryCallback inStatusQueryCallback) {
        try {
            Bundle var7 = new Bundle();
            if (inStatusQuery != null) {
                var7.putString("ORDER_ID", inStatusQuery.mOrderId);
                var7.putString("MID", inStatusQuery.mMerchantId);
            }

            //this.mCertificate = inCertificate;
            StatusQueryThread StatusQuery = new StatusQueryThread(inCtxt, ser, StatusQueryURL, var7, inStatusQueryCallback);
            StatusQuery.start();
        } catch (Exception var71) {
            ser = null;
            paytm.printStackTrace(var71);
        }

    }

        public synchronized void cancelTransaction(Context inCtxt, PaytmCancel inCancel, PaytmClientCertificate inCertificate, PaytmCancelTransaction inCancellationHandler) {
            try {
                Bundle var7 = new Bundle();
                if(inCancel != null) {
                    var7 = new Bundle();
                    var7.putString("MID", inCancel.mMerchantId);
                    var7.putString("ORDER_ID", inCancel.mOrderId);
                }

                this.mCertificate = inCertificate;
                CancelTransactionThread CancelTransaction = new PaytmPGService.CancelTransactionThread(inCtxt, var7, inCancellationHandler);
                CancelTransaction.start();
            } catch (Exception var71) {
                getService().stopService();
                paytm.printStackTrace(var71);
            }

        }

        private ApplicationInfo getApplicationinfo(Context ctx) {
            ApplicationInfo app = null;

            try {
                PackageManager e = ctx.getPackageManager();
                app = e.getApplicationInfo(ctx.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException var4) {
                var4.printStackTrace();
            }

            return app;
        }

    private class StatusQueryThread extends Thread {
        private Context mCtxt;
        private String mParams;
        protected volatile PaytmStatusQueryCallback mStatusQueryCallback;
        private volatile String mStatusQueryURL;
        private static final String JSON_DATA = "JsonData=";
        PaytmPGService mService;

        public StatusQueryThread(Context inCtxt, PaytmPGService ser, String StatusQueryURL, Bundle inParams, PaytmStatusQueryCallback inStatusQueryCallback) {
            this.mCtxt = inCtxt;
            this.mParams = "JsonData=" + paytm.getJSONString(inParams);
            paytm.debugLog("Params is " + this.mParams);
            this.mStatusQueryCallback = inStatusQueryCallback;
            mService = ser;
            mStatusQueryURL = StatusQueryURL;
        }

        public synchronized void run() {
            String Response = "";

            PaytmStatusQueryCallback StatusQueryCallback;
            try {
               /* HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public synchronized boolean verify(String hostname, SSLSession session) {
                        debugLog("verifying the hostname");
                        return true;
                    }
                });*/
                URL var8 = new URL(this.mStatusQueryURL);
                paytm.debugLog("URL is " + var8.toString());
                URLConnection UrlConnection = var8.openConnection();
                paytm.debugLog("New Connection is created.");
                if (URLUtil.isHttpsUrl(var8.toString())) {
                    paytm.debugLog("Https url");
                    paytm.debugLog("Setting SSLSocketFactory to connection...");
                    ((HttpsURLConnection) UrlConnection).setSSLSocketFactory(new PaytmSSLSocketFactory(this.mCtxt, null));
                    paytm.debugLog("SSLSocketFactory is set to connection.");
                }

                UrlConnection.setDoOutput(true);
                ((HttpURLConnection) UrlConnection).setRequestMethod("POST");
                if (this.mParams != null && this.mParams.length() > 0) {
                    paytm.debugLog("Getting the output stream to post");
                    PrintWriter StatusQueryCallback1 = new PrintWriter(UrlConnection.getOutputStream());
                    paytm.debugLog("posting......");
                    StatusQueryCallback1.print(this.mParams);
                    StatusQueryCallback1.close();
                    paytm.debugLog("posted parameters and closing output stream");
                    int iResponseCode = ((HttpURLConnection) UrlConnection).getResponseCode();
                    String ResponseMessage = ((HttpURLConnection) UrlConnection).getResponseMessage();
                    paytm.debugLog("Response code is " + iResponseCode);
                    paytm.debugLog("Response Message is " + ResponseMessage);
                    if (iResponseCode == 200) {
                        paytm.debugLog("Getting the input stream to read response");
                        Scanner InStream = new Scanner(UrlConnection.getInputStream());
                        paytm.debugLog("reading......");

                        while (InStream.hasNextLine()) {
                            Response = Response + InStream.nextLine();
                        }

                        InStream.close();
                        paytm.debugLog("read response and closing input stream");
                    }
                }

                paytm.debugLog("Response is " + Response);
                if (Response != null && Response.length() > 0) {
                    StatusQueryCallback = this.mStatusQueryCallback;
                    JSONObject jObject=null;
                    try {
                        jObject = new JSONObject(Response);
                    } catch (Exception xc) {

                    }
                    Bundle var7 = new Bundle();
                    if (jObject != null) {
                        var7.putString("TXNAMOUNT", jObject.getString("TXNAMOUNT"));
                        var7.putString("STATUS", jObject.getString("STATUS"));
                    }
                    if (StatusQueryCallback != null) {
                        StatusQueryCallback.onStatusQueryCompleted(var7);
                    }
                } else {
                    StatusQueryCallback = this.mStatusQueryCallback;
                    if (StatusQueryCallback != null) {
                        StatusQueryCallback.onStatusQueryFailed("");
                    }
                }

                mService = null;
            } catch (Exception var9) {
                StatusQueryCallback = this.mStatusQueryCallback;
                if (StatusQueryCallback != null) {
                    StatusQueryCallback.onStatusQueryFailed("");
                }

                mService = null;
                paytm.printStackTrace(var9);
            }

        }
    }

        private class CancelTransactionThread extends Thread {
            private Context mCtxt;
            private String mParams;
            private PaytmCancelTransaction mCancellationHandler;
            private static final String JSON_DATA = "JsonData=";
            private static final String OK = "OK";

            public CancelTransactionThread(Context inCtxt, Bundle inParams, PaytmCancelTransaction inCancellationHandler) {
                this.mCtxt = inCtxt;
                this.mParams = "JsonData=" + paytm.getJSONString(inParams);
                paytm.debugLog("Params is " + this.mParams);
                this.mCancellationHandler = inCancellationHandler;
            }

            public synchronized void run() {
                String Response = "";

                try {
                    /*HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                        public synchronized boolean verify(String hostname, SSLSession session) {
                            paytm.debugLog("verifying the hostname");
                            return true;
                        }
                    });*/
                    URL var8 = new URL(PaytmPGService.this.mCancelTransactionURL);
                    paytm.debugLog("URL is " + var8.toString());
                    URLConnection UrlConnection = var8.openConnection();
                    paytm.debugLog("New Connection is created.");
                    if(URLUtil.isHttpsUrl(var8.toString())) {
                        paytm.debugLog("Https url");
                        paytm.debugLog("Setting SSLSocketFactory to connection...");
                        ((HttpsURLConnection)UrlConnection).setSSLSocketFactory(new PaytmSSLSocketFactory(this.mCtxt, PaytmPGService.this.mCertificate));
                        paytm.debugLog("SSLSocketFactory is set to connection.");
                    }

                    UrlConnection.setDoOutput(true);
                    ((HttpURLConnection)UrlConnection).setRequestMethod("POST");
                    if(this.mParams != null && this.mParams.length() > 0) {
                        paytm.debugLog("Getting the output stream to post");
                        PrintWriter Out = new PrintWriter(UrlConnection.getOutputStream());
                        paytm.debugLog("posting......");
                        Out.print(this.mParams);
                        Out.close();
                        paytm.debugLog("posted parameters and closing output stream");
                        int iResponseCode = ((HttpURLConnection)UrlConnection).getResponseCode();
                        String ResponseMessage = ((HttpURLConnection)UrlConnection).getResponseMessage();
                        paytm.debugLog("Response code is " + iResponseCode);
                        paytm.debugLog("Response Message is " + ResponseMessage);
                        if(iResponseCode == 200) {
                            paytm.debugLog("Getting the input stream to read response");
                            Scanner InStream = new Scanner(UrlConnection.getInputStream());
                            paytm.debugLog("reading......");

                            while(InStream.hasNextLine()) {
                                Response = Response + InStream.nextLine();
                            }

                            InStream.close();
                            paytm.debugLog("read response and closing input stream");
                        }
                    }

                    if(this.mCancellationHandler != null) {
                        paytm.debugLog("Response is " + Response);
                        if(Response != null && Response.length() > 0 & Response.equalsIgnoreCase("OK")) {
                            this.mCancellationHandler.onCancellationSuccess();
                        } else {
                            this.mCancellationHandler.onCancellationFailure();
                        }
                    }
                } catch (Exception var81) {
                    if(this.mCancellationHandler != null) {
                        this.mCancellationHandler.onCancellationFailure();
                    }

                    paytm.printStackTrace(var81);
                }

            }
        }
}
