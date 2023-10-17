package com.youth4work.prepapp.util;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.ParseException;

public class PrepUtils {

    private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;
    public static Activity activity;

    public static double RoundTo2Decimals(double val) {
        Double formattedValue = 0d;
        try {
            DecimalFormat df2 = new DecimalFormat("###.##");
            formattedValue = df2.parse(df2.format(val)).doubleValue();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formattedValue;
    }

    public static double RoundTo1Decimals(double val) {
        Double formattedValue = 0d;
        try {
            DecimalFormat df2 = new DecimalFormat("###.#");
            formattedValue = df2.parse(df2.format(val)).doubleValue();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formattedValue;
    }


    // Will work only if array adapter is attached to the spinner
    public static void setSpinnerSelectedValue(String value, @NonNull Spinner spinner) {
        ArrayAdapter<String> spinnerAdap = (ArrayAdapter<String>) spinner.getAdapter();
        int pos = spinnerAdap.getPosition(value);
        spinner.setSelection(pos);
    }

    public static void goToActivity(@NonNull final Activity ctx, final Class goToClass, boolean clearTop) {
        Intent intent = new Intent(ctx, goToClass);
        if (clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        ctx.startActivity(intent);
        if (clearTop) {
            ctx.finish();
        }
    }

    public static int getDPI(@NonNull Activity mAct, int size) {
        DisplayMetrics metrics = new DisplayMetrics();
        mAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
