package com.youth4work.prepapp.ui.workmail.manager;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.util.Map;

public class FeedRequest extends Request<Object> {
    private Map<String, String> postParams;
    private Map<String, String> headerParams;
    private Class<?> className;
    private Response.Listener<Object> listener;
    private Priority priority = Priority.NORMAL;
    private static long cacheHitButRefreshed = 30 * 60 * 1000; // in 30 minutes cache will be hit, but also refreshed on background
    private static long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely

    public FeedRequest(int method, String url, Class<?> className, Response.Listener<Object> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.className = className;
        this.listener = listener;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.postParams = postParams;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("Test", getUrl() +" "+json );
            if(className != null && className != String.class) {
                if(json != null && json.startsWith("[")) {
                    json = "{ \"data\" : " + json + " }";
                }
                Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PROTECTED).create();
                return Response.success(gson.fromJson(json, className), parseIgnoreCacheHeaders(response));//HttpHeaderParser.parseCacheHeaders(response));
            }
            else {
                return Response.success((Object) json, parseIgnoreCacheHeaders(response));//HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams != null ? headerParams : super.getHeaders();

    }

    @Override
    protected Map<String, String> getParams() {
        return postParams;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
