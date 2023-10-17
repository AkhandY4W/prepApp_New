package com.youth4work.prepapp.util

import android.app.Activity
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.youth4work.prepapp.R
import com.youth4work.prepapp.ui.home.VerificationActivity
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity
import com.youth4work.prepapp.ui.quiz.ReviewTestActivity
import com.youth4work.prepapp.util.Constants.update
import java.util.*


fun showUpgradeDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.daily_limit_title)
        message(R.string.daily_limit_message)
        cancelOnTouchOutside(false)
        positiveButton(R.string.upgrade) { UpgradePlanActivity.show(context) }
        negativeButton(R.string.not_upgrade) { ReviewTestActivity.show(context, Calendar.getInstance().getTime(), "0") }

    }
}

fun showQuestionBankOverDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.daily_limit_title)
        message(R.string.daily_limit_message)
        cancelOnTouchOutside(false)
        positiveButton(R.string.ok) { (context as Activity).finish() }

    }
}

fun UpDateDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.update_title)
        message(R.string.update_message)
        cancelOnTouchOutside(false)
        positiveButton(R.string.ok) { update(context) }
        negativeButton(R.string.cancle) { dismiss() }


    }

}
fun DownLoadCompanyApp(context: Context) {
    MaterialDialog(context).show {
       // title(R.string.update_title)
        message(R.string.download_company_app)
        cancelOnTouchOutside(false)
        positiveButton(R.string.ok) { Constants.DownLoadCompantApp(context) }
        negativeButton(R.string.cancle) { dismiss() }


    }

}

fun showFreeTrailExpiredDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.free_trail_expire_title)
        message(R.string.free_trail_expire_msg)
        cancelOnTouchOutside(false)
        positiveButton(R.string.upgrade_now) { UpgradePlanActivity.show(context) }
        negativeButton(R.string.ok) { (context as Activity).finish() }

    }
}

fun varifyMobileNo(context: Context, mobileno: String) {
    MaterialDialog(context).show {
        message(1, Constants.fromHtml("<font color=#000000><p><b>We will verifying the Phone number:</b></p> <p><b>" + mobileno + " </b></p><b> Is this OK, or would you like to edit the number?</b></font>"))
        cancelOnTouchOutside(false)
        positiveButton(R.string.edit_verify) { VerificationActivity.show(context) }
        negativeButton(R.string.not_now) { dismiss() }
    }
}
fun varifyMobileNo4Forums(context: Context) {
    MaterialDialog(context).show {
        message(R.string.varifyMobileNo4Forums)
        cancelOnTouchOutside(false)
        positiveButton(R.string.verify) { VerificationActivity.show(context) }

    }
}

fun showQuitTestDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.are_u_sure)
        message(R.string.quit_test_msg)
        cancelOnTouchOutside(false)
        positiveButton(R.string.ok) { (context as Activity).finish() }
        negativeButton(R.string.cancle) { dismiss() }

    }
}

fun showQuitMockTestDialog(context: Context) {
    MaterialDialog(context).show {
        title(R.string.are_u_sure)
        message(R.string.quit_mock_test_msg)
        cancelOnTouchOutside(false)
        positiveButton(R.string.ok) { (context as Activity).finish() }
        negativeButton(R.string.cancle) { dismiss() }

    }
}

/*
fun setupDialog(editText: MaterialEditText,context: Context,num: Int,num2: Int) {
    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val number2:NumberPicker
    val number:NumberPicker
    val dialog=MaterialDialog(context).show {

        title(1,"")
        customView(R.layout.date_picker)
        positiveButton(R.string.ok) {
            if (editText.id == R.id.et_batch_from){
               val startYear:Int = number2.getValue()
            }
            else{
              val endYear = number2.getValue()
            }
            showToast(" " + months[number.getValue()] + "," + number2.getValue())
            editText.setText(months[number.getValue()] + "," + number2.getValue())
        }

    }
    number=dialog.findViewById(R.id.numberPicker);
    number2=dialog.findViewById(R.id.numberPicker2);
    number.minValue = 0
    number2.minValue = 1970
    number.maxValue = 11
    number2.maxValue = 2025
    if (editText.id == R.id.et_batch_from)
        number2.value = num
    else
        number2.value = num2
    number.displayedValues = months
}*/
