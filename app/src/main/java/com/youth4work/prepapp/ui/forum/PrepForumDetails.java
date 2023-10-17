package com.youth4work.prepapp.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Answers;
import com.youth4work.prepapp.network.model.ForumDetails;
import com.youth4work.prepapp.ui.adapter.AnswersItem;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.Toaster;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;
//import com.google.android.gms.analytics.Tracker;

public class PrepForumDetails extends BaseActivity {
    @Nullable
    @BindView(R.id.list_prep_forum_details)
    RecyclerView prepForumList;
    private Tracker mTracker;
    private PrepService prepService;
    static ForumDetails mprepForumDetails;
    protected UserManager mUserManager;

    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;

    int forumid;
    String answer;
    int answerId;

    @Nullable
    @BindView(R.id.que)
    TextView txtquestion;

    @Nullable
    @BindView(R.id.questionByName)
    TextView txtquestionByName;

    @Nullable
    @BindView(R.id.imageAnswer)
    CircularImageView imageView;

    @Nullable
    @BindView(R.id.Description)
    TextView txtDescription;
    @Nullable
    @BindView(R.id.date)
    TextView txtDate;
    String questionTitle;
    @BindView(R.id.fab_add_answer)
    FloatingActionButton fabAddAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_forum_details);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressActivity.showLoading();
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(PrepForumDetails.this).getToken()[0]);
        forumid = getIntent().getIntExtra("obj", 0);
        mUserManager = UserManager.getInstance(this);
        fillDetails();
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "ForumDetails");
        //Picasso.with(PrepForumDetails.this).load(mUserManager.getUser().getImgUrl()).into(commenter_photo);
        fabAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PrepForumDetails.this,AddAnswerActivity.class);
                intent.putExtra("obj", forumid);
                startActivity(intent);
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

    private void fillDetails() {
        prepService.prepForumDetails(forumid, mUserManager.getUser().getUserId()).enqueue(new Callback<ForumDetails>() {
            @Override
            public void onResponse(Call<ForumDetails> call, @NonNull Response<ForumDetails> response) {
                if(response.isSuccessful())
                    mprepForumDetails = response.body();
                if (mprepForumDetails.ForumId > 0) {
                    fillList();
                    questionTitle=mprepForumDetails.Title;
                    txtquestion.setText("\n"+ questionTitle);
                    txtDescription.setText(mprepForumDetails.Forum);
                    String commentedTime = mprepForumDetails.LastModified;
                    String text = "By "+Constants.getFirstName(mprepForumDetails.CreatedByName)+" • "+"Answers "+mprepForumDetails.TotalAnswer+" • "+"Views "+mprepForumDetails.TotalView;
                    txtquestionByName.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    Picasso.get().load(mprepForumDetails.CreatedByPic).into(imageView);
                    final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
                    AppBarLayout appBarLayout = findViewById(R.id.app_bar);
                    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        boolean isShow = true;
                        int scrollRange = -1;

                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                            if (scrollRange == -1) {
                                scrollRange = appBarLayout.getTotalScrollRange();
                            }
                            if (scrollRange + verticalOffset == 0) {
                                collapsingToolbarLayout.setTitle(questionTitle);
                                isShow = true;
                            } else if(isShow) {
                                collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                                isShow = false;
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ForumDetails> call, Throwable t) {
                Toaster.showLong(PrepForumDetails.this, t.toString());
                progressActivity.showContent();
            }
        });


    }

    void fillList() {
        {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            progressActivity.showContent();
            if (prepForumList != null && mprepForumDetails.TotalAnswer > 0) {
                prepForumList.setLayoutManager(llm);
                prepForumList.setHasFixedSize(true);
                // prepForumList.addItemDecoration(new DividerItemDecoration(getApplication(), true));
                FastItemAdapter<AnswersItem> PrepForumItemFastItemAdapter = new FastItemAdapter<>();
                prepForumList.setAdapter(PrepForumItemFastItemAdapter);

                for (Answers answers : mprepForumDetails.answersList) {
                    if (answers.getAnswerId() == mprepForumDetails.getGivenAnsweredId()) {
                        answer = answers.getAnswer();
                        answerId = answers.getAnswerId();
                    }
                    PrepForumItemFastItemAdapter.add(new AnswersItem(answers, PrepForumDetails.this, answers.getAnswerId(), mUserManager.getUser().getUserId()));

                }

              /*  PrepForumItemFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
                    Intent commentsAnswers = new Intent(PrepForumDetails.this, CommentsActivity.class);
                    commentsAnswers.putExtra("Answer id", item.answers.getAnswerId());
                    commentsAnswers.putExtra("comment",item.answers.getAnswer());
                    commentsAnswers.putExtra("commenter_name",item.answers.getAnswerByName());
                    commentsAnswers.putExtra("comenter_img_url",item.answers.getAnswerByPic());
                    startActivity(commentsAnswers);
                    return true;
                });*/
            } else {
                progressActivity.showContent();
                prepForumList.setVisibility(View.GONE);
                //txtLabel.setVisibility(View.GONE);
                //txtLabelWarning.setVisibility(View.VISIBLE);

            }

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
}
