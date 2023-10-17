package com.youth4work.prepapp.network.model.response


import com.google.gson.annotations.SerializedName

data class SubCatDetailsItem(
    @SerializedName("aspirants")
    val aspirants: Int,
    @SerializedName("attempts")
    val attempts: Int,
    @SerializedName("exams")
    val exams: List<Exam>,
    @SerializedName("parentCategory")
    val parentCategory: String,
    @SerializedName("questions")
    val questions: Int,
    @SerializedName("subCategory")
    val subCategory: String,
    @SerializedName("subCategoryImg")
    val subCategoryImg: String,
    @SerializedName("subCatid")
    val subCatid: Int,
    @SerializedName("subDescription")
    val subDescription: String
)