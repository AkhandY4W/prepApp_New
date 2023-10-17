package com.youth4work.prepapp.network.model.response;

import com.android.volley.VolleyError;

import java.io.Serializable;
import java.util.ArrayList;

public class BusinessObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private VolleyError volleyError;
    private String emptyMsg;
    public String getEmptyMsg() {
        return emptyMsg;
    }

    public void setEmptyMsg(String emptyMsg) {
        this.emptyMsg = emptyMsg;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private boolean status;

    public VolleyError getVolleyError() {
        return volleyError;
    }

    public void setVolleyError(VolleyError volleyError) {
        this.volleyError = volleyError;
    }

    public ArrayList<?> getArrListBusinessObject() {
        return null;
    }
}
