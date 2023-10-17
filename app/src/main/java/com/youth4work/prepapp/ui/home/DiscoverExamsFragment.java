package com.youth4work.prepapp.ui.home;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.adapter.ParentCategoryItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.views.CustomTextViewFontBold;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.google.android.gms.analytics.Tracker;

public class DiscoverExamsFragment extends BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
   @BindView(R.id.txt_header)
    CustomTextViewFontBold txtHeader;
   @BindView(R.id.exams_recycler_view)
    RecyclerView examsRecyclerView;
   @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    private List<Category> mCategories;
    private PrepService prepService;
    private Subscription subscription;
    private FastItemAdapter fastItemAdapter;
    private Tracker mTracker;

    public DiscoverExamsFragment() {
    }

    public static DiscoverExamsFragment newInstance(int sectionNumber) {
        DiscoverExamsFragment fragment = new DiscoverExamsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public static DiscoverExamsFragment newInstance() {
        DiscoverExamsFragment fragment = new DiscoverExamsFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover_exam, container, false);
        ButterKnife.bind(this, rootView);

        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Discover exam fragment");

        fastItemAdapter = new FastItemAdapter();
        initExams();
        return rootView;
    }

    private void initExams() {
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        LinearLayoutManager llm = new GridLayoutManager(getContext(), 1,
                RecyclerView.VERTICAL,false);
        examsRecyclerView.setLayoutManager(llm);
        examsRecyclerView.addOnScrollListener(new HeaderRecyclerOnScrollListener(llm) {

            @Override
            public void onShowHeader() {
                txtHeader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHideHeader() {
                txtHeader.setVisibility(View.GONE);
            }
        });
        examsRecyclerView.setHasFixedSize(true);
        initExamsList();
    }

    private void initExamsList() {
        progressActivity.showLoading();
        subscription = Observable.zip(prepService.popularExams(), prepService.categories(), (popularCategories, categories) -> {
            List<Category> categoryList = new ArrayList<>();
            categoryList.addAll(popularCategories);
            categoryList.addAll(categories);
            return categoryList;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {
                        progressActivity.showContent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressActivity.showContent();
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        mCategories = categories;
                        initializeAdapter();
                    }
                });
    }

    private void initializeAdapter() {

        if (examsRecyclerView != null) examsRecyclerView.setAdapter(fastItemAdapter);

        for (int i = 0; i < mCategories.size(); i++) {
            Category category = mCategories.get(i);
            if (category.getParentCategory() != null)
                fastItemAdapter.add(new CategoryItem(mCategories.get(i),"DiscoverExamsFragment",self));
            else fastItemAdapter.add(new ParentCategoryItem(mCategories.get(i)));
        }

        fastItemAdapter.withFilterPredicate((item, constraint) -> {
            if (item instanceof CategoryItem) {
                return !((CategoryItem) item).category.getCategory().toLowerCase().contains(constraint.toString().toLowerCase());
            } else if (item instanceof ParentCategoryItem) {
                return !((ParentCategoryItem) item).category.getCategory().toLowerCase().contains(constraint.toString().toLowerCase());
            }
            return false;
        });

        fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {

            if (item instanceof CategoryItem) {
                mUserManager.getUser().setSelectedCatID(((CategoryItem) item).category.getCatid());
                mUserManager.setCategory(((CategoryItem) item).category);
                mUserManager.setUser(mUserManager.getUser());
                DashboardActivity.show(getActivity(),true);
            } else if (item instanceof ParentCategoryItem) {
                CategoryExamsActivity.show(getActivity(), ((ParentCategoryItem) item).category);
            }
            return true;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam_categories, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView;
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fastItemAdapter.filter(query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null) {
                    fastItemAdapter.filter(query);
                }

                return false;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!subscription.isUnsubscribed()) subscription.unsubscribe();
        //ButterKnife.unbind(this);
    }
}
