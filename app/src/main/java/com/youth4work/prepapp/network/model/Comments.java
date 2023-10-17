package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comments {


    @SerializedName("createdByName")
    private String CreatedByName;
    @SerializedName("createdByPic")
    private String CreatedByPic;
    @SerializedName("createdByUserName")
    private String CreatedByUserName;
    @SerializedName("createdByUserType")
    private String CreatedByUserType;
    @SerializedName("lastModified")
    private String LastModified;
    @SerializedName("questionId")
    private int QuestionId;
    @SerializedName("totalComment")
    private int TotalComment;

    @SerializedName("comments")
    private List<Comment> Comment;

    public String getCreatedByName() {
        return CreatedByName;
    }

    public void setCreatedByName(String CreatedByName) {
        this.CreatedByName = CreatedByName;
    }

    public String getCreatedByPic() {
        return CreatedByPic;
    }

    public void setCreatedByPic(String CreatedByPic) {
        this.CreatedByPic = CreatedByPic;
    }

    public String getCreatedByUserName() {
        return CreatedByUserName;
    }

    public void setCreatedByUserName(String CreatedByUserName) {
        this.CreatedByUserName = CreatedByUserName;
    }

    public String getCreatedByUserType() {
        return CreatedByUserType;
    }

    public void setCreatedByUserType(String CreatedByUserType) {
        this.CreatedByUserType = CreatedByUserType;
    }

    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String LastModified) {
        this.LastModified = LastModified;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int QuestionId) {
        this.QuestionId = QuestionId;
    }

    public int getTotalComment() {
        return TotalComment;
    }

    public void setTotalComment(int TotalComment) {
        this.TotalComment = TotalComment;
    }

    public List<Comment> getComment() {
        return Comment;
    }

    public void setComment(List<Comment> Comment) {
        this.Comment = Comment;
    }

    public static class Comment {
        @SerializedName("comment")
        private String Comment;
        @SerializedName("commentByName")
        private String CommentByName;
        @SerializedName("commentByPic")
        private String CommentByPic;
        @SerializedName("commentByUserName")
        private String CommentByUserName;
        @SerializedName("commentByUserType")
        private String CommentByUserType;
        @SerializedName("commentId")
        private int CommentId;
        @SerializedName("lastModified")
        private String LastModified;
        @SerializedName("questionId")
        private int QuestionId;
        @SerializedName("commentImageUrl")
        private String CommentImage;

        public String getCommentImage() {
            return CommentImage;
        }

        public void setCommentImage(String commentImage) {
            CommentImage = commentImage;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public String getCommentByName() {
            return CommentByName;
        }

        public void setCommentByName(String CommentByName) {
            this.CommentByName = CommentByName;
        }

        public String getCommentByPic() {
            return CommentByPic;
        }

        public void setCommentByPic(String CommentByPic) {
            this.CommentByPic = CommentByPic;
        }

        public String getCommentByUserName() {
            return CommentByUserName;
        }

        public void setCommentByUserName(String CommentByUserName) {
            this.CommentByUserName = CommentByUserName;
        }

        public String getCommentByUserType() {
            return CommentByUserType;
        }

        public void setCommentByUserType(String CommentByUserType) {
            this.CommentByUserType = CommentByUserType;
        }

        public int getCommentId() {
            return CommentId;
        }

        public void setCommentId(int CommentId) {
            this.CommentId = CommentId;
        }

        public String getLastModified() {
            return LastModified;
        }

        public void setLastModified(String LastModified) {
            this.LastModified = LastModified;
        }

        public int getQuestionId() {
            return QuestionId;
        }

        public void setQuestionId(int QuestionId) {
            this.QuestionId = QuestionId;
        }
    }
}
