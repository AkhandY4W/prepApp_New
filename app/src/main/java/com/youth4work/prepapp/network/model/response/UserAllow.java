package com.youth4work.prepapp.network.model.response;

import com.google.gson.annotations.SerializedName;

public class UserAllow {

    @SerializedName("alert")
    private String Alert;
    @SerializedName("isAllow")
    private boolean IsAllow;
    @SerializedName("attemptedToday")
    private int attemptedToday;
    @SerializedName("attemptedTotal")
    private int attemptedTotal;
    @SerializedName("qsLeft")
    private int qsLeft;

    public String getAlert() {
        return Alert;
    }

    public void setAlert(String Alert) {
        this.Alert = Alert;
    }

    public boolean isIsAllow() {
        return IsAllow;
    }

    public void setIsAllow(boolean IsAllow) {
        this.IsAllow = IsAllow;
    }

    public int getAttemptedToday() {
        return attemptedToday;
    }

    public void setAttemptedToday(int attemptedToday) {
        this.attemptedToday = attemptedToday;
    }

    public int getAttemptedTotal() {
        return attemptedTotal;
    }

    public void setAttemptedTotal(int attemptedTotal) {
        this.attemptedTotal = attemptedTotal;
    }

    public int getQsLeft() {
        return qsLeft;
    }

    public void setQsLeft(int qsLeft) {
        this.qsLeft = qsLeft;
    }
}
