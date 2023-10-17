package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/13/2016.
 */
public class Votes{
    @SerializedName("answerId")
    public String AnswerId;
    @SerializedName("headLine")
    public String HeadLine;
    @SerializedName("name")
    public String Name;
    @SerializedName("pic")
    public String Pic;
    @SerializedName("userName")
    public String UserName;
    @SerializedName("userType")
    public String UserType;
    @SerializedName("voteDate")
    public String VoteDate;
    @SerializedName("votedBy")
    public String votedBy;




}