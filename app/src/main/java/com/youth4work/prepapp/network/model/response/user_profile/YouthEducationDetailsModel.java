package com.youth4work.prepapp.network.model.response.user_profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class YouthEducationDetailsModel {
    @SerializedName("SchoolDetails")
    ArrayList<SchoolEducationDetails> schoolEducationDetails;
    @SerializedName("CollegeDetails")
    ArrayList<CollegeEducationDetails> collegeEducationDetails;

    public ArrayList<SchoolEducationDetails> getSchoolEducationDetails() {
        return schoolEducationDetails;
    }

    public void setSchoolEducationDetails(ArrayList<SchoolEducationDetails> schoolEducationDetails) {
        this.schoolEducationDetails = schoolEducationDetails;
    }

    public ArrayList<CollegeEducationDetails> getCollegeEducationDetails() {
        return collegeEducationDetails;
    }

    public void setCollegeEducationDetails(ArrayList<CollegeEducationDetails> collegeEducationDetails) {
        this.collegeEducationDetails = collegeEducationDetails;
    }

    public class SchoolEducationDetails {
        @SerializedName("Id")
        private int Id;
        @SerializedName("UserId")
        private long UserId;
        @SerializedName("Class")
        private int ClassName;
        @SerializedName("StreamId")
        private int StreamId;
        @SerializedName("StreamName")
        private String StreamName;
        @SerializedName("SchoolName")
        private String SchoolName;
        @SerializedName("BoardId")
        private int BoardId;
        @SerializedName("BoardName")
        private String BoardName;
        @SerializedName("StateId")
        private int StateId;
        @SerializedName("StateName")
        private String StateName;
        @SerializedName("PassOut")
        private int PassOut;
        @SerializedName("MarksType")
        private int MarksType;
        @SerializedName("Percentage")
        private Double Percentage;
        @SerializedName("Grade")
        private String Grade;
        @SerializedName("CGP")
        private float CGP;
        @SerializedName("OutOfCGP")
        private float OutOfCGP;
        @SerializedName("IsMarksHidden")
        private Boolean IsMarksHidden;
        @SerializedName("IsVerified")
        private Boolean IsVerified;

        public int getId() {
            return Id;
        }

        public long getUserId() {
            return UserId;
        }

        public int getClassName() {
            return ClassName;
        }

        public int getStreamId() {
            return StreamId;
        }

        public String getStreamName() {
            return StreamName;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public int getBoardId() {
            return BoardId;
        }

        public String getBoardName() {
            return BoardName;
        }

        public int getStateId() {
            return StateId;
        }

        public String getStateName() {
            return StateName;
        }

        public int getPassOut() {
            return PassOut;
        }

        public int getMarksType() {
            return MarksType;
        }

        public Double getPercentage() {
            return Percentage;
        }

        public String getGrade() {
            return Grade;
        }

        public float getCGP() {
            return CGP;
        }

        public float getOutOfCGP() {
            return OutOfCGP;
        }

        public Boolean getMarksHidden() {
            return IsMarksHidden;
        }

        public Boolean getVerified() {
            return IsVerified;
        }
    }

    public class CollegeEducationDetails{
        @SerializedName("Id")
        private int Id;
        @SerializedName("UserId")
        private long UserId;
        @SerializedName("College")
        private String College;
        @SerializedName("CollegeLogo")
        private String CollegeLogo;
        @SerializedName("University")
        private String University;
        @SerializedName("Course")
        private String Course;
        @SerializedName("Specialization")
        private String Specialization;
        @SerializedName("Batch")
        private String Batch;
        @SerializedName("BatchStart")
        private int BatchStart;
        @SerializedName("BatchEnd")
        private int BatchEnd;
        @SerializedName("Score")
        private String Score;
        @SerializedName("HideScore")
        private Boolean HideScore;
        @SerializedName("Achievement")
        private String Achievement;
        @SerializedName("IsVerified")
        private Boolean IsVerified;
        @SerializedName("IsCurrent")
        private Boolean IsCurrent;
        @SerializedName("ColgIsVerified")
        private String ColgIsVerified;

        public int getId() {
            return Id;
        }

        public long getUserId() {
            return UserId;
        }

        public String getCollege() {
            return College;
        }

        public String getCollegeLogo() {
            return CollegeLogo;
        }

        public String getUniversity() {
            return University;
        }

        public String getCourse() {
            return Course;
        }

        public String getSpecialization() {
            return Specialization;
        }

        public String getBatch() {
            return Batch;
        }

        public int getBatchStart() {
            return BatchStart;
        }

        public int getBatchEnd() {
            return BatchEnd;
        }

        public String getScore() {
            return Score;
        }

        public Boolean getHideScore() {
            return HideScore;
        }

        public String getAchievement() {
            return Achievement;
        }

        public Boolean getVerified() {
            return IsVerified;
        }

        public Boolean getCurrent() {
            return IsCurrent;
        }

        public String getColgIsVerified() {
            return ColgIsVerified;
        }
    }
}