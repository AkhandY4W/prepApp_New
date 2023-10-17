package com.youth4work.prepapp.PayTM;

import java.util.Map;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */
public class PaytmOrder {
    public String mOrderId;
    public String mCustomerId;
    public String mTransactionAmount;
    public String mEmail;
    public String mMobileNumber;
    private Map<String, String> requestParamMap;

    public PaytmOrder(String inOrderId, String inCustomerId, String inTransactionAmount, String inEmail, String inMobileNumber) {
        this.mOrderId = inOrderId;
        this.mCustomerId = inCustomerId;
        this.mTransactionAmount = inTransactionAmount;
        this.mEmail = inEmail;
        this.mMobileNumber = inMobileNumber;
    }

    public PaytmOrder(Map<String, String> requestParamMap) {
        this.requestParamMap = requestParamMap;
    }

    public Map<String, String> getRequestParamMap() {
        return this.requestParamMap;
    }
}

