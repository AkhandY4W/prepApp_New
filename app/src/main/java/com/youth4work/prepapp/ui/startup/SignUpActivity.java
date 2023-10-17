package com.youth4work.prepapp.ui.startup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.network.model.request.UserRegister;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.Toaster;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements Validator.ValidationListener,LocationListener {

        private static final String EXTRA_NAME = "extra_name";
        private static final String EXTRA_EMAIL = "extra_email";
        private static final String EXTRA_SOCIAL_TYPE = "extra_social_type";
        private static final String EXTRA_SOCIAL_ID = "extra_social_id";
        private static final String EXTRA_SOCIAL_INFO = "extra_social_info";

        @NotEmpty(sequence = 0)
        @Order(1)
        @BindView(R.id.et_full_name)
        MaterialEditText etFullName;

        @NotEmpty(sequence = 1)
        @Order(2)
        @BindView(R.id.et_username)
        MaterialEditText etUsername;

        @NotEmpty(sequence = 3)
        @Order(3)
        @Email(sequence = 4)
        @BindView(R.id.et_email)
        MaterialEditText etEmail;

        @NotEmpty(sequence = 5)
        @Password(sequence = 6)
        @Order(4)
        @BindView(R.id.et_password)
        MaterialEditText etPassword;

        @NotEmpty(sequence = 8)
        @Order(6)
        @BindView(R.id.et_mobile)
        MaterialEditText etMobile;

        @BindView(R.id.sv)
        ScrollView sv;

        private Tracker mTracker;

        @BindView(R.id.check_box)
        CheckBox checkAgreement;

        @BindView(R.id.progressActivity)
        ProgressRelativeLayout progressActivity;

        @BindView(R.id.termAndCondition)
        TextView termAndCondition;

        private Validator validator;

        String emailfrommobile = "";

        String mobilenofrommobile = "";

        String finallocation="Other";

        LocationManager locationManager;

        @BindView(R.id.loadinglayout)
        LinearLayout loadinglayout;

        @BindView(R.id.btn_next_signup)
        PrepButton btnNextSignup;

        private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT = 1;
        private boolean isSocialRegister = false;
        private String mSocialType, mSocialId, mSocialInformation;

        public static void show(@NonNull Context fromActivity) {
            Intent intent = new Intent(fromActivity, SignUpActivity.class);
            fromActivity.startActivity(intent);
        }

        public static void show(@NonNull Context fromActivity, String name, String email, String socialType, String socialId, String socialInfo) {
            Intent intent = new Intent(fromActivity, SignUpActivity.class);
            intent.putExtra(EXTRA_NAME, name);
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_SOCIAL_TYPE, socialType);
            intent.putExtra(EXTRA_SOCIAL_ID, socialId);
            intent.putExtra(EXTRA_SOCIAL_INFO, socialInfo);
            fromActivity.startActivity(intent);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ButterKnife.bind(this);
            //getAutoCompleteData();
            initBundleAndFillForm();
            PrepApplication application = (PrepApplication) getApplication();
            mTracker = application.getDefaultTracker();
            Constants.sendScreenImageName(mTracker, "SignUp");
            Button yourButton = findViewById(R.id.yourButton);

            yourButton.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            });

            termAndCondition.setOnClickListener(v ->
                    AllWebView.LoadWebView(SignUpActivity.this, "https://www.youth4work.com/terms","SignUpActivity"));

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
            etUsername.setFilters(new InputFilter[]{
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
                    (ContextCompat.checkSelfPermission(SignUpActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(SignUpActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED)) {
                    //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT);
            } else {
                etEmail.setText(Constants.getPrimaryEmailID(this));
                emailfrommobile = etEmail.getText().toString().trim();
                etMobile.setText(Constants.getMobileNo(this));
                mobilenofrommobile = etMobile.getText().toString().trim();
                getLocation();
            }

            setupValidations();
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
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_READ_CONTACTS_and_ACCOUNT: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 2) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                            etEmail.setText(Constants.getPrimaryEmailID(this));
                        if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                            etMobile.setText(Constants.getMobileNo(this));
                        if(grantResults[2]==PackageManager.PERMISSION_GRANTED)
                            getLocation();
                    }
                    return;
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }

        }

        private void setupValidations() {
            validator.put(etUsername, usernameValidationRule);
            validator.put(etEmail, emailValidationRule);
            validator.put(etMobile, mobileValidationRule);
            validator.put(etFullName, fullNameValidationRule);
        }

        private void initBundleAndFillForm() {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                isSocialRegister = true;
                etFullName.setText(bundle.getString(EXTRA_NAME));
                etEmail.setText(bundle.getString(EXTRA_EMAIL));
                mSocialType = bundle.getString(EXTRA_SOCIAL_TYPE);
                mSocialId = bundle.getString(EXTRA_SOCIAL_ID);
                mSocialInformation = bundle.getString(EXTRA_SOCIAL_INFO);
            }
        }
      /*  protected void getAutoCompleteData() {
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
                        }
                    });
        }*/
        private void validateAndSignUp() {

            validator.validate(true);

        }

        @Override
        public void onValidationSucceeded() {

             /* if (isSocialRegister) {
            registerSocialUser();
            } else {*/
            btnNextSignup.setVisibility(View.GONE);
            loadinglayout.setVisibility(View.VISIBLE);
            loadinglayout.bringToFront();
            registerUser();
            }
       /*private void registerSocialUser() {
            UserRegisterSocial user = new UserRegisterSocial(etFullName.getText().toString(),
                    etUsername.getText().toString(),
                    etPassword.getText().toString(),
                    "ST",
                    etEmail.getText().toString(),
                    etMobile.getText().toString(),
                    "A", "Others", "Others", finallocation, "Others", "Others", "Others",true,
                    mSocialType,
                    mSocialId,
                    mSocialInformation);
            progressActivity.showLoading();
            prepService.registerSocial(user).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    progressActivity.showContent();
                    if (response.isSuccessful()) {
                        Toaster.showLong(SignUpActivity.this, "Registration complete! Please login.");
                        SignUpActivity.this.finish();
                        LoginActivity.show(SignUpActivity.this);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressActivity.showContent();
                }
            });

        }*/

        @Override
        public void onValidationFailed(@NonNull List<ValidationError> errors) {

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

        private void registerUser() {
            user = new UserRegister();
            if (user != null) {
                String finalmobileno = etMobile.getText().toString().trim();
                String finalEmailid = etEmail.getText().toString().trim();
                String ustatus = emailfrommobile.equals(finalEmailid) ? "A" : "P";
                Boolean mobilestatus = mobilenofrommobile.equals(finalmobileno) ? true : false;
                user.setData(etFullName.getText().toString(),
                        etUsername.getText().toString(),
                        etPassword.getText().toString().replace(" ", ""),
                        "ST",
                        etEmail.getText().toString(),
                        etMobile.getText().toString().replace("+91", ""),
                        ustatus, "Other", "Other", finallocation, "Other", "Other", "Other",mobilestatus);
                prepService.register(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        progressActivity.showContent();
                        loadinglayout.setVisibility(View.GONE);
                        btnNextSignup.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null) {
                            UserManager.getInstance(SignUpActivity.this).setUser(response.body());
                            //progressActivity.showContent();

                      /*  Intercom.client().registerIdentifiedUser(new Registration().withUserId(Integer.toString(response.body().getUserId())));
                        DashboardActivity.show(self, mUserManager.getCategory());*/
                            doRegisterGcmUser(false);
                            Toaster.showLong(SignUpActivity.this, "Registration complete!");
                            finish();
                            Intent intent = new Intent(SignUpActivity.this, SplashActivity.class);
                            AppEventsLogger logger = AppEventsLogger.newLogger(SignUpActivity.this);
                            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        loadinglayout.setVisibility(View.GONE);
                        btnNextSignup.setVisibility(View.VISIBLE);
                        progressActivity.showContent();
                    }
                });
            }
        }

            @OnClick(R.id.btn_next_signup)
            public void onClick () {
                if (!checkAgreement.isChecked()) {
                    Toast.makeText(SignUpActivity.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                    checkAgreement.requestFocus();
                } else {
                    Toaster.showShort(this, "Wait...");
                    validateAndSignUp();
                }
            }

            private QuickRule fullNameValidationRule = new QuickRule<MaterialEditText>(15) {

                @Override
                public boolean isValid(MaterialEditText materialEditText) {
                    boolean isValid = true;
                    String name = materialEditText.getText().toString().trim();
                    if (name.contains(" ") && (name.charAt(name.length() - 1)) != ' ') {
                        String split[] = name.split("\\s+");
                        if (split[0].length() > 1 && split[1].length() > 0) {
                            isValid = true;
                        } else {
                            isValid = false;
                        }
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


                    return isValid;
                }

                @Override
                public String getMessage(Context context) {
                    return "Email already registered!";
                }
            };

            private QuickRule mobileValidationRule = new QuickRule<MaterialEditText>(16) {

                @Override
                public boolean isValid(MaterialEditText materialEditText) {
                    boolean isValid = true;
                    String mobile = materialEditText.getText().toString().trim().replace("+91", "");

                    if (mobile != null && mobile.length() > 0) {
                        //   Toaster.showShort(SignUpActivity.this, "Validating Mobile...");
                        //String responseString;
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
            String msg="";
            @Override
            public boolean isValid(MaterialEditText materialEditText) {
                boolean isValid = true;
                String username = materialEditText.getText().toString().trim();
                if ( username!=null && username.length() > 5 ) {
                    // Toaster.showShort(SignUpActivity.this, "Validating UserName...");
                    String responseString;
                    try {
                        responseString = prepService.usernameExists(username).execute().body().string();
                        if(responseString!=null)
                            if (responseString.equalsIgnoreCase("\"0\"")) {
                                isValid = true;
                            } else if (responseString.equalsIgnoreCase("\"1\"")) {
                                isValid = false;
                                msg="Username already exists ! Try different !";
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    msg="Username has length of less than 6 characters !";
                    isValid = false;
                }

                return isValid;
            }

            @Override
            public String getMessage(Context context) {
                return msg;
            }
        };


        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
        void getLocation() {
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, SignUpActivity.this);
            }
            catch(SecurityException e) {
                e.printStackTrace();
            }
        }
       @Override
       public void onLocationChanged(Location location) {
           //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

           try {
               Geocoder geocoder = new Geocoder(this, Locale.getDefault());
               List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
               if (addresses != null && addresses.size()>0) {
                  //locationText.setText(addresses.get(0).getLocality());
                   finallocation=addresses.get(0).getLocality().trim().equals("")?addresses.get(0).getLocality().trim():finallocation;
               }
           }catch(Exception e)
           {

           }

       }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(SignUpActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

}
