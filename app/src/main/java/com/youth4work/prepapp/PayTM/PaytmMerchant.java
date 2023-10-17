package com.youth4work.prepapp.PayTM;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */

public class PaytmMerchant {
    public String mMerchantIdentifier;
    public String mChannelId;
    public String mIndustryTypeId;
    public String mWebsite;
    public String mTheme;
    public String mChecksumGenerationURL;
    public String mChecksumVerificationURL;

    public PaytmMerchant(String inMerchantIdentifier, String inChannelId, String inIndustryTypeId, String inWebsite, String inTheme, String inChecksumGenerationUR, String inChecksumVerificationURL) {
        this.mMerchantIdentifier = inMerchantIdentifier;
        this.mChannelId = inChannelId;
        this.mIndustryTypeId = inIndustryTypeId;
        this.mWebsite = inWebsite;
        this.mTheme = inTheme;
        this.mChecksumGenerationURL = inChecksumGenerationUR;
        this.mChecksumVerificationURL = inChecksumVerificationURL;
    }

    public PaytmMerchant(String inChecksumGenerationUR, String inChecksumVerificationURL) {
        this.mChecksumGenerationURL = inChecksumGenerationUR;
        this.mChecksumVerificationURL = inChecksumVerificationURL;
    }
}
