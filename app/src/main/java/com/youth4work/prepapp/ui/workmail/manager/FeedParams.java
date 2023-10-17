package com.youth4work.prepapp.ui.workmail.manager;


import com.android.volley.Request;

import java.util.Map;

public class FeedParams {
    private int method = Request.Method.GET;
    private Class<?> className;
    private String url;
    private String tag;
    private boolean isCacheOnly;
    private boolean shouldCache;
    private Map<String, String> postParams;
    private Map<String, String> headerParams;
    private Interfaces.IDataRetrievalListener listener;
    private Request.Priority priority = Request.Priority.NORMAL;

    public FeedParams (String url, Class<?> className, Interfaces.IDataRetrievalListener listener){
        this.url = url.replace(" ", "%20");
        this.className = className;
        this.listener = listener;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Class<?> getClassName() {
        return className;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isCacheOnly() {
        return isCacheOnly;
    }

    public void setIsCacheOnly(boolean isCacheOnly) {
        this.isCacheOnly = isCacheOnly;
    }

    public Map<String, String> getPostParams() {
        return postParams;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.postParams = postParams;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    public Interfaces.IDataRetrievalListener getListener() {
        return listener;
    }

    public boolean shouldCache() {
        return shouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public Request.Priority getPriority() {
        return priority;
    }

    public void setPriority(Request.Priority priority) {
        this.priority = priority;
    }
}
