package com.youth4work.prepapp.ui.startup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
import com.youth4work.prepapp.util.Toaster;
import com.youth4work.prepapp.util.ViewUtils;

import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.txt_email)
    MaterialEditText txtEmail;

    @Nullable
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    boolean requestSent = false;
    @Nullable
    @BindView(R.id.btn_send_reset_link)
    PrepButton btnSendResetLink;

    public static void show(@NonNull Activity fromActivity) {
        Intent intent = new Intent(fromActivity, ForgotPasswordActivity.class);
        fromActivity.startActivityForResult(intent, 5000);
    }

    public final static boolean isValidEmail(@NonNull CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
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

    @OnClick(R.id.btn_send_reset_link)
    void OnSendResetClicked() {
        assert txtEmail != null;
        progressActivity.showLoading();
        if (isValidEmail(txtEmail.getText().toString())) {
            prepService.emailIDExists(txtEmail.getText().toString()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if(response.isSuccessful())
                    try {
                        if (response.body().string().equalsIgnoreCase("\"0\"")) {
                            progressActivity.showContent();
                            Toaster.showLong(ForgotPasswordActivity.this, "Email not registered!");
                        } else {
                            if (!requestSent) {
                                prepService.forgotPassword(txtEmail.getText().toString()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            progressActivity.showContent();
                                            requestSent = true;
                                            btnSendResetLink.setText("Proceed to login");
                                            ViewUtils.setGone(txtEmail, true);
                                            ViewUtils.setGone(txtForgotPassword, false);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        progressActivity.showContent();
                                    }
                                });
                            } else {
                                //Intent returnIntent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                                //startActivity(returnIntent);
                                progressActivity.showContent();
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("email", txtEmail.getText().toString());
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                    } catch (IOException e) {
                        progressActivity.showContent();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressActivity.showContent();
                }
            });
        } else {
            progressActivity.showContent();
            txtEmail.setError("Please enter valid email.");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //Intent Login = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        //startActivity(Login);
        finish();
    }
}
