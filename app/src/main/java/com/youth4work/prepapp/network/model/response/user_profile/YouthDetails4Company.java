package com.youth4work.prepapp.network.model.response.user_profile;

import com.google.gson.annotations.SerializedName;

public class YouthDetails4Company {
    @SerializedName("UserId")
    private Long userId;
    @SerializedName("Name")
    private String name;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("UserType")
    private String userType;
    @SerializedName("ImgUrl")
    private String imgUrl;
    @SerializedName("EmailID")
    private String emailID;
    @SerializedName("ContactNo")
    private Long contactNo;
    @SerializedName("UserStatus")
    private String userStatus;
    @SerializedName("Course")
    private String course;
    @SerializedName("talents")
    private String talents;
    @SerializedName("Location")
    private String location;
    @SerializedName("CompanyID")
    private Long companyID;
    @SerializedName("CompanyName")
    private String companyName;
    @SerializedName("Designation")
    private String designation;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTalents() {
        return talents;
    }

    public void setTalents(String talents) {
        this.talents = talents;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


                /*"UserId": 3647076,
                "Name": "Mayank Patel",
                "UserName": "mayankpatel19",
                "UserType": null,
                "ImgUrl": "https://www.youth4work.com/Images/Users/User-default-image-unknown.png",
                "EmailID": "mayank999_udaipur@yahoo.com",
                "ContactNo": "9413217649",
                "UserStatus": null,
                "Course": "Ph.D",
                "talents": "Wireless Communication,Data Structure,Python",
                "Location": null,
                "CompanyID": 0,
                "CompanyName": null,
                "Designation": "Fresher"*/
}
