package com.youth4work.prepapp.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
import com.youth4work.prepapp.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class PaymentStatusActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.txt_payment_icon)
    IconTextView txtPaymentIcon;
    @Nullable
    @BindView(R.id.txt_payment)
    TextView txtPayment;
    @Nullable
    @BindView(R.id.btn_after_payment)
    PrepButton btnAfterPayment;
    private Tracker mTracker;
    public static int planValidity=0;

    public static final String EXTRA_TRANSACTION_ID = "transaction_id";
    public static final String EXTRA_PAYMENT_STATUS = "status";
    public static final String EXTRA_SERVICE_ID = "id";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_MOBILE = "EXTRA_MOBILE";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_Amount = "EXTRA_Amount";
    public static final String EXTRA_Real_Amount = "EXTRA_Real_Amount";
    public static final String EXTRA_COUPON_CODE = "EXTRA_COUPON_CODE";
    public static final String EXTRA_TAX = "EXTRA_TAX";

    public static void show(@NonNull Context context, boolean status, String transactionId, int serviceID, boolean isFromOrder, int amount,int validity,String couponCode,String tax,String realAmount) {
        Intent intent = new Intent(context, PaymentStatusActivity.class);
        intent.putExtra(EXTRA_PAYMENT_STATUS, status);
        intent.putExtra(EXTRA_TRANSACTION_ID, transactionId);
        intent.putExtra(EXTRA_SERVICE_ID, serviceID);
        intent.putExtra(EXTRA_Amount, amount);
        intent.putExtra(EXTRA_COUPON_CODE, couponCode);
        intent.putExtra(EXTRA_Real_Amount, realAmount);
        intent.putExtra(EXTRA_TAX, tax);
        intent.putExtra("isFromOrder", isFromOrder);
        planValidity=validity;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        ButterKnife.bind(this);
        // Change in all activity to show icon
        Iconify
                .with(new FontAwesomeModule());
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Payment status");
        setTitle("Payment");
        if (getIntent().getExtras().getBoolean(EXTRA_PAYMENT_STATUS)) {
            txtPayment.setText(R.string.payment_successfull_n_your_pro_pack_is_now_active);
            txtPaymentIcon.setText(R.string.correct_icon);
            txtPaymentIcon.setTextColor(getResources().getColor(R.color.fruit_salad));
            //UserUpgrade(Long userid, int amount, int serviceid, String transID, String appName, String couponCode, String actualamount, String appVersionId, String domainName, int testCategoryIdUpgrade, String taxamount) {
            String currentVersion = null;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName().trim(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            UserUpgrade userUpgrade = new UserUpgrade(mUserManager.getUser().getUserId(), getIntent().getIntExtra(EXTRA_Amount,0),getIntent().getIntExtra(EXTRA_SERVICE_ID, -1), getIntent().getStringExtra(EXTRA_TRANSACTION_ID),getApplicationContext().getPackageName(), getIntent().getStringExtra(EXTRA_COUPON_CODE),EXTRA_Real_Amount,currentVersion,"",mUserManager.getUser().getSelectedCatID(), getIntent().getStringExtra(EXTRA_TAX));
            btnAfterPayment.setText("Continue");
            prepService.upgradePlan(userUpgrade).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    mUserManager.getUser().setPrepPlanID(getIntent().getExtras().getInt(EXTRA_SERVICE_ID));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Calendar calendar = Calendar.getInstance((Locale.ENGLISH));
                    try {
                        calendar.setTime(sdf.parse(mUserManager.getUser().getPlanStartDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calendar.add(Calendar.DATE, planValidity);
                    mUserManager.getUser().setPlanEndDate(sdf.format(calendar.getTime()));
                    mUserManager.setUser(mUserManager.getUser());
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } else {
            btnAfterPayment.setText("Go back");
            txtPayment.setText(R.string.payment_failure_message);
            txtPaymentIcon.setTextColor(getResources().getColor(R.color.red_orange));
            txtPaymentIcon.setText(R.string.wrong_icon);
        }
    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {

    }

    @Override
    public void onAppNotFound() {

    }

    @OnClick(R.id.btn_after_payment)
    public void onClick() {
        if (getIntent().getExtras().getBoolean("status")) {
            finish();
            DashboardActivity.show(PaymentStatusActivity.this,false);
        } else {
            finish();
        }
    }
}
