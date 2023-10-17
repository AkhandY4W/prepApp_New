package com.youth4work.prepapp.ui.quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.widget.IconTextView;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Question;
import com.youth4work.prepapp.network.model.Subject;
import com.youth4work.prepapp.network.model.request.PushAnswer;
import com.youth4work.prepapp.network.model.response.UserAllow;
import com.youth4work.prepapp.ui.adapter.QuizAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.CountDownTimer;
import com.youth4work.prepapp.util.DividerItemDecoration;
import com.youth4work.prepapp.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.showFreeTrailExpiredDialog;
import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.showQuestionBankOverDialog;
import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.showQuitTestDialog;
import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.showUpgradeDialog;

/*import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.Tracker;*/

public class DailyTestActivity extends BaseActivity {

    private static List<Subject> mSubjectList;
    private static int TestType = 0;
    private static int testId;
    @Nullable
    @BindView(R.id.dailyTestRecyclerView)
    RecyclerView dailyTestRecyclerView;
    @Nullable
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @Nullable
    @BindView(R.id.btn_next)
    Button btntNext;

    @BindView(R.id.btn_disc)
    Button btntDisc;

    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    Boolean isAllow;
    int qLimit;
    int mLow = 0;
    private TextView txtTitle;
    private Question mQuestion;
    private QuizAdapter mQuizAdapter;
    private TimeRemainingTimer mTimeRemainingTimer;
    @NonNull
    private QuestionState state = QuestionState.INITIAL;
    // Question defaults
    private int mPrevWinLoseCount = 0;
    private int mWinOrLose = 0;
    private double mPrevScore = 1500;
    private int mTimeTaken = 0;
    private int mHigh = 0, mcheck = 0;
    private int mAttemptedToday = 0;
    private int mAttemptedTotal = 0;
    private long mAttemptedDate = 0;
    private UserAllow userAllow;
    @NonNull
    private Random mRandomNumber = new Random();
    private MediaPlayer mediaPlayer;
    private Dialog dailog;
    private boolean isFreeTrailExpired = false;
    private Tracker mTracker;
    String ttype;

    public static void show(@NonNull Context fromActivity, int testid, int testtype) {
        testId = testid;
        TestType = testtype;
        Intent intent = new Intent(fromActivity, DailyTestActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_test);
        ButterKnife.bind(this);
        ttype = (TestType == 3 ? "Mock Test" : (TestType == 2 ? "Section Test" : "Topic Test"));
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Daily " + ttype);

        checkCanAttempt();

    btntDisc.setOnClickListener(v -> OnDiscussionClicked());
    }

    private void checkCanAttempt() {

        progressActivity.showLoading();
        prepService.canAttempt(mUserManager.getUser().getUserId(), testId, TestType).enqueue(new Callback<UserAllow>() {
            @Override
            public void onResponse(Call<UserAllow> call, Response<UserAllow> response) {
                if (response.isSuccessful()) {
                    initAttemptData(response.body());
                    setupToolbar();
                    initQuizUI();
                    startTest();
                }
            }

            @Override
            public void onFailure(Call<UserAllow> call, Throwable t) {

            }
        });
    }

    private void initAttemptData(UserAllow userAllow) {
        this.userAllow = userAllow;
        isFreeTrailExpired = !userAllow.isIsAllow();
        //mAttemptedToday = userAllow.getAttemptedToday();
        //mAttemptedTotal = userAllow.getAttemptedTotal();
    }


    @Override
    public void setTitle(CharSequence title) {
        txtTitle.setText(title);
    }

    private void initButtonUI() {
        switch (state) {
            case INITIAL:
                btnConfirm.setEnabled(false);
                ViewUtils.setGone(btnConfirm, false);
                ViewUtils.setGone(btntDisc, true);
                ViewUtils.setGone(btntNext, true);
                break;
            case SELECTED:
                btnConfirm.setEnabled(true);
                ViewUtils.setGone(btnConfirm, false);
                ViewUtils.setGone(btntDisc, true);
                ViewUtils.setGone(btntNext, true);
                break;
            case CONFIRMED:
            case TIME_UP:
                ViewUtils.setGone(btnConfirm, true);
                ViewUtils.setGone(btntDisc, false);
                ViewUtils.setGone(btntNext, false);
                break;
        }
    }

    private void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.daily_test_toolbar);
        if (toolbar != null) {
            txtTitle = findViewById(R.id.txt_action_bar_title);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }

    private void initQuizUI() {
        dailyTestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyTestRecyclerView.setHasFixedSize(true);
        dailyTestRecyclerView.addItemDecoration(new DividerItemDecoration(DailyTestActivity.this, false));
    }

    private void setupQuestionAndAnswers(Question question) {
        try {
            mAttemptedTotal++;
            //setTitle(mAttemptedDate != -1 ? "Daily Exam - Q" + Integer.toString(mAttemptedToday + 1) + "/Q20" : "Practice Test - Q" + Integer.toString(mAttemptedToday + 1));
            setTitle("Daily " + ttype + " - Q" + mAttemptedTotal + "/Q" + mHigh);
            mQuestion = question;
            mQuestion.setOptions(mQuestion.getOptions());
            state = QuestionState.INITIAL;
            initOptions();
            initTimer();
            initButtonUI();
        } catch (Exception e) {
        }
    }

    private void initOptions() {
        mQuizAdapter = new QuizAdapter(mQuestion, false, false, self);
        assert dailyTestRecyclerView != null;
        dailyTestRecyclerView.getItemAnimator().setChangeDuration(0);
        dailyTestRecyclerView.setAdapter(mQuizAdapter);

        mQuizAdapter.setOnItemClickListener((itemView, position) -> {
            if (state != QuestionState.CONFIRMED && state != QuestionState.TIME_UP) {
                mQuestion.setOptionSelected(position - 1);
                state = QuestionState.SELECTED;
                initButtonUI();
                mQuizAdapter.updateList(mQuestion, true, false);
            } else if (state == QuestionState.TIME_UP) {
                showDialog(DialogState.TIME_UP, false);
            } else {
                if (!isFreeTrailExpired) checkAnswerAndShowDialog(false);
                else
                 //PrepDialogsUtils.showFreeTrailExpiredDialog(this);
                 showFreeTrailExpiredDialog(this);
            }
        });
    }

    private void initTimer() {
        mTimeTaken = 0;
        mTimeRemainingTimer = new TimeRemainingTimer((mQuestion.getTime2solve()) * 1000, 1000);
        mTimeRemainingTimer.start();
    }

    private void showDialog(@NonNull DialogState state, boolean playMusic) {
        dailog=new Dialog(DailyTestActivity.this);

        dailog.setContentView(R.layout.layout_dailog);

        IconTextView txtCorrectMessage = dailog.findViewById(R.id.txt_correct_message);
        TextView txtWrongMessage = dailog.findViewById(R.id.txt_wrong_message);
        TextView txtTimeupMessage = dailog.findViewById(R.id.txt_timeup_message);
        ImageView imgDiscussion = dailog.findViewById(R.id.txt_discussion);
        imgDiscussion.setOnClickListener(v -> OnDiscussionClicked());
      //  dailog.setOnClickListener(v -> OnDiscussionClicked());

        ViewUtils.setGone(txtCorrectMessage, true);
        ViewUtils.setGone(txtWrongMessage, true);
         ViewUtils.setGone(txtTimeupMessage, true);

        btntNext.setEnabled(false);

        int fileName = 0;
        switch (state) {
            case CORRECT:
                ViewUtils.setGone(txtCorrectMessage, false);
                fileName = R.raw.right;
                mPrevWinLoseCount = 1;
                mWinOrLose = 1;
                break;
            case WRONG:
                ViewUtils.setGone(txtWrongMessage, false);
                fileName = R.raw.wrong;
                mPrevWinLoseCount = 0;
                mWinOrLose = 0;
                break;
            case TIME_UP:
                ViewUtils.setGone(txtTimeupMessage, false);
                fileName = R.raw.timeup;
                mPrevWinLoseCount = 0;
                mWinOrLose = 0;
                if (mTimeRemainingTimer != null) {
                    mTimeRemainingTimer.cancel();
                }
                break;
        }

        if (playMusic) playMusic(fileName);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                dailog.dismiss();
            }
        }, 2000);


        if (dailog != null) {
            dailog.setOnDismissListener(dialog -> btntNext.setEnabled(true));

            dailog.show();
        }
    }

    private void checkAnswerAndShowDialog(boolean playMusic) {

        if (mQuestion.isCorrectOrWrong()) showDialog(DialogState.CORRECT, playMusic);
        else showDialog(DialogState.WRONG, playMusic);
    }

    private void playMusic(int fileName) {
        try {
            mediaPlayer = MediaPlayer.create(DailyTestActivity.this, fileName);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchNextQuestion() {

        if (!progressActivity.isLoading())
            progressActivity.showLoading();
        int testid = TestType == 3 ? getTestId() : testId;
        if(mAttemptedToday+1==mHigh)
            btntNext.setText("Submit");
        if (testid == 0 || mAttemptedToday == mHigh) {
            Calendar currentSelectedDate = Calendar.getInstance();
            long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
            String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            finish();
            ReviewTestActivity.show(DailyTestActivity.this, currentSelectedDate.getTime(), String.valueOf(Integer.parseInt(dayNo) + 1), mAttemptedToday);
        } else {
            if (TestType == 2) {
                prepService.sum_question(mPrevWinLoseCount, mWinOrLose, mPrevScore, testid, mUserManager.getUser().getUserId()).enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, @NonNull Response<Question> response) {
                        if (response.isSuccessful() && response.body().getQuestion() != null) {
                            setupQuestionAndAnswers(response.body());
                            progressActivity.showContent();
                        } else {
                            mAttemptedToday++;
                            fetchNextQuestion();
                        }
                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {

                    }
                });
            } else {
                prepService.question(mPrevWinLoseCount, mWinOrLose, mPrevScore, testid, mUserManager.getUser().getUserId()).enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Call<Question> call, @NonNull Response<Question> response) {
                        if (response.isSuccessful() && response.body().getQuestion() != null) {
                            setupQuestionAndAnswers(response.body());
                            progressActivity.showContent();
                        } else {
                            if (TestType == 3) {
                                mcheck++;
                                mLow = 0;
                            }
                            mAttemptedToday++;
                            fetchNextQuestion();
                        }
                    }

                    @Override
                    public void onFailure(Call<Question> call, Throwable t) {

                    }
                });
            }
        }
    }

    private int getTestId() {
        int testId = 0;
        if (mcheck < mSubjectList.size()) {
            int mslot = mSubjectList.get(mcheck).getQsSlot();
            testId = mSubjectList.get(mcheck).getTestid();
            if (mLow < mslot) {
                mLow++;
            } else {
                mLow = 0;
                mcheck++;
            }


        }
        return testId;
    }

    void startTest() {
        if (userAllow.isIsAllow()) {
            progressActivity.showLoading();
            if (TestType == 3) {
                prepService.subjects(testId).enqueue(new Callback<List<Subject>>() {
                    @Override
                    public void onResponse(Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                        if (response.isSuccessful()) {
                            mSubjectList = response.body();
                            if (mSubjectList != null) {
                                for (Subject sub : mSubjectList) {
                                    mHigh += sub.getQsSlot();
                                }
                                if (userAllow.getQsLeft() < mHigh) {
                                    mHigh = userAllow.getQsLeft();
                                }
                                fetchNextQuestion();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Subject>> call, Throwable t) {

                    }
                });
            } else {
                mHigh = 20;
                if (userAllow.getQsLeft() < mHigh) {
                    mHigh = userAllow.getQsLeft();
                }
                fetchNextQuestion();

            }
        } else {
            if (userAllow.getQsLeft() == -1) {
                showQuestionBankOverDialog(DailyTestActivity.this);
            } else {
                //PrepDialogsUtils.showFreeTrailExpiredDialog(DailyTestActivity.this);
                showUpgradeDialog(DailyTestActivity.this);
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        showQuitTestDialog(DailyTestActivity.this);

        return false;
    }

    @OnClick(R.id.btn_confirm)
    void OnConfirmClicked() {
        state = QuestionState.CONFIRMED;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        mQuizAdapter.updateList(mQuestion, false, true);
        checkAnswerAndShowDialog(true);
        ViewUtils.setGone(btnConfirm, true);
        ViewUtils.setGone(btntDisc, false);
        ViewUtils.setGone(btntNext, false);

        mTimeRemainingTimer.cancel();
    }

    @OnClick(R.id.btn_next)
    void OnNextClicked() {
        if (!isFreeTrailExpired) {
            btntNext.setEnabled(false);
            pushAnswer(true);

        } else {
            //PrepDialogsUtils.showFreeTrailExpiredDialog(this);
            showUpgradeDialog(DailyTestActivity.this);
        }
    }


    private void pushAnswer(boolean fetchQuestion) {

        PushAnswer answer = new PushAnswer(Integer.parseInt(mQuestion.getSelectedAnswerId()), mWinOrLose, mQuestion.getId(), mUserManager.getUser().getUserId(), mQuestion.getTime2solve(), mTimeTaken);
//(1,1,40849,104034,60,20)
        Call<ResponseBody> answerCall = prepService.pushAnswer(answer);
        answerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject;
                        btntNext.setEnabled(true);
                        String responseStr = response.body() != null ? response.body().string() : "1500";

                        try {
                            jsonObject = new JSONObject(responseStr);
                            mPrevScore = Double.parseDouble(jsonObject.getString("value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mAttemptedToday++;
                    // mAttemptedTotal++;
                    if (mAttemptedToday >= mHigh) {
                        Calendar currentSelectedDate = Calendar.getInstance();
                        // currentSelectedDate.setTimeInMillis(mAttemptedDate);

                        long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                        String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        finish();
                        ReviewTestActivity.show(DailyTestActivity.this, currentSelectedDate.getTime(), String.valueOf(Integer.parseInt(dayNo) + 1), mHigh);
                    } else if (fetchQuestion) {
                        fetchNextQuestion();
                    }
                }
                else {
                    btntNext.setEnabled(true);
                    try {
                        Toast.makeText(DailyTestActivity.this, response.errorBody().string()+"", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btntNext.setEnabled(true);
            }
        });
    }

    private void incrementQuestionsCount()
    {
        mUserManager.incrementQuestionsCount();
    }

    void OnDiscussionClicked() {
        if (dailog.isShowing()) dailog.dismiss();
        Question qs=mQuestion;
        for (int i=0;i<mQuestion.getOptions().size();i++) {
            if (mQuestion.getOptions().get(i).isAnswer()) {
                qs.getOptions().get(0).setOption(mQuestion.getOptions().get(i).getOption());
                qs.getOptions().get(0).setOptionImgUrl(mQuestion.getOptions().get(i).getOptionImgUrl());
                break;
            }
        }
        DiscussionActivity.show(self, qs);
    }

    @Override
    public void onBackPressed() {
        if (mTimeRemainingTimer != null) {
            mTimeRemainingTimer.cancel();
        }
        showQuitTestDialog(DailyTestActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeRemainingTimer != null) {
            mTimeRemainingTimer.cancel();
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

    enum DialogState {
        CORRECT,
        WRONG,
        TIME_UP
    }

    enum QuestionState {
        INITIAL,
        SELECTED,
        CONFIRMED,
        TIME_UP
    }

    public class TimeRemainingTimer extends CountDownTimer {

        public TimeRemainingTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = Math.round((float) millisUntilFinished / 1000.0f);
            if (progress == 5) {
                playMusic(R.raw.tick_tock);
            }
            mTimeTaken++;
            ((TextView) findViewById(R.id.timerView)).setText(Integer.toString(progress));

        }

        @Override
        public void onFinish() {
            mTimeTaken = mQuestion.getTime2solve();
            ((TextView) findViewById(R.id.timerView)).setText("0");
            mQuizAdapter.updateList(mQuestion, false, true);
            showDialog(DialogState.TIME_UP, true);
            state = QuestionState.TIME_UP;
            initButtonUI();
            mPrevWinLoseCount = 0;
            mWinOrLose = 0;

        }
    }
}
