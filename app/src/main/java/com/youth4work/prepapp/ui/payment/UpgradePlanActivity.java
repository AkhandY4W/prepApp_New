package com.youth4work.prepapp.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Plan;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.ui.adapter.PlansAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.ChooseExamActivity;
import com.youth4work.prepapp.ui.home.MyPrepActivity;
import com.youth4work.prepapp.ui.views.CustomTextViewFontRegular;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import io.intercom.android.sdk.Intercom;
import com.google.android.gms.analytics.Tracker;*/

public class UpgradePlanActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.txt_plan_header)
    TextView txtPlanHeader;
    @Nullable
    @BindView(R.id.list_plans)
    RecyclerView listPlans;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @BindView(R.id.txt_plan_message)
    CustomTextViewFontRegular txtPlanMessage;
    @BindView(R.id.coupon_applied_button)
    CustomTextViewFontRegular couponAppliedButton;
    PlansAdapter mPlansAdapter;
    @BindView(R.id.automaticcoupon)
    LinearLayout automaticCoupon;
    List<Plan> mPlans;
    private Plan currentPlan;

    private Tracker mTracker;
    private String couponcode = "";
        private int amount = 0;
        UserUpgrade userUpgrade;

    public static void show(@NonNull Context fromActivity) {
        Intent intent = new Intent(fromActivity, UpgradePlanActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_plan);
        ButterKnife.bind(this);
        couponcode = getIntent().getStringExtra("promocode");
        userUpgrade = new Gson().fromJson(PreferencesManager.instance(UpgradePlanActivity.this).getPendingPaymentDetails(),UserUpgrade.class);
        BaseActivity.getPayment(UpgradePlanActivity.this,userUpgrade,true);
        couponAppliedButton.setOnClickListener(view -> {
            startActivity(new Intent(UpgradePlanActivity.this, ApplyCouponActivity.class));
        });
    com.joanzapata.iconify.Iconify
                .with(new FontAwesomeModule());
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Upgrade plan");
      if (couponcode != null && !couponcode.isEmpty()) {
            setupServicepack(couponcode);
        } else {
            setupServicepack("abcd");
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

    private boolean isCouponValid() {
        boolean res = false;
        if (mPlans.size() > 0) {
            for (int i = 0; i < mPlans.size(); i++) {
                if (mPlans.get(i).getAmount() != mPlans.get(i).getDisAmount()) {
                    res = true;
                }
            }
        }
        return res;
    }

    private void setupServicepack(String couponcode) {
        progressActivity.showLoading();
        prepService.plans(couponcode,mUserManager.getUser().getUserId()).enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, @NonNull Response<List<Plan>> response) {
                mPlans = response.body();
                filterPlans();
                setupPlans();
                if (!couponcode.equals("abcd") && !isCouponValid()) {
                    Toast.makeText(UpgradePlanActivity.this,"Invalid Coupon Code, Please Try Another Coupon Code",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {

            }
        });
    }

    private void filterPlans() {
        List<Plan> filteredPlans = new ArrayList<>();
        if (mUserManager.getUser().getPrepPlanID() != 0) {
            currentPlan = getCurrentPlan();
            for (Plan plan : mPlans) {
                if (plan.getAmount() > currentPlan.getAmount()) {
                    filteredPlans.add(plan);
                    automaticCoupon.setVisibility(View.VISIBLE);
                }
            }
            mPlans.clear();
            mPlans = filteredPlans;
        }
        else {
            automaticCoupon.setVisibility(View.VISIBLE);
        }
    }

    private Plan getCurrentPlan() {
        Plan currentPlan = null;
        for (Plan plan : mPlans) {
            if (plan.getServiceID() == mUserManager.getUser().getPrepPlanID()) {
                currentPlan = plan;
            }
        }
        return currentPlan;
    }

    private void setupPlans() {
        listPlans.setLayoutManager(new LinearLayoutManager(UpgradePlanActivity.this));
        listPlans.setHasFixedSize(true);
        mPlansAdapter = new PlansAdapter(mPlans);
        listPlans.setAdapter(mPlansAdapter);
        progressActivity.showContent();
        // You have already upgraded to xx months plan which expires on dd/mm/yyyy.

        if (currentPlan != null) {
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
            String message = "You have already upgraded to " + Math.abs(currentPlan.getValidity() / 30) + " months plan which expires on " + sfd.format(mUserManager.getEndDate());
            txtPlanMessage.setText(message);
            txtPlanMessage.setVisibility(View.VISIBLE);

        }

        if (mPlans.size() == 0) {
            txtPlanHeader.setVisibility(View.GONE);
        }

        mPlansAdapter.setOnItemClickListener((itemView, position) -> {
            PlanReviewActivity.show(UpgradePlanActivity.this, mPlans.get(position), couponcode);
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }
}

