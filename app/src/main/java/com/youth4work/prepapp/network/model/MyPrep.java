package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravindra on 16/03/16.
 */
public class MyPrep {

    /**
     * Attempts : 79
     * Category : Electronics and Communication Engineering
     * Catid : 252
     * ParentCategory : Practice Tests
     * StartDate : /Date(1455000705133+0530)/
     */

    @SerializedName("attempts")
    private int mAttempts;
    @SerializedName("category")
    private String mCategory;
    @SerializedName("catid")
    private int mCatid;
    @SerializedName("parentCategory")
    private String mParentCategory;
    @SerializedName("startDate")
    private String mStartDate;

    public int getAttempts() {
        return mAttempts;
    }

    public void setAttempts(int attempts) {
        mAttempts = attempts;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public int getCatid() {
        return mCatid;
    }

    public void setCatid(int catid) {
        mCatid = catid;
    }

    public String getParentCategory() {
        return mParentCategory;
    }

    public void setParentCategory(String parentCategory) {
        mParentCategory = parentCategory;
    }

    public String getStartDate() {
        mStartDate = mStartDate.replace("/Date(", "");
        mStartDate = mStartDate.replace("+0530)/", "");

        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }
}
