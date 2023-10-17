package com.youth4work.prepapp.network.model.response.user_profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TalentProfileModel {
    @SerializedName("UserId")
    private Long UserId;
    @SerializedName("TalentId")
    private int TalentId;
    @SerializedName("Talent")
    private String Talent;
    @SerializedName("Score")
    private float Score;
    @SerializedName("TotalYouth")
    private int TotalYouth;
    @SerializedName("TotalPoint")
    private int TotalPoint;
    @SerializedName("SocialLike")
    private int SocialLike;
    @SerializedName("yRank")
    private int yRank;
    @SerializedName("Recomendations")
    private ArrayList<Recomendations> Recomendations;

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public int getTalentId() {
        return TalentId;
    }

    public void setTalentId(int talentId) {
        TalentId = talentId;
    }

    public String getTalent() {
        return Talent;
    }

    public void setTalent(String talent) {
        Talent = talent;
    }

    public float getScore() {
        return Score;
    }

    public void setScore(float score) {
        Score = score;
    }

    public int getTotalYouth() {
        return TotalYouth;
    }

    public void setTotalYouth(int totalYouth) {
        TotalYouth = totalYouth;
    }

    public int getTotalPoint() {
        return TotalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        TotalPoint = totalPoint;
    }

    public int getSocialLike() {
        return SocialLike;
    }

    public void setSocialLike(int socialLike) {
        SocialLike = socialLike;
    }

    public int getyRank() {
        return yRank;
    }

    public void setyRank(int yRank) {
        this.yRank = yRank;
    }

    public ArrayList<TalentProfileModel.Recomendations> getRecomendations() {
        return Recomendations;
    }

    public void setRecomendations(ArrayList<TalentProfileModel.Recomendations> recomendations) {
        Recomendations = recomendations;
    }

   public class Recomendations {
        @SerializedName("Id")
        private int Id;
        @SerializedName("UserId")
        private Long UserId;
        @SerializedName("UserName")
        private String UserName;
        @SerializedName("TalentId")
        private int TalentId;
        @SerializedName("Recomendation")
        private String Recomendation;
        @SerializedName("OnDate")
        private String OnDate;
        @SerializedName("RecomendedById")
        private Long RecomendedById;
        @SerializedName("RecomendedByName")
        private String RecomendedByName;
        @SerializedName("RecomendedByUserName")
        private String RecomendedByUserName;
        @SerializedName("RecomendedByUserType")
        private String RecomendedByUserType;
        @SerializedName("RecomendedByPic")
        private String RecomendedByPic;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public Long getUserId() {
            return UserId;
        }

        public void setUserId(Long userId) {
            UserId = userId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public int getTalentId() {
            return TalentId;
        }

        public void setTalentId(int talentId) {
            TalentId = talentId;
        }

        public String getRecomendation() {
            return Recomendation;
        }

        public void setRecomendation(String recomendation) {
            Recomendation = recomendation;
        }

        public String getOnDate() {
            return OnDate;
        }

        public void setOnDate(String onDate) {
            OnDate = onDate;
        }

        public Long getRecomendedById() {
            return RecomendedById;
        }

        public void setRecomendedById(Long recomendedById) {
            RecomendedById = recomendedById;
        }

        public String getRecomendedByName() {
            return RecomendedByName;
        }

        public void setRecomendedByName(String recomendedByName) {
            RecomendedByName = recomendedByName;
        }

        public String getRecomendedByUserName() {
            return RecomendedByUserName;
        }

        public void setRecomendedByUserName(String recomendedByUserName) {
            RecomendedByUserName = recomendedByUserName;
        }

        public String getRecomendedByUserType() {
            return RecomendedByUserType;
        }

        public void setRecomendedByUserType(String recomendedByUserType) {
            RecomendedByUserType = recomendedByUserType;
        }

        public String getRecomendedByPic() {
            return RecomendedByPic;
        }

        public void setRecomendedByPic(String recomendedByPic) {
            RecomendedByPic = recomendedByPic;
        }
    }
}
