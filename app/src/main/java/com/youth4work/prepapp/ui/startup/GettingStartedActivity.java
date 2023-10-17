package com.youth4work.prepapp.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.ChooseExamActivity;
import com.youth4work.prepapp.ui.views.PrepButton;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GettingStartedActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.txt_header)
    TextView txtHeader;
    @Nullable
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @Nullable
    @BindView(R.id.btn_get_started)
    PrepButton btnGetStarted;

    public static void show(@NonNull Context fromActivity) {
        Intent intent = new Intent(fromActivity, GettingStartedActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);
        ButterKnife.bind(this);
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

    @OnClick(R.id.btn_get_started)
    void OnGetStartedClicked() {
        GettingStartedActivity.this.finish();
        ChooseExamActivity.show(this);
    }
}
