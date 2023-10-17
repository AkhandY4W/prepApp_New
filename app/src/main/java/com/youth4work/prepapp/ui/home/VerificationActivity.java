package com.youth4work.prepapp.ui.home;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.startup.AllWebView;
import com.youth4work.prepapp.ui.startup.LoginActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
import com.youth4work.prepapp.util.Keyboard;
import com.youth4work.prepapp.util.Toaster;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends BaseActivity {

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_Code)
    EditText etCode;
    @BindView(R.id.btgetcode)
    PrepButton btGetCode;
    @BindView(R.id.btsubmitotp)
    PrepButton btSubmitOtp;
    @BindView(R.id.txt_retryotp)
    TextView txtRetryotp;
    @BindView(R.id.txtemail_id)
    TextView txtEmailId;
    @BindView(R.id.varifyemailimage)
    ImageView varifyEmailImage;
    @BindView(R.id.btnemailverify)
    PrepButton btnEmailVerify;
    @BindView(R.id.loadinglayout)
    LinearLayout loadingLayout;
    @BindView(R.id.mobileverify)
    LinearLayout mobileVerify;
    @BindView(R.id.pinlayout)
    LinearLayout pinLayout;
    @BindView(R.id.txtmobileno)
    TextView txtMobileNo;
    @BindView(R.id.varifymobileimage)
    ImageView varifyMobileImage;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.btnletstart)
    PrepButton btnLetStart;
    @BindView(R.id.txtwebverify)
    TextView txtwebverify;
    @BindView(R.id.layoutchangeemail)
    LinearLayout layoutchangeemail;
    @BindView(R.id.imgmobilevarifysucess)
    ImageView imgmobilevarifysucess;
    //private PrepService prepService;
    int retrycount = 0;
    User oUser = null;
    @BindView(R.id.btngotemailcode)
    PrepButton btnGotEmailCode;

    public static void show(@NonNull Context fromActivity) {
        Intent intent = new Intent(fromActivity, VerificationActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        oUser = mUserManager.getUser();
        //prepService = PrepApi.provideRetrofit();
        etMobile.setText(oUser.getContactNo());
        txtEmailId.setText(oUser.getEmailID());
        checkNumber();
        if (oUser.isMobileVerified()) {
            mobileVerify.setVisibility(View.VISIBLE);
            varifyMobileImage.setVisibility(View.VISIBLE);
            txtMobileNo.setText(etMobile.getText().toString().trim());
            pinLayout.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            textView8.setVisibility(View.GONE);
            imgmobilevarifysucess.setVisibility(View.VISIBLE);
            //txtRetryotp.setVisibility(View.GONE);
            btnLetStart.setVisibility(View.VISIBLE);
        }
        if (oUser.getUserStatus().equals("A")) {
            varifyEmailImage.setVisibility(View.VISIBLE);
            layoutchangeemail.setVisibility(View.GONE);
        } else {
            btnEmailVerify.setVisibility(View.VISIBLE);
            //txtwebverify.setVisibility(View.VISIBLE);
        }
        btGetCode.setOnClickListener(v -> {

            if (etMobile.getText().toString().trim().equals(mUserManager.getUser().getContactNo())) {
                GetOtp();
            } else {
                CheckNoRegister();

            }
        });
        txtwebverify.setOnClickListener(v ->
                AllWebView.LoadWebView(VerificationActivity.this, "https://www.youth4work.com/User/EditProfile", "VerificationActivity"));
        txtRetryotp.setOnClickListener(v -> {
            if (retrycount < 3) {
                if (etMobile.getText().toString().trim().equals(mUserManager.getUser().getContactNo())) {
                    GetOtp();
                    retrycount++;
                } else {
                    retrycount++;
                    CheckNoRegister();

                }
            } else {
                Toast.makeText(VerificationActivity.this, "Try again after some time", Toast.LENGTH_SHORT).show();
                txtRetryotp.setVisibility(View.GONE);
                btnLetStart.setVisibility(View.VISIBLE);
            }
        });

        btSubmitOtp.setOnClickListener(v -> prepService.verifyYMobile(oUser.getUserId(), etMobile.getText().toString().trim(), etCode.getText().toString().trim()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() == 1) {
                    oUser.setMobileVerified(true);
                    mUserManager.setUser(oUser);
                    // String as= String.valueOf(response.body());
                    mobileVerify.setVisibility(View.VISIBLE);
                    varifyMobileImage.setVisibility(View.VISIBLE);
                    txtMobileNo.setText(etMobile.getText().toString().trim());
                    pinLayout.setVisibility(View.GONE);
                    textView7.setVisibility(View.GONE);
                    textView8.setVisibility(View.GONE);
                    txtRetryotp.setVisibility(View.GONE);
                    btnLetStart.setVisibility(View.VISIBLE);
                    imgmobilevarifysucess.setVisibility(View.VISIBLE);
                    Toast.makeText(VerificationActivity.this, "Mobile Number has succesfully verified", Toast.LENGTH_SHORT).show();
                    Keyboard.hideSoftInput(etCode);
                } else {
                    etCode.setError("Wrong OTP,Please try again");
                    etCode.requestFocus();
                    Toast.makeText(VerificationActivity.this, "Wrong OTP, Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(VerificationActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
            }
        }));
        btnEmailVerify.setOnClickListener(v -> {
            loadingLayout.setVisibility(View.VISIBLE);
            btnLetStart.setVisibility(View.GONE);
            prepService.SendverificationLink(txtEmailId.getText().toString().trim()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        btnEmailVerify.setVisibility(View.INVISIBLE);
                        Toast.makeText(VerificationActivity.this, "Verification mail has send to your register Emailid", Toast.LENGTH_SHORT).show();
                        btnGotEmailCode.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
                        // btnLetStart.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(VerificationActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnGotEmailCode.setOnClickListener(v -> {
            mPreferencesManager.clearAllUserData();
            finish();
            Toaster.showLong(VerificationActivity.this, "Login again to check status");
            AppEventsLogger.clearUserID();
            LoginActivity.show(VerificationActivity.this);
        });

        btnLetStart.setOnClickListener(v ->
                DashboardActivity.showfromverification(VerificationActivity.this, true));
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

    @Override
    public void onBackPressed() {
        DashboardActivity.showfromverification(VerificationActivity.this, true);
        //super.onBackPressed();

    }

    public void GetOtp() {
        loadingLayout.setVisibility(View.VISIBLE);
        btGetCode.setVisibility(View.GONE);
        prepService.getOtpCode(oUser.getUserId(), etMobile.getText().toString().trim()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body()) {
                    loadingLayout.setVisibility(View.GONE);
                    btSubmitOtp.setVisibility(View.VISIBLE);
                    etCode.setVisibility(View.VISIBLE);
                    txtRetryotp.setVisibility(View.VISIBLE);
                    Toast.makeText(VerificationActivity.this, "OTP send Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    btSubmitOtp.setVisibility(View.VISIBLE);
                    etCode.setVisibility(View.VISIBLE);
                    txtRetryotp.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(VerificationActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CheckNoRegister() {
        //boolean isValid = true;
        String mobile = etMobile.getText().toString().replace("+1","").replace("+91","").replace("+", "");
        if (mobile != null && mobile.length() > 0) {
            //   Toaster.showShort(SignUpActivity.this, "Validating Mobile...");
            //+5215555215554
            prepService.mobileExists(mobile).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = null;
                        try {
                            responseString = response.body().string();
                            if(responseString!=null) {
                                if (responseString.equalsIgnoreCase("\"0\"")) {
                                    GetOtp();
                                } else if (responseString.contains("1")) {
                                    loadingLayout.setVisibility(View.GONE);
                                    Toast.makeText(VerificationActivity.this, "The Number is already register", Toast.LENGTH_SHORT).show();
                                    etMobile.setError("The Number is already register");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        else {
            etMobile.setError("Please enter valid mobile no");
        }
    }

    public void checkNumber(){
        String phoneNumber = etMobile.getText().toString().replace("+1","").replace("+91","").replace("+", "");
        if (phoneNumber.length() <= 10) {
            //return phoneNumber;
            etMobile.setText(phoneNumber);
            //CheckNoRegister(phoneNumber);
        } else if (phoneNumber.length() > 10) {
            String newNumber= phoneNumber.substring(phoneNumber.length() - 10);
            etMobile.setText(newNumber);
           // CheckNoRegister(phoneNumber);
        } else {
            // whatever is appropriate in this case
            etMobile.setError("Please enter valid mobile no");
            //throw new IllegalArgumentException("word has less than 10 characters!");
        }
    }
}

