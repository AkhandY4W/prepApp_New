package com.youth4work.prepapp.network.model.request;

public class GcmRegister {

    private Long userid;
    private String DeviceId;
    private String GcmTokenID;
    private String emailid;
    private String AppName;

    public GcmRegister(Long userId, String deviceId, String gcmToken, String emailID,String appName) {
        this.userid = userId;
        this.DeviceId = deviceId;
        this.GcmTokenID = gcmToken;
        this.emailid = emailID;
        this.AppName=appName;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDeviceid() {
        return DeviceId;
    }

    public void setDeviceid(String deviceid) {
        DeviceId = deviceid;
    }

    public String getGcmTokenID() {
        return GcmTokenID;
    }

    public void setGcmTokenID(String gcmTokenID) {
        GcmTokenID = gcmTokenID;
    }

    public String getEmailID() {
        return emailid;
    }

    public void setEmailID(String emailID) {
        emailid = emailID;
    }
}
