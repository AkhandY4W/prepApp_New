package com.youth4work.prepapp.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.ChooseExamActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.home.MyPrepActivity;
import com.youth4work.prepapp.ui.views.PrepButton;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.Keyboard;
import com.youth4work.prepapp.util.PreferencesManager;
import com.youth4work.prepapp.util.PrepDialogsUtils2Kt;
import com.youth4work.prepapp.util.Toaster;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements Validator.ValidationListener, View.OnTouchListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400;
    private static final int FORGOT_PASSWORD = 5000;
    @Nullable
    @NotEmpty(sequence = 1)
    // @Email(sequence = 2)
    @BindView(R.id.txt_email)
    EditText txtEmail;
    @Nullable
    @NotEmpty(sequence = 3, messageResId = R.string.password_empty)
    @Password
    @BindView(R.id.txt_password)
    EditText txtPassword;
    @Nullable
    @BindView(R.id.btn_sign_in)
    PrepButton btnSignIn;
    @Nullable
    @BindView(R.id.btn_google_login)
    PrepButton btnGoogleLogin;
    @Nullable
    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @Nullable
    @BindView(R.id.txt_new_user_sign_up)
    TextView txtNewUserSignUp;
    @Nullable
    @BindView(R.id.btn_sign_in_plus)
    SignInButton mGbtnSignIn;
    /*@BindView(R.id.btn_facebook_login)
    LoginButton loginButtonfb;*/
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressActivity;
    private Validator validator;
    // Google client to interact with Google API
    // private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private Tracker mTracker;
  /*private static final String EMAIL = "email";
    CallbackManager callbackManager;*/
  @BindView(R.id.login_frame)
  FrameLayout frameLayout;

    @BindView(R.id.sign_in)
    TextView txtSigninFrame;

    @BindView(R.id.sign_up)
    TextView txtSignUPFrame;
    public static void show(@NonNull Context fromActivity) {
        Intent intent = new Intent(fromActivity, LoginActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Constants.sendScreenImageName(mTracker, "Login Activity");
        //Google plus button touch
        btnGoogleLogin.setOnTouchListener(this);
        txtEmail.setText(email);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();*/

        Keyboard.hideSoftInput(txtEmail);
        validator = new Validator(this);
        validator.setValidationListener(this);
        if (mPreferencesManager.isNewUser())
            doRegisterGcmUser(true);
        //callbackManager = CallbackManager.Factory.create();
        txtSigninFrame.setOnClickListener(view -> {
            txtForgotPassword.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            txtNewUserSignUp.setVisibility(View.GONE);
            txtSigninFrame.setVisibility(View.GONE);
            txtSignUPFrame.setVisibility(View.VISIBLE);
        });

        txtSignUPFrame.setOnClickListener(view -> {
            Intent intent=new Intent(LoginActivity.this, RegisterationActivity.class);
            startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    private void signInWithGplus() {
        /*if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }*/
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                //mGoogleApiClient.connect();
            }
        }
    }

    @OnClick(R.id.btn_sign_in)
    void onSignInClicked() {
        validateAndLogin();

    }

    /*@OnClick(R.id.btn_facebook_login)
    void onFacebookLoginClicked() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @OnClick(R.id.btn_google_login)
    void onGoogleLoginClicked() {

    }*/

    @OnClick(R.id.txt_forgot_password)
    void onForgotPasswordClicked() {

        ForgotPasswordActivity.show(this);
    }

    @OnClick(R.id.txt_new_user_sign_up)
    void onNewUserSignUpClicked() {
        startActivity(new Intent(LoginActivity.this,RegisterationActivity.class));

    }

    private void validateAndLogin() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        progressActivity.showLoading();
        if (txtEmail != null && txtPassword != null) {
            prepService.login(txtEmail.getText().toString(), txtPassword.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body().getName() != null) {
                        if(response.body().getUserType().equals("ST"))
                        {
                            UserManager.getInstance(LoginActivity.this).setUser(response.body());
                            PreferencesManager.instance(LoginActivity.this).saveLoginDetails(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());
                            mTracker.set("&uid", String.valueOf(mUserManager.getUser().getUserId()));
                            mTracker.send(new HitBuilders.EventBuilder()
                                 .setCategory("UX")
                                 .setAction("User Sign In")
                                 .build());
                            Constants.logLoginEventFb(mUserManager.getUser().getUserId(), mUserManager.getUser().getUserName(), LoginActivity.this);
                             progressActivity.showContent();
                             LoginActivity.this.finish();
                             startActivity(new Intent(LoginActivity.this, MyPrepActivity.class));
                             doRegisterGcmUser(false);
                            /* prepService.getMyPrepList(response.body().getUserId(), 1, 100).enqueue(new Callback<List<Category>>() {
                                 @Override
                                 public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                                     if (response.isSuccessful())
                                         mPreferencesManager.setMYPrepList(response.body());
                                     for (Category category : response.body()) {
                                         if (category.getCatid() == mUserManager.getUser().getSelectedCatID()) {
                                             mUserManager.setCategory(category);
                                         }
                                     }

                                     LoginActivity.this.finish();
                                     progressActivity.showContent();
                                     DashboardActivity.show(self, true);
                                 }

                                 @Override
                                 public void onFailure(Call<List<Category>> call, Throwable t) {

                                 }
                             });*/
                        }
                        else {
                            progressActivity.showContent();
                            PrepDialogsUtils2Kt.DownLoadCompanyApp(LoginActivity.this);
                        }
                    } else {
                        progressActivity.showContent();
                        Toaster.showLong(LoginActivity.this, "Invalid email or password!");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onValidationFailed(@NonNull List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

   /* @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        //getProfileInformation();

        // Update the UI after signin
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
       // mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

           /* if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }*/
        } else if (requestCode == FORGOT_PASSWORD) {
            String email = data != null ? data.getStringExtra("email") : "";
            txtEmail.setText(email);
            txtPassword.setSelection(0);
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

        } else {
            btnSignIn.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

   /* private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(PrepApplication.TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl);
                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

                final String finalPersonPhotoUrl = personPhotoUrl;
                prepService.socialLogin(currentPerson.getId(), "gp").enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, @NonNull Response<User> response) {
                        if (response.body().getName() != null) {
                            UserManager.getInstance(LoginActivity.this).setUser(response.body());
                            LoginActivity.this.finish();
                            progressActivity.showContent();
                            if (mUserManager.getUser().getSelectedCatID() == 0) {
                                ChooseExamActivity.show(LoginActivity.this);
                            } else {
                                prepService.getMyPrepList(mUserManager.getUser().getUserId(), 1, 100).enqueue(new Callback<List<Category>>() {
                                    @Override
                                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                                        mPreferencesManager.setMYPrepList(response.body());
                                        for (Category category : response.body()) {
                                            if (category.getCatid() == mUserManager.getCategory().getCatid()) {
                                                mUserManager.setCategory(category);
                                            }
                                        }

                                        DashboardActivity.show(self,true);
                                    }

                                    @Override
                                    public void onFailure(Call<List<Category>> call, Throwable t) {

                                    }
                                });
                            }
                            doRegisterGcmUser();
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id", currentPerson.getId());
                                jsonObject.put("email", email);
                                jsonObject.put("name", personName);
                                jsonObject.put("personPhotoUrl", finalPersonPhotoUrl);
                                jsonObject.put("aboutme", currentPerson.getAboutMe());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SignUpActivity.show(LoginActivity.this, personName, email, "gp", currentPerson.getId(), jsonObject.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


   /* @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(PrepApplication.TAG, "onTouch() called with: " + "v = [" + v + "], event = [" + event + "]");
        signInWithGplus();
        mGbtnSignIn.performClick();
        return false;
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(PrepApplication.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }*/

}
