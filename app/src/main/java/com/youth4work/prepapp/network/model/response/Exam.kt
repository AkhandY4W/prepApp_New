package com.youth4work.prepapp.network.model.response


import com.google.gson.annotations.SerializedName

data class Exam(
    @SerializedName("attempts")
    val attempts: Int,
    @SerializedName("catid")
    val catid: Int,
    @SerializedName("exam")
    val exam: String,
    @SerializedName("examid")
    val examid: Int,
    @SerializedName("iconurl")
    val iconurl: String,
    @SerializedName("tested")
    val tested: Int
)