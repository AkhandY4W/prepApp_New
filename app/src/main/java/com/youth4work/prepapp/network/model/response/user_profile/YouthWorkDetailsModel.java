package com.youth4work.prepapp.network.model.response.user_profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class YouthWorkDetailsModel {
    @SerializedName("WorkDetails")
    private ArrayList<WorkDetails> workDetails;

    @SerializedName("Projects")
    private ArrayList<YouthProjects> youthProjects;

    public ArrayList<WorkDetails> getWorkDetails() {
        return workDetails;
    }

    public void setWorkDetails(ArrayList<WorkDetails> workDetails) {
        this.workDetails = workDetails;
    }

    public ArrayList<YouthProjects> getYouthProjects() {
        return youthProjects;
    }

    public void setYouthProjects(ArrayList<YouthProjects> youthProjects) {
        this.youthProjects = youthProjects;
    }

   public class WorkDetails {
        @SerializedName("Id")
        private Long Id;
        @SerializedName("JobTitle")
        private String JobTitle;
        @SerializedName("Company")
        private String Company;
        @SerializedName("CompanyId")
        private Long CompanyId;
        @SerializedName("Logo")
        private String Logo;
        @SerializedName("Location")
        private String Location;
        @SerializedName("Department")
        private String Department;
        @SerializedName("Function")
        private String Function;
        @SerializedName("JobType")
        private String JobType;
        @SerializedName("WorkFrom")
        private String WorkFrom;
        @SerializedName("WorkTo")
        private String WorkTo;
        @SerializedName("TotalMonth")
        private int TotalMonth;
        @SerializedName("IsPresent")
        private Boolean IsPresent;
        @SerializedName("Responsibility")
        private String Responsibility;
        @SerializedName("Achievement")
        private String Achievement;
        @SerializedName("Status")
        private String Status;

        public Long getId() {
            return Id;
        }

        public String getJobTitle() {
            return JobTitle;
        }

        public String getCompany() {
            return Company;
        }

        public Long getCompanyId() {
            return CompanyId;
        }

        public String getLogo() {
            return Logo;
        }

        public String getLocation() {
            return Location;
        }

        public String getDepartment() {
            return Department;
        }

        public String getFunction() {
            return Function;
        }

        public String getJobType() {
            return JobType;
        }

        public String getWorkFrom() {
            return WorkFrom;
        }

        public String getWorkTo() {
            return WorkTo;
        }

        public int getTotalMonth() {
            return TotalMonth;
        }

        public Boolean getIsPresent() {
            return IsPresent;
        }

        public String getResponsibility() {
            return Responsibility;
        }

        public String getAchievement() {
            return Achievement;
        }

        public String getStatus() {
            return Status;
        }
    }

  public class YouthProjects {
        @SerializedName("Id")
        private Long Id;
        @SerializedName("WorkOrEducationId")
        private Long WorkOrEducationId;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Description")
        private String Description;
        @SerializedName("From")
        private String From;
        @SerializedName("To")
        private String To;
        @SerializedName("IsPresent")
        private Boolean IsPresent;
        @SerializedName("URL")
        private String URL;
        @SerializedName("occupation")
        private String occupation;

        public Long getId() {
            return Id;
        }

        public Long getWorkOrEducationId() {
            return WorkOrEducationId;
        }

        public String getName() {
            return Name;
        }

        public String getDescription() {
            return Description;
        }

        public String getFrom() {
            return From;
        }

        public String getTo() {
            return To;
        }

        public Boolean getPresent() {
            return IsPresent;
        }

        public String getURL() {
            return URL;
        }

        public String getOccupation() {
            return occupation;
        }
    }
}