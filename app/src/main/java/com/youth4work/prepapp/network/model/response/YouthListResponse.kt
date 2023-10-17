package com.youth4work.prepapp.network.model.response


import com.google.gson.annotations.SerializedName

data class YouthListResponse(
    @SerializedName("totalCount")
    public val totalCount: Int,
    @SerializedName("youthList")
    public val youthList: List<Youth>
)