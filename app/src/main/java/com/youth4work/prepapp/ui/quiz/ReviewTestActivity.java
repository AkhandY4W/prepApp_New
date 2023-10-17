package com.youth4work.prepapp.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Attempt;
import com.youth4work.prepapp.network.model.EducationDetails;
import com.youth4work.prepapp.network.model.Review;
import com.youth4work.prepapp.ui.adapter.AttemptItem;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.startup.UpdateEducation;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.google.android.gms.analytics.Tracker;

public class ReviewTestActivity extends BaseActivity {

    public static final String SELECTED_DATE = "selectedDate";
    public static final String DAY_NO = "day";
    @Nullable
    @BindView(R.id.progressList)
    ProgressBar mListAttemptsProgressBar;
    @Nullable
    @BindView(R.id.attemptsRecyclerView)
    RecyclerView mListAttempts;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    /*@Nullable
    @BindView(R.id.txt_day)
    TextView txtDay;*/
    @Nullable
    @BindView(R.id.txt_score)
    TextView txtScore;
    @Nullable
    @BindView(R.id.txt_accuracy)
    TextView txtAccuracy;
    @BindView(R.id.accuracyCircleView)
    ArcProgress accuracyCircleView;
    @BindView(R.id.scoreCircleView)
    ArcProgress scoreCircleView;
    @BindView(R.id.speedCircleView)
    ArcProgress speedCircleView;
    @Nullable
    @BindView(R.id.txt_speed)
    TextView txtSpeed;
    @BindView(R.id.txt_exam_name)
    TextView txtExamName;
    public static int mHigh=20;
    private Tracker mTracker;
    private Date mSelectedDate;
    private String mDay;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private PrepService prepService;
    private FastItemAdapter<AttemptItem> fastItemAdapter;
    private EducationDetails educationDetails;
    @NonNull
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private FooterAdapter<ProgressItem> footerAdapter;
    public static void show(@NonNull Context fromActivity, @NonNull Date selectedDate, String dayNo,int mHigh1) {
        Intent intent = new Intent(fromActivity, ReviewTestActivity.class);
        intent.putExtra(SELECTED_DATE, selectedDate.getTime());
        intent.putExtra(DAY_NO, dayNo);
        mHigh=mHigh1;
        fromActivity.startActivity(intent);
    }
    public static void show(@NonNull Context fromActivity, @NonNull Date selectedDate, String dayNo) {
        Intent intent = new Intent(fromActivity, ReviewTestActivity.class);
        intent.putExtra(SELECTED_DATE, selectedDate.getTime());
        intent.putExtra(DAY_NO, dayNo);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_test);
        ButterKnife.bind(this);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Review Test");
        setTitle(mUserManager.getCategory().getCategory());
        txtExamName.setText("of "+mUserManager.getCategory().getCategory());
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(ReviewTestActivity.this).getToken()[0]);
        footerAdapter = new FooterAdapter<>();
        fastItemAdapter = new FastItemAdapter<>();
        mListAttempts.setNestedScrollingEnabled(false);
        mListAttempts.setAdapter(footerAdapter.wrap(fastItemAdapter));
        mListAttempts.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {
                footerAdapter.clear();
                footerAdapter.add(new ProgressItem().withEnabled(false));
                //simulate networking (2 seconds)
                if (loading) {
                    loading = false;
                    getMoreQS(currentPage);

                };
            }
        });
        getEducationdetails();
        initData();
        initUI();
        getResultsAndStats();

        SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
        int appUsedCountReview = preferences.getInt("appUsedCountReview", 0);
        appUsedCountReview++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("appUsedCountReview", appUsedCountReview);
        editor.apply();

        if (appUsedCountReview == 1 ||appUsedCountReview == 5  || appUsedCountReview == 500 || appUsedCountReview == 1000) {

            Intent intent=new Intent(ReviewTestActivity.this, RatingActivity.class);
            startActivity(intent);
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

    private void getEducationdetails(){
        prepService.geteducationdetails(mUserManager.getUser().getUserId()).enqueue(new Callback<EducationDetails>() {
            @Override
            public void onResponse(Call<EducationDetails> call, Response<EducationDetails> response) {
                if(response.isSuccessful() && response.body()!=null){
                    educationDetails=response.body();
                    if(educationDetails!=null) {
                        if ((educationDetails.getCollege() != null && educationDetails.getCollege().toLowerCase().equals("other")) || (educationDetails.getCourse() != null && educationDetails.getCourse().toLowerCase().equals("other")) || (educationDetails.getSpecialization() != null && educationDetails.getSpecialization().toLowerCase().equals("other")) || (educationDetails.getUniversity() != null && educationDetails.getUniversity().toLowerCase().equals("other"))) {
                            UpdateEducation.showeducationdeatils(ReviewTestActivity.this, educationDetails);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<EducationDetails> call, Throwable t) {

            }
        });
    }
    private void initData() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getIntent().getExtras().getLong(SELECTED_DATE, -1)));
        mSelectedDate = cal.getTime();
        mDay = getIntent().getStringExtra(DAY_NO);
    }

    private void getResultsAndStats() {

        progressActivity.showLoading();
        Observable.zip(prepService.getprepQsAttempt(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID(), simpleDateFormat.format(mSelectedDate), 1, mHigh), prepService.userStats(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID()), Review::new)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Review>() {
                    @Override
                    public void onCompleted() {
                        progressActivity.showContent();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(@NonNull Review review) {
                        setResultsAndStats(review);
                    }
                });
    }

    private void setResultsAndStats(@NonNull Review review) {
        mListAttempts.setAdapter(fastItemAdapter);
        List<Attempt> mreviewAttempt=review.getAttempts();
        for (Attempt attempt : mreviewAttempt) {
            fastItemAdapter.add(new AttemptItem(ReviewTestActivity.this,attempt));
        }

        /*fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
            Question mQuestion= new Question();
            mQuestion.setId(item.attempt.getQuestionid());
            mQuestion.setQuestion(item.attempt.getQuestion());
            youth4work.androidprototype.youth4work.ui.quiz.DiscussionActivity.show(self, mQuestion);
            return true;
        });*/
        txtAccuracy.setText((float)Math.ceil(review.getUserStats().getAcuracy()) + "%");
        txtSpeed.setText((int)Math.ceil(review.getUserStats().getSpeed()) + " s/Q");
        txtScore.setText((float)Math.ceil(review.getUserStats().getScore())+"");
        speedCircleView.setProgress((int)Math.round(review.getUserStats().getSpeed()));
        accuracyCircleView.setProgress((int)Math.round(review.getUserStats().getAcuracy()));
        scoreCircleView.setProgress((int)Math.round(review.getUserStats().getScore()));
        speedCircleView.setArcAngle(270F);
        accuracyCircleView.setArcAngle(270F);
        scoreCircleView.setArcAngle(270F);
    }
    private void getMoreQS(int index) {
        prepService.getNextprepQsAttempt(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID(), simpleDateFormat.format(mSelectedDate), index, mHigh).enqueue(new Callback<List<Attempt>>() {
            @Override
            public void onResponse(Call<List<Attempt>> call, @NonNull Response<List<Attempt>> response) {
                int size = response.body().size();
                if(response.isSuccessful())
                    if (size == 0) {
                        footerAdapter.clear();
                        loading=false;
                    } else {
                        setResults(response.body());
                        loading = true;
                    }
            }
            @Override
            public void onFailure(Call<List<Attempt>> call, Throwable t) {
            }
        });
    }
    private void setResults(@NonNull List<Attempt> mreviewAttempt) {
        for (Attempt attempt : mreviewAttempt) {
            fastItemAdapter.add(new AttemptItem(ReviewTestActivity.this,attempt));
        }
       /* fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
            Question mQuestion= new Question();
            mQuestion.setId(item.attempt.getQuestionid());
            mQuestion.setQuestion(item.attempt.getQuestion());
            DiscussionActivity.show(self, mQuestion);
            return true;
        });*/
    }
    private void initUI() {
        if (mListAttempts != null) {
            mListAttempts.setLayoutManager(new LinearLayoutManager(this));
            mListAttempts.setHasFixedSize(false);
            // mListAttempts.addItemDecoration(new DividerItemDecoration(ReviewTestActivity.this, true));
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
        super.onBackPressed();
        DashboardActivity.show(ReviewTestActivity.this,false);
    }
}
