package com.youth4work.prepapp.ui.home;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.analytics.Tracker;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;

public class InviteFriendActivity extends BaseActivity {
    Button btninvite;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        btninvite=(Button)findViewById(R.id.btn_invite);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker,"Refer");

        btninvite.setOnClickListener(view -> inviteFriends());
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

}

