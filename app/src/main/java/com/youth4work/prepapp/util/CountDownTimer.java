/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youth4work.prepapp.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {

    private static final int MSG = 1;
    private final long mMillisInFuture;
    private final long mCountdownInterval;
    private long mStopTimeInFuture;
    // ************AccurateCountdownTimer***************
    private int mTickCounter;

    // ************AccurateCountdownTimer***************
    private long mStartTime;
    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimer.this) {
                final long millisLeft = mStopTimeInFuture
                        - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // ************AccurateCountdownTimer***************
                    long now = SystemClock.elapsedRealtime();
                    long extraDelay = now - mStartTime - mTickCounter
                            * mCountdownInterval;
                    mTickCounter++;
                    long delay = lastTickStart + mCountdownInterval - now
                            - extraDelay;

                    // ************AccurateCountdownTimer***************

                    // take into account user's onTick taking time to execute

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0)
                        delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;

        // ************AccurateCountdownTimer***************
        mTickCounter = 0;
        // ************AccurateCountdownTimer***************
    }

    public final void cancel() {
        mHandler.removeMessages(MSG);
    }

    public synchronized final CountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }

        // ************AccurateCountdownTimer***************
        mStartTime = SystemClock.elapsedRealtime();
        mStopTimeInFuture = mStartTime + mMillisInFuture;
        // ************AccurateCountdownTimer***************

        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();
}
