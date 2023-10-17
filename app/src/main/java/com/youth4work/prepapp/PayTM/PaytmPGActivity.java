package com.youth4work.prepapp.PayTM;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;



public class PaytmPGActivity extends Activity {
    protected volatile ProgressBar mProgress;
    private volatile PaytmWebView mWV;
    private volatile PaytmPGActivity.AuthenticatorTask mAuthenticator;
    private volatile Bundle mParams;
    private static final String CHECKSUMHASH = "CHECKSUMHASH";
    private static final String CLIENT_AUTHENTICATION_FAILED = "Client authentication failed. Please try again later.";
    private static final String CLIENT_AUTHENTICATION_FAILED_DUE_TO_SERVER_ERROR = "Client authentication failed due to server error. Please try again later.";
    private static final String UI_INITIALIZATION_ERROR_OCCURED = "Some error occured while initializing UI of Payment Gateway Activity";
    private static final String UI_WEBVIEW_ERROR_OCCURED = "Some UI error occured in WebView of Payment Gateway Activity";
    private static final String PAYT_STATUS = "payt_STATUS";
    private static final String SUCCESS = "1";
    private static final String TRANSACTION_CANCELLED = "Transaction Cancelled.";
    private static final String TRANSACTION_NOT_CANCELLED = "Transaction not Cancelled.";
    private static final String USER_CANCELED_TRANSACTION = "Transaction cancelled by user.";
    private Dialog mDlg;
    private boolean mbIsCancellingRequest;
    private boolean mbChecksumGenerated;
    private boolean mbHideHeader;
    private boolean mbSendAllChecksumResponseParametersToPGServer;

    public PaytmPGActivity() {
    }

    protected synchronized void onCreate(Bundle inSavedInstanceState) {
        super.onCreate(inSavedInstanceState);
        if(this.initUI()) {
            this.startTransaction();
        } else {
            this.finish();
            PaytmPaymentTransactionCallback PaymentTransactionCallback = PaytmPGService.getService().mPaymentTransactionCallback;
            if(PaymentTransactionCallback != null) {
                PaymentTransactionCallback.someUIErrorOccurred("Some error occured while initializing UI of Payment Gateway Activity");
            }
        }

    }

    private synchronized boolean initUI() {
        try {
            if(this.getIntent() != null) {
                this.mbHideHeader = this.getIntent().getBooleanExtra("HIDE_HEADER", false);
                this.mbSendAllChecksumResponseParametersToPGServer = this.getIntent().getBooleanExtra("SEND_ALL_CHECKSUM_RESPONSE_PARAMETERS_TO_PG_SERVER", false);
            }

            paytm.debugLog("Hide Header " + this.mbHideHeader);
            paytm.debugLog("Initializing the UI of Transaction Page...");
            RelativeLayout inEx = new RelativeLayout(this);
            RelativeLayout TopBar = new RelativeLayout(this);
            RelativeLayout.LayoutParams TopBarParams = new RelativeLayout.LayoutParams(-1, -2);
            TopBar.setLayoutParams(TopBarParams);
            TopBar.setId(1);
            TopBar.setBackgroundColor(Color.parseColor("#bdbdbd"));
            Button Cancel = new Button(this, (AttributeSet)null, 16842825);
            RelativeLayout.LayoutParams CancelParams = new RelativeLayout.LayoutParams(-2, -2);
            CancelParams.addRule(15);
            CancelParams.leftMargin = (int)(this.getResources().getDisplayMetrics().density * 5.0F);
            Cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    paytm.debugLog("User pressed back button which is present in Header Bar.");
                    PaytmPGActivity.this.cancelTransaction();
                }
            });
            Cancel.setLayoutParams(CancelParams);
            Cancel.setText("Cancel");
            TextView AppName = new TextView(this);
            RelativeLayout.LayoutParams AppNameParams = new RelativeLayout.LayoutParams(-2, -2);
            AppNameParams.addRule(13);
            AppName.setLayoutParams(AppNameParams);
            AppName.setTextColor(-16777216);
            AppName.setText("Paytm Payments");
            TopBar.addView(Cancel);
            TopBar.addView(AppName);
            RelativeLayout WebLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams WebLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
            WebLayoutParams.addRule(3, TopBar.getId());
            WebLayout.setLayoutParams(WebLayoutParams);
            this.mWV = new PaytmWebView(this, this.mParams);
            this.mWV.setVisibility(8);
            RelativeLayout.LayoutParams WebParams = new RelativeLayout.LayoutParams(-1, -1);
            this.mWV.setLayoutParams(WebParams);
            this.mProgress = new ProgressBar(this, (AttributeSet)null, 16842873);
            RelativeLayout.LayoutParams ProgressParams = new RelativeLayout.LayoutParams(-2, -2);
            ProgressParams.addRule(13);
            this.mProgress.setLayoutParams(ProgressParams);
            WebLayout.addView(this.mWV);
            WebLayout.addView(this.mProgress);
            inEx.addView(TopBar);
            inEx.addView(WebLayout);
            if(this.mbHideHeader) {
                TopBar.setVisibility(8);
            }

            this.requestWindowFeature(1);
            this.setContentView(inEx);
            paytm.debugLog("Initialized UI of Transaction Page.");
            return true;
        } catch (Exception var12) {
            paytm.debugLog("Some exception occurred while initializing UI.");
            paytm.printStackTrace(var12);
            return false;
        }
    }

    protected synchronized void onDestroy() {
        super.onDestroy();

        try {
            if(this.mAuthenticator != null) {
                this.mAuthenticator.cancel(true);
            }

            PaytmPGService.getService().stopService();
        } catch (Exception var2) {
            PaytmPGService.getService().stopService();
            paytm.debugLog("Some exception occurred while destroying the PaytmPGActivity.");
            paytm.printStackTrace(var2);
        }

    }

    private synchronized boolean extractJSON(String inResult) {
        boolean bSuccess = false;

        try {
            paytm.debugLog("Parsing JSON");
            JSONObject inEx = new JSONObject(inResult);
            Iterator Keys = inEx.keys();
            paytm.debugLog("Appending Key Value pairs");
            paytm.debugLog("Send All Checksum Response Parameters to PG " + this.mbSendAllChecksumResponseParametersToPGServer);

            while(Keys.hasNext()) {
                String Key = (String)Keys.next();
                String Value = inEx.getString(Key);
                Key = Key.trim();
                paytm.debugLog(Key + " = " + Value);
                if(Key.equals("CHECKSUMHASH")) {
                    this.mParams.putString(Key, Value);
                } else if(this.mbSendAllChecksumResponseParametersToPGServer) {
                    this.mParams.putString(Key, Value);
                }

                if(Key.equals("payt_STATUS") && Value.equals("1")) {
                    bSuccess = true;
                }
            }
        } catch (Exception var7) {
            paytm.debugLog("Some exception occurred while extracting the checksum from CAS Response.");
            paytm.printStackTrace(var7);
        }

        return bSuccess;
    }

    private synchronized void startTransaction() {
        paytm.debugLog("Starting the Process...");
        if(this.getIntent() != null && this.getIntent().getBundleExtra("Parameters") != null) {
            this.mParams = this.getIntent().getBundleExtra("Parameters");
            if(this.mParams != null && this.mParams.size() > 0) {
                paytm.debugLog("Starting the Client Authentication...");
                this.mAuthenticator = new PaytmPGActivity.AuthenticatorTask();
                if(PaytmPGService.getService() != null) {
                    this.mAuthenticator.execute(new String[]{PaytmPGService.getService().mCASURL});
                }
            }
        }

    }

    public synchronized boolean onKeyDown(int keyCode, KeyEvent event) {
        paytm.debugLog("User pressed key and key code is " + keyCode);
        if(keyCode == 4) {
            paytm.debugLog("User pressed hard key back button");
            this.cancelTransaction();
        }

        return super.onKeyDown(keyCode, event);
    }

    private synchronized void displayToastNotification(final String inMsg) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    PaytmPGActivity.this.mbIsCancellingRequest = false;
                    paytm.debugLog(inMsg);
                    Toast.makeText(PaytmPGActivity.this, inMsg, 0).show();
                    paytm.debugLog("User cancelled " + PaytmPGActivity.this.mParams);
                    PaytmPGService.getService().mPaymentTransactionCallback.onTransactionFailure("Transaction cancelled by user.", PaytmPGActivity.this.mParams);
                    paytm.debugLog("user cancellation");
                    PaytmPGActivity.this.finish();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    private synchronized void cancelTransaction() {
        if(!this.mbIsCancellingRequest) {
            paytm.debugLog("Displaying Confirmation Dialog");
            AlertDialog.Builder DlgBuilder = new AlertDialog.Builder(this);
            DlgBuilder.setTitle("Cancel Transaction");
            DlgBuilder.setMessage("Are you sure you want to cancel transaction");
            DlgBuilder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PaytmPGActivity.this.onBackPressed();
                }
            });
            DlgBuilder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PaytmPGActivity.this.mDlg.dismiss();
                }
            });
            this.mDlg = DlgBuilder.create();
            this.mDlg.show();
        }

    }

    public void onBackPressed() {
        PaytmPGService.getService().mPaymentTransactionCallback.onBackPressedCancelTransaction();
        super.onBackPressed();
    }

    private class AuthenticatorTask extends AsyncTask<String, Void, String> {
        private AuthenticatorTask() {
        }

        protected synchronized String doInBackground(String... inURL) {
            String Response = "";
            URLConnection UrlConnection = null;

            try {
               /* HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        paytm.debugLog("verifying the hostname");
                        return true;
                    }
                });*/
                URL inEx = new URL(inURL[0]);
                paytm.debugLog("URL is " + inEx.toString());
                UrlConnection = inEx.openConnection();
                paytm.debugLog("New Connection is created.");
                if(URLUtil.isHttpsUrl(inEx.toString())) {
                    paytm.debugLog("Https url");
                    paytm.debugLog("Setting SSLSocketFactory to connection...");
                    ((HttpsURLConnection)UrlConnection).setSSLSocketFactory(new PaytmSSLSocketFactory(PaytmPGActivity.this, PaytmPGService.getService().mCertificate));
                    paytm.debugLog("SSLSocketFactory is set to connection.");
                }

                UrlConnection.setDoOutput(true);
                ((HttpURLConnection)UrlConnection).setRequestMethod("POST");
                String Params = paytm.getStringFromBundle(PaytmPGActivity.this.mParams);
                if(Params != null && Params.length() > 0) {
                    paytm.debugLog("Getting the output stream to post");
                    PrintWriter Out = new PrintWriter(UrlConnection.getOutputStream());
                    paytm.debugLog("posting......");
                    Out.print(Params);
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
            } catch (Exception var11) {
                paytm.debugLog("Some exception occurred while making client authentication.");
                paytm.printStackTrace(var11);
            }

            try {
                if(UrlConnection != null) {
                    ((HttpURLConnection)UrlConnection).disconnect();
                }

                paytm.debugLog("connection is disconnected");
            } catch (Exception var10) {
                paytm.printStackTrace(var10);
            }

            return Response;
        }

        protected synchronized void onPostExecute(String inResult) {
            PaytmPaymentTransactionCallback PaymentTransactionCallback;
            try {
                if(inResult != null && !inResult.equalsIgnoreCase("")) {
                    paytm.debugLog("Response is " + inResult);
                    boolean inEx1 = PaytmPGActivity.this.extractJSON(inResult);
                    if(inEx1) {
                        PaytmPGActivity.this.mbChecksumGenerated = true;
                        PaytmPGActivity.this.mWV.setVisibility(0);
                        PaytmPGActivity.this.mWV.postUrl(PaytmPGService.getService().mPGURL, paytm.getURLEncodedStringFromBundle(PaytmPGActivity.this.mParams).getBytes());
                        PaytmPGActivity.this.mWV.requestFocus(130);
                    } else {
                        PaytmPGActivity.this.finish();
                        PaymentTransactionCallback = PaytmPGService.getService().mPaymentTransactionCallback;
                        if(PaymentTransactionCallback != null) {
                            PaymentTransactionCallback.clientAuthenticationFailed("Client authentication failed. Please try again later.");
                        }
                    }
                } else {
                    PaytmPGActivity.this.finish();
                    PaytmPaymentTransactionCallback inEx = PaytmPGService.getService().mPaymentTransactionCallback;
                    if(inEx != null) {
                        inEx.clientAuthenticationFailed("Client authentication failed due to server error. Please try again later.");
                    }
                }
            } catch (Exception var4) {
                PaytmPGActivity.this.finish();
                PaymentTransactionCallback = PaytmPGService.getService().mPaymentTransactionCallback;
                if(PaymentTransactionCallback != null) {
                    PaymentTransactionCallback.someUIErrorOccurred("Some UI error occured in WebView of Payment Gateway Activity");
                }

                paytm.debugLog("Some exception occurred while posting data to PG Server.");
                paytm.printStackTrace(var4);
            }

        }
    }
}

