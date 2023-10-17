package com.youth4work.prepapp.ui.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.request.CommentRequest;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Toaster;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddSolutionActivity extends BaseActivity {
    EditText etAddQusAns;
    Button btnSubmit, btnCancle;
    int questionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_solution);
        etAddQusAns = findViewById(R.id.et_add_ques_ans);
        btnCancle = findViewById(R.id.btn_cancle);
        btnSubmit = findViewById(R.id.btn_submit);
        questionId=getIntent().getIntExtra("QuestionId",0);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAddQusAns.getText().length() > 0) {
                    prepService.postComment(new CommentRequest(mUserManager.getUser().getUserId(), questionId, etAddQusAns.getText().toString())).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful())
                                etAddQusAns.setText("");
                            Toaster.showLong(AddSolutionActivity.this, "Posted successfully!");
                            recreate();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
                else{
                    Toast.makeText(AddSolutionActivity.this,"Please type your solution",Toast.LENGTH_SHORT).show();
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
