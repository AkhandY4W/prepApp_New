package com.youth4work.prepapp.network.model.response


import com.google.gson.annotations.SerializedName

data class Youth(
    @SerializedName("college")
    val college: String,
    @SerializedName("course")
    val course: Any,
    @SerializedName("emailID")
    val emailID: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("imageURL")
    val imageURL: String,
    @SerializedName("locationName")
    val locationName: String,
    @SerializedName("mailIDUser")
    val mailIDUser: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("onDate")
    val onDate: String,
    @SerializedName("profileHeading")
    val profileHeading: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userProfile")
    val userProfile: Boolean
)