package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/16/2016.
 */
public class CommentsAnswersData {

    @SerializedName("answerId")
    private int AnswerId;
    @SerializedName("comment")
    private String Comment;
    @SerializedName("commentByName")
    private String CommentByName;
    @SerializedName("commentByPic")
    private String CommentByPic;
    @SerializedName("createdByUserType")
    private String CreatedByUserType;
    @SerializedName("commentByUserName")
    private String CommentByUserName;
    @SerializedName("commentId")
    private int CommentId;
    @SerializedName("commentDate")
    private String CommentDate;
    @SerializedName("commentByUserId")
    private int CommentByUserId;

    public String getCommentByUserName() {
        return CommentByUserName;
    }

    public void setCommentByUserName(String commentByUserName) {
        CommentByUserName = commentByUserName;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCommentByName() {
        return CommentByName;
    }

    public void setCommentByName(String commentByName) {
        CommentByName = commentByName;
    }

    public String getCommentByPic() {
        return CommentByPic;
    }

    public void setCommentByPic(String commentByPic) {
        CommentByPic = commentByPic;
    }

    public String getCreatedByUserType() {
        return CreatedByUserType;
    }

    public void setCreatedByUserType(String createdByUserType) {
        CreatedByUserType = createdByUserType;
    }

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public int getCommentByUserId() {
        return CommentByUserId;
    }

    public void setCommentByUserId(int commentByUserId) {
        CommentByUserId = commentByUserId;
    }


}
