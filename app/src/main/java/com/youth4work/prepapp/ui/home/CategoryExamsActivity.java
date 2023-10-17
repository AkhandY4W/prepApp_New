package com.youth4work.prepapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.adapter.SubCategoryListingAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class CategoryExamsActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.exams_recycler_view)
    RecyclerView examsRecyclerView;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    private Category mCategory;
    private List<Category> mCategories;
    //private FastItemAdapter<CategoryItem> fastItemAdapter;
    private Tracker mTracker;
    private int pageNo = 1;
    private boolean loading = true;
    //private FooterAdapter<ProgressItem> footerAdapter;
    SubCategoryListingAdapter adapter;
    GridLayoutManager llm;

    public static void show(@NonNull Context fromActivity, Category category) {
        Intent intent = new Intent(fromActivity, CategoryExamsActivity.class);
        intent.putExtra(DashboardActivity.CATEGORY, new Gson().toJson(category));
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_exams);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "CategoryExamsActivity");
        ButterKnife.bind(this);
        getContent();
        initUI();
        getExamsByCategory();
        progressActivity.showLoading();
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

    private void getContent() {
        mCategory = new Gson().fromJson(getIntent().getExtras().getString(DashboardActivity.CATEGORY, ""), Category.class);
    }

    private void getExamsByCategory() {
        prepService.subCategories(mCategory.getCatid(), 1, 10).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    mCategories = response.body();
                    int size = response.body().size();
                    progressActivity.showContent();
                    if(size>0) {
                        initializeAdapter();
                    }
                   else  {
                        Toast.makeText(CategoryExamsActivity.this, "There is no Exam in this Category ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }

        });

        examsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                getExamsByCategory(pageNo++);
            }
        });
    }

    private void getExamsByCategory(int i) {
        prepService.subCategories(mCategory.getCatid(), i, 10).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> catNew = new ArrayList<>();
                    catNew = response.body();
                 if(catNew.size()>0) {
                     mCategories.addAll(catNew);
                     progressActivity.showContent();
                     adapter.notifyDataSetChanged();
                 }
                 } else {
                    response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exam_categories, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView;
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals(""))
                    adapter.getFilter().filter(query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals(""))
                    adapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
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

    private void initUI() {
        if (examsRecyclerView != null) {
            llm = new GridLayoutManager(this, 1);
            examsRecyclerView.setLayoutManager(llm);
            examsRecyclerView.setHasFixedSize(false);
            //examsRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), true));
        }
    }

    private void initializeAdapter() {
        adapter = new SubCategoryListingAdapter(CategoryExamsActivity.this, mCategories, "CategoryExamsActivity");
        examsRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new SubCategoryListingAdapter.OnItemClickListener() {
            @Override
            public void onForumClick(@NonNull Category category) {
                mUserManager.getUser().setSelectedCatID(category.getCatid());
                category.setSubCategoryImg(!category.getSubCategoryImg().equals("") ? category.getSubCategoryImg() : category.getSubCatImg());
                mUserManager.setCategory(category);
                mUserManager.setUser(mUserManager.getUser());
                Intent i = new Intent(CategoryExamsActivity.this, DashboardActivity.class);
                i.putExtra("menuFragment", "4");
                i.putExtra("Catid", category.getCatid());
                startActivity(i);
            }

            @Override
            public void onTakeTestClick(@NonNull Category category) {
                mUserManager.getUser().setSelectedCatID(category.getCatid());
                category.setSubCategoryImg(!category.getSubCategoryImg().equals("") ? category.getSubCatImg() : "");
                mUserManager.setCategory(category);
                mUserManager.setUser(mUserManager.getUser());
                finish();
                DashboardActivity.show(CategoryExamsActivity.this, true);

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            llm.setSpanCount(1);
        } else {
            //show in two columns
            llm.setSpanCount(2);
        }
    }

}
