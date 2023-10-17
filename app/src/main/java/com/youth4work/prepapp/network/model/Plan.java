package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

public class Plan {
    @SerializedName("amount")
    private int Amount;
    @SerializedName("disAmount")
    private int DisAmount;
    @SerializedName("serviceDesc")
    private String ServiceDesc;
    @SerializedName("serviceID")
    private int ServiceID;
    @SerializedName("serviceName")
    private String ServiceName;
    @SerializedName("validity")
    private int Validity;
    @SerializedName("quantity")
    private String quantity;

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public String getServiceDesc() {
        return ServiceDesc;
    }

    public void setServiceDesc(String ServiceDesc) {
        this.ServiceDesc = ServiceDesc;
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int ServiceID) {
        this.ServiceID = ServiceID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String ServiceName) {
        this.ServiceName = ServiceName;
    }

    public int getValidity() {
        return Validity;
    }

    public void setValidity(int Validity) {
        this.Validity = Validity;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
    public int getDisAmount()
    {
        return DisAmount;
    }

    public void setDisAmount(int disAmount)
    {
        DisAmount = disAmount;
    }

}
