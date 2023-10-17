package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentCategory {

    @SerializedName("catAttempts")
    private int CatAttempts;
    @SerializedName("category")
    private String Category;
    @SerializedName("catid")
    private int Catid;
    @SerializedName("categoryImg")
    private String CategoryImg;
    @SerializedName("subCatlist")
    private List<SubCat> SubCatlist;

    public ParentCategory(String category, int catid, String categoryImg, List<SubCat> subCatlist) {
        Category = category;
        Catid = catid;
        CategoryImg = categoryImg;
        SubCatlist = subCatlist;
    }

    public int getCatAttempts() {
        return CatAttempts;
    }

    public void setCatAttempts(int catAttempts) {
        CatAttempts = catAttempts;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getCatid() {
        return Catid;
    }

    public void setCatid(int catid) {
        Catid = catid;
    }

    public String getCategoryImg() {
        return CategoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        CategoryImg = categoryImg;
    }

    public List<SubCat> getSubCatlist() {
        return SubCatlist;
    }

    public void setSubCatlist(List<SubCat> subCatlist) {
        SubCatlist = subCatlist;
    }

    public static class SubCat {
        @SerializedName("fkCategoryId")
        private int FkCategoryId;
        @SerializedName("subCategoryId")
        private int SubCategoryId;
        @SerializedName("subCategory")
        private String SubCategory;
        @SerializedName("subCategoryImages")
        private String SubCategoryImages;
        @SerializedName("subCategoryDesc")
        private String SubCategoryDesc;
        @SerializedName("subCategoryQues")
        private int SubCategoryQues;
        @SerializedName("subCategoryAspirants")
        private int SubCategoryAspirants;
        @SerializedName("subCategoryTestTakers")
        private int SubCategoryTestTakers;
        @SerializedName("category")
        private String Category;

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }

        public int getFkCategoryId() {
            return FkCategoryId;
        }

        public void setFkCategoryId(int fkCategoryId) {
            FkCategoryId = fkCategoryId;
        }

        public int getSubCategoryId() {
            return SubCategoryId;
        }

        public void setSubCategoryId(int subCategoryId) {
            SubCategoryId = subCategoryId;
        }

        public String getSubCategory() {
            return SubCategory;
        }

        public void setSubCategory(String subCategory) {
            SubCategory = subCategory;
        }

        public String getSubCategoryImages() {
            return SubCategoryImages;
        }

        public void setSubCategoryImages(String subCategoryImages) {
            SubCategoryImages = subCategoryImages;
        }

        public String getSubCategoryDesc() {
            return SubCategoryDesc;
        }

        public void setSubCategoryDesc(String subCategoryDesc) {
            SubCategoryDesc = subCategoryDesc;
        }

        public int getSubCategoryQues() {
            return SubCategoryQues;
        }

        public void setSubCategoryQues(int subCategoryQues) {
            SubCategoryQues = subCategoryQues;
        }

        public int getSubCategoryAspirants() {
            return SubCategoryAspirants;
        }

        public void setSubCategoryAspirants(int subCategoryAspirants) {
            SubCategoryAspirants = subCategoryAspirants;
        }

        public int getSubCategoryTestTakers() {
            return SubCategoryTestTakers;
        }

        public void setSubCategoryTestTakers(int subCategoryTestTakers) {
            SubCategoryTestTakers = subCategoryTestTakers;
        }


    }

}
