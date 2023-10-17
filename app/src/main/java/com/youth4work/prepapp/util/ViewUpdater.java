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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

public class ViewUpdater {

    public static final NumberFormat FORMAT_INT = NumberFormat
            .getIntegerInstance();

    public View view;

    public View[] childViews;

    @NonNull
    public View initialize(@NonNull final View view, @NonNull final int[] children) {
        final View[] views = new View[children.length];
        for (int i = 0; i < children.length; i++)
            views[i] = view.findViewById(children[i]);
        view.setTag(views);
        this.view = view;
        childViews = views;
        return view;
    }

    public void setCurrentView(@NonNull final View view) {
        this.view = view;
        childViews = getChildren(view);
    }

    @NonNull
    public View[] getChildren(@NonNull final View parentView) {
        return (View[]) parentView.getTag();
    }

    @NonNull
    public TextView textView(final int childViewIndex) {
        return (TextView) childViews[childViewIndex];
    }

    @NonNull
    public TextView textView(@NonNull final View parentView, final int childViewIndex) {
        return (TextView) getChildren(parentView)[childViewIndex];
    }

    @NonNull
    public ImageView imageView(final int childViewIndex) {
        return (ImageView) childViews[childViewIndex];
    }

    @NonNull
    public ImageView imageView(@NonNull final View parentView, final int childViewIndex) {
        return (ImageView) getChildren(parentView)[childViewIndex];
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <V extends View> V view(final int childViewIndex) {
        return (V) childViews[childViewIndex];
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <V extends View> V view(@NonNull final View parentView,
                                   final int childViewIndex) {
        return (V) getChildren(parentView)[childViewIndex];
    }

    @NonNull
    public TextView setText(final int childViewIndex, final CharSequence text) {
        final TextView textView = textView(childViewIndex);
        textView.setText(text);
        return textView;
    }

    @NonNull
    public TextView setText(@NonNull final View parentView, final int childViewIndex,
                            final CharSequence text) {
        final TextView textView = textView(parentView, childViewIndex);
        textView.setText(text);
        return textView;
    }

    @NonNull
    public TextView setText(final int childViewIndex, final int resourceId) {
        final TextView textView = textView(childViewIndex);
        textView.setText(resourceId);
        return textView;
    }

    @NonNull
    public TextView setText(@NonNull final View parentView, final int childViewIndex,
                            final int resourceId) {
        final TextView textView = textView(parentView, childViewIndex);
        textView.setText(resourceId);
        return textView;
    }

    @NonNull
    public TextView setNumber(final int childViewIndex, final long number) {
        final TextView textView = textView(childViewIndex);
        textView.setText(FORMAT_INT.format(number));
        return textView;
    }

    @NonNull
    public TextView setNumber(@NonNull final View parentView, final int childViewIndex,
                              final long number) {
        final TextView textView = textView(parentView, childViewIndex);
        textView.setText(FORMAT_INT.format(number));
        return textView;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <T> T getView(final int childViewIndex,
                         final Class<T> childViewClass) {
        return (T) childViews[childViewIndex];
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <T> T getView(@NonNull final View parentView, final int childViewIndex,
                         final Class<T> childViewClass) {
        return (T) getChildren(parentView)[childViewIndex];
    }

    @Nullable
    public View setGone(final int childViewIndex, boolean gone) {
        return ViewUtils.setGone(view(childViewIndex), gone);
    }

    @Nullable
    public View setGone(@NonNull final View parentView, final int childViewIndex,
                        boolean gone) {
        return ViewUtils.setGone(view(parentView, childViewIndex), gone);
    }

    @NonNull
    public CompoundButton setChecked(final int childViewIndex,
                                     final boolean checked) {
        final CompoundButton button = view(childViewIndex);
        button.setChecked(checked);
        return button;
    }

    @NonNull
    public CompoundButton setChecked(@NonNull final View parentView,
                                     final int childViewIndex, final boolean checked) {
        final CompoundButton button = view(parentView, childViewIndex);
        button.setChecked(checked);
        return button;
    }

    @NonNull
    public TextView setVisibleText(final int childViewIndex,
                                   final CharSequence text) {
        TextView view = textView(childViewIndex);
        view.setText(text);
        ViewUtils.setGone(view, TextUtils.isEmpty(text));
        return view;
    }

    @NonNull
    public TextView setVisibleText(@NonNull final View parentView,
                                   final int childViewIndex, final CharSequence text) {
        TextView view = textView(parentView, childViewIndex);
        view.setText(text);
        ViewUtils.setGone(view, TextUtils.isEmpty(text));
        return view;
    }

    private CharSequence formatRelativeTimeSpan(final long time) {
        return DateUtils.getRelativeTimeSpanString(time);
    }

    @NonNull
    public TextView setRelativeTimeSpan(final int childViewIndex, final long time) {
        return setText(childViewIndex, formatRelativeTimeSpan(time));
    }

    @NonNull
    public TextView setRelativeTimeSpan(@NonNull final View parentView,
                                        final int childViewIndex, final long time) {
        return setText(parentView, childViewIndex, formatRelativeTimeSpan(time));
    }
}
