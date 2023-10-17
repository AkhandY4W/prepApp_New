package com.youth4work.prepapp.ui.forum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.Tracker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.PrepForum;
import com.youth4work.prepapp.ui.adapter.PrepForumItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.home.NoInternetActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.Toaster;

import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class ForumFragment extends BaseFragment {
    @Nullable
    @BindView(R.id.list_prep_forum)
    RecyclerView prepForumList;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private PrepService prepService;
    private List<PrepForum> mprepForum;
    View rootView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;

    @BindView(R.id.warning)
    CardView txtWarning;

    @BindView(R.id.warninglayout)
    LinearLayout warninglayout;

    @BindView(R.id.ask_question)
    FloatingActionButton askQuestion;
    Animation animation;

    protected UserManager mUserManager;

    private OnFragmentInteractionListener mListener;
    private Tracker mTracker;

    public ForumFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static ForumFragment newInstance() {
        ForumFragment fragment = new ForumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Forum Fragment");
        Constants.logEvent4FCM(self, "Forum Screen", "Forum Screen", new Date(), "Screen", "SELECT_CONTENT");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_forum, container, false);
        ButterKnife.bind(this, rootView);
        progressActivity.showLoading();
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        mUserManager = UserManager.getInstance(getActivity());
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left);
        animation.setDuration(1000);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //getActivity().recreate();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ForumFragment.this).attach(ForumFragment.this).commit();
            }
        });

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        prepService.prepForum(mUserManager.getUser().getSelectedCatID(), 1, 100).enqueue(new Callback<List<PrepForum>>() {
            @Override
            public void onResponse(Call<List<PrepForum>> call, @NonNull Response<List<PrepForum>> response) {
                if (response.isSuccessful())
                    mprepForum = response.body();
                if (mprepForum != null) {
                    int size = mprepForum.size();
                    if (size == 0) {
                        prepForumList.setVisibility(View.GONE);
                        txtWarning.setVisibility(View.VISIBLE);
                        warninglayout.setVisibility(View.VISIBLE);
                        progressActivity.showContent();
                    } else {
                        fillList();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PrepForum>> call, Throwable t) {
                Toaster.showLong(getActivity(), t.toString());
                //progressActivity.showContent();
                Intent intent = new Intent(self, NoInternetActivity.class);
                startActivity(intent);
            }
        });
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(self,AskQuestionActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void fillList() {
        {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            if (progressActivity != null)
                progressActivity.showContent();
            if (prepForumList != null) {
                prepForumList.setLayoutManager(llm);
                prepForumList.setHasFixedSize(true);
                // prepForumList.addItemDecoration(new DividerItemDecoration(self, true));

                FastItemAdapter<PrepForumItem> PrepForumItemFastItemAdapter = new FastItemAdapter<>();
                prepForumList.setAdapter(PrepForumItemFastItemAdapter);
                for (PrepForum PrepForum : mprepForum) {
                    PrepForumItemFastItemAdapter.add(new PrepForumItem(PrepForum, self));
                }
                PrepForumItemFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
                    Intent prepForumDetails = new Intent(getActivity(), PrepForumDetails.class);
                    prepForumDetails.putExtra("obj", item.prepForum.getForumId());
                    startActivity(prepForumDetails);
                    return true;
                });
            }
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


    }
}
