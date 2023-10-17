package com.youth4work.prepapp.ui.startup;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.AutoCompleteData;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.network.model.request.UserRegister;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.Toaster;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterationActivity extends BaseActivity implements Validator.ValidationListener, View.OnClickListener {
    private static final String TAG = "LOCATION_TAG";
    TextView txtTermsCondition;
    CheckBox checkBox;
    Boolean isChecked = false;
    Button btnNext, btnBack, btnSeePass, btnSignUP;
    RelativeLayout regLayoutPhase1, regLayoutPhase2;
    @Order(1)
    MaterialEditText etFullName;
    @Order(2)
    MaterialEditText etEmail;
    @Order(3)
    MaterialEditText etUserName;
    @NotEmpty(sequence = 5)
    @Password(sequence = 6, message = "Password must have atleast 6 characters")

    @Order(4)
    MaterialEditText etPassword;
    @NotEmpty(sequence = 8)
    @Order(5)
    MaterialEditText etMobile;
    MaterialEditText etBatchTo, etBatchFrom;
    MaterialAutoCompleteTextView etLocation, etSpec, etDegree, etCollege;
    ScrollView sv;
    ProgressRelativeLayout progressActivity;

    protected static AutoCompleteData mAutoCompleteData;
    private Tracker mTracker;
    private Validator validator;
    String emailfrommobile = "";
    String mobilenofrommobile = "";
    int startYear = 0, endYear = 0;
    private Toast mToast;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT = 1;
    long animationDuration = 1000;
    private FusedLocationProviderClient fusedLocationClient;
    Location mLastLocation;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_register);
        txtTermsCondition = findViewById(R.id.txt_terms);
        checkBox = findViewById(R.id.check_box);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        btnSeePass = findViewById(R.id.yourButton);
        btnSignUP = findViewById(R.id.btn_signup);
        etEmail = findViewById(R.id.et_email);
        etFullName = findViewById(R.id.et_full_name);
        etMobile = findViewById(R.id.et_mobile);
        etPassword = findViewById(R.id.et_password);
        etUserName = findViewById(R.id.et_user_name);
        etCollege = findViewById(R.id.et_college);
        etDegree = findViewById(R.id.et_degree);
        etLocation = findViewById(R.id.et_location);
        etSpec = findViewById(R.id.et_spec);
        etBatchFrom = findViewById(R.id.et_batch_from);
        etBatchTo = findViewById(R.id.et_batch_to);
        btnNext = findViewById(R.id.btn_next);
        regLayoutPhase1 = findViewById(R.id.register_phase1_layout);
        regLayoutPhase2 = findViewById(R.id.register_phase2_layout);
        sv = findViewById(R.id.scroll);
        progressActivity = findViewById(R.id.progressActivity);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSignUP.setOnClickListener(this);
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);
        etCollege.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etCollege.setSelection(etCollege.getText().toString().trim().length());
            } else {
                etCollege.setSelection(0);
            }
        });
        etDegree.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etDegree.setSelection(etDegree.getText().toString().trim().length());
            } else {
                etDegree.setSelection(0);
            }
        });
        etSpec.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etSpec.setSelection(etSpec.getText().toString().trim().length());
            } else {
                etSpec.setSelection(0);
            }
        });
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "SignUp");
        String text = "I have read & agree to Youth4work terms & conditions";
        SpannableString ss = new SpannableString(text);
        if (mAutoCompleteData != null)
            setAdapters();
        else
            getAutoCompleteData();
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.setValidationMode(Validator.Mode.IMMEDIATE);
        etFullName.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
        etUserName.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9_]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
        etEmail.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9_@.]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                (ContextCompat.checkSelfPermission(RegisterationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(RegisterationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT);
        } else {
            etEmail.setText(Constants.getPrimaryEmailID(this));
            emailfrommobile = etEmail.getText().toString().trim();
           // etMobile.setText(Constants.getMobileNo(this));
            mobilenofrommobile = etMobile.getText().toString().trim();
            getLocation();
        }
        setupValidations();
        btnSeePass.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnSeePass.setBackground(getResources().getDrawable(R.drawable.assets_01_ic_view));
                    break;
                case MotionEvent.ACTION_UP:
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnSeePass.setBackground(getResources().getDrawable(R.drawable.ic_password_view_off_24dp));
                    break;
            }
            return true;
        });
        etBatchFrom.setInputType(InputType.TYPE_NULL);
        etBatchTo.setInputType(InputType.TYPE_NULL);
        etBatchFrom.setOnFocusChangeListener((View view, boolean b) -> {
            if (etBatchFrom.hasFocus())
                setupDialog(etBatchFrom);
            etBatchFrom.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etBatchFrom.getWindowToken(), 0);

        });
        etBatchTo.setOnFocusChangeListener((view, b) -> {
            if (etBatchTo.hasFocus())
                setupDialog(etBatchTo);
            etBatchTo.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etBatchTo.getWindowToken(), 0);

        });
        txtTermsCondition.setOnClickListener(v -> {
            if (!isChecked && !checkBox.isChecked()) {
                isChecked = true;
                checkBox.setChecked(isChecked);
            } else {
                isChecked = false;
                checkBox.setChecked(isChecked);
            }
        });
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                AllWebView.LoadWebView(RegisterationActivity.this, "https://www.youth4work.com/terms", "RegisterationActivity");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 34, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTermsCondition.setText(ss);
        txtTermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                validateAndNextPhase();
                break;

            case R.id.btn_signup:
                RegisterUser();
                break;

            case R.id.btn_back:
                btnNext.setVisibility(View.VISIBLE);
                btnSignUP.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                slideDown(regLayoutPhase2);
                slideUp(regLayoutPhase1);
                //viewGoneAnimator(regLayoutPhase2);
                //viewVisibleAnimator(regLayoutPhase1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void setupValidations() {
        validator.put(etUserName, usernameValidationRule);
        validator.put(etEmail, emailValidationRule);
        validator.put(etMobile, mobileValidationRule);
        validator.put(etFullName, fullNameValidationRule);
    }

    private QuickRule fullNameValidationRule = new QuickRule<MaterialEditText>(15) {

        @Override
        public boolean isValid(MaterialEditText materialEditText) {
            boolean isValid = true;
            String name = materialEditText.getText().toString().trim();
            if (name.contains(" ") && (name.charAt(name.length() - 1)) != ' ') {
                String split[] = name.split("\\s+");
                isValid = split[0].length() > 1 && split[1].length() > 0;
            } else {
                isValid = false;
            }
            return isValid;
        }

        @Override
        public String getMessage(Context context) {
            return "Write your full name";
        }
    };
    private QuickRule emailValidationRule = new QuickRule<MaterialEditText>(15) {

        @Override
        public boolean isValid(MaterialEditText materialEditText) {
            boolean isValid = true;
            String email = materialEditText.getText().toString().trim();
            if (isValidEmail(email)) {
                if (email != null && email.length() > 0) {
                    String responseString;
                    //  Toaster.showShort(SignUpActivity.this, "Validating Email...");
                    try {
                        responseString = prepService.emailIDExists(email).execute().body().string();
                        if (responseString != null)
                            if (responseString.equalsIgnoreCase("\"0\"")) {
                                isValid = true;

                            } else if (responseString.equalsIgnoreCase("\"1\"")) {
                                isValid = false;
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                isValid = false;
            }
            return isValid;
        }

        @Override
        public String getMessage(Context context) {
            return (!isValidEmail(etEmail.getText().toString().trim())) ? "Please enter correct email" : "Email already registered!";
        }
    };

    private QuickRule mobileValidationRule = new QuickRule<MaterialEditText>(16) {

        @Override
        public boolean isValid(MaterialEditText materialEditText) {
            boolean isValid = true;
            String mobile = materialEditText.getText().toString().trim().replace("+", "");

            if (mobile != null && mobile.length() > 0) {
                try {
                    String responseString = prepService.mobileExists(mobile).execute().body().string();
                    if (responseString != null)
                        if (responseString.equalsIgnoreCase("\"0\"")) {
                            isValid = true;
                        } else if (responseString.contains("1")) {
                            isValid = false;
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return isValid;
        }

        @Override
        public String getMessage(Context context) {
            return "Mobile number already registered!";
        }
    };

    private QuickRule usernameValidationRule = new QuickRule<MaterialEditText>(17) {
        String msg = "";

        @Override
        public boolean isValid(MaterialEditText materialEditText) {
            boolean isValid = true;
            String username = materialEditText.getText().toString().trim();
            if (username != null && username.length() > 5) {
                // Toaster.showShort(SignUpActivity.this, "Validating UserName...");
                String responseString;
                try {
                    responseString = prepService.usernameExists(username).execute().body().string();
                    if (responseString != null)
                        if (responseString.equalsIgnoreCase("\"0\"")) {
                            isValid = true;
                        } else if (responseString.equalsIgnoreCase("\"1\"")) {
                            isValid = false;
                            msg = "Username already exists ! Try different !";
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                msg = "Username must have atleast 6 characters!";
                isValid = false;
            }

            return isValid;
        }

        @Override
        public String getMessage(Context context) {
            return msg;
        }
    };


    private void RegisterUser() {
        if (etCollege.getText().toString().trim().length() <= 0) {
            etCollege.setError("College Not be Empty");
            etCollege.requestFocus();
        } else if (etDegree.getText().toString().trim().length() <= 0) {
            etDegree.setError("Degree Not be Empty");
            etDegree.requestFocus();
        } else if (etSpec.getText().toString().trim().length() <= 0) {
            etSpec.setError("Specialization Not be Empty");
            etSpec.requestFocus();
        } else if (etLocation.getText().toString().trim().length() <= 0) {
            etLocation.setError("Location Not be Empty");
            etLocation.requestFocus();
        } else if (etBatchFrom.getText().toString().trim().length() <= 0) {
            etBatchFrom.setError("Batch From Not be Empty");
            etBatchFrom.requestFocus();
        } else if (etBatchTo.getText().toString().trim().length() <= 0) {
            etBatchTo.setError("Batch To Not be Empty");
            etBatchTo.requestFocus();
        } else if (startYear > endYear) {
            showToast("Batch from should be less than batch to");
        } else if (!checkBox.isChecked()) {
            Toast.makeText(RegisterationActivity.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            checkBox.requestFocus();
        } else {
            progressActivity.showLoading();
            user = new UserRegister();
            if (user != null) {
                String finalmobileno = etMobile.getText().toString().trim();
                String finalEmailid = etEmail.getText().toString().trim();
                String ustatus = emailfrommobile.equals(finalEmailid) ? "A" : "P";
                Boolean mobilestatus = mobilenofrommobile.equals(finalmobileno);
                user.setData(etFullName.getText().toString(),
                        etUserName.getText().toString(),
                        etPassword.getText().toString().replace(" ", ""),
                        "ST",
                        etEmail.getText().toString(),
                        etMobile.getText().toString().replace("+1", "").replace("+91", "").replace("+", ""),
                        ustatus, String.valueOf(startYear),
                        String.valueOf(endYear),
                        etLocation.getText().toString().trim(),
                        etDegree.getText().toString().trim(),
                        etSpec.getText().toString().trim(),
                        etCollege.getText().toString().trim(), mobilestatus);
                //Log.e("UserJson",user.toString());
                prepService.register(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserManager.getInstance(RegisterationActivity.this).setUser(response.body());
                            doRegisterGcmUser(false);
                            PreferencesManager.instance(RegisterationActivity.this).saveLoginDetails(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                            progressActivity.showContent();
                            Toaster.showLong(RegisterationActivity.this, "Registration complete!");
                            finish();
                            Intent intent = new Intent(RegisterationActivity.this, SplashActivity.class);
                            AppEventsLogger logger = AppEventsLogger.newLogger(RegisterationActivity.this);
                            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressActivity.showContent();
                        Toast.makeText(RegisterationActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onValidationSucceeded() {


        slideDown(regLayoutPhase1);
        slideUp(regLayoutPhase2);
        /*viewGoneAnimator(regLayoutPhase1);
        viewVisibleAnimator(regLayoutPhase2);*/
        btnSignUP.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof MaterialEditText) {
                ((MaterialEditText) view).setError(message);
                view.requestFocus();
                sv.scrollTo(0, view.getTop());
            } else if (view instanceof MaterialAutoCompleteTextView) {
                ((MaterialAutoCompleteTextView) view).setError(message);
                view.requestFocus();
                sv.scrollTo(0, view.getTop());
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        etEmail.setText(Constants.getPrimaryEmailID(this));
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                      //  etMobile.setText(Constants.getMobileNo(this));
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED)
                        getLocation();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                mLastLocation = location;
                setAddress(location);
            }
        });

    }
    private void setAddress(Location location) {
        Geocoder geocoder = new Geocoder(RegisterationActivity.this,
                Locale.getDefault());
        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address
                    1);
             } catch (IOException ioException) {
            // Catch network or other I/O problems
            resultMessage = RegisterationActivity.this
                    .getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);
        }

        if (addresses == null || addresses.size() == 0) {
            if (resultMessage.isEmpty()) {
                resultMessage = RegisterationActivity.this
                        .getString(R.string.no_address_found);
                Log.e(TAG, resultMessage);
            }
        } else {
            Address address = addresses.get(0);
            //StringBuilder out = new StringBuilder();
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread
           /* for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                out.append(address.getAddressLine(i));
            }*/
                       resultMessage = !address.getLocality().trim().equals("") ?address.getLocality().trim():resultMessage;

        }
        etLocation.setText(resultMessage);
    }
    private void validateAndNextPhase() {

        validator.validate(true);


    }

    private void setAdapters() {
        etCollege.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getColleges()));
        etLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getCities()));
        etSpec.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getSpecializations()));
        etDegree.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, mAutoCompleteData.getDegrees()));
    }

    protected void getAutoCompleteData() {

        Observable.zip(prepService.colleges(), prepService.degrees(), prepService.specializations(), prepService.cities(), AutoCompleteData::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AutoCompleteData>() {
                    @Override
                    public void onCompleted() {

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

    void setupDialog(MaterialEditText editText) {
        Button btnCancle, btnOk;
        NumberPicker numberPicker, numberPicker1;
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.date_picker);
        btnCancle = myDialog.findViewById(R.id.btn_cancel);
        btnOk = myDialog.findViewById(R.id.btn_ok);
        numberPicker = myDialog.findViewById(R.id.numberPicker);
        numberPicker1 = myDialog.findViewById(R.id.numberPicker2);
        numberPicker.setMinValue(0);
        numberPicker1.setMinValue(1970);
        numberPicker.setMaxValue(11);
        numberPicker1.setMaxValue(2025);
        numberPicker.setDisplayedValues(months);
        myDialog.show();
        btnOk.setOnClickListener(v -> {
            if (editText.getId() == R.id.et_batch_from)
                startYear = numberPicker1.getValue();
            else
                endYear = numberPicker1.getValue();
            showToast(" " + months[(numberPicker.getValue())] + ", " + numberPicker1.getValue());
            editText.setText(months[(numberPicker.getValue())] + ", " + numberPicker1.getValue());
            myDialog.dismiss();
        });
        btnCancle.setOnClickListener(v ->

                myDialog.dismiss());

    }

    private void showToast(String message) {

        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void viewGoneAnimator(View view) {

        view.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    private void viewVisibleAnimator(View view) {

        view.animate()
                .alpha(.5f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

    }

    // slide the view from below itself to the current position
    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth(),                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                view.getWidth(),// toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}
