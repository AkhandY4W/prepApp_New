package com.youth4work.prepapp.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewFontLight extends AppCompatTextView {

    public CustomTextViewFontLight(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewFontLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewFontLight(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Light.ttf");
            setTypeface(tf);
        }
    }
}