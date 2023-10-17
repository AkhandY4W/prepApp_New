package com.youth4work.prepapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PayTM.Log;
import com.youth4work.prepapp.PayTM.PaytmMerchant;
import com.youth4work.prepapp.PayTM.PaytmPGService;
import com.youth4work.prepapp.PayTM.PaytmStatusQuery;
import com.youth4work.prepapp.PayTM.PaytmStatusQueryCallback;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.ui.adapter.CategoryItemNew;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.startup.AllWebView;
import com.youth4work.prepapp.ui.startup.LoginActivity;
import com.youth4work.prepapp.ui.views.CustomTextViewFontBold;
import com.youth4work.prepapp.ui.workmail.WorkMailActivity;
import com.youth4work.prepapp.util.CircleTransforms;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

//import com.google.android.gms.analytics.Tracker;

public class ChooseExamActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*@BindView(R.id.tabs)
    TabLayout tabLayout;*/
    //@BindView(R.id.container)
   // ViewPager mViewPager;
    ImageView imgUserAvatar;
    TextView txtUsername;
    TextView txtUserMessage;

    private Tracker mTracker;
    boolean doubleBackToExitPressedOnce = false;
    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.exams_recycler_view)
    RecyclerView examsRecyclerView;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    private List<ParentCategory> mCategories;
    private PrepService prepService;
    CategoryItemNew adapter;
    UserUpgrade userUpgrade;
    public static void show(@NonNull Context fromActivity, Category category) {
        Intent intent = new Intent(fromActivity, CategoryExamsActivity.class);
        intent.putExtra(DashboardActivity.CATEGORY, new Gson().toJson(category));
        fromActivity.startActivity(intent);
    }

    public static void show(@NonNull Context fromActivity) {
        Intent intent = new Intent(fromActivity, ChooseExamActivity.class);
        fromActivity.startActivity(intent);
    }

    @NonNull
    public static PractiseFragment newInstance() {
        PractiseFragment fragment = new PractiseFragment();
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);
        ButterKnife.bind(this);
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Choose Exam");
        userUpgrade = new Gson().fromJson(PreferencesManager.instance(ChooseExamActivity.this).getPendingPaymentDetails(),UserUpgrade.class);
//        Log.e("user payment", userUpgrade.toString());
        setupToolbar();
        initExams();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exam_categories, menu);
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
                    adapter.getFilter().filter(query);
                }

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.action_search){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initExams() {
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(ChooseExamActivity.this).getToken()[0]);
        LinearLayoutManager llm = new GridLayoutManager(this, 1,
                RecyclerView.HORIZONTAL, false);
        examsRecyclerView.setLayoutManager(llm);
        examsRecyclerView.setHasFixedSize(true);
        initExamsList();
    }

    private void initExamsList() {
        progressActivity.showLoading();
        prepService.getcategories().enqueue(new Callback<List<ParentCategory>>() {
            @Override
            public void onResponse(Call<List<ParentCategory>> call, Response<List<ParentCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCategories = response.body();
                    txtMessage.setVisibility(View.GONE);
                    txtMessage.setText("No test found, Please try again later");
                    prepService.getPopularExams().enqueue(new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                progressActivity.showContent();
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
                            progressActivity.showContent();
                            Toast.makeText(ChooseExamActivity.this, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<ParentCategory>> call, Throwable t) {
                progressActivity.showContent();
                Toast.makeText(ChooseExamActivity.this, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initializeAdapter() {

        examsRecyclerView.setHasFixedSize(true);

        adapter = new CategoryItemNew(mCategories, "DiscoverExamsFragment", self);

        examsRecyclerView.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));

        examsRecyclerView.setAdapter(adapter);
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mUserManager.getCategory() == null) {
            navigationView.getMenu().findItem(R.id.nav_about_test).setVisible(false);
        }
        if (mUserManager.getUser().isMobileVerified()) {
            navigationView.getMenu().findItem(R.id.nav_verify_mobile).setIcon(R.drawable.ic_verify_phone_number);
            navigationView.getMenu().findItem(R.id.nav_verify_mobile).setTitle("Verified Mobile");
        }
        if (mUserManager.getUser().getUserStatus().equals("A")) {
            navigationView.getMenu().findItem(R.id.nav_verify_email).setIcon(R.drawable.ic_verify_email_id);
            navigationView.getMenu().findItem(R.id.nav_verify_email).setTitle("Verified Email");
        }
        if(userUpgrade!=null){
            navigationView.getMenu().findItem(R.id.payment_status).setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        imgUserAvatar = header.findViewById(R.id.img_user_avatar);
        txtUsername = header.findViewById(R.id.txt_username);
        txtUserMessage = header.findViewById(R.id.txt_user_message);
        txtUsername.setText(mUserManager.getUser().getName());
        txtUserMessage.setText(mUserManager.getUser().getEmailID());
        Picasso.get().load(mUserManager.getUser().getImgUrl()).transform(new CircleTransforms()).into(imgUserAvatar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_invite_friend:
                Intent intent = new Intent(ChooseExamActivity.this, InviteFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_upgrade:
                UpgradePlanActivity.show(this);
                break;
            case R.id.nav_about_test:
                AllWebView.LoadWebView(ChooseExamActivity.this, "https://www.prep.youth4work.com/" + Constants.EncodeMaster(mUserManager.getCategory().getParentCategory()) + "/" + Constants.EncodeMaster(mUserManager.getCategory().getCategory()) + "-Test/About", "ChooseExamActivity");
                break;
            case R.id.nav_verify_email:
            case R.id.nav_verify_mobile:
                finish();
                VerificationActivity.show(this);
                break;
            case R.id.nav_change_course:
                startActivity(new Intent(ChooseExamActivity.this,MyPrepActivity.class));
                break;
                case R.id.work_mail:
                startActivity(new Intent(ChooseExamActivity.this, WorkMailActivity.class));
                break;
            case R.id.nav_logout:
                mPreferencesManager.clearAllUserData();
                AppEventsLogger.clearUserID();
                finish();
                LoginActivity.show(this);
                break;
            case R.id.payment_status:
                BaseActivity.getPayment(ChooseExamActivity.this,userUpgrade,false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}