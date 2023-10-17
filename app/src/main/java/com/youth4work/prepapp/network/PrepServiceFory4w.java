package com.youth4work.prepapp.network;

import androidx.annotation.NonNull;


import com.youth4work.prepapp.network.model.response.Message;
import com.youth4work.prepapp.network.model.response.user_profile.ProfileData;
import com.youth4work.prepapp.network.model.response.user_profile.TalentProfileModel;
import com.youth4work.prepapp.network.model.response.user_profile.YouthEducationDetailsModel;
import com.youth4work.prepapp.network.model.response.user_profile.YouthWorkDetailsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PrepServiceFory4w {

    @NonNull
    @GET("yMail")
    Call<Message> yMail(
            @Query("userId") long userId,
            @Query("pageNumber") int pageNumber,
            @Query("pagesize") int pagesize,
            @Query("isCount") String isCount
    );
    @NonNull
    @GET("y/{userId}/EducationProfile")
    Call<YouthEducationDetailsModel> GetEducationProfile(
            @Path("userId") long userId);
    @NonNull
    @GET("y/{userId}/workprofile")
    Call<YouthWorkDetailsModel> getWorkprofile(
            @Path("userId") long userId);
    @NonNull
    @GET("y/TalentProfile")
    Call<ArrayList<TalentProfileModel>> getTalentProfile(
            @Query("userId") long userId,
            @Query("pageNumber") int pageNo,
            @Query("pageSize") int pageSize);
    @NonNull
    @GET("y/{userId}")
    Call<ProfileData> GetProfile(
            @Path("userId") long userId);

}
