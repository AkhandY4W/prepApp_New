package com.youth4work.prepapp.ui.forum;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.Answers;
import com.youth4work.prepapp.network.model.request.ForumAnswerEditRequest;
import com.youth4work.prepapp.network.model.request.ForumAnswerRequest;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Toaster;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.varifyMobileNo4Forums;

public class AddAnswerActivity extends BaseActivity {
    EditText etAddQuestionAnswer;
    Button btnSubmit, btnCancle, btnUpdate;
    int forumid, answerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        etAddQuestionAnswer = findViewById(R.id.et_add_question_ans);
        btnCancle = findViewById(R.id.btn_cancle);
        btnSubmit = findViewById(R.id.btn_submit);
        btnUpdate = findViewById(R.id.btn_update);
        forumid = getIntent().getIntExtra("obj", 0);
        mUserManager = UserManager.getInstance(this);
        for (Answers answers : PrepForumDetails.mprepForumDetails.answersList) {
            if (answers.getAnswerId() == PrepForumDetails.mprepForumDetails.getGivenAnsweredId()) {
                etAddQuestionAnswer.setText(answers.getAnswer());
                answerId = answers.getAnswerId();
                btnSubmit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
            }
        }
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etAddQuestionAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etAddQuestionAnswer.hasFocus()) {
                    etAddQuestionAnswer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (etAddQuestionAnswer.getText().toString().length() > 0) {
                                // btnSubmit.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.assets_01_ic_send_active, null));
                                if (btnSubmit != null)
                                    btnSubmit.setOnClickListener((View v) -> {
                                        // btnSubmit.setEnabled(false);
                                        //if (btnSubmit.getText().toString().equalsIgnoreCase("submit")) {
                                        if (mUserManager.getUser().isMobileVerified() && mUserManager.getUser().getUserStatus().equals("A")) {

                                            if (!etAddQuestionAnswer.getText().toString().isEmpty()) {

                                            prepService.PostForumAnswer(new ForumAnswerRequest(forumid, etAddQuestionAnswer.getText().toString(), mUserManager.getUser().getUserId())).enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful())
                                                        Toaster.showLong(AddAnswerActivity.this, "Posted successfully!");
                                                        finish();
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toaster.showLong(AddAnswerActivity.this, "Oops Some error occured, Please try again in a while!");
                                                    btnSubmit.setEnabled(true);
                                                }
                                            });
                                        } else {
                                            Toaster.showLong(AddAnswerActivity.this, "Please provide a valid input!");
                                            btnSubmit.setEnabled(true);
                                        }
                                        }
                                        else {
                                            varifyMobileNo4Forums(AddAnswerActivity.this);
                                        }

                                    });

                                if (btnUpdate != null)
                                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            {
                                                if (mUserManager.getUser().isMobileVerified() && mUserManager.getUser().getUserStatus().equals("A")) {

                                                    if (!etAddQuestionAnswer.getText().toString().isEmpty()) {
                                                        prepService.EditForumAnswer(new ForumAnswerEditRequest(answerId, etAddQuestionAnswer.getText().toString(), mUserManager.getUser().getUserId())).enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful())
                                                                    Toaster.showLong(AddAnswerActivity.this, "Posted successfully!");
                                                                etAddQuestionAnswer.setText("");
                                                                recreate();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                Toaster.showLong(AddAnswerActivity.this, "Oops Some error occured, Please try again in a while!");
                                                            }
                                                        });
                                                    } else {
                                                        Toaster.showLong(AddAnswerActivity.this, "Please provide a valid input!");
                                                    }
                                                }
                                                else {
                                                    varifyMobileNo4Forums(AddAnswerActivity.this);
                                                }
                                            }
                                        }
                                    });

                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
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
}
