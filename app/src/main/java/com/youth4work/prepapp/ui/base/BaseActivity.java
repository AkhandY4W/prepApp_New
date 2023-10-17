package com.youth4work.prepapp.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.youth4work.prepapp.PayTM.PaytmPGService;
import com.youth4work.prepapp.PayTM.PaytmStatusQuery;
import com.youth4work.prepapp.PayTM.PaytmStatusQueryCallback;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.AutoCompleteData;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.request.GcmRegister;
import com.youth4work.prepapp.network.model.request.LoginRequest;
import com.youth4work.prepapp.network.model.request.UserRegister;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.network.model.response.LoginResponse;
import com.youth4work.prepapp.ui.home.CategoryExamsActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.ui.home.NoInternetActivity;
import com.youth4work.prepapp.ui.payment.PaymentStatusActivity;
import com.youth4work.prepapp.ui.quiz.ReviewTestActivity;
import com.youth4work.prepapp.util.CheckNetwork;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;*/

public abstract class BaseActivity extends RxAppCompatActivity {

    public static PrepService prepService;
    protected static Context self;
    protected static AutoCompleteData mAutoCompleteData;
    protected static UserRegister user;
    protected PreferencesManager mPreferencesManager;
    protected static UserManager mUserManager;
    protected String email;
    static Tracker mTracker;
    String token;
    Long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        self = this;
        if(!CheckNetwork.isInternetAvailable(this)){
            {
                Intent intent=new Intent(BaseActivity.this, NoInternetActivity.class);
                startActivity(intent);
            }
        }

        mPreferencesManager = PreferencesManager.instance(this);
        mUserManager = UserManager.getInstance(this);
        Constants.logEvent4FCM(self,self.getClass().getSimpleName(),self.getClass().getSimpleName(),new Date(),"Screen","SELECT_CONTENT");
        String[] arr=mPreferencesManager.getToken();
        if(arr.length>0&&arr!=null) {
            token = arr[0];
            time = Long.valueOf(arr[1]);
        }
        PrepApplication application=(PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        if((token == null)) {
            getToken();
        }
        else {
            if(time>System.currentTimeMillis()){
                prepService=PrepApi.createService(PrepService.class,token);
            }
            else {
                getToken();
            }
        }

    }

    private void getToken() {
        LoginRequest loginRequest = new LoginRequest("YOUTH4WORKAPP", "YOUTH4WORK@14FEB");
        prepService = PrepApi.createService(PrepService.class);
        prepService.getAuth(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String authtoken = response.body().getToken();
                    mPreferencesManager.settoken(authtoken,String.valueOf(System.currentTimeMillis() +TimeUnit.MINUTES.toMillis(1440)));
                    prepService = PrepApi.createService(PrepService.class, authtoken);

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //Toast.makeText(context, "Somethig went wrong,Please try again...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public View.OnClickListener errorClickListener = v -> {
        finish();
        startActivity(getIntent());
    };


    public static void getPayment(Context mContxt, UserUpgrade userUpgrade, Boolean fromUpgrade) {
        if (userUpgrade != null) {
            if (userUpgrade.getExpiredDate() > System.currentTimeMillis()) {
                PaytmPGService service = PaytmPGService.getProductionService();
                service.queryStatus(mContxt, service, "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS", new PaytmStatusQuery(userUpgrade.getTransID(), "JagBro12772375477072"), null, new PaytmStatusQueryCallback() {
                    @Override
                    public void onStatusQueryCompleted(Bundle var1) {
                        if (var1.getString("STATUS").equals("TXN_SUCCESS") && var1.getString("TXNAMOUNT").equals(userUpgrade.getActualamount())) {
                            // PaymentStatusActivity.show(mContxt, true, "PayTM" + inResponse.getString("TXNID"), mPlan.getServiceID(), true, new Double(var1.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
                            Product product = new Product()
                                    .setId(String.valueOf(userUpgrade.getUserid()))
                                    .setName(String.valueOf(userUpgrade.getTestCategoryIdUpgrade()))
                                    .setCategory("Prep Pack")
                                    .setBrand("Youth4work")
                                    .setVariant("Prep Pack " + userUpgrade.getServiceid())
                                    .setPrice(Integer.valueOf(userUpgrade.getActualamount()))
                                    .setQuantity(1);
                            ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                                    .setTransactionId("PayTM" + userUpgrade.getTransID())
                                    .setTransactionAffiliation("Youth4work - App")
                                    .setTransactionRevenue(Integer.valueOf(userUpgrade.getActualamount()));
                            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                                    .addProduct(product)
                                    .setProductAction(productAction);

                            mTracker.setScreenName("Paytm Payment Success");
                            mTracker.send(builder.build());
                            AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(mContxt);
                            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContxt);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "PayTM" + userUpgrade.getTransID());
                            bundle1.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                            bundle1.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(userUpgrade.getActualamount()));
                            bundle1.putDouble(FirebaseAnalytics.Param.VALUE, Integer.valueOf(userUpgrade.getActualamount()));
                            bundle1.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle1);
                            Bundle bundlefb = new Bundle();
                            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
                            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "[{\"id\": \"" + userUpgrade.getServiceid() + "\", \"quantity\": 1, \"item_price\": " + userUpgrade.getActualamount() + "}]");
                            appEventsLogger.logPurchase(BigDecimal.valueOf(Long.parseLong(userUpgrade.getActualamount())), Currency.getInstance("INR"), bundlefb);
                            PreferencesManager.instance(mContxt).savePaymentDetails(null);
                            PaymentStatusActivity.show(mContxt, true, userUpgrade.getTransID(), userUpgrade.getServiceid(), true, Integer.valueOf(userUpgrade.getActualamount()), userUpgrade.getPlanValidity(), userUpgrade.getCouponCode(), userUpgrade.getTaxamount(), userUpgrade.getActualamount());

                        } else if (!fromUpgrade) {
                            PaymentStatusActivity.show(mContxt, false, userUpgrade.getTransID(), userUpgrade.getServiceid(), true, Integer.valueOf(userUpgrade.getActualamount()), userUpgrade.getPlanValidity(), userUpgrade.getCouponCode(), userUpgrade.getTaxamount(), userUpgrade.getActualamount());

                        }
                    }

                    @Override
                    public void onStatusQueryFailed(String var1) {

                    }
                });
            } else {
                PreferencesManager.instance(mContxt).savePaymentDetails(null);
            }
        }
    }

    public void SubmitMockTest(int testId, Long userid) {

        prepService.SubmitTest(testId, userid).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Toast.makeText(BaseActivity.this, "Test submit sucessfully", Toast.LENGTH_SHORT).show();
                        Calendar currentSelectedDate = Calendar.getInstance();
                        long diff = (currentSelectedDate.getTimeInMillis() - mUserManager.getStartDate().getTime());
                        String dayNo = Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        ReviewTestActivity.show(self, currentSelectedDate.getTime(), dayNo, 20);

                    } else {
                        Toast.makeText(BaseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(BaseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void AddCategoryData(int catId, Category category){
        mUserManager.getUser().setSelectedCatID(catId);
        mUserManager.setCategory(category);
        mUserManager.setUser(mUserManager.getUser());
        DashboardActivity.show(self, true);

    }
    protected void inviteFriends() {
        Constants.logEvent4FCM(self,"inviteFriends","inviteFriends",new Date(),"Screen","SELECT_CONTENT");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //String category = mUserManager.getCategory().getCategory() != null ? mUserManager.getCategory().getCategory() : "competitive exams";
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I am preparing for Online Exam Preparation with Prep App. Are you preparing for any competitive exam? With Prep App, yes you can. Download today. https://play.google.com/store/apps/details?id=com.youth4work.prepapp&referrer=utm_source%3D"+ mUserManager.getUser().getUserName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void doRegisterGcmUser(Boolean FirstCheck) {
        String deviceId = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String gcmToken = mPreferencesManager.getGCMToken();
        if (gcmToken != null && !deviceId.equals("")) {
            GcmRegister gcmRegister = new GcmRegister(FirstCheck?0:
                    mUserManager.getUser().getUserId(), deviceId,gcmToken,FirstCheck?"app@youth4work.com":
                    mUserManager.getUser().getEmailID(),getApplication().getPackageName());
            prepService.registerGcm(gcmRegister).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if (response.isSuccessful()&&FirstCheck==false) {
                        mPreferencesManager.setGCMRegistered(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract void onTransactionSuccess();

    public abstract void onTransactionSubmitted();

    public abstract void onTransactionFailed();

    public abstract void onAppNotFound();
}
