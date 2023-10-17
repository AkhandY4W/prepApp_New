package com.youth4work.prepapp.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Section;
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

public class SectionTestActivityNew extends BaseActivity {
    RecyclerView subjectsRecyclerView;
    LinearLayout start_test;
    ProgressRelativeLayout progressActivity;
    TextView txtPreviousYearTest,txtSubName,txtAsp;
    ImageView imgSubject;
    private List<Subject> mSubjectList;
    private PrepService prepService;
    private static Section mSection;
    private Tracker mTracker;
    LinearLayout noSectionLayout;
    public static void show(@NonNull Context fromActivity, Section section) {
       mSection=section;
        Intent intent=new Intent(fromActivity, SectionTestActivityNew.class);
        fromActivity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_section_test_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subjectsRecyclerView= findViewById(R.id.subjects_recycler_view);
        start_test= findViewById(R.id.btn_take_test);
        progressActivity = findViewById(R.id.progressActivity);
        txtAsp=findViewById(R.id.txt_asp);
        txtSubName=findViewById(R.id.txt_cat_name);
        imgSubject=findViewById(R.id.img_cat);
        noSectionLayout=findViewById(R.id.card_not_found);
        txtPreviousYearTest=findViewById(R.id.txt_previous_year_test);
        txtPreviousYearTest.setVisibility(View.GONE);
        txtSubName.setText(mSection.getSubject());
        txtAsp.setText(mSection.getAspirants()>1000?mSection.getAspirants()/1000+"k Aspirants":mSection.getAspirants()+" Aspirants");
        if(!mSection.getSubCategoryImg().equals(""))
        Picasso.get().load(mSection.getSubCategoryImg()).into(imgSubject);
        start_test.setOnClickListener(v ->
                TestRulesActivity.show(self,mSection.getSubjectId(),2)

        );
        PrepApplication application = (PrepApplication)getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "SectionTestActivity");

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(SectionTestActivityNew.this).getToken()[0]);
        setTitle(mSection.getSubject());
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

        prepService.prepTests(mSection.getSubjectId(),1,50).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, @NonNull Response<List<Subject>> response) {
                if(response.isSuccessful())
                mSubjectList = response.body();
                setSubjects();
                if(mSubjectList.size()<1){
                    noSectionLayout.setVisibility(View.VISIBLE);
                }
                else {
                    noSectionLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {

            }
        });
    }

    private void setSubjects() {
        LinearLayoutManager llm = new LinearLayoutManager(SectionTestActivityNew.this);
        progressActivity.showContent();
        if (subjectsRecyclerView != null) {
            subjectsRecyclerView.setLayoutManager(llm);
            subjectsRecyclerView.setHasFixedSize(true);
            //subjectsRecyclerView.addItemDecoration(new DividerItemDecoration(self, true));
            FastItemAdapter<SubjectItem> subjectItemFastItemAdapter = new FastItemAdapter<>();
            subjectsRecyclerView.setAdapter(subjectItemFastItemAdapter);
            for (Subject subject : mSubjectList) {
                subjectItemFastItemAdapter.add(new SubjectItem(subject,self));
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
