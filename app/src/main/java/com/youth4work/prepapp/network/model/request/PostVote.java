package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/18/2016.
 */
public class PostVote {
    /*
{
  "answerId": 533,
   "userId":72,
   "UpOrDown":true
}
     */

    public PostVote(int answerId,Boolean UpOrDown, Long userId) {
        this.answerId = answerId;
        this.UpOrDown = UpOrDown;
        this.userId=userId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public Boolean getUpOrDown() {
        return UpOrDown;
    }

    public void setUpOrDown(Boolean upOrDown) {
        UpOrDown = upOrDown;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @SerializedName("answerId")
    private int answerId;
    @SerializedName("UpOrDown")
    private Boolean UpOrDown;
    @SerializedName("userId")
    private Long userId;

}
