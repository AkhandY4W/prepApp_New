package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

public class Rank {


    @SerializedName("email")
    private String Email;
    @SerializedName("imgUrl")
    private String ImgUrl;
    @SerializedName("name")
    private String Name;
    @SerializedName("rank")
    private int Rank;
    @SerializedName("userId")
    private int UserId;
    @SerializedName("userName")
    private String UserName;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String ImgUrl) {
        this.ImgUrl = ImgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int Rank) {
        this.Rank = Rank;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
