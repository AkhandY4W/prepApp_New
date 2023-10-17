package com.youth4work.prepapp.network.model.response.user_profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 16-Sep-16.
 */
public class ProfileData {
    /*
    {
  "UserId": 2193,
  "UserName": "shreya",
  "Name": "Shreya Shrivastava",
  "Pic": "~/Images/Users/2193.jpg?v=20160204140151",
  "HeadLine": "Test Engineer",
  "Gender": "F",
  "BirthDate": "8/31/1990 12:00:00 AM",
  "Location": "Delhi",
  "State": "Delhi                                             ",
  "Country": "India",
  "EmailId": "shreya1234.shree@y4w.in",
  "IsEmailVerified": false,
  "IsMobileVerified": false,
  "AlternateEmailId": "shreya.shrivastava11236@yahoo.com",
  "MobileNo": "8130212546",
  "AlternateContactNo": null,
  "PhoneNo": "8130704769",
  "Address": "Laxmi Nagar, Delhi",
  "PinCode": "110092",
  "About": "I am a Software Test Engineer.",
  "Aim": "To acquire more knowledge everyday to enhance my skill and satisfy myself with my work....",
  "ResumeUrl": "~/Documents/New_Openings_Resume/2193.docx",
  "VideoResumeUrl": "",
  "IsPremium": false,
  "ExtraCurricularActivities": "TT,Badminton,Solving Puzzles,texting,music,dance",
  "PreferedLocation": [
    "Noida",
    "Gurgaon"
  ],
  "FacebookVerified": true,
  "GooglePlusVerified": true,
  "TweeterVerified": true,
  "profileCompleted": 95,
  "profileViewdInLast30Days": 68
}
     */

    @SerializedName("HeadLine")
    private String HeadLine;

    @SerializedName("BirthDate")
    private String BirthDate;
    
    @SerializedName("MobileNo")
    private String MobileNo;

    @SerializedName("EmailId")
    private String EmailId;

    @SerializedName("Aim")
    private String Aim;

    @SerializedName("Gender")
    private String Gender;

    @SerializedName("About")
    private String About;

    @SerializedName("Address")
    private String Address;
    
    @SerializedName("PinCode")
    private String AddressPinCode;

    public String getHeadLine() {
        return HeadLine;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getEmailId() {
        return EmailId;
    }

    public String getAim() {
        return Aim;
    }

    public String getGender() {
        return Gender;
    }

    public String getAbout() {
        return About;
    }

    public String getAddress() {
        return Address;
    }

    public String getAddressPinCode() {
        return AddressPinCode;
    }

    @SerializedName("Location")
    private String Location;

    public String getLocation() {
        return Location;
    }

    /* @SerializedName("Address")
    private String Address;
    
    @SerializedName("Address")
    private String Address;*/
}
