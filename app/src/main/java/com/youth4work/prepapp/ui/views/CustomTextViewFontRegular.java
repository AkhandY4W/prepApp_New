package com.youth4work.prepapp.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewFontRegular extends AppCompatTextView {

    public CustomTextViewFontRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewFontRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewFontRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            setTypeface(tf);
        }
    }
}