package com.youth4work.prepapp.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.BuildConfig;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.ui.adapter.CategoryItem;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.forum.ForumFragment;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;
import com.youth4work.prepapp.ui.startup.AllWebView;
import com.youth4work.prepapp.ui.startup.LoginActivity;
import com.youth4work.prepapp.ui.workmail.WorkMailActivity;
import com.youth4work.prepapp.util.CircleTransforms;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.PrepDialogsUtils;
import com.youth4work.prepapp.util.Toaster;

import org.jsoup.Jsoup;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.UpDateDialog;
import static com.youth4work.prepapp.util.PrepDialogsUtils2Kt.varifyMobileNo;

//import com.google.android.gms.analytics.Tracker;
//import io.intercom.android.sdk.Intercom;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.OnFragmentInteractionListener {
    public static final String CATEGORY = "category";
    @Nullable
    @BindView(R.id.tabs)
    TabLayout tabs;
    @Nullable
    @BindView(R.id.container)
    ViewPager container;
    @Nullable
    @BindView(R.id.nav_view)
    NavigationView navView;
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    ImageView imgUserAvatar;
    TextView txtUsername;
    TextView txtUserMessage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    @NonNull
    private String mPracticeDay = "DASHBOARD";
    private Category mCategory;
    private Tracker mTracker;
    static boolean populateSubject = false;
    static boolean flag1 = false;
    boolean doubleBackToExitPressedOnce = false;
    private AppUpdateManager appUpdateManager;

    private static final int REQUEST_APP_UPDATE = 123;
    UserUpgrade userUpgrade;

    public static void show(@NonNull Context fromActivity, boolean populateSubject1) {
        populateSubject = populateSubject1;
        Intent intent = new Intent(fromActivity, DashboardActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void show(@NonNull Context fromActivity, boolean populateSubject1, ParentCategory.SubCat category,String Category) {
        populateSubject = populateSubject1;
        mUserManager.getUser().setSelectedCatID(category.getSubCategoryId());
        Category categorynew=new Category(category.getSubCategoryId());
        categorynew.setCatid(category.getSubCategoryId());
        categorynew.setAttempts(category.getSubCategoryAspirants());
        categorynew.setCategory(category.getSubCategory());
        if(!category.getSubCategoryImages().equals(""))
        categorynew.setSubCategoryImg(category.getSubCategoryImages());
        categorynew.setParentCategory(Category);
        mUserManager.setCategory(categorynew);
        mUserManager.setUser(mUserManager.getUser());
        Intent intent = new Intent(fromActivity, DashboardActivity.class);
        fromActivity.startActivity(intent);
    }

    public static void showfromverification(@NonNull Context context, boolean flag) {
        Intent intent = new Intent(context, DashboardActivity.class);
        flag1 = flag;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getContent();
        userUpgrade = new Gson().fromJson(mPreferencesManager.getPendingPaymentDetails(),UserUpgrade.class);
        setupToolbar();
        setupTabs();
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        appUpdateManager = AppUpdateManagerFactory.create(DashboardActivity.this);

        // Don't need to do this here anymore
        // Returns an intent object that you use to check for an update.
        //Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(
                appUpdateInfo -> {
                    // Checks that the platform will allow the specified type of update.
                    if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Request the update.
                        SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
                        int appUsedCount = preferences.getInt("appUsedCount", 0);
                        appUsedCount++;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("appUsedCount", appUsedCount);
                        editor.apply();
                        if (appUsedCount % 5 == 0) {
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        this,
                                        REQUEST_APP_UPDATE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Dashboard");
        int menuFragment = getIntent().getStringExtra("menuFragment") != null ? Integer.parseInt(getIntent().getStringExtra("menuFragment")) : -1;
        if (menuFragment != -1) {
            int categoryID = getIntent().getStringExtra("Catid") != null ? Integer.parseInt(getIntent().getStringExtra("Catid")) : 0;
            if (categoryID != 0 && mUserManager.getUser().getSelectedCatID() != categoryID) {
                mUserManager.getUser().setSelectedCatID(categoryID);
                fillCategory(categoryID, menuFragment);
                getIntent().removeExtra("menuFragment");
                getIntent().removeExtra("Catid");
            } else {
                selectPage(menuFragment);
            }
        }
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
        mCategory = mUserManager.getCategory();
        if (mCategory == null) {
            finish();
            ChooseExamActivity.show(this);
        }
        else {
        if (!mUserManager.getUser().isMobileVerified() && flag1 == false) {
            varifyMobileNo(DashboardActivity.this, mUserManager.getUser().getContactNo());
        }
        flag1 = false;
    }
    }

    private void setupTabs() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);

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
        if (mUserManager.getCategory() != null) {
            setTitle(mUserManager.getCategory().getCategory());
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
            navigationView.getMenu().findItem(R.id.chat).setVisible(true);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);
            imgUserAvatar = header.findViewById(R.id.img_user_avatar);
            txtUsername = header.findViewById(R.id.txt_username);
            txtUserMessage = header.findViewById(R.id.txt_user_message);
            txtUsername.setText(mUserManager.getUser().getName());
            txtUserMessage.setText(mUserManager.getUser().getEmailID());
            Picasso.get().load(mUserManager.getUser().getImgUrl()).transform(new CircleTransforms()).into(imgUserAvatar);

        } else {
            navigationView.getMenu().findItem(R.id.nav_about_test).setVisible(false);
        }

    }

    void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        mViewPager.setCurrentItem(pageIndex);
    }

    private void fillCategory(int catid, int mFragment) {
        ProgressDialog pd = new ProgressDialog(self);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        prepService.getMyPrepList(mUserManager.getUser().getUserId(), 1, 100).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful())
                    mPreferencesManager.setMYPrepList(response.body());
                for (Category category : response.body()) {
                    if (category.getCatid() == mUserManager.getUser().getSelectedCatID()) {
                        mUserManager.setCategory(category);
                        setTitle(category.getCategory());
                        populateSubject = false;
                    }
                }
                pd.dismiss();
                selectPage(mFragment);

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
          finish();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                ChooseExamActivity.show(this);
                //startActivity(new Intent(DashboardActivity.this,DiscoverExamsActivity.class));
                break;
            case R.id.nav_invite_friend:
                Intent intent = new Intent(DashboardActivity.this, InviteFriendActivity.class);
                startActivity(intent);
                break;
           /* case R.id.help:
//                Intercom.client().displayMessageComposer();
                break;
           */
           case R.id.nav_upgrade:
                UpgradePlanActivity.show(this);
                break;
            case R.id.nav_about_test:
                AllWebView.LoadWebView(DashboardActivity.this, "https://www.prep.youth4work.com/" + Constants.EncodeMaster(mUserManager.getCategory().getParentCategory()) + "/" + Constants.EncodeMaster(mUserManager.getCategory().getCategory()) + "-Test/About", "DashboardActivity");
                break;
            case R.id.nav_change_course:
                finish();
                startActivity(new Intent(DashboardActivity.this,MyPrepActivity.class));
                break;
                case R.id.chat:
                startActivity(new Intent(DashboardActivity.this, ChatActivity.class));
                break;
            case R.id.work_mail:
                startActivity(new Intent(DashboardActivity.this, WorkMailActivity.class));
                break;
            case R.id.nav_verify_mobile:
            case R.id.nav_verify_email:
                finish();
                VerificationActivity.show(this);
                break;
            case R.id.nav_logout:
                mPreferencesManager.clearAllUserData();
                finish();
                Toaster.showLong(this, "Logged out successfully!");
                AppEventsLogger.clearUserID();
                LoginActivity.show(this);
                break;
            case R.id.payment_status:
                BaseActivity.getPayment(DashboardActivity.this,userUpgrade,false);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment result = new Fragment();
            switch (position) {
                case 0:
                    result = PractiseFragmentNew.newInstance(populateSubject);
                    break;
                case 1:
                    result = MockTestFragment.newInstance();
                    break;
                case 2:
                    result = ReportsFragment.newInstance();
                    break;
                    case 3:
                    result = ChatFragment.newInstance();
                    break;
                case 4:
                    result = ForumFragment.newInstance();
                    break;
            }
            return result;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.practise);
                case 1:
                    return getString(R.string.mock_test);
                case 2:
                    return getString(R.string.report);
                case 3:
                    return getString(R.string.chat);
                case 4:
                    return getString(R.string.forum);


            }
            return null;
        }
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=it").timeout(300000)

                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")

                        .referrer("http://www.google.com")

                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                return newVersion;

            } catch (Exception e) {

                return newVersion;

            }

        }

        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            String currentVersion = null;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName().trim(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (!currentVersion.equalsIgnoreCase(onlineVersion)) {

                    SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
                    int appUsedCount = preferences.getInt("appUsedCount", 0);
                    appUsedCount++;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("appUsedCount", appUsedCount);
                    editor.apply();

                    if (appUsedCount == 1 || appUsedCount == 5 || appUsedCount == 10 || appUsedCount == 50 || appUsedCount == 100 || appUsedCount == 1000 || appUsedCount == 200 || appUsedCount == 400 || appUsedCount == 500 || appUsedCount % 50 == 0) {

                        UpDateDialog(DashboardActivity.this);
                    }

                }

                Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

            }
        }
    }
}


