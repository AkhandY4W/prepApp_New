package com.youth4work.prepapp.ui.startup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.AutoCompleteData;
import com.youth4work.prepapp.network.model.EducationDetails;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.views.PrepButton;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateEducation extends BaseActivity {
    MaterialAutoCompleteTextView etCollege;
    MaterialAutoCompleteTextView etDegree;
    MaterialAutoCompleteTextView etSpecialization;
    MaterialEditText etBatchFrom;
    MaterialEditText etBatchTo;
    ///Button signup;
    int startYear = 0, endYear = 0;

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.loadinglayout)
    LinearLayout loadinglayout;
    @BindView(R.id.btn_sign_up)
    PrepButton btnupdate;
    private Toast mToast;
    ProgressRelativeLayout progressActivity;
    protected static EducationDetails educationDetails;

    public static void showeducationdeatils(@NonNull Context fromActivity, EducationDetails educationdtl) {
        Intent intent = new Intent(fromActivity, UpdateEducation.class);
        educationDetails = educationdtl;
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_education);
        ButterKnife.bind(this);
        educationDetails.setUserId(mUserManager.getUser().getUserId());
        etCollege = findViewById(R.id.et_college);
        etDegree = findViewById(R.id.et_degree);
        etSpecialization = findViewById(R.id.et_specialization);
        etBatchTo = findViewById(R.id.et_batch_to);
        etBatchFrom = findViewById(R.id.et_batch_from);
        progressActivity = findViewById(R.id.activity_sign_up3);

        if (mAutoCompleteData != null)
            setAdapters();
        else
        getAutoCompleteData();

        etBatchFrom.setInputType(InputType.TYPE_NULL);
        etBatchTo.setInputType(InputType.TYPE_NULL);
        etBatchFrom.setOnFocusChangeListener((view, b) -> {
            if (etBatchFrom.hasFocus())
                setupDialog(etBatchFrom);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etBatchFrom.getWindowToken(), 0);

        });
        etBatchTo.setOnFocusChangeListener((view, b) -> {
            if (etBatchTo.hasFocus())
                setupDialog(etBatchTo);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etBatchFrom.getWindowToken(), 0);

        });
        setcurrenteducationdat();
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

    public void setcurrenteducationdat() {
        if (educationDetails != null && educationDetails.getEducationid() > 0) {
            startYear = educationDetails.getBatchStart();
            endYear = educationDetails.getBatchEnd();
            etBatchFrom.setText("Jan" + "," + startYear);
            etBatchTo.setText("Jan" + "," + endYear);
            if (educationDetails.getSpecialization().toLowerCase().equals("other")) {
                etSpecialization.setText("");
            } else {
                etSpecialization.setText(educationDetails.getSpecialization());

            }
            if (educationDetails.getCourse().toLowerCase().equals("other")) {
                etDegree.setText("");
            } else {
                etDegree.setText(educationDetails.getCourse());
            }
            if (educationDetails.getCollege().toLowerCase().equals("other")) {
                etCollege.setText("");
            } else {
                etCollege.setText(educationDetails.getCollege());
            }

        }
    }

    void setupDialog(MaterialEditText editText) {
        Button btnCancle,btnOk;
        NumberPicker numberPicker, numberPicker1;
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.date_picker);
        btnCancle=myDialog.findViewById(R.id.btn_cancel);
        btnOk=myDialog.findViewById(R.id.btn_ok);
        numberPicker = myDialog.findViewById(R.id.numberPicker);
        numberPicker1 = myDialog.findViewById(R.id.numberPicker2);
        numberPicker.setMinValue(0);
        numberPicker1.setMinValue(1970);
        numberPicker.setMaxValue(11);
        numberPicker1.setMaxValue(2025);
        if (editText.getId() == R.id.et_batch_from)
            numberPicker1.setValue(educationDetails.getBatchStart());
        else
            numberPicker1.setValue(educationDetails.getBatchEnd());
        numberPicker.setDisplayedValues(months);
        myDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getId() == R.id.et_batch_from)
                    startYear = numberPicker1.getValue();
                else
                    endYear = numberPicker1.getValue();
                showToast(" " + months[(numberPicker.getValue())] + "," + numberPicker1.getValue());
                editText.setText(months[(numberPicker.getValue())] + "," + numberPicker1.getValue());
                myDialog.dismiss();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    private void setAdapters() {
        etCollege.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getColleges()));
        etSpecialization.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getSpecializations()));
        etDegree.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getDegrees()));
    }

    @OnClick(R.id.btn_sign_up)
    public void onClick() {
        String batchTo = etBatchTo.getText().toString();
        String batchFrom = etBatchFrom.getText().toString();
        if (etCollege.getText().toString().trim().length() <= 0) {
            showToast("College Not be Empty");
            etCollege.requestFocus();
        } else if (etDegree.getText().toString().trim().length() <= 0) {
            showToast("Degree Not be Empty");
            etDegree.requestFocus();
        } else if (etSpecialization.getText().toString().trim().length() <= 0) {
            showToast("Specialization Not be Empty");
            etSpecialization.requestFocus();
        } else if (etBatchFrom.getText().toString().trim().length() <= 0) {
            showToast("Batch From Not be Empty");
            etBatchFrom.requestFocus();
        } else if (etBatchTo.getText().toString().trim().length() <= 0) {
            showToast("Batch To Not be Empty");
            etBatchTo.requestFocus();
        } else if (startYear > endYear) {
            showToast("Start year can not be less than and equal to End year");
        } else {
            educationDetails.setCollege(etCollege.getText().toString());
            educationDetails.setCourse(etDegree.getText().toString());
            educationDetails.setSpecialization(etSpecialization.getText().toString());
            educationDetails.setBatchStart(startYear);
            educationDetails.setBatchEnd(endYear);
            updateeducationdetails(educationDetails);
            loadinglayout.setVisibility(View.VISIBLE);
            btnupdate.setVisibility(View.GONE);

        }
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        // View view = mToast.getView();
        //view.setBackgroundResource(R.color.colorAccent);
        mToast.show();
    }

    protected void getAutoCompleteData() {
        progressActivity.showLoading();
        Observable.zip(prepService.colleges(), prepService.degrees(), prepService.specializations(), prepService.cities(), AutoCompleteData::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AutoCompleteData>() {
                    @Override
                    public void onCompleted() {
                        progressActivity.showContent();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AutoCompleteData autoCompleteData) {
                        mAutoCompleteData = autoCompleteData;
                        setAdapters();
                    }
                });

    }

    private void updateeducationdetails(EducationDetails educationDetails) {
        prepService.UpdateYCurrentEducation(educationDetails).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject;
                        String result = response.body().string();

                        try {
                            jsonObject = new JSONObject(result.toLowerCase());
                            Integer sucess = Integer.valueOf((jsonObject.getString("value")));
                            if (sucess > 0) {
                                finish();
                                Toast.makeText(UpdateEducation.this, "Update sucessfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UpdateEducation.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadinglayout.setVisibility(View.GONE);
                btnupdate.setVisibility(View.VISIBLE);
                Toast.makeText(UpdateEducation.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

