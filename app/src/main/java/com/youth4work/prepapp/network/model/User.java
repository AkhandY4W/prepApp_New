package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName(value="contactNo",alternate="ContactNo")
    private String ContactNo;
    @SerializedName(value="emailID",alternate="EmailID")
    private String EmailID;
    @SerializedName(value="imgUrl",alternate="ImgUrl")
    private String ImgUrl;
    @SerializedName(value="name",alternate="Name")
    private String Name;
    @SerializedName(value="prepPlan")
    private String PrepPlan;
    @SerializedName("prepPlanID")
    private int PrepPlanID;
    @SerializedName(value="userId",alternate="UserId")
    private Long UserId;
    @SerializedName(value="userName",alternate="UserName")
    private String UserName;
    @SerializedName(value="userStatus",alternate="UserStatus")
    private String UserStatus;
    @SerializedName(value="userType",alternate="UserType")
    private String UserType;
    @SerializedName("planEndDate")
    private String planEndDate;
    @SerializedName("planStartDate")
    private String planStartDate;
    @SerializedName("selectedCatID")
    private int selectedCatID;
    @SerializedName("noOfViewed")
    private int noOfViewed;
    @SerializedName("profileCompleted")
    private int profileCompleted;
    @SerializedName("noOfAttemptedTest")
    private int noOfAttemptedTest;
    @SerializedName("noOfCourse")
    private int noOfCourse;
    @SerializedName("isMobileVerified")
    private boolean isMobileVerified;
    @SerializedName("City")
    private String City;
    private String passWord;
    private boolean loginStatus;
    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }


    public boolean isLoggedIn() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public int getNoOfViewed() {
        return noOfViewed;
    }

    public void setNoOfViewed(int noOfViewed) {
        this.noOfViewed = noOfViewed;
    }

    public int getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(int profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public int getNoOfAttemptedTest() {
        return noOfAttemptedTest;
    }

    public void setNoOfAttemptedTest(int noOfAttemptedTest) {
        this.noOfAttemptedTest = noOfAttemptedTest;
    }

    public int getNoOfCourse() {
        return noOfCourse;
    }

    public void setNoOfCourse(int noOfCourse) {
        this.noOfCourse = noOfCourse;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }



    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String ImgUrl) {
        this.ImgUrl = ImgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrepPlan() {
        return PrepPlan;
    }

    public void setPrepPlan(String PrepPlan) {
        this.PrepPlan = PrepPlan;
    }

    public int getPrepPlanID() {
        return PrepPlanID;
    }

    public void setPrepPlanID(int PrepPlanID) {
        this.PrepPlanID = PrepPlanID;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String UserStatus) {
        this.UserStatus = UserStatus;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getPlanEndDate() {
     /*   planEndDate = planEndDate.replace("/Date(", "");
        planEndDate = planEndDate.replace("+0530)/", "");*/
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getPlanStartDate() {
      /*  planStartDate = planStartDate.replace("/Date(", "");
        planStartDate = planStartDate.replace("+0530)/", "");*/
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public int getSelectedCatID() {
        return selectedCatID;
    }

    public void setSelectedCatID(int selectedCatID) {
        this.selectedCatID = selectedCatID;
    }
}
