package com.youth4work.prepapp.ui.workmail.manager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

/**
 * Created by naveenmishra on 28/06/15.
 */
public class MultipartFeedRequest extends FeedRequest{
    private String body;
    private String encodingType = "application/json";

    public MultipartFeedRequest(int method, String url, Class<?> className, Response.Listener<Object> listener, Response.ErrorListener errorListener) {
        super(method, url, className, listener, errorListener);
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setContentType(String encodingType){
        this.encodingType = encodingType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body.getBytes();
    }

    @Override
    public String getBodyContentType() {
        return encodingType;
    }
}
