package com.youth4work.prepapp.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButtonFont extends AppCompatButton {

    public CustomButtonFont(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButtonFont(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonFont(@NonNull Context context) {
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