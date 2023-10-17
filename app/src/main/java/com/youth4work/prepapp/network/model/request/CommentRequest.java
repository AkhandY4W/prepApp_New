package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravindra on 17/03/16.
 */
public class CommentRequest {


    /**
     * UserID : 72
     * Questionid : 1000
     * Comment : test
     */

    @SerializedName("UserID")
    private Long mUserID;
    @SerializedName("Questionid")
    private int mQuestionid;
    @SerializedName("Comment")
    private String mComment;

    public CommentRequest(Long mUserID, int mQuestionid, String mComment) {
        this.mUserID = mUserID;
        this.mQuestionid = mQuestionid;
        this.mComment = mComment;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long userID) {
        mUserID = userID;
    }


    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public int getmQuestionid() {
        return mQuestionid;
    }

    public void setmQuestionid(int mQuestionid) {
        this.mQuestionid = mQuestionid;
    }
}
