package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestResult {

    @SerializedName("url2Share")
    private String Url2Share;
    @SerializedName("city")
    private Object city;
    @SerializedName("cityyRank")
    private int cityyRank;
    @SerializedName("college")
    private Object college;
    @SerializedName("collegeyRank")
    private int collegeyRank;
    @SerializedName("globalYrank")
    private int globalYrank;
    @SerializedName("lost")
    private int lost;
    @SerializedName("test")
    private String test;
    @SerializedName("testid")
    private int testid;
    @SerializedName("timeTaken")
    private String timeTaken;
    @SerializedName("win")
    private int win;
    @SerializedName("yScore")
    private double yScore;
    @SerializedName("attemptedQs")
    private List<Attempt> attemptedQs;

    public String getUrl2Share() {
        return Url2Share;
    }

    public void setUrl2Share(String Url2Share) {
        this.Url2Share = Url2Share;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public int getCityyRank() {
        return cityyRank;
    }

    public void setCityyRank(int cityyRank) {
        this.cityyRank = cityyRank;
    }

    public Object getCollege() {
        return college;
    }

    public void setCollege(Object college) {
        this.college = college;
    }

    public int getCollegeyRank() {
        return collegeyRank;
    }

    public void setCollegeyRank(int collegeyRank) {
        this.collegeyRank = collegeyRank;
    }

    public int getGlobalYrank() {
        return globalYrank;
    }

    public void setGlobalYrank(int globalYrank) {
        this.globalYrank = globalYrank;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public double getYScore() {
        return yScore;
    }

    public void setYScore(double yScore) {
        this.yScore = yScore;
    }

    public List<Attempt> getAttemptedQs() {
        return attemptedQs;
    }

    public void setAttemptedQs(List<Attempt> attemptedQs) {
        this.attemptedQs = attemptedQs;
    }
}
