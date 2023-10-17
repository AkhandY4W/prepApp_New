package com.youth4work.prepapp.network.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Chat extends BusinessObject {
    @SerializedName("data")
    private ArrayList<Message> mArrListMessages;

    public ArrayList<Message> getArrListMessages() {
        return mArrListMessages;
    }

}
