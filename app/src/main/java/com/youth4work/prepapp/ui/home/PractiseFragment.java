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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Section;
import com.youth4work.prepapp.ui.adapter.SectionItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.quiz.SectionTestActivity;
import com.youth4work.prepapp.ui.quiz.TestRulesActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
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

//import com.google.android.gms.analytics.Tracker;
//import io.intercom.android.sdk.Intercom;

public class PractiseFragment extends BaseFragment {

    @Nullable
    @BindView(R.id.subjects_recycler_view)
    RecyclerView subjectsRecyclerView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @Nullable
    @BindView(R.id.btn_take_test)
    PrepButton btnTakeTest;
    @BindView(R.id.card_not_found)
    CardView cardNotFound;
    @BindView(R.id.txt_dummy)
    TextView txtDummy;
    @Nullable
    private OnFragmentInteractionListener mListener;
    private List<Section> mSubjectList;
    private PrepService prepService;
    protected PreferencesManager mPreferencesManager;
    private Tracker mTracker;
    private Date currentSelectedDate = Calendar.getInstance().getTime();
    static boolean populateSubject;
    public PractiseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @NonNull
    public static PractiseFragment newInstance(boolean populateSubject1) {
        populateSubject=populateSubject1;
        PractiseFragment fragment = new PractiseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practise, container, false);
        ButterKnife.bind(this, view);
        mPreferencesManager = PreferencesManager.instance(getActivity());
        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        if(mUserManager.getCategory()!=null) {
            Constants.logEvent4FCM(self, String.valueOf(mUserManager.getCategory().getCatid()), mUserManager.getCategory().getCategory(), new Date(), "Mock Test", "VIEW_ITEM");
            Product product =  new Product()
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
            mTracker.setScreenName("Practise Fragment");
            mTracker.send(builder.build());
        }

        prepService =PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        initUI();
        return view;
    }

    @OnClick({R.id.btn_take_test})
    public void onClick(@NonNull View view) {
        TestRulesActivity.show(self,mUserManager.getUser().getSelectedCatID(),3);

    }

    private void initUI() {
        getPrepTestBySubCat();
    }

    void getPrepTestBySubCat() {
        progressActivity.showLoading();

        if (populateSubject){
            prepService.sections(mUserManager.getUser().getSelectedCatID(),1,100).enqueue(new Callback<List<Section>>() {
                @Override
                public void onResponse(Call<List<Section>> call, @NonNull Response<List<Section>> response) {
                    if(response.isSuccessful())
                        mPreferencesManager.setMySectionList(response.body());
                    populateSubject=false;
                    mSubjectList = response.body();
                    setSubjects();
                }


                @Override
                public void onFailure(Call<List<Section>> call, Throwable t) {

                }
            });
        }
        else {
            mSubjectList = mPreferencesManager.getMySectionList();
            setSubjects();

        }


    }


    private void setSubjects() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        if (progressActivity != null) {
            progressActivity.showContent();
        }
        if (subjectsRecyclerView != null) {
            subjectsRecyclerView.setLayoutManager(llm);
            subjectsRecyclerView.setHasFixedSize(true);
            //subjectsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), true));
            if(mSubjectList.size()>=1) {
                SectionItem sectionAdapter = new SectionItem(mSubjectList);
                subjectsRecyclerView.setAdapter(sectionAdapter);
                sectionAdapter.setOnItemClickListener((itemView, position) ->
                {
                    //fcm
                    Constants.logEvent4FCM(self, String.valueOf(mSubjectList.get(position).getSubjectId()), mSubjectList.get(position).getSubject(), new Date(), "Section Test", "VIEW_ITEM");
                    SectionTestActivity.show(self, mSubjectList.get(position).getSubjectId(), mSubjectList.get(position).getSubject());
                });
            }
            else {
                cardNotFound.setVisibility(View.VISIBLE);
                subjectsRecyclerView.setVisibility(View.GONE);
                btnTakeTest.setVisibility(View.GONE);
                txtDummy.setVisibility(View.GONE);
            }

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
