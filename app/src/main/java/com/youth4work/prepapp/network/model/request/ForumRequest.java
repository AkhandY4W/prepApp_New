package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/16/2016.
 */
public class ForumRequest {
    /*
    {
        "SubCatId": 33,
        "Title": "testing ios",
        "Forum":"this is just testing 4 ios ",
        "CreatedByUserId":72
    }
     */
    @SerializedName("SubCatId")
    private int SubCatId;

    public Long getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public int getSubCatId() {
        return SubCatId;
    }

    public void setSubCatId(int subCatId) {
        SubCatId = subCatId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getForum() {
        return Forum;
    }

    public void setForum(String forum) {
        Forum = forum;
    }

    @SerializedName("CreatedByUserId")
    private Long CreatedByUserId;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Forum")
    private String Forum;

    public ForumRequest(Long CreatedByUserId,int SubCatId, String Title, String Forum) {
        this.CreatedByUserId = CreatedByUserId;
        this.Title = Title;
        this.Forum = Forum;
        this.SubCatId=SubCatId;
    }


}
