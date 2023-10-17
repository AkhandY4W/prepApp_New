package com.youth4work.prepapp.ui.forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.request.CommentOnForumAnswerRequest;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Toaster;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.varifyMobileNo4Forums;

public class AddCommentActivity extends BaseActivity {
    EditText etAddQusAnsComment;
    Button btnSubmit, btnCancle;
    int answerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        etAddQusAnsComment = findViewById(R.id.et_add_ques_ans_comment);
        btnCancle = findViewById(R.id.btn_cancle);
        btnSubmit = findViewById(R.id.btn_submit);
        answerId = getIntent().getIntExtra("Answer id", 0);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (btnSubmit != null)
            btnSubmit.setOnClickListener((View v) -> {
                btnSubmit.setEnabled(false);
                if (mUserManager.getUser().isMobileVerified() && mUserManager.getUser().getUserStatus().equals("A")) {

                    if (!etAddQusAnsComment.getText().toString().isEmpty()) {
                        prepService.CommentOnForumAnswer(new CommentOnForumAnswerRequest(answerId, etAddQusAnsComment.getText().toString(), mUserManager.getUser().getUserId())).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                etAddQusAnsComment.setText("");
                                btnSubmit.setEnabled(true);
                                Toaster.showLong(AddCommentActivity.this, "Posted successfully!");
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toaster.showLong(AddCommentActivity.this, "Oops Some error occured, Please try again in a while!");
                                btnSubmit.setEnabled(true);
                            }
                        });
                    } else {
                        Toaster.showLong(AddCommentActivity.this, "Please provide a valid input!");
                        btnSubmit.setEnabled(true);
                    }

                } else {
                    varifyMobileNo4Forums(AddCommentActivity.this);
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
