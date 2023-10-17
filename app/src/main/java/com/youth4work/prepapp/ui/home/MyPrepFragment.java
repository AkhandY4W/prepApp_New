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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;

public class MyPrepFragment extends BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.my_exams_recycler_view)
    RecyclerView myExamsRecyclerView;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    private FastItemAdapter<CategoryItem> fastItemAdapter;
    private PrepService prepService;

  private Tracker mTracker;

    public MyPrepFragment() {
    }

    public static MyPrepFragment newInstance(int sectionNumber) {
        MyPrepFragment fragment = new MyPrepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public static MyPrepFragment newInstance() {
        MyPrepFragment fragment = new MyPrepFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_prep, container, false);
        ButterKnife.bind(this, rootView);
        PrepApplication application = (PrepApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "My Prep Fragment");
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(self).getToken()[0]);
        fastItemAdapter = new FastItemAdapter();
        setupMyExam();
       // setupMyExamsRecyclerView(mPreferencesManager.getMYPrepList());
        return rootView;
    }

    private void setupMyExam() {
        prepService.getMyPrepList(mUserManager.getUser().getUserId(),1,100).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    setupMyExamsRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(self, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            }
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
                fastItemAdapter.filter(query);
                return false;
            }
        });
    }

    private void setupMyExamsRecyclerView(List<Category> categories) {
        if (categories!=null && categories.size() > 0) {
            progressActivity.showContent();
            txtMessage.setVisibility(View.GONE);

            LinearLayoutManager llm = new GridLayoutManager(self, 2,
                    RecyclerView.VERTICAL,false);
            myExamsRecyclerView.setLayoutManager(llm);
            myExamsRecyclerView.setAdapter(fastItemAdapter);
            for (Category category : categories) fastItemAdapter.add(new CategoryItem(category,"MyPrepFragment",self));

            fastItemAdapter.withFilterPredicate((item, constraint) -> !item.category.getCategory().toLowerCase().contains(constraint.toString().toLowerCase()));

            fastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
                mUserManager.getUser().setSelectedCatID(item.category.getCatid());
                mUserManager.setCategory(item.category);
                mUserManager.setUser(mUserManager.getUser());
                getActivity().finish();
                DashboardActivity.show(getActivity(),true);
                return true;
            });
        } else {
            progressActivity.showContent();
            txtMessage.setVisibility(View.VISIBLE);
            if (myExamsRecyclerView != null) {
                myExamsRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }
}
