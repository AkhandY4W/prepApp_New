package com.youth4work.prepapp.PayTM;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */

import android.os.Bundle;

public interface PaytmPaymentTransactionCallback {
    void onTransactionSuccess(Bundle var1);

    void onTransactionFailure(String var1, Bundle var2);

    void networkNotAvailable();

    void clientAuthenticationFailed(String var1);

    void someUIErrorOccurred(String var1);

    void onErrorLoadingWebPage(int var1, String var2, String var3);

    void onBackPressedCancelTransaction();
}
