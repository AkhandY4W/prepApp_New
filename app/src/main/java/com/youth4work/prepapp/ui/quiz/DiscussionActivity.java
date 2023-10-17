package com.youth4work.prepapp.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Comments;
import com.youth4work.prepapp.network.model.Question;
import com.youth4work.prepapp.network.model.request.CommentRequest;
import com.youth4work.prepapp.ui.adapter.DiscussionItem;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class DiscussionActivity extends BaseActivity {

    @NonNull
    private static String QUESTION = "QUESTION";
    static Question qs;
    @Nullable
    @BindView(R.id.discussionsRecyclerView)
    RecyclerView discussionsRecyclerView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    /*@BindView(R.id.fab_new_solution)
    FloatingActionButton fabNewSolution;*/
    @BindView(R.id.que)
    TextView que;
    @BindView(R.id.que_option1)
    TextView queOption1;
    @BindView(R.id.ans_image)
    ImageView ansImage;
    @BindView(R.id.que_image)
    ImageView queImage;

    @BindView(R.id.et_ans)
    EditText etAns;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Question mQuestion;
    private Tracker mTracker;

    public static void show(@NonNull Context fromActivity, Question mQuestion) {
        qs=mQuestion;
        Intent intent = new Intent(fromActivity, DiscussionActivity.class);
        intent.putExtra(QUESTION, new Gson().toJson(mQuestion));
        fromActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Question Discussion");
        queOption1.setText(qs.getOptions().get(0).getOption());
        mQuestion = new Gson().fromJson(getIntent().getStringExtra(QUESTION), Question.class);
        if(qs.getQuestionImgUrl()!=null&&!qs.getQuestionImgUrl().equals("")){
            queImage.setVisibility(View.VISIBLE);
            Picasso.get().load(qs.getQuestionImgUrl()).into(queImage);
        }
        if(qs.getOptions().get(0).getOptionImgUrl()!=null&&!qs.getOptions().get(0).getOptionImgUrl().equals("")){
            ansImage.setVisibility(View.VISIBLE);
            Picasso.get().load(qs.getOptions().get(0).getOptionImgUrl()).into(ansImage);
        }
        que.setText("\n"+(mQuestion.getQuestion()));
        progressActivity.showLoading();
        loadComments();
       /* fabNewSolution.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionActivity.this, AddSolutionActivity.class);
            intent.putExtra("QuestionId", mQuestion.getId());
            startActivity(intent);
        });*/
        etAns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnSubmit.setEnabled(false);
                btnSubmit.setBackgroundResource(R.drawable.ic_send_gray_24dp);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0){
                    btnSubmit.setEnabled(true);
                    btnSubmit.setBackgroundResource(R.drawable.ic_send_blue_24dp);
                }
                else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setBackgroundResource(R.drawable.ic_send_gray_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //btnSubmit.setBackgroundResource(R.drawable.ic_send_blue_24dp);

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAns.getText().length() > 0) {
                    prepService.postComment(new CommentRequest(mUserManager.getUser().getUserId(), mQuestion.getId(), etAns.getText().toString())).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful())
                                etAns.setText("");
                            Toaster.showLong(DiscussionActivity.this, "Posted successfully!");
                            recreate();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
                else{
                    Toast.makeText(DiscussionActivity.this,"Please type your solution",Toast.LENGTH_SHORT).show();
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

    private void loadComments() {
        prepService.comments(mQuestion.getId()).enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, @NonNull Response<Comments> response) {
                if (response.isSuccessful())

                    setupCommentsUI(response.body());
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {

            }
        });

    }

    private void setupCommentsUI(@NonNull Comments comments) {
        discussionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        discussionsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        FastItemAdapter fastItemAdapter = new FastItemAdapter();
        discussionsRecyclerView.setAdapter(fastItemAdapter);
        for (Comments.Comment comment : comments.getComment()) {
            fastItemAdapter.add(new DiscussionItem(comment));
        }
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange=appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Discussion");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        progressActivity.showContent();
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
        finish();
    }


}