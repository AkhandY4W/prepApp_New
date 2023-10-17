package com.youth4work.prepapp.network.model.request;

public class UserRegisterSocial {
    private String Name;
    private String UserName;
    private String Password;
    private String UserType;
    private String EmailID;
    private String ContactNo;
    private String UserStatus;
    private String BatchFrom;
    private String BatchTo;
    private String LocationName;
    private String Degree;
    private String Spec;
    private String College;
    private String SocialType;
    private String SocialID;
    private String infofromSocialSite;
    private Boolean mobileStatus;

    public UserRegisterSocial(String name, String userName, String password, String userType, String emailID, String contactNo, String userStatus, String batchFrom, String batchTo, String locationName, String degree, String spec, String college,Boolean mobilestatus, String socialType, String socialId, String socialInformation) {
        Name = name;
        UserName = userName;
        Password = password;
        UserType = userType;
        EmailID = emailID;
        ContactNo = contactNo;
        UserStatus = userStatus;
        BatchFrom = batchFrom;
        BatchTo = batchTo;
        LocationName = locationName;
        Degree = degree;
        Spec = spec;
        College = college;
        SocialType = socialType;
        SocialID = socialId;
        infofromSocialSite = socialInformation;
        mobileStatus = mobilestatus;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getBatchFrom() {
        return BatchFrom;
    }

    public void setBatchFrom(String batchFrom) {
        BatchFrom = batchFrom;
    }

    public String getBatchTo() {
        return BatchTo;
    }

    public void setBatchTo(String batchTo) {
        BatchTo = batchTo;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public Boolean getmobilestatus() {
        return mobileStatus;
    }

    public void setmobileStatus(Boolean mobilestatus) {
        mobileStatus = mobilestatus;
    }


    public String getSocialType() {
        return SocialType;
    }

    public void setSocialType(String socialType) {
        SocialType = socialType;
    }

    public String getSocialID() {
        return SocialID;
    }

    public void setSocialID(String socialID) {
        SocialID = socialID;
    }

    public String getInfofromSocialSite() {
        return infofromSocialSite;
    }

    public void setInfofromSocialSite(String infofromSocialSite) {
        this.infofromSocialSite = infofromSocialSite;
    }
}
