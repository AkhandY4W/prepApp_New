package com.youth4work.prepapp.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.navigation.NavigationView;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.adapter.SubCategoryListingAdapter;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.startup.AllWebView;
import com.youth4work.prepapp.ui.startup.LoginActivity;
import com.youth4work.prepapp.ui.startup.RegisterationActivity;
import com.youth4work.prepapp.ui.workmail.WorkMailActivity;
import com.youth4work.prepapp.util.CircleTransforms;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPrepActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imgUserAvatar;
    TextView txtUsername;
    TextView txtUserMessage;
    boolean doubleBackToExitPressedOnce = false;
    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.exams_recycler_view)
    RecyclerView myExamsRecyclerView;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_test)
    TextView txtTest;
    @BindView(R.id.card_not_found)
    RelativeLayout cardNotFound;
    SubCategoryListingAdapter adapter;
    private PrepService prepService;
    private Tracker mTracker;

    StaggeredGridLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);
        ButterKnife.bind(this);
        PrepApplication application = new PrepApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "My Prep");
        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(MyPrepActivity.this).getToken()[0]);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, getMarginInDp(16), 0);
        myExamsRecyclerView.setLayoutParams(params);
        setupToolbar();
        setupMyExam();
        SpannableString ss = new SpannableString(getString(R.string.you_haven_t_taken_any_tests_go_to_all_prep));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ChooseExamActivity.show(MyPrepActivity.this);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 34, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTest.setText(ss);
        txtTest.setMovementMethod(LinkMovementMethod.getInstance());

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

    private int getMarginInDp(int sizeInPx) {
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                sizeInPx,
                r.getDisplayMetrics()
        );
        return px;
    }

    private void setupMyExam() {
        progressActivity.showLoading();
        prepService.getMyPrepList(mUserManager.getUser().getUserId(), 1, 100).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressActivity.showContent();
                    if (response.body().size() < 1) {
                        // startActivity(new Intent(MyPrepActivity.this,DiscoverExamsActivity.class));
                        cardNotFound.setVisibility(View.VISIBLE);
                    } else {
                        cardNotFound.setVisibility(View.GONE);
                        setupMyExamsRecyclerView(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressActivity.showContent();
                Toast.makeText(self, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
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
                if(!query.equals(""))
                adapter.getFilter().filter(query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.equals(""))
                    adapter.getFilter().filter(query);
                return true;
            }
        });
        return true;
    }

    private void setupMyExamsRecyclerView(List<Category> categories) {
        if (categories != null && categories.size() > 0) {
            progressActivity.showContent();
            txtMessage.setVisibility(View.GONE);
            llm = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
            myExamsRecyclerView.setLayoutManager(llm);
            adapter = new SubCategoryListingAdapter(this, categories, "MyPrepFragment");
            myExamsRecyclerView.setAdapter(adapter);
            setOnItemListener();
        } else {
            progressActivity.showContent();
            txtMessage.setVisibility(View.VISIBLE);
            if (myExamsRecyclerView != null) {
                myExamsRecyclerView.setVisibility(View.GONE);
            }
        }
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
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mUserManager.getCategory() == null || !mUserManager.getCategory().getCategory().equals("")) {
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
                ChooseExamActivity.show(MyPrepActivity.this);
                break;
            case R.id.nav_invite_friend:
                Intent intent = new Intent(MyPrepActivity.this, InviteFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_upgrade:
                UpgradePlanActivity.show(this);
                break;
            case R.id.nav_about_test:
                AllWebView.LoadWebView(MyPrepActivity.this, "https://www.prep.youth4work.com/" + Constants.EncodeMaster(mUserManager.getCategory().getParentCategory()) + "/" + Constants.EncodeMaster(mUserManager.getCategory().getCategory()) + "-Test/About", "ChooseExamActivity");
                break;
            case R.id.work_mail:
                startActivity(new Intent(MyPrepActivity.this, WorkMailActivity.class));
                break;
            case R.id.nav_verify_email:
            case R.id.nav_verify_mobile:
                finish();
                VerificationActivity.show(this);
                break;
            case R.id.nav_change_course:
                startActivity(new Intent(MyPrepActivity.this, MyPrepActivity.class));
                break;
            case R.id.nav_logout:
                mPreferencesManager.clearAllUserData();
                AppEventsLogger.clearUserID();
                finish();
                LoginActivity.show(this);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setOnItemListener() {
        if (adapter != null) {
            adapter.setOnClickListener(new SubCategoryListingAdapter.OnItemClickListener() {
                @Override
                public void onForumClick(@NonNull Category category) {
                    mUserManager.getUser().setSelectedCatID(category.getCatid());
                    category.setSubCategoryImg(!category.getSubCategoryImg().equals("") ? category.getSubCategoryImg() : category.getSubCatImg());
                    mUserManager.setCategory(category);
                    mUserManager.setUser(mUserManager.getUser());
                    Intent i = new Intent(MyPrepActivity.this, DashboardActivity.class);
                    i.putExtra("menuFragment", "4");
                    i.putExtra("Catid", category.getCatid());
                    startActivity(i);
                }

                @Override
                public void onTakeTestClick(@NonNull Category category) {
                    mUserManager.getUser().setSelectedCatID(category.getCatid());
                   category.setSubCategoryImg(!category.getSubCatImg().equals("") ? category.getSubCatImg() : "");
                    mUserManager.setCategory(category);
                    mUserManager.setUser(mUserManager.getUser());
                    finish();
                    DashboardActivity.show(MyPrepActivity.this, true);

                }
            });
        }
    }
}
