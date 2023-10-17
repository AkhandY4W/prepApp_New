package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Samar on 5/12/2016.
 */
public class ForumDetails {
    @SerializedName("forumId")
    public int ForumId;
    @SerializedName("createdByName")
    public String CreatedByName;
    @SerializedName("createdByPic")
    public String CreatedByPic;
    @SerializedName("title")
    public String Title;
    @SerializedName("forum")
    public String Forum;
    @SerializedName("lastModified")
    public String LastModified;
    @SerializedName("totalAnswer")
    public int TotalAnswer;
    @SerializedName("totalView")
    public int TotalView;
    @SerializedName("givenAnsweredId")
    public int GivenAnsweredId;
    @SerializedName("answers")
    public List<Answers> answersList;
    public int getGivenAnsweredId() {
        return GivenAnsweredId;
    }


    }







