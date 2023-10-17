package com.youth4work.prepapp.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Section;
import com.youth4work.prepapp.ui.adapter.MockSectionItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.quiz.MockTestRulesActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockTestFragment extends BaseFragment {

    @Nullable
    @BindView(R.id.subjects_recycler_view)
    RecyclerView subjectsRecyclerView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @Nullable
    @BindView(R.id.btn_upgrade)
    Button btnTakeTest;
    @BindView(R.id.card_not_found)
    LinearLayout cardNotFound;
    @BindView(R.id.upgrade_layout)
    LinearLayout upgradeLayout;
    @BindView(R.id.section_test)
    CardView sectionTest;
    @BindView(R.id.txt_dummy_section)
    TextView txtDummySection;
    @BindView(R.id.img_cat)
    ImageView imgCat;
    @BindView(R.id.no_test_img)
    ImageView noTestImg;

    @Nullable
    private BaseFragment.OnFragmentInteractionListener mListener;
    private List<Section> mSubjectList;
    private PrepService prepService;
    private Tracker mTracker;

    public MockTestFragment() {

    }
    public static MockTestFragment newInstance() {
        MockTestFragment fragment = new MockTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mock_test, container, false);
        ButterKnife.bind(this, view);
        sectionTest.setVisibility(View.GONE);
        txtDummySection.setVisibility(View.GONE);
        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        if (mUserManager.getCategory() != null) {
            Constants.logEvent4FCM(self, String.valueOf(mUserManager.getCategory().getCatid()), mUserManager.getCategory().getCategory(), new Date(), "Mock Test", "VIEW_ITEM");
            Product product = new Product()
                    .setId(String.valueOf(mUserManager.getCategory().getCatid()))
                    .setName(mUserManager.getCategory().getCategory())
                    .setCategory("Prep Pack")
                    .setBrand("Youth4work")
                    .setPosition(mUserManager.getCategory().getCatid())
                    .setCustomDimension(1, "1")
                    .setCustomDimension(2, "India")
                    .setCustomDimension(3, "program");
            ProductAction productAction = new ProductAction(ProductAction.ACTION_DETAIL)
                    .setProductActionList("Prep Pack");
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productAction);
            mTracker.setScreenName("Mock Fragment");
            mTracker.send(builder.build());
            if(mUserManager.getCategory().getSubCategoryImg()!=null&&!mUserManager.getCategory().getSubCategoryImg().equals(""))
                Picasso.get().load(mUserManager.getCategory().getSubCategoryImg()).into(imgCat);
        }
        noTestImg.setBackgroundResource(R.drawable.ic_case_no_my_prep);
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        getPrepTestBySubCat();
        return view;
    }

    @OnClick({R.id.btn_upgrade})
    public void onClick(@NonNull View view) {
        UpgradePlanActivity.show(self);
    }


    private void getPrepTestBySubCat() {
        progressActivity.showLoading();
        prepService.mockPrepSubject(mUserManager.getUser().getSelectedCatID(), 1, 100).enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, @NonNull Response<List<Section>> response) {
                if (response.isSuccessful())
                    progressActivity.showContent();
                if (response.body().size() > 0) {
                    mSubjectList = response.body();
                    upgradeLayout.setVisibility(View.VISIBLE);
                    cardNotFound.setVisibility(View.GONE);
                    setSubjects();
                } else {
                    imgCat.setVisibility(View.GONE);
                    cardNotFound.setVisibility(View.VISIBLE);
                    upgradeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {

            }
        });
    }

    private void setSubjects() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        if (mUserManager.getUser().getPrepPlanID() < 1) {
            upgradeLayout.setVisibility(View.VISIBLE);
        } else {
            upgradeLayout.setVisibility(View.GONE);
        }
        if (subjectsRecyclerView != null) {
            subjectsRecyclerView.setLayoutManager(llm);
            subjectsRecyclerView.setHasFixedSize(true);
            MockSectionItem sectionAdapter;

            if (mUserManager.getUser().getPrepPlanID() != 0) {
                sectionAdapter = new MockSectionItem(mSubjectList, true);
            } else {
                sectionAdapter = new MockSectionItem(mSubjectList, false);
            }
            subjectsRecyclerView.setAdapter(sectionAdapter);
            sectionAdapter.setOnItemClickListener((itemView, position) ->
            {
                if (position == 0) {
                    MockTestRulesActivity.show(self, mSubjectList.get(position).getSubjectId());
                } else if (mUserManager.getUser().getPrepPlanID() > 1) {
                    MockTestRulesActivity.show(self, mSubjectList.get(position).getSubjectId());
                } else {
                    UpgradePlanActivity.show(self);
                }
            });
        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragment.OnFragmentInteractionListener) {
            mListener = (BaseFragment.OnFragmentInteractionListener) context;
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

