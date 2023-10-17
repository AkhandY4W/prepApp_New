package com.youth4work.prepapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.youth4work.prepapp.BuildConfig;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.home.VerificationActivity;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.quiz.ReviewTestActivity;

import java.util.Calendar;

import static com.youth4work.prepapp.util.Constants.update;

public class PrepDialogsUtils {
    @NonNull
    private static PrepDialogsUtils ourInstance = new PrepDialogsUtils();

    private PrepDialogsUtils() {
    }

    @NonNull
    public static PrepDialogsUtils getInstance() {
        return ourInstance;
    }
/*

    public static void showUpgradeDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("Daily free question limit has reached")
                .content("Do you want to attempt more questions and prepare better?")
                .positiveText("UPGRADE")
                .negativeText("I'll try tomm. ")
                .buttonsGravity(GravityEnum.CENTER)
                .cancelable(false)
                .onNegative((dialog, which) -> ReviewTestActivity.show(context, Calendar.getInstance().getTime(),"0"))
                .onPositive((dialog, which) -> UpgradePlanActivity.show(context))
                .show();
    }

    public static void showQuestionBankOverDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("Questions Bank Over")
                .content("No more questions left to attempt.\nPlease wait for us to add new questions.")
                .positiveText("OK")
                .cancelable(false)
                .onPositive((dialog, which) -> ((Activity) context).finish())
                .show();
    }


    public static void UpDateDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("New Version Arrived")
                .content("Please Update App")
                .positiveText("OK")
                .negativeText("CANCEL")
                .cancelable(false)
                .onPositive((dialog, which) -> update(context))
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }
    public static void showFreeTrailExpiredDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("Free Trail Expired")
                .content("Free question limit has reached.")
                .positiveText("UPGRADE NOW")
                .negativeText("OK")
               */
/* .onNegative((dialog, which) -> dialog.dismiss())*//*

                .cancelable(false)
                .onNegative((dialog, which) -> ((Activity)context).finish())
                .onPositive((dialog, which) -> UpgradePlanActivity.show(context))
                .show();

    }

    public static void varifyMobileNo(@NonNull Context context, String mobileno){
        new MaterialDialog.Builder(context)
                //.title(Html.fromHtml("<b>We will verifying the Phone number</b>"))
                .content(Html.fromHtml("<p><b>We will verifying the Phone number:</b></p> <p><b>"+mobileno+" </b></p><b> Is this OK, or would you like to edit the number?</b>"))
                .positiveText("Edit/Verify")
                .negativeText("Not Now")
                .dividerColorRes(R.color.light_gray)
                .contentColorRes(R.color.darkblack)
                .buttonsGravity(GravityEnum.CENTER)
                .onNegative((dialog, which) -> dialog.dismiss())
                .onPositive((dialog, which) -> VerificationActivity.show(context))
                .show();

    }

    public static void showQuitTestDialog(@NonNull final Context context) {
        new MaterialDialog.Builder(context)
                .title("Are you sure?")
                .content("Quitting will end the test.\nYou can resume later.")
                .positiveText("OK")
                .negativeText("CANCEL")
                .onNegative((dialog, which) -> dialog.dismiss())
                //.onPositive((dialog, which) -> DashboardActivity.show(context))
                .onPositive((dialog, which) -> ((Activity) context).finish())
                .show();
    }
    public static void showQuitMockTestDialog(@NonNull final Context context) {
        new MaterialDialog.Builder(context)
                .title("Are you sure?")
                .content("Quitting will submit the test.\nYou can resume later.")
                .positiveText("OK")
                .negativeText("CANCEL")
                .onNegative((dialog, which) -> dialog.dismiss())
                //.onPositive((dialog, which) -> DashboardActivity.show(context))
                .onPositive((dialog, which) -> ((Activity) context).finish())
                .show();
    }

    public static void showDeleteDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("Are you sure?")
                .positiveText("CANCEL")
                .negativeText("DELETE")
                .show();
    }

    public static void showConnectionErrorDialog(@NonNull Context context) {
        new MaterialDialog.Builder(context)
                .title("Connection Error")
                .content("Unable to connect to Youth4Work\nPlease check your mobile network settings and try again.")
                .positiveText("CANCEL")
                .negativeText("UPGRADE NOW")
                .show();
    }


*/
}
