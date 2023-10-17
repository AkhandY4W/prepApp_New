package com.youth4work.prepapp.ui.startup;

/*import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;*/

public class FacebookLogin /* extends Fragment implements View.OnTouchListener*/ {
/*
    private LoginButton mButtonLogin;
    private PrepButton btnFacebookLogin;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private PrepService prepService;
    private UserManager mUserManager;

    @NonNull
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(@NonNull LoginResult loginResult) {

            Profile profile = Profile.getCurrentProfile();

            if (profile == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        loginWithFBID(currentProfile, loginResult);
                    }
                };
            } else {
                loginWithFBID(profile, loginResult);
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            Log.d(PrepApplication.TAG, "onError " + e);
        }
    };

    public FacebookLogin() {

    }

    private void loginWithFBID(Profile profile, LoginResult loginResult) {
        prepService = PrepApi.provideRetrofit();
        prepService.socialLogin(profile.getId(), "fb").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, @NonNull Response<User> response) {
                setupFBLogin(response, loginResult, profile);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setupFBLogin(@NonNull Response<User> response, @NonNull LoginResult loginResult, @NonNull Profile profile) {
        if (response.body().getName() != null) {
            UserManager.getInstance(getContext()).setUser(response.body());
            getActivity().finish();

            mUserManager = UserManager.getInstance(getContext());
            if (mUserManager.getUser().getSelectedCatID() == 0 && mUserManager.getUser().getPrepPlan().equalsIgnoreCase("Trial Plan")) {
                GettingStartedActivity.show(getActivity());
            } else if (mUserManager.getUser().getSelectedCatID() == 0) {
                ChooseExamActivity.show(getActivity());
            } else {
                if (mUserManager.getCategory() == null)
                    mUserManager.setCategory(new Category(mUserManager.getUser().getSelectedCatID()));

                DashboardActivity.show(getActivity(),true);
            }
            doRegisterGcmUser();
        } else {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    (object, fbResponse) -> {
                        try {
                            navigateToSignUp(profile.getName(), object.getString("email"), profile, object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }

    private void navigateToSignUp(String name, String email, @NonNull Profile profile, @NonNull JSONObject object) {
        SignUpActivity.show(getActivity(), name, email, "fb", profile.getId(), object.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View loginFragmentView = inflater.inflate(R.layout.facebook_login_layout, container, false);
        btnFacebookLogin = (PrepButton) loginFragmentView.findViewById(R.id.btn_facebook_login);
        btnFacebookLogin.setOnTouchListener(this);
        return loginFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupLoginButton(view);
    }

    public void doRegisterGcmUser() {
        PreferencesManager mPreferencesManager = PreferencesManager.instance(getContext());
        GcmRegister gcmRegister = new GcmRegister(mUserManager.getUser().getUserId(), "", mPreferencesManager.getGCMToken(), mUserManager.getUser().getEmailID(),getActivity().getPackageName());
        prepService.registerGcm(gcmRegister).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mPreferencesManager.setGCMRegistered(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d(PrepApplication.TAG, "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d(PrepApplication.TAG, "" + currentProfile);
            }
        };
    }

    private void setupLoginButton(@NonNull View view) {
        mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        mButtonLogin.setReadPermissions("public_profile, email");
        mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mButtonLogin.performClick();
        return false;
    }*/
}
