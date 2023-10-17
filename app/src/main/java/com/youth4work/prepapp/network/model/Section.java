package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by y4wmac on 17/06/16.
 */

public class Section {

    @SerializedName("category")
    private String Subject;
    @SerializedName("catid")
    private int SubjectId;
    @SerializedName("attempts")
    private int Attempts;
    @SerializedName("subCategoryImg")
    private String SubCategoryImg;
    @SerializedName("subDescription")
    private String SubDescription;
    @SerializedName("questions")
    private int Questions;
    @SerializedName("aspirants")
    private int Aspirants;

    public int getSubjectId()
    {
        return SubjectId;
    }

    public void setSubjectId(int subjectid) {
        SubjectId = subjectid;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getAttempts() {
        return Attempts;
    }

    public void setAttempts(int attempts) {
        Attempts = attempts;
    }

    public String getSubCategoryImg() {
        return SubCategoryImg;
    }

    public void setSubCategoryImg(String subCategoryImg) {
        SubCategoryImg = subCategoryImg;
    }

    public String getSubDescription() {
        return SubDescription;
    }

    public void setSubDescription(String subDescription) {
        SubDescription = subDescription;
    }

    public int getQuestions() {
        return Questions;
    }

    public void setQuestions(int questions) {
        Questions = questions;
    }

    public int getAspirants() {
        return Aspirants;
    }

    public void setAspirants(int aspirants) {
        Aspirants = aspirants;
    }

}
