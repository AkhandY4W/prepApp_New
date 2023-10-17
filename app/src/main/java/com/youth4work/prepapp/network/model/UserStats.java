package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

public class UserStats {

    @SerializedName("acuracy")
    private double Acuracy;
    @SerializedName("speed")
    private double Speed;
    @SerializedName("score")
    private double Score;
    @SerializedName("scoreStats")

    public double getAcuracy() {
        return Acuracy;
    }

    public void setAcuracy(double Acuracy) {
        this.Acuracy = Acuracy;
    }

    public double getSpeed() {
        return Speed;
    }

    public void setSpeed(double Speed) {
        this.Speed = Speed;
    }
    public double getScore() {
        return Score;
    }

    public void setScore(double Score) {
        this.Score = Score;
    }

}
