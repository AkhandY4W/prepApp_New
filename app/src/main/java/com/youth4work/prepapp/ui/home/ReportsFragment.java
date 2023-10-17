package com.youth4work.prepapp.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Report;
import com.youth4work.prepapp.network.model.SubjectStats;
import com.youth4work.prepapp.ui.adapter.SubjectStatItem;
import com.youth4work.prepapp.ui.adapter.UserStatsItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.quiz.ReviewTestActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.google.android.gms.analytics.Tracker;

public class ReportsFragment extends BaseFragment {

    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;


    @BindView(R.id.btn_review_qs)
    TextView btn_review_qs;

    @BindView(R.id.reports_recycler_view)
    RecyclerView reportsRecyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private OnFragmentInteractionListener mListener;
    private Subscription subscription;
    private FastItemAdapter fastItemAdapter;
    private Tracker mTracker;

    public ReportsFragment() {

    }

    @NonNull
    public static ReportsFragment newInstance() {
        ReportsFragment fragment = new ReportsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        ButterKnife.bind(this, view);

        btn_review_qs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentSelectedDate = Calendar.getInstance();
                long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                ReviewTestActivity.show(self,currentSelectedDate.getTime(),dayNo,20);
            }
        });

        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Reports Fragment");
        Constants.logEvent4FCM(self,"Report Screen","Report Screen",new Date(),"Screen","SELECT_CONTENT");
        initReports();
        return view;
    }

    private void initReports() {

        progressActivity.showLoading();
        PrepService prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        subscription = Observable.zip(prepService.subjectStats(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID()), prepService.userStats(mUserManager.getUser().getUserId(), mUserManager.getUser().getSelectedCatID()), Report::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Report>() {
                    @Override
                    public void onCompleted() {
                        try {
                            progressActivity.showContent();
                        }
                        catch(Exception ex) {
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Report report) {
                        setupReport(report);
                    }
                });
    }

    private void setupReport(Report report) {
        try {
            fastItemAdapter = new FastItemAdapter();

            reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            reportsRecyclerView.setHasFixedSize(true);
            reportsRecyclerView.setAdapter(fastItemAdapter);

            fastItemAdapter.add(new UserStatsItem(report.getUserStats()));
            for (SubjectStats subjectStats : report.getSubjectStatses()) {
                fastItemAdapter.add(new SubjectStatItem(subjectStats));
            }
        } catch (Exception e) {

        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (subscription.isUnsubscribed()) subscription.unsubscribe();
    }
}
