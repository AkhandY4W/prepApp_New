package com.youth4work.prepapp.ui.public_profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.response.user_profile.ProfileData;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.public_profile.fragment.UserProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    @BindView(R.id.profile_pic)
    CircularImageView profile_pic;

    @BindView(R.id.text_username)
    TextView text_username;

    @BindView(R.id.text_location)
    TextView text_location;

    @BindView(R.id.text_talent)
    TextView text_talent;
    ProfileData profileData;
    static String mFullName;
    static String mImgUrl;
    static Long mUserId;
    public static void show(Context context, String fullName, String imgUrl,Long userId){
        mUserId=userId;
        mFullName=fullName;
        mImgUrl=imgUrl;
    context.startActivity(new Intent(context,ProfileActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        text_username.setText(mFullName);

        Picasso.get().load(mImgUrl).into(profile_pic);
       // Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.assets_01_ic_edit_white);
       // toolbar.setOverflowIcon(drawable);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(mFullName);
        }

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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


    public void setText_location(String location, String headline) {
          text_talent.setText(headline);
            text_location.setText(location);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserProfile(mUserId), "About");
       // adapter.addFrag(new YStoriesFragment(), "Blog");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AllWebViewActivity.LoadWebView(ProfileActivity.this, "https://www.youth4work.com/User/EditProfile","EditProfile");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

