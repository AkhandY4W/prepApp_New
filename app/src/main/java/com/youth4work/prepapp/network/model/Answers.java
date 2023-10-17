package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/13/2016.
 */
public class Answers {
    @SerializedName("aRank")
    public int ARank;
    @SerializedName("answer")
    public String Answer;
    @SerializedName("answerByName")
    public String AnswerByName;
    @SerializedName("answerByPic")
    public String AnswerByPic;
    @SerializedName("answerByUserId")
    public int AnswerByUserId;
    @SerializedName("answerByUserName")
    public String AnswerByUserName;
    @SerializedName("answerByUserType")
    public String AnswerByUserType;
    @SerializedName("answerId")
    public int AnswerId;
    @SerializedName("forumId")
    public String ForumId;
    @SerializedName("lastModified")
    public String LastModified;
    @SerializedName("totalComment")
    public String TotalComment;

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public int getARank() {
        return ARank;
    }

    public void setARank(int ARank) {
        this.ARank = ARank;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getAnswerByName() {
        return AnswerByName;
    }

    public void setAnswerByName(String answerByName) {
        AnswerByName = answerByName;
    }

    public String getAnswerByPic() {
        return AnswerByPic;
    }

    public void setAnswerByPic(String answerByPic) {
        AnswerByPic = answerByPic;
    }

    public int getAnswerByUserId() {
        return AnswerByUserId;
    }

    public void setAnswerByUserId(int answerByUserId) {
        AnswerByUserId = answerByUserId;
    }

    public String getAnswerByUserName() {
        return AnswerByUserName;
    }

    public void setAnswerByUserName(String answerByUserName) {
        AnswerByUserName = answerByUserName;
    }

    public String getAnswerByUserType() {
        return AnswerByUserType;
    }

    public void setAnswerByUserType(String answerByUserType) {
        AnswerByUserType = answerByUserType;
    }

    public String getForumId() {
        return ForumId;
    }

    public void setForumId(String forumId) {
        ForumId = forumId;
    }

    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String lastModified) {
        LastModified = lastModified;
    }

    public String getTotalComment() {
        return TotalComment;
    }

    public void setTotalComment(String totalComment) {
        TotalComment = totalComment;
    }



}
