/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youth4work.prepapp.util;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ViewUtils {

    private ViewUtils() {

    }

    @Nullable
    public static <V extends View> V setGone(@Nullable final V view, final boolean gone) {
        if (view != null)
            if (gone) {
                if (GONE != view.getVisibility())
                    view.setVisibility(GONE);
            } else {
                if (VISIBLE != view.getVisibility()) {
                    view.setVisibility(VISIBLE);
                    view.bringToFront();
                }
            }
        return view;
    }

    @Nullable
    public static <V extends View> V setInvisible(@Nullable final V view,
                                                  final boolean invisible) {
        if (view != null)
            if (invisible) {
                if (INVISIBLE != view.getVisibility())
                    view.setVisibility(INVISIBLE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    public static void increaseHitRectBy(final int amount, @NonNull final View delegate) {
        increaseHitRectBy(amount, amount, amount, amount, delegate);
    }

    public static void increaseHitRectBy(final int top, final int left, final int bottom, final int right, @NonNull final View delegate) {
        final View parent = (View) delegate.getParent();
        if (parent != null && delegate.getContext() != null) {
            parent.post(() -> {
                final float densityDpi = delegate.getContext().getResources().getDisplayMetrics().densityDpi;
                final Rect r = new Rect();
                delegate.getHitRect(r);
                r.top -= transformToDensityPixel(top, densityDpi);
                r.left -= transformToDensityPixel(left, densityDpi);
                r.bottom += transformToDensityPixel(bottom, densityDpi);
                r.right += transformToDensityPixel(right, densityDpi);
                parent.setTouchDelegate(new TouchDelegate(r, delegate));
            });
        }
    }

    public static int transformToDensityPixel(int regularPixel, @NonNull DisplayMetrics displayMetrics) {
        return transformToDensityPixel(regularPixel, displayMetrics.densityDpi);
    }

    public static int transformToDensityPixel(int regularPixel, float densityDpi) {
        return (int) (regularPixel * densityDpi);
    }
}
