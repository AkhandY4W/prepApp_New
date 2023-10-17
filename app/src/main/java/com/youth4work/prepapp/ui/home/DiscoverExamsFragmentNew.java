package com.youth4work.prepapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.adapter.CategoryItemNew;
import com.youth4work.prepapp.ui.adapter.ParentCategoryItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.views.CustomTextViewFontBold;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.google.android.gms.analytics.Tracker;

public class DiscoverExamsFragmentNew extends BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.txt_header)
    CustomTextViewFontBold txtHeader;
    @BindView(R.id.exams_recycler_view)
    RecyclerView examsRecyclerView;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    private List<ParentCategory> mCategories;
    private PrepService prepService;
    private Subscription subscription;
    private Tracker mTracker;

    public DiscoverExamsFragmentNew() {
    }

    public static DiscoverExamsFragmentNew newInstance(int sectionNumber) {
        DiscoverExamsFragmentNew fragment = new DiscoverExamsFragmentNew();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static DiscoverExamsFragmentNew newInstance() {
        DiscoverExamsFragmentNew fragment = new DiscoverExamsFragmentNew();
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

        initExams();
        return rootView;
    }

    private void initExams() {
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        LinearLayoutManager llm = new GridLayoutManager(getContext(), 1,
                RecyclerView.HORIZONTAL, false);
        examsRecyclerView.setLayoutManager(llm);
        examsRecyclerView.addOnScrollListener(new HeaderRecyclerOnScrollListener(llm) {

            @Override
            public void onShowHeader() {
                txtHeader.setVisibility(View.GONE);
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
        prepService.getcategories().enqueue(new Callback<List<ParentCategory>>() {
            @Override
            public void onResponse(Call<List<ParentCategory>> call, Response<List<ParentCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCategories = response.body();
                    prepService.getPopularExams().enqueue(new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Category> categoryList = response.body();
                                List<ParentCategory.SubCat> categoryListnew = new ArrayList<>();

                                for (int i = 0; i < categoryList.size(); i++) {
                                    ParentCategory.SubCat partentSubCat=new ParentCategory.SubCat();
                                    partentSubCat.setSubCategoryId(categoryList.get(i).getCatid());
                                    partentSubCat.setSubCategory(categoryList.get(i).getCategory());
                                    partentSubCat.setSubCategoryAspirants(Integer.parseInt(categoryList.get(i).getAspirants()));
                                    partentSubCat.setCategory(categoryList.get(i).getParentCategory());
                                    partentSubCat.setSubCategoryImages(categoryList.get(i).getSubCategoryImg());
                                    categoryListnew.add(partentSubCat);
                                }
                                if (categoryListnew != null) {
                                    ParentCategory parentCategory = new ParentCategory("Popular", 0, "", categoryListnew);
                                    mCategories.add(0, parentCategory);
                                }
                                initializeAdapter();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Category>> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<ParentCategory>> call, Throwable t) {

            }
        });
    }

    private void initializeAdapter() {

        examsRecyclerView.setHasFixedSize(true);

        CategoryItemNew adapter = new CategoryItemNew(mCategories, "DiscoverExamsFragment", self);

        examsRecyclerView.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));

        examsRecyclerView.setAdapter(adapter);
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

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null) {

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
