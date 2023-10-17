package com.youth4work.prepapp.PayTM;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */
@TargetApi(21)
public class PaytmWebView extends WebView {
    private static final String HTML_OUT = "HTMLOUT";
    private static final String JAVA_SCRIPT = "javascript:window.HTMLOUT.processResponse(document.getElementById(\'response\').value);";
    private static final String Y = "Y";
    private static final String SUCCESS = "01";
    private final PaytmPGActivity mContext;
    private volatile boolean mbMerchantCallbackURLLoaded;
    private static final String CALLBACK = "/CAS/Response";

    public PaytmWebView(Context inContext, Bundle inParams) {
        super(inContext);
        this.mContext = (PaytmPGActivity)inContext;
        this.setWebViewClient(new PaytmWebView.PaytmWebViewClient());
        this.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                paytm.debugLog("JavaScript Alert " + url);
                return super.onJsAlert(view, url, message, result);
            }
        });
        this.getSettings().setJavaScriptEnabled(true);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if(currentapiVersion >= 21) {
            this.getSettings().setMixedContentMode(0);
        }

        this.addJavascriptInterface(new PaytmWebView.PaytmJavaScriptInterface(), "HTMLOUT");
    }

    private synchronized void startProgressDialog() {
        try {
            ((Activity)this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    PaytmWebView.this.mContext.mProgress.setVisibility(0);
                    paytm.debugLog("Progress dialog started");
                }
            });
        } catch (Exception var2) {
            paytm.printStackTrace(var2);
        }

    }

    private synchronized void stopProgressDialog() {
        try {
            ((Activity)this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    PaytmWebView.this.mContext.mProgress.setVisibility(8);
                    paytm.debugLog("Progress dialog ended");
                }
            });
        } catch (Exception var2) {
            paytm.printStackTrace(var2);
        }

    }

    private synchronized Bundle parseResponse(String inResponse) {
        paytm.debugLog("Parsing the Merchant Response");
        Bundle Response = new Bundle();

        try {
            JSONObject inEx = new JSONObject(inResponse);
            if(inEx != null && inEx.length() > 0) {
                Iterator Keys = inEx.keys();

                while(Keys.hasNext()) {
                    String Key = (String)Keys.next();
                    String Value = inEx.getString(Key);
                    paytm.debugLog(Key + " = " + Value);
                    Response.putString(Key, Value);
                }
            }
        } catch (Exception var7) {
            paytm.debugLog("Error while parsing the Merchant Response");
            paytm.printStackTrace(var7);
        }

        return Response;
    }

    private synchronized boolean isValidChecksum(Bundle inResponse) {
        boolean bIsValid = false;

        try {
            if(inResponse != null && inResponse.size() > 0 && inResponse.containsKey("IS_CHECKSUM_VALID") && inResponse.getString("IS_CHECKSUM_VALID").equalsIgnoreCase("Y")) {
                bIsValid = true;
            }
        } catch (Exception var4) {
            paytm.printStackTrace(var4);
        }

        return bIsValid;
    }

    private class PaytmJavaScriptInterface {
        private PaytmJavaScriptInterface() {
        }

        @JavascriptInterface
        public synchronized void processResponse(String inResponse) {
            try {
                paytm.debugLog("Merchant Response is " + inResponse);
                Bundle inEx = PaytmWebView.this.parseResponse(inResponse);
                if(PaytmWebView.this.isValidChecksum(inEx)) {
                    paytm.debugLog("Valid Checksum");
                    if(PaytmPGService.getService().mMerchant.mChecksumVerificationURL != null && PaytmPGService.getService().mMerchant.mChecksumVerificationURL.length() > 0) {
                        if(PaytmWebView.this.mbMerchantCallbackURLLoaded) {
                            this.returnResponse(inEx);
                        } else {
                            paytm.debugLog("Valid Checksum. But Merchant Specific URL is present, So posting all parameters to Merchant specific URL");
                            PaytmWebView.this.postUrl(PaytmPGService.getService().mMerchant.mChecksumVerificationURL, paytm.getURLEncodedStringFromBundle(inEx).getBytes());
                        }
                    } else {
                        paytm.debugLog("Returning the response back to Merchant Application");
                        this.returnResponse(inEx);
                    }
                } else {
                    paytm.debugLog("Invalid Checksum");
                    if(!PaytmWebView.this.mbMerchantCallbackURLLoaded) {
                        if(PaytmPGService.getService().mMerchant.mChecksumVerificationURL != null && PaytmPGService.getService().mMerchant.mChecksumVerificationURL.length() > 0) {
                            paytm.debugLog("Invalid Checksum. So posting all parameters to Merchant specific URL");
                            PaytmWebView.this.postUrl(PaytmPGService.getService().mMerchant.mChecksumVerificationURL, paytm.getURLEncodedStringFromBundle(inEx).getBytes());
                        } else {
                            paytm.debugLog("Invalid Checksum. Validated by Paytm Server. Merchant Specific URL not set.");
                            this.returnResponse(inEx);
                        }
                    } else {
                        paytm.debugLog("Invalid Checksum. Validated by Merchant specific Server");
                        this.returnResponse(inEx);
                    }
                }
            } catch (Exception var3) {
                paytm.printStackTrace(var3);
            }

        }

        private synchronized void returnResponse(final Bundle inResponse) {
          // inResponse.putInt("TXNAMOUNT",Integer.valueOf(inResponse.getString("TXNAMOUNT")));
            try {
                ((Activity)PaytmWebView.this.getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            ((Activity)PaytmWebView.this.getContext()).finish();
                            PaytmPaymentTransactionCallback inEx = PaytmPGService.getService().mPaymentTransactionCallback;
                            if(inEx != null) {
                                if(inResponse.getString("RESPCODE").equalsIgnoreCase("01") && PaytmWebView.this.isValidChecksum(inResponse)) {
                                    inEx.onTransactionSuccess(inResponse);
                                } else {
                                    inEx.onTransactionFailure(inResponse.getString("RESPMSG"), inResponse);
                                }
                            }
                        } catch (Exception var2) {
                            paytm.printStackTrace(var2);
                        }

                    }
                });
            } catch (Exception var3) {
                paytm.printStackTrace(var3);
            }

        }
    }

    private class PaytmWebViewClient extends WebViewClient {
        private PaytmWebViewClient() {
        }

        public synchronized void onPageStarted(WebView inView, String inUrl, Bitmap inFavicon) {
            super.onPageStarted(inView, inUrl, inFavicon);
            paytm.debugLog("Page started loading " + inUrl);
            PaytmWebView.this.startProgressDialog();
        }

        public synchronized void onPageFinished(WebView inView, String inUrl) {
            try {
                super.onPageFinished(inView, inUrl);
                paytm.debugLog("Page finished loading " + inUrl);
                PaytmWebView.this.stopProgressDialog();
                if(inUrl.equalsIgnoreCase(PaytmPGService.getService().mMerchant.mChecksumVerificationURL)) {
                    paytm.debugLog("Merchant specific Callback Url is finished loading. Extract data now. ");
                    PaytmWebView.this.mbMerchantCallbackURLLoaded = true;
                    PaytmWebView.this.loadUrl("javascript:window.HTMLOUT.processResponse(document.getElementById(\'response\').value);");
                } else if(inUrl.endsWith("/CAS/Response")) {
                    paytm.debugLog("CAS Callback Url is finished loading. Extract data now. ");
                    PaytmWebView.this.loadUrl("javascript:window.HTMLOUT.processResponse(document.getElementById(\'response\').value);");
                }
            } catch (Exception var4) {
                paytm.printStackTrace(var4);
            }

        }

        public synchronized void onReceivedError(WebView inView, int iniErrorCode, String inDescription, String inFailingUrl) {
            super.onReceivedError(inView, iniErrorCode, inDescription, inFailingUrl);
            paytm.debugLog("Error occured while loading url " + inFailingUrl);
            paytm.debugLog("Error code is " + iniErrorCode + "Description is " + inDescription);
            if(iniErrorCode == -6) {
                ((Activity)PaytmWebView.this.getContext()).finish();
                PaytmPaymentTransactionCallback PaymentTransactionCallback = PaytmPGService.getService().mPaymentTransactionCallback;
                if(PaymentTransactionCallback != null) {
                    PaymentTransactionCallback.onErrorLoadingWebPage(iniErrorCode, inDescription, inFailingUrl);
                }
            }

        }

        public synchronized void onReceivedSslError(WebView inView, SslErrorHandler inHandler, SslError inError) {
            paytm.debugLog("SSL Error occured " + inError.toString());
            paytm.debugLog("SSL Handler is " + inHandler);
            if(inHandler != null) {
                inHandler.cancel();
            }

        }
    }
}

