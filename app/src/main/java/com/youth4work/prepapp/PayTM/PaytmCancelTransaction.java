package com.youth4work.prepapp.PayTM;

/**
 * Created by jagbros-4 on 20-Jun-17.
 */

public interface PaytmCancelTransaction {
    void onCancellationSuccess();

    void onCancellationFailure();
}
