package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("logoUrl")
    private String logoUrl;
    @SerializedName("noAttempts")
    private int noAttempts;
    @SerializedName("noOfQs")
    private int noOfQs;
    @SerializedName("qsSlot")
    private int qsSlot;
    @SerializedName("testUrl")
    private Object testUrl;
    @SerializedName("testid")
    private int testid;
    @SerializedName("tested")
    private int Tested;
    @SerializedName("testname")
    private String testname;
    @SerializedName("subsubjectdescription")
    private String Subsubjectdescription;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getNoAttempts() {
        return noAttempts;
    }

    public void setNoAttempts(int noAttempts) {
        this.noAttempts = noAttempts;
    }

    public int getNoOfQs() {
        return noOfQs;
    }

    public void setNoOfQs(int noOfQs) {
        this.noOfQs = noOfQs;
    }

    public int getQsSlot() {
        return qsSlot;
    }

    public void setQsSlot(int qsSlot) {
        this.qsSlot = qsSlot;
    }

    public Object getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(Object testUrl) {
        this.testUrl = testUrl;
    }

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public int getTested() {
        return Tested;
    }

    public void setTested(int tested) {
        Tested = tested;
    }

    public String getSubsubjectdescription() {
        return Subsubjectdescription;
    }

    public void setSubsubjectdescription(String subsubjectdescription) {
        Subsubjectdescription = subsubjectdescription;
    }
}