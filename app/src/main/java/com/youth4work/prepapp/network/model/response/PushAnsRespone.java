package com.youth4work.prepapp.network.model.response;

import com.google.gson.annotations.SerializedName;

public class PushAnsRespone {

    @SerializedName("Key")
    private String key;
    @SerializedName("Value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
