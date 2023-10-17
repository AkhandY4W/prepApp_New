package com.youth4work.prepapp.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.youth4work.prepapp.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    @Nullable
    private Drawable mDivider;

    public DividerItemDecoration(@NonNull Context context, boolean color) {
        if (color) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider_questions);
        } else {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

    }

    @Override
    public void onDrawOver(Canvas c, @NonNull RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}