package com.youth4work.prepapp.ui.forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.request.ForumRequest;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Toaster;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.varifyMobileNo4Forums;

public class AskQuestionActivity extends BaseActivity {
    EditText etAddQuestion, etAddQuestionDesc;
    Button btnSubmit, btnCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        etAddQuestion = findViewById(R.id.et_add_question);
        etAddQuestionDesc = findViewById(R.id.et_add_question_desc);
        btnCancle = findViewById(R.id.btn_cancle);
        btnSubmit = findViewById(R.id.btn_submit);

        btnCancle.setOnClickListener(v -> {
            finish();
            ForumFragment.newInstance();
        });

        if (btnSubmit != null)
            btnSubmit.setOnClickListener((View v) -> {
                btnSubmit.setEnabled(false);
                if (mUserManager.getUser().isMobileVerified() && mUserManager.getUser().getUserStatus().equals("A")) {

                    if (!etAddQuestion.getText().toString().isEmpty() && etAddQuestion.getText().length() >= 30) {
                        if (etAddQuestionDesc != null)
                            if (!etAddQuestionDesc.getText().toString().isEmpty() && etAddQuestionDesc.getText().length() >= 60) {
                                prepService.PostForum(new ForumRequest(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID(), etAddQuestion.getText().toString(), etAddQuestionDesc.getText().toString())).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        etAddQuestionDesc.setText("");
                                        etAddQuestion.setText("");
                                        Toaster.showLong(AskQuestionActivity.this, "Posted successfully!");
                                        finish();
                                        ForumFragment.newInstance();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        btnSubmit.setEnabled(true);
                                    }
                                });
                            } else {
                                Toaster.showShort(AskQuestionActivity.this, "Please enter description upto 60 character.");
                                btnSubmit.setEnabled(true);
                                etAddQuestionDesc.requestFocus();
                            }
                    } else {
                        Toaster.showShort(AskQuestionActivity.this, "Please enter title upto 30 character.");
                        etAddQuestion.requestFocus();
                        btnSubmit.setEnabled(true);
                    }
                } else {
                    varifyMobileNo4Forums(AskQuestionActivity.this);
                }
            } );

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
}
