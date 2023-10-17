package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anil Sharma on 2/12/2018.
 */

public class EducationDetails {
    @SerializedName("batchEnd")
    private int batchEnd;
    @SerializedName("batchStart")
    private int batchStart;
    @SerializedName("college")
    private String college;
    @SerializedName("course")
    private String course;
    @SerializedName("specialization")
    private String specialization;
    @SerializedName("university")
    private String university;
    @SerializedName("id")
    private Long educationid;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @SerializedName("UserId")
    private Long userId;
    public int getBatchEnd() {
        return batchEnd;
    }

    public void setBatchEnd(int batchEnd) {
        this.batchEnd = batchEnd;
    }

    public int getBatchStart() {
        return batchStart;
    }

    public void setBatchStart(int batchStart) {
        this.batchStart = batchStart;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Long getEducationid() {
        return educationid;
    }

    public void setEducationid(Long educationid) {
        this.educationid = educationid;
    }


}
