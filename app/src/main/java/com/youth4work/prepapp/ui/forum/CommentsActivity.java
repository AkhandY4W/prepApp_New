package com.youth4work.prepapp.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.CommentsAnswersData;
import com.youth4work.prepapp.ui.adapter.CommentsAnswersItem;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.Toaster;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.analytics.Tracker;


public class CommentsActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.list_comments)
    RecyclerView prepForumList;

    @Nullable
    @BindView(R.id.txtNoComment)
    LinearLayout txtNoComment;



    private Tracker mTracker;
    private PrepService prepService;
    private CommentsAnswersData commentsAnswers;
    private List<CommentsAnswersData> commentsAnswersList;
    int answerId = 0;
    protected UserManager mUserManager;
    String comment, commenterName, commenterImage;
    @Nullable
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    @BindView(R.id.imageAnswer)
    ImageView imageAnswer;
    @BindView(R.id.txt_comment)
    TextView txtComment;
    @BindView(R.id.questionByName)
    TextView questionByName;
    @BindView(R.id.fab_add_comment)
    FloatingActionButton fabAddComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ///getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressActivity.showLoading();
        mUserManager = UserManager.getInstance(this);
        answerId = getIntent().getIntExtra("Answer id", 0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comment = bundle.getString("comment", "");
            commenterName = bundle.getString("commenter_name", "");
            commenterImage = bundle.getString("comenter_img_url", "");
            Picasso.get().load(commenterImage).into(imageAnswer);
            txtComment.setText("\n"+ Html.fromHtml(comment));
        }
        fabAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommentsActivity.this,AddCommentActivity.class);
                intent.putExtra("Answer id", answerId);
                startActivity(intent);
            }
        });
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Comments Activity");

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(CommentsActivity.this).getToken()[0]);
        prepService.commentsAnswer(answerId, 1, 100).enqueue(new Callback<List<CommentsAnswersData>>() {
            @Override
            public void onResponse(Call<List<CommentsAnswersData>> call, @NonNull Response<List<CommentsAnswersData>> response) {
                if (response.isSuccessful())
                    commentsAnswersList = response.body();
                if (commenterName != null) {
                    questionByName.setText("By " + commenterName + " • " + commentsAnswersList.size() + " Comments");
                }// getSupportActionBar().setTitle("Comments ("+Integer.toString(commentsAnswersList.size())+")");
                final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
                AppBarLayout appBarLayout = findViewById(R.id.app_bar);
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = true;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle(Html.fromHtml(comment));
                            isShow = true;
                        } else if(isShow) {
                            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                            isShow = false;
                        }
                    }
                });
                fillList();
            }

            @Override
            public void onFailure(Call<List<CommentsAnswersData>> call, Throwable t) {
                Toaster.showLong(CommentsActivity.this, "Oops Some error occured, Please try again in a while!");
                progressActivity.showContent();
            }
        });

    }

    void fillList() {
        {
            if (commentsAnswersList.size() == 0) {
                txtNoComment.setVisibility(View.VISIBLE);
                prepForumList.setVisibility(View.GONE);
                progressActivity.showContent();
            } else {
                LinearLayoutManager llm = new LinearLayoutManager(this);
                if (prepForumList != null) {
                    prepForumList.setLayoutManager(llm);
                    prepForumList.setHasFixedSize(true);
                    //prepForumList.addItemDecoration(new DividerItemDecoration(getApplication(), true));
                    FastItemAdapter<CommentsAnswersItem> PrepForumItemFastItemAdapter = new FastItemAdapter<>();
                    prepForumList.setAdapter(PrepForumItemFastItemAdapter);

                    for (CommentsAnswersData commentsAnswersData : commentsAnswersList) {
                        PrepForumItemFastItemAdapter.add(new CommentsAnswersItem(commentsAnswersData, this));
                    }
                    progressActivity.showContent();
                    PrepForumItemFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
                        return true;
                    });
                }
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
