package com.youth4work.prepapp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.CouponCode;
import com.youth4work.prepapp.ui.adapter.CouponAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyCouponActivity extends BaseActivity {

    Button btnApply;
    EditText editTextcouponcode;
    private Tracker mTracker;
    List<CouponCode> mcouponcode;
    RecyclerView listCoupon;
    CouponAdapter mcouponAdapter;
    ProgressRelativeLayout progressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        progressActivity = findViewById(R.id.progressActivity);
        progressActivity.showLoading();
        Constants.sendScreenImageName(mTracker, "Apply Coupon");
        btnApply = findViewById(R.id.btnapply);
        editTextcouponcode = findViewById(R.id.editTextcouponcode);
        listCoupon = findViewById(R.id.list_coupon);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promoCode = editTextcouponcode.getText().toString().trim();
                if (promoCode != null && !promoCode.isEmpty()) {
                    Intent intent = new Intent(ApplyCouponActivity.this, UpgradePlanActivity.class);
                    intent.putExtra("promocode", promoCode);
                    AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(ApplyCouponActivity.this);
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(ApplyCouponActivity.this);
                    Bundle bundlefb = new Bundle();
                    bundlefb.putString(FirebaseAnalytics.Param.ITEM_ID, promoCode);
                    bundlefb.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundlefb);
                    appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, bundlefb);
                    startActivity(intent);

                } else {
                    editTextcouponcode.setError("Field cannot be left blank.");
                    Toast.makeText(ApplyCouponActivity.this, "Enter Coupon Code", Toast.LENGTH_LONG).show();
                }
            }
        });
        prepService.couponcode().enqueue(new Callback<List<CouponCode>>() {
            @Override
            public void onResponse(Call<List<CouponCode>> call, Response<List<CouponCode>> response) {
                mcouponcode = response.body();
                setuplistcopuon();
            }


            @Override
            public void onFailure(Call<List<CouponCode>> call, Throwable t) {

            }
        });


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

    private void setuplistcopuon() {
        listCoupon.setLayoutManager(new LinearLayoutManager(ApplyCouponActivity.this));
        listCoupon.setHasFixedSize(true);
        mcouponAdapter = new CouponAdapter(mcouponcode, ApplyCouponActivity.this);
        listCoupon.setAdapter(mcouponAdapter);
        progressActivity.showContent();

    }
}
