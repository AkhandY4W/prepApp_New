package com.youth4work.prepapp.ui.workmail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.PrepApiFory4w;
import com.youth4work.prepapp.network.PrepServiceFory4w;
import com.youth4work.prepapp.network.model.response.Message;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.workmail.adapter.MessageAdapter;
import com.youth4work.prepapp.ui.workmail.fragment.MyBottomSheetDialogWorkMailFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkMailActivity extends BaseActivity implements MyBottomSheetDialogWorkMailFragment.BottomSheetListener {
    List<Message> frms;
    MessageAdapter mAdapter;
    UserManager oUser;
    int pageNumber = 1;
    PrepServiceFory4w prepServiceFory4w;
    RecyclerView rclMessage;
    ProgressRelativeLayout progressActivity;
    /* LinearLayout companyWorkmail, trashWorkmail, unreadWorkmail,
             readWorkmail, allWorkmail, individualWorkmail,
             groupWorkmail;*/
    CardView bottomSheetFilterWorkmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_mail_activity);
        bottomSheetFilterWorkmail = findViewById(R.id.bottom_sheet_filter_workmail);
        progressActivity = findViewById(R.id.work_mail_progress);
        progressActivity.showLoading();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Work Mail");
        rclMessage = findViewById(R.id.notifications);
        LoadDataFromApi();

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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_mail, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final MenuItem myFilterItem=menu.findItem(R.id.action_filter);
        final SearchView searchView;
        searchView = (SearchView) myActionMenuItem.getActionView();
        myActionMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
               myFilterItem.setVisible(false);
               return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                myFilterItem.setVisible(false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.filter(query);
                return false;
            }
        });
        return true;
    }

    public void showBottomSheetDialog() {
        BottomSheetDialogFragment myBottomSheet = MyBottomSheetDialogWorkMailFragment.newInstance(this);
        FragmentManager fm = getSupportFragmentManager();
        myBottomSheet.show(fm, myBottomSheet.getTag());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showBottomSheetDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void LoadDataFromApi() {
        prepServiceFory4w = PrepApiFory4w.provideRetrofit();
        oUser = UserManager.getInstance(getApplicationContext());
        prepServiceFory4w.yMail(oUser.getUser().getUserId(), pageNumber++, 50, "false").enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, @NonNull Response<Message> response) {
                if (response != null && response.body() != null) {
                    Message message = response.body();
                    if (message != null)
                        progressActivity.showContent();
                    frms = message.getmArrListMessages();
                    if (frms != null && frms.size() != 0) {
                        TextView noMail = findViewById(R.id.no_mail);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        mAdapter = new MessageAdapter(frms, getApplicationContext());
                        rclMessage.setHasFixedSize(true);
                        rclMessage.setLayoutManager(llm);
                        rclMessage.setAdapter(mAdapter);
                        rclMessage.setVisibility(View.VISIBLE);
                        noMail.setVisibility(View.INVISIBLE);

                        mAdapter.setOnItemClickListner((itemView, position) -> {
                            Message msg = frms.get(position);
                            Gson gson = new Gson();
                            String m = gson.toJson(msg, Message.class);
                            Intent conversation = new Intent(WorkMailActivity.this, ConversationActivity.class);
                            conversation.putExtra("msg", m);
                            startActivity(conversation);
                        });
                       rclMessage.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
                            @Override
                            public void onLoadMore(int page) {
                                LoadMoreDataFromApi(pageNumber++);
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    public void LoadMoreDataFromApi(int page) {
        prepServiceFory4w.yMail(oUser.getUser().getUserId(), page, 10, "false").enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, @NonNull Response<Message> response) {
                Message message = response.body();
                if (message != null) {
                    List<Message> frmsNew = message.getmArrListMessages();
                    if (frmsNew != null && frmsNew.size() != 0) {
                        frms.addAll(frmsNew);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    @Override
    public void onOptionClick(String text) {
        Toast.makeText(self, text + "", Toast.LENGTH_SHORT).show();
    }

}