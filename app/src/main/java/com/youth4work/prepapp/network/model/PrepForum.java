package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/12/2016.
 */
public class PrepForum {
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
    @SerializedName("forumId")
    private int ForumId;
    @SerializedName("givenAnsweredId")
    private int GivenAnsweredId;
    @SerializedName("subCatId")
    private int SubCatId;
    @SerializedName("totalAnswer")
    private int TotalAnswer;
    @SerializedName("totalView")
    private int TotalView;
    @SerializedName("createdByUserId")
    private int CreatedByUserId;
    @SerializedName("forum")
    private String Forum;
    @SerializedName("title")
    private String Title;
    @SerializedName("logourl")
    private String logourl;


    public String getLastModified() {
        return LastModified;
    }

    public void setLastModified(String lastModified) {
        LastModified = lastModified;
    }

   /* public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }*/

    public String getCreatedByName() {
        return CreatedByName;
    }

    public void setCreatedByName(String createdByName) {
        CreatedByName = createdByName;
    }

    public String getCreatedByPic() {
        return CreatedByPic;
    }

    public void setCreatedByPic(String createdByPic) {
        CreatedByPic = createdByPic;
    }

    public String getCreatedByUserName() {
        return CreatedByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        CreatedByUserName = createdByUserName;
    }

    public String getCreatedByUserType() {
        return CreatedByUserType;
    }

    public void setCreatedByUserType(String createdByUserType) {
        CreatedByUserType = createdByUserType;
    }

    public int getForumId() {
        return ForumId;
    }

    public void setForumId(int forumId) {
        ForumId = forumId;
    }


    public int getSubCatId() {
        return SubCatId;
    }

    public void setSubCatId(int subCatId) {
        SubCatId = subCatId;
    }

    public int getTotalAnswer() {
        return TotalAnswer;
    }

    public void setTotalAnswer(int totalAnswer) {
        TotalAnswer = totalAnswer;
    }

    public int getTotalView() {
        return TotalView;
    }

    public void setTotalView(int totalView) {
        TotalView = totalView;
    }

    public int getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getForum() {
        return Forum;
    }

    public void setForum(String forum) {
        Forum = forum;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

}
