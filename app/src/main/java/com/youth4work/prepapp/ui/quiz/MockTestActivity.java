package com.youth4work.prepapp.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.AllMockQSModel;
import com.youth4work.prepapp.network.model.MockSection;
import com.youth4work.prepapp.network.model.response.UserAllow;
import com.youth4work.prepapp.ui.adapter.MockTestAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.showQuitMockTestDialog;

public class MockTestActivity extends BaseActivity implements View.OnClickListener {
    FrameLayout simpleFrameLayout;
    static TabLayout tabLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageButton imageButton;
    GridLayout gridLayoutRight;
    Context context = this;
    private static int SubjectID, questionno;
    TextView txtPrevious, txtNext, txtSkip;
    TextView titleTest, txtTotalTime;
    ProgressBar progressBarTimer;
    static ArrayList<AllMockQSModel.MockQuestion> items;
    static AllMockQSModel allMockQS;
    AllMockQSModel.MockTest mockTest;
    static AllMockQSModel.MockQuestion mockQuestion;

    // private MockQuizAdapter mQuizAdapter;
    List<MockTestAdapter.QuestionListItem> itemList = new ArrayList<>();
    RecyclerView mRecyclerViewList;
    static MockTestAdapter adapter;
    Float pStatus = 0.0F;
    private Handler handler = new Handler();
    Fragment fragment = new MockQuizFragment();
    ProgressRelativeLayout progressActivity;
    Button btnSubmitMockTest;
    int timer;
    static boolean changeData = true;
    //ArrayList<String> sectionName = new ArrayList<>();
    static ArrayList<MockSection> mockSections = new ArrayList<>();

    public static void show(@NonNull Context fromActivity, int subjectid, int questionNo) {
        SubjectID = subjectid;
        questionno = questionNo;
        Intent intent = new Intent(fromActivity, MockTestActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void setQuestionNo(int questionNo) {
        questionno = questionNo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_test2);
        simpleFrameLayout = findViewById(R.id.simpleFrameLayout);
        tabLayout = findViewById(R.id.simpleTabLayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        imageButton = findViewById(R.id.btn_right_side_navigation);
        gridLayoutRight = findViewById(R.id.drawer_right);
        mRecyclerViewList = findViewById(R.id.recycler_view_list);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        titleTest = findViewById(R.id.title);
        txtTotalTime = findViewById(R.id.txt_total_time);
        progressBarTimer = findViewById(R.id.progress_bar_timer);
        progressActivity = findViewById(R.id.progressActivity);
        progressActivity.showLoading();
        btnSubmitMockTest = findViewById(R.id.btn_submit_test);
        txtSkip = findViewById(R.id.btn_skip);
        txtNext = findViewById(R.id.btn_next);
        txtPrevious = findViewById(R.id.btn_previous);
        txtSkip.setOnClickListener(this);
        txtPrevious.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        btnSubmitMockTest.setOnClickListener(this);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        // Maximum Progress
        progressBarTimer.setProgressDrawable(drawable);
        CanAttemptMockTest();
        imageButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(gridLayoutRight)) {
                drawerLayout.closeDrawer(gridLayoutRight);
                onResume();
            } else if (!drawerLayout.isDrawerOpen(gridLayoutRight)) {
                drawerLayout.openDrawer(gridLayoutRight);
                drawerLayout.bringChildToFront(gridLayoutRight);
                onPause();
            }

        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                if (changeData) {
                    Fragment fragment;
                    int pos = getFirstQuestionPos(mockSections.get(tab.getPosition()).getSectionName());
                    fragment = new MockQuizFragment();
                    MockQuizFragment.newInstance(pos, items);
                    questionno = pos;
                    loadFragment(fragment);
                } else {
                    changeData = true;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    private Integer getFirstQuestionPos(String sectionname) {
        int res = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getSectionName().equals(sectionname)) {
                res = items.get(i).getsNo();
                break;
            }
        }
        return res;
    }

    private void CanAttemptMockTest() {
        prepService.canAttemptMockTest(mUserManager.getUser().getUserId(), SubjectID).enqueue(new Callback<UserAllow>() {
            @Override
            public void onResponse(Call<UserAllow> call, Response<UserAllow> response) {
                if (response.isSuccessful()) {
                    UserAllow muserAllow = response.body();
                    assert muserAllow != null;
                    if (!muserAllow.isIsAllow()) {
                        Toast.makeText(MockTestActivity.this, muserAllow.getAlert(), Toast.LENGTH_SHORT).show();
                        Calendar currentSelectedDate = Calendar.getInstance();
                        long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                        String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        ReviewTestActivity.show(self, currentSelectedDate.getTime(), dayNo, 20);

                    } else {
                        getMockTestData();
                        StartTest();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAllow> call, Throwable t) {
                Toast.makeText(MockTestActivity.this, "Sorry,you have not been allowed to attempt this test !", Toast.LENGTH_SHORT).show();
                Calendar currentSelectedDate = Calendar.getInstance();
                long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                ReviewTestActivity.show(self, currentSelectedDate.getTime(), dayNo, 20);

            }
        });
    }

    private void StartTest() {
        prepService.StartTest(SubjectID, mUserManager.getUser().getUserId()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timer = response.body();
                    if (timer >= 1) {
                        int timerMint = timer / 60;
                        startTimer(timerMint);
                        progressBarTimer.setProgress(0);   // Main Progress
                        progressBarTimer.setSecondaryProgress(timer); // Secondary Progress
                        progressBarTimer.setMax(timer);
                        new Thread(() -> {

                            // TODO Auto-generated method stub
                            while (pStatus < timer) {
                                pStatus += 1;
                                handler.post(() -> {
                                    // TODO Auto-generated method stub
                                    progressBarTimer.setProgress(Math.round(pStatus));
                                    //tv.setText(pStatus + "%");

                                });
                                try {
                                    // Sleep for 200 milliseconds.
                                    // Just to display the progress slowly
                                    Thread.sleep(1000); //thread will take approx 1 second to finish
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(MockTestActivity.this, "Time out" + "\nTest Submit", Toast.LENGTH_SHORT).show();
                        Calendar currentSelectedDate = Calendar.getInstance();
                        long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                        String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        ReviewTestActivity.show(self, currentSelectedDate.getTime(), dayNo, 20);
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(MockTestActivity.this, "Time out" + "\n Test Submit", Toast.LENGTH_SHORT).show();
                Calendar currentSelectedDate = Calendar.getInstance();
                long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                ReviewTestActivity.show(self, currentSelectedDate.getTime(), dayNo, 20);
            }
        });
    }

    private List<AllMockQSModel.MockQuestion> getMockTestData() {
        prepService.getAllMockQS(SubjectID, mUserManager.getUser().getUserId()).enqueue(new Callback<AllMockQSModel>() {
            @Override
            public void onResponse(Call<AllMockQSModel> call, Response<AllMockQSModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allMockQS = response.body();
                    items = allMockQS.getMockQuestions();
                    mockTest = allMockQS.getMockTest();
                    progressActivity.showContent();
                    if (allMockQS != null && mockTest != null && items != null) {
                        mockQuestion = items.get(questionno - 1);
                        //sharedprefernceMockQuestionList = mPreferencesManager.getMockQuestions(MockTestActivity.this);
                        titleTest.setText(mockTest.getTestName());
                        for (int i = 0; i < items.size(); i++) {
                            String sectionNameSingle = items.get(i).getSectionName();
                            int sectionSNo = items.get(i).getSectionId();
                            int serialNo = items.get(i).getsNo();
                            mockSections.add(new MockSection(sectionNameSingle, sectionSNo, serialNo));
                        }
                        //Old Way
                        /*HashSet<MockSection> hashSet = new HashSet<MockSection>();
                        hashSet.addAll(mockSections);*/

                        //New Way
                        HashSet<MockSection> hashSet = new HashSet<>(mockSections);
                        mockSections.clear();
                        mockSections.addAll(hashSet);
                        Collections.sort(mockSections);
                        for (int i = 0; i < mockSections.size(); i++) {
                            TabLayout.Tab firstTab = tabLayout.newTab();
                            firstTab.setText(mockSections.get(i).getSectionName());
                            tabLayout.addTab(firstTab);
                        }
                        for (int j = 0; j < mockSections.size(); j++) {
                            itemList.add(new MockTestAdapter.QuestionListItem(mockSections.get(j).getSectionName(), SubjectID));
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).getSectionId() == mockSections.get(j).getSectionId()) {
                                    itemList.add(new MockTestAdapter.QuestionListItem(items.get(i).getsNo(), items.get(i).getQuestion()));
                                }
                            }
                        }
                        MockQuizFragment.newInstance(1, items);
                        loadFragment(fragment);
                        if (itemList != null) {
                            adapter = new MockTestAdapter(context, itemList, items);
                            adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
                            mRecyclerViewList.setLayoutManager(new LinearLayoutManager(context));
                            mRecyclerViewList.setAdapter(adapter);
                            adapter.setOnItemClickListener((itemView, position) ->
                            {
                                drawerLayout.closeDrawer(gridLayoutRight);
                                fragment = new MockQuizFragment();
                                MockQuizFragment.newInstance(position, items);
                                questionno = position;
                                //mockQuestion.setsNo(position);
                                loadFragment(fragment);
                                adapter.notifyDataSetChanged();
                                getCurrentSelectedTab(items.get(position-1).getSectionName(), false);

                            });
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<AllMockQSModel> call, Throwable t) {
                Toast.makeText(MockTestActivity.this, t + "error", Toast.LENGTH_SHORT).show();
            }
        });
        return items;
    }

    public static boolean getCurrentSelectedTab(String sectionName, Boolean loadData) {
        int pos = tabLayout.getSelectedTabPosition();
        if (sectionName.equals(mockSections.get(pos).getSectionName())) {
            changeData=true;
        } else {
            for (int i = 0; i < mockSections.size(); i++) {
                if (mockSections.get(i).getSectionName().equals(sectionName)) {
                    pos=i;
                    changeData = loadData;
                    break;
                }
            }
        }
        Objects.requireNonNull(tabLayout.getTabAt(pos)).select();
        return changeData;
    }

    private void startTimer(final int minuti) {
        CountDownTimer countDownTimer = new CountDownTimer(60 * minuti * 1000, 500) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                txtTotalTime.setText(String.format(String.format("%02d", seconds / 3600) + ":" + String.format(String.format("%02d", (seconds % 3600) / 60)) + ":" + String.format("%02d", seconds % 60)));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                if (txtTotalTime.getText().equals("00:00:00")) {
                    txtTotalTime.setText("Stop");
                    SubmitMockTest(SubjectID, mUserManager.getUser().getUserId());
                } else {
                    txtTotalTime.setText("02:00");
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new MockQuizFragment();
        switch (v.getId()) {

            case R.id.btn_previous:
                if (questionno <= 1) {
                    //txtPrevious.setClickable(false);
                    MockQuizFragment.newInstance(1, items);
                    loadFragment(fragment);
                    getCurrentSelectedTab(items.get(1).getSectionName(), false);
                } else {
                    questionno = questionno - 1;
                    MockQuizFragment.newInstance(questionno, items);
                    loadFragment(fragment);
                    int no = questionno - 1;
                    getCurrentSelectedTab(items.get(no).getSectionName(), false);
                    }
                break;
            case R.id.btn_next:
                if (drawerLayout.isDrawerOpen(gridLayoutRight)) {
                    drawerLayout.closeDrawer(gridLayoutRight);
                } else if (!drawerLayout.isDrawerOpen(gridLayoutRight)) {
                    drawerLayout.openDrawer(gridLayoutRight);
                    drawerLayout.bringChildToFront(gridLayoutRight);
                }
                //adapter.notifyDataSetChanged();
                break;
            case R.id.btn_skip:
                if (questionno == items.size()) {
                    Toast.makeText(context, "No more question left to skip", Toast.LENGTH_SHORT).show();
                } else {
                    getCurrentSelectedTab(items.get(questionno).getSectionName(), false);
                    questionno = questionno + 1;
                    MockQuizFragment.newInstance(questionno, items);
                    loadFragment(fragment);
                }
                break;
            case R.id.btn_submit_test:
                SubmitMockTest(SubjectID, mUserManager.getUser().getUserId());
                break;
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.simpleFrameLayout, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public void onBackPressed() {

        showQuitMockTestDialog(MockTestActivity.this);
    }
}
