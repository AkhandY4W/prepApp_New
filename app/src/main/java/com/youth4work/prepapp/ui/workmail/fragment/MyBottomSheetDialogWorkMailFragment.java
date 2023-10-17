package com.youth4work.prepapp.ui.workmail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.youth4work.prepapp.R;


/**
 * Created by Youth4Work on 16-Sep-16.
 */
public class MyBottomSheetDialogWorkMailFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mBottomSheetListener;
    public static Context mContext;
    LinearLayout allWorkmail, readWorkmail, unreadWorkmail, trashWorkmail,
            companyWorkmail, groupWorkmail, individualWorkmail;

    public static MyBottomSheetDialogWorkMailFragment newInstance(Context context) {
        MyBottomSheetDialogWorkMailFragment f = new MyBottomSheetDialogWorkMailFragment();
        Bundle args = new Bundle();
        mContext = context;
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_layout_for_workmail, container, false);
        allWorkmail = v.findViewById(R.id.all_workmail);
        readWorkmail = v.findViewById(R.id.read_workmail);
        unreadWorkmail = v.findViewById(R.id.unread_workmail);
        trashWorkmail = v.findViewById(R.id.trash_workmail);
        companyWorkmail = v.findViewById(R.id.company_workmail);
        individualWorkmail = v.findViewById(R.id.individual_workmail);
        groupWorkmail = v.findViewById(R.id.group_workmail);
        allWorkmail.setOnClickListener(this);
        readWorkmail.setOnClickListener(this);
        unreadWorkmail.setOnClickListener(this);
        trashWorkmail.setOnClickListener(this);
        companyWorkmail.setOnClickListener(this);
        individualWorkmail.setOnClickListener(this);
        groupWorkmail.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final Handler handler = new Handler();
        switch (v.getId()) {
            case R.id.all_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("All workmail");
                        dismiss();
                    }
                }, 100);
                break;
            case R.id.read_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Read workmail");
                        dismiss();
                    }
                }, 100);

                break;
            case R.id.unread_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Unread workmail");
                        dismiss();
                    }
                }, 100);

                break;
            case R.id.trash_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Trash workmail");
                        dismiss();
                    }
                }, 100);

                break;
            case R.id.company_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Company workmail");
                        dismiss();
                    }
                }, 100);

                break;
            case R.id.individual_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Individual workmail");
                        dismiss();
                    }
                }, 100);

                break;
            case R.id.group_workmail:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetListener.onOptionClick("Group workmail");
                        dismiss();
                    }
                }, 100);

                break;
        }
    }

    public interface BottomSheetListener {
        void onOptionClick(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}
