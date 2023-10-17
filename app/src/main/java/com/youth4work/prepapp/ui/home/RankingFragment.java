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

import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Rank;
import com.youth4work.prepapp.ui.adapter.RanksAdapter;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.util.DividerItemDecoration;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingFragment extends BaseFragment {

    @Nullable
    @BindView(R.id.ranks_recycler_view)
    RecyclerView ranksRecyclerView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @BindView(R.id.txt_message)
    TextView txtMessage;

    @Nullable
    private OnFragmentInteractionListener mListener;
    private RanksAdapter ranksAdapter;
    private PrepService prepService;

    public RankingFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this, view);

        initRanks();

        return view;
    }

    private void initRanks() {

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);

        progressActivity.showLoading();

        prepService.ranksByCategory(mUserManager.getUser().getSelectedCatID(), 1, 100).enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, @NonNull Response<List<Rank>> response) {
                if(response.isSuccessful())
                setRanks(response.body());
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {

            }
        });
    }

    private void setRanks(List<Rank> ranks) {
        if (ranks.size() > 0 ) {
            ranksAdapter = new RanksAdapter(getContext(), ranks);
            if (ranksRecyclerView != null) {
                ranksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ranksRecyclerView.setHasFixedSize(true);
                ranksRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), true));
                ranksRecyclerView.setAdapter(ranksAdapter);
                progressActivity.showContent();
                txtMessage.setVisibility(View.GONE);
            }
        } else {
            if (progressActivity != null) {
                progressActivity.showContent();
            }
            txtMessage.setVisibility(View.VISIBLE);
            if (ranksRecyclerView != null) {
                ranksRecyclerView.setVisibility(View.GONE);
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
        //ButterKnife.unbind(this);
    }
}
