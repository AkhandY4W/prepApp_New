package com.youth4work.prepapp.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Subject;
import com.youth4work.prepapp.ui.adapter.SubjectItem;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class SectionTestActivity  extends BaseActivity {
    RecyclerView subjectsRecyclerView;
    Button start_test;
    ProgressRelativeLayout progressActivity;
    private List<Subject> mSubjectList;
    private PrepService prepService;
    private static int SubjectID;
    private static String Subject;
    private Tracker mTracker;
    public static void show(@NonNull Context fromActivity,int subjectid,String subject) {
        SubjectID=subjectid;
        Subject=subject;
        Intent intent=new Intent(fromActivity,SectionTestActivity.class);
        fromActivity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subjectsRecyclerView= findViewById(R.id.subjects_recycler_view);
        start_test= findViewById(R.id.btn_take_test);
        progressActivity = findViewById(R.id.progressActivity);
        start_test.setOnClickListener(v ->
                TestRulesActivity.show(self,SubjectID,2)

        );
        PrepApplication application = (PrepApplication)getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "SectionTestActivity");

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(SectionTestActivity.this).getToken()[0]);
        setTitle(Subject);
        getPrepTestBySubject();
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

    void getPrepTestBySubject() {

        progressActivity.showLoading();

        prepService.prepTests(SubjectID,1,100).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                if(response.isSuccessful())
                mSubjectList = response.body();
                setSubjects();

            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {

            }
        });
    }

    private void setSubjects() {
        LinearLayoutManager llm = new LinearLayoutManager(SectionTestActivity.this);
        progressActivity.showContent();
        if (subjectsRecyclerView != null) {
            subjectsRecyclerView.setLayoutManager(llm);
            subjectsRecyclerView.setHasFixedSize(true);
            //subjectsRecyclerView.addItemDecoration(new DividerItemDecoration(self, true));
            FastItemAdapter<SubjectItem> subjectItemFastItemAdapter = new FastItemAdapter<>();
            subjectsRecyclerView.setAdapter(subjectItemFastItemAdapter);
            for (Subject subject : mSubjectList) {
                subjectItemFastItemAdapter.add(new SubjectItem(subject,SectionTestActivity.this));
            }

            subjectItemFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
                //fcm
                Constants.logEvent4FCM(self,String.valueOf(item.mSubject.getTestid()),item.mSubject.getTestname(),new Date(),"Topic Test","VIEW_ITEM");
                TestRulesActivity.show(self,item.mSubject.getTestid(),1);
                return true;
            });
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
        finish();
    }
}
