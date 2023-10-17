package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anil Sharma on 1/9/2018.
 */

public class CouponCode {
    @SerializedName("couponCode")
    private String CouponCode;

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getCouponCodeDesc() {
        return CouponCodeDesc;
    }

    public void setCouponCodeDesc(String couponCodeDesc) {
        CouponCodeDesc = couponCodeDesc;
    }

    @SerializedName("couponCodeDesc")
    private String CouponCodeDesc;

}
