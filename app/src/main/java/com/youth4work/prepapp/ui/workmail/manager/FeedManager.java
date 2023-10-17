package com.youth4work.prepapp.ui.workmail.manager;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.network.model.response.BusinessObject;
import com.youth4work.prepapp.util.UrlConstants;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class FeedManager {
    private static final String TAG = FeedManager.class.getName();
    private static FeedManager mFeedManager;

    public static FeedManager getInstance() {
        if (mFeedManager == null)
            mFeedManager = new FeedManager();
        return mFeedManager;
    }

    public void queueJob(final FeedParams feedParams){
        if(!feedParams.isCacheOnly()) {
            FeedRequest jsonRequest = new FeedRequest(feedParams.getMethod(), feedParams.getUrl(), feedParams.getClassName(), new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    feedParams.getListener().onDataRetrieved((BusinessObject) response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BusinessObject businessObject = new BusinessObject();
                    businessObject.setVolleyError(error);
                    feedParams.getListener().onDataRetrieved(businessObject);
                }
            });

            jsonRequest.setShouldCache(feedParams.shouldCache());
            jsonRequest.setTag(feedParams.getTag());
            jsonRequest.setPostParams(feedParams.getPostParams());
            jsonRequest.setHeaderParams(getHeaderParamsAuthentication(feedParams.getHeaderParams()));
            jsonRequest.setRetryPolicy(getRetryPolicy());
            jsonRequest.setPriority(feedParams.getPriority());
            PrepApplication.singleton().addToRequestQueue(jsonRequest);
        }
        else {
            getDataFromCache(feedParams);
        }
    }

    public void queueJobStringResponse(final FeedParams feedParams, final Interfaces.IDataRetrievalListenerString listenerString){
        if(!feedParams.isCacheOnly()) {
            FeedRequest jsonRequest = new FeedRequest(feedParams.getMethod(), feedParams.getUrl(), feedParams.getClassName(), new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    if(response != null)
                        listenerString.onDataRetrieved(String.valueOf(response));
                    else
                        listenerString.onDataRetrieved(null);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listenerString.onDataRetrieved(String.valueOf(error));
                }
            });

            jsonRequest.setShouldCache(feedParams.shouldCache());
            jsonRequest.setTag(feedParams.getTag());
            jsonRequest.setPostParams(feedParams.getPostParams());
            jsonRequest.setHeaderParams(getHeaderParamsAuthentication(feedParams.getHeaderParams()));
            jsonRequest.setPriority(feedParams.getPriority());
            jsonRequest.setRetryPolicy(getRetryPolicy());
            PrepApplication.singleton().addToRequestQueue(jsonRequest);
        }
        else {
            getDataFromCache(feedParams);
        }
    }

    public DefaultRetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public void queueJobMultipart(final FeedParams feedParams, String body, final Interfaces.IDataRetrievalListenerString listenerString){
        if(!feedParams.isCacheOnly()) {
            MultipartFeedRequest jsonRequest = new MultipartFeedRequest(feedParams.getMethod(), feedParams.getUrl(), feedParams.getClassName(), new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    if(response != null)
                        listenerString.onDataRetrieved(String.valueOf(response));
                    else
                        listenerString.onDataRetrieved(null);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listenerString.onDataRetrieved(String.valueOf(error));
                }
            });
            jsonRequest.setBody(body);
            jsonRequest.setShouldCache(feedParams.shouldCache());
            jsonRequest.setTag(feedParams.getTag());
            jsonRequest.setPostParams(feedParams.getPostParams());
            jsonRequest.setHeaderParams(getHeaderParamsAuthentication(feedParams.getHeaderParams()));
            jsonRequest.setRetryPolicy(getRetryPolicy());
            jsonRequest.setPriority(feedParams.getPriority());
            PrepApplication.singleton().addToRequestQueue(jsonRequest);
        }
        else {
            getDataFromCache(feedParams);
        }
    }

    public void getDataFromCache(FeedParams feedParams){
        Cache cache = PrepApplication.singleton().getRequestQueue().getCache();
        String url = feedParams.getUrl();
        Cache.Entry entry = cache.get(feedParams.getUrl());
        Log.d(TAG, "Cache hit : url = " + url);
        if(entry != null){
            try {
                String data=new String(entry.data,"UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                try {
                    if(feedParams.getClassName() != null && feedParams.getClassName() != String.class) {
                        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PROTECTED).create();
                        feedParams.getListener().onDataRetrieved((BusinessObject) gson.fromJson(data, feedParams.getClassName()));
                    }
                } catch (Exception e) {
                    BusinessObject businessObject = new BusinessObject();
                    businessObject.setVolleyError(new ParseError(e));
                    feedParams.getListener().onDataRetrieved(businessObject);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        feedParams.getListener().onDataRetrieved(null);
    }

    public void bindImage(NetworkImageView imgNetWorkView, String url){
        ImageLoader imageLoader = PrepApplication.singleton().getImageLoader();
        imgNetWorkView.setImageUrl(url, imageLoader);
    }

    public void bindImage(final ImageView imageView, String url) {
        if(!TextUtils.isEmpty(url)) {
            ImageLoader imageLoader = PrepApplication.singleton().getImageLoader();
            imageLoader.get(url, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Image Load Error: " + error.getMessage());
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                //load image into imageview
                                imageView.setImageBitmap(response.getBitmap());
                            }
                        }
                    }
            );
        }
    }

    private Map<String, String> getHeaderParamsAuthentication(Map<String, String> params) {
        if(params == null)
            params = new HashMap<>();
        params.put(
                "Authorization",
                String.format("Basic %s", Base64.encodeToString(
                        String.format("%s:%s", UrlConstants.USERNAME, UrlConstants.PASSWORD).getBytes(),
                        Base64.DEFAULT)));
        return params;
    }
}
