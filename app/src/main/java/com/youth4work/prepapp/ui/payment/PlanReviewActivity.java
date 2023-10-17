package com.youth4work.prepapp.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
//import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
//import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
//import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
//import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;
import com.youth4work.prepapp.BuildConfig;
import com.youth4work.prepapp.PayTM.PaytmCancel;
import com.youth4work.prepapp.PayTM.PaytmCancelTransaction;
import com.youth4work.prepapp.PayTM.PaytmMerchant;
import com.youth4work.prepapp.PayTM.PaytmOrder;
import com.youth4work.prepapp.PayTM.PaytmPGService;
import com.youth4work.prepapp.PayTM.PaytmPaymentTransactionCallback;
import com.youth4work.prepapp.PayTM.PaytmStatusQuery;
import com.youth4work.prepapp.PayTM.PaytmStatusQueryCallback;
import com.youth4work.prepapp.PrepApplication;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Plan;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.ui.home.DashboardActivity;
import com.youth4work.prepapp.util.PreferencesManager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.google.android.gms.analytics.Tracker;

public class PlanReviewActivity extends BaseActivity implements PaymentStatusListener {

    public static final String EXTRA_PLAN = "extra_plan";
    /*  @Nullable
      @BindView(R.id.toolbar)
      Toolbar toolbar;*/
    @Nullable
    @BindView(R.id.txt_plan_name)
    TextView txtPlanName;
    @Nullable
    @BindView(R.id.txt_plan_price)
    IconTextView txtPlanPrice;
    @Nullable
    @BindView(R.id.txt_plan_description)
    TextView txtPlanDescription;
    @Nullable
    @BindView(R.id.lyt_payment_details)
    RelativeLayout lytPaymentDetails;
    @Nullable
    @BindView(R.id.viewSep)
    View viewSep;
    @Nullable
    @BindView(R.id.txt_total_label)
    TextView txtTotalLabel;
    @Nullable
    @BindView(R.id.txt_total_price)
    IconTextView txtTotalPrice;
    @Nullable
    @BindView(R.id.payment_cv)
    CardView paymentCv;
    @Nullable
    @BindView(R.id.txt_personal_details)
    TextView txtPersonalDetails;
    @Nullable
    @BindView(R.id.txt_name)
    TextView txtName;
    @Nullable
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @Nullable
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @Nullable
    @BindView(R.id.lyt_personal_details)
    LinearLayout lytPersonalDetails;
    @Nullable
    @BindView(R.id.personal_details_cv)
    CardView personalDetailsCv;

    private Plan mPlan;
    String orderId;
    @BindView(R.id.pay_with_paytm)
    Button payWithPaytm;
    private Tracker mTracker;
    private static int planValidity;
    private static final int TEZ_REQUEST_CODE = 123;
    ImageView imgGPay;
    Button btnGooglePay, btnZeroUpdate;
    TextView txtGPay, paywith;
    RelativeLayout paymentGatwayLayout;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    final int UPI_PAYMENT = 0;
    String couponCode="";
    int taxAmount;
    PaymentApp paymentApp;
    private EasyUpiPayment easyUpiPayment;
    public static final String  FAILURE="FAILURE";
    public static final String  SUBMITTED="SUBMITTED";
    public static final String SUCCESS = "SUCCESS";
    String transId="";
    public static void show(@NonNull Context fromActivity, Plan selectedPlan, String couponcode) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(fromActivity);
        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(fromActivity);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(selectedPlan.getServiceID()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedPlan.getServiceName());
        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(selectedPlan.getDisAmount()));
        bundle.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Prep Pack");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);
        Bundle bundlefb = new Bundle();
        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, selectedPlan.getServiceName());
        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, String.valueOf(selectedPlan.getServiceID()));
        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, selectedPlan.getDisAmount(), bundlefb);
        Intent intent = new Intent(fromActivity, PlanReviewActivity.class);
        intent.putExtra(EXTRA_PLAN, new Gson().toJson(selectedPlan));
        intent.putExtra("couponcode", couponcode);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_plan_review);
        ButterKnife.bind(this);
        txtGPay = findViewById(R.id.textView6);
        btnGooglePay = findViewById(R.id.btn_google_pay);
        btnZeroUpdate = findViewById(R.id.btn_zero_update);
        paywith = findViewById(R.id.paywith);
        imgGPay = findViewById(R.id.imageView4);
        paymentGatwayLayout = findViewById(R.id.payment_gatway_layout);
        boolean isAppInstalled = appInstalledOrNot(GOOGLE_TEZ_PACKAGE_NAME);
        Iconify
                .with(new FontAwesomeModule());
        couponCode=getIntent().getStringExtra("couponcode");
        PrepApplication application = (PrepApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mPlan = new Gson().fromJson(getIntent().getStringExtra(EXTRA_PLAN), Plan.class);
        Product product = new Product()
                .setId(mUserManager.getCategory() != null ? String.valueOf(mUserManager.getCategory().getCatid()) : "0")
                .setName(mUserManager.getCategory() != null ? mUserManager.getCategory().getCategory() : "Unknown")
                .setCategory("Prep Pack")
                .setBrand("Youth4work")
                .setVariant(mPlan.getServiceName())
                .setPrice(mPlan.getAmount())
                .setQuantity(1);
        ProductAction productAction = new ProductAction(ProductAction.ACTION_ADD);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);
        mTracker.setScreenName("PlanReview Activity");
        mTracker.send(builder.build());
        paymentApp=PaymentApp.GOOGLE_PAY;
        initPlan();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnGooglePay.setOnClickListener(v -> {

            if(isAppInstalled)
                payUsingUpi(mPlan.getDisAmount()+".00", "youth4work@okicici", "Youth4work Apps", mPlan.getServiceName());
            else{
                Toast.makeText(PlanReviewActivity.this, "Error Ocurred", Toast.LENGTH_SHORT).show();
            }
        });
        if (mPlan.getDisAmount() == 0) {
            paymentGatwayLayout.setVisibility(View.GONE);
            paywith.setVisibility(View.GONE);
            btnZeroUpdate.setVisibility(View.VISIBLE);
        }
        payWithPaytm.setOnClickListener(view ->
                onStartTransaction(view));

    }

    public void payUsingUpi(String amount, String upiId, String name, String note) {
        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with()
                .setPayeeVpa(upiId)
                .setPayeeName(name)
                .setTransactionId(orderId)
                .setTransactionRefId(orderId)
                .setDescription(note)
                .setAmount(amount);
        // END INITIALIZATION

        try {
            // Build instance
            easyUpiPayment = builder.build();
            easyUpiPayment.setPaymentStatusListener(this);
            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(PlanReviewActivity.this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void ZeroUpdate(View view) {
        updatePlan(mPlan.getServiceID(), getIntent().getStringExtra("couponcode"), 0);
    }



    @Override
    protected void onStart() {
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initPlan() {
        int disAmount=mPlan.getDisAmount();
        mPlan.setDisAmount((int) (Math.round(mPlan.getDisAmount() * 1.18)));
        taxAmount=mPlan.getDisAmount()-disAmount;
        txtPlanName.setText(mPlan.getServiceName());
        txtPlanDescription.setText(mPlan.getServiceDesc());
        txtPlanPrice.setText("{fa-inr} " + (mPlan.getDisAmount() + " only"));
        txtTotalPrice.setText("{fa-inr} " + (mPlan.getDisAmount()) + " only");
        txtName.setText(mUserManager.getUser().getName());
        txtPhone.setText(mUserManager.getUser().getContactNo());
        txtEmail.setText(mUserManager.getUser().getEmailID());
        planValidity = mPlan.getValidity();
    }

    private void updatePlan(int serviceid, String ccode, int amt) {
        //Long userid, int amount, int serviceid, String transID, String appName, String couponCode, String actualamount, String appVersionId, String domainName, String testCategoryIdUpgrade, String taxamount
        String currentVersion = null;
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName().trim(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        UserUpgrade userUpgrade = new UserUpgrade(mUserManager.getUser().getUserId(), amt, serviceid, ccode,  getApplicationContext().getPackageName(),couponCode,String.valueOf(mPlan.getAmount()),currentVersion,"",mUserManager.getUser().getSelectedCatID(),"0");
        prepService.doUpgradeOn100pDis(userUpgrade).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mUserManager.getUser().setPrepPlanID(serviceid);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Calendar calendar = Calendar.getInstance((Locale.ENGLISH));
                try {
                    calendar.setTime(sdf.parse(mUserManager.getUser().getPlanStartDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.add(Calendar.DATE, planValidity);
                mUserManager.getUser().setPlanEndDate(sdf.format(calendar.getTime()));
                mUserManager.setUser(mUserManager.getUser());
                Intent intent = new Intent(PlanReviewActivity.this, DashboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PlanReviewActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String GenrateOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "Prep" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        return orderId;
    }

    private void initOrderId() {
        GenrateOrderId();
        EditText orderIdEditText = findViewById(R.id.order_id);
        EditText transactionAmount = findViewById(R.id.transaction_amount);
        EditText merchantIdEditText = findViewById(R.id.merchant_id);
        EditText customerIdEditText = findViewById(R.id.customer_id);
        EditText channelIdEditText = findViewById(R.id.channel_id);
        EditText industryTypeIdEditText = findViewById(R.id.industry_type_id);
        EditText websiteEditText = findViewById(R.id.website);
        EditText theme = findViewById(R.id.theme);
        EditText custEmailIdEditText = findViewById(R.id.cust_email_id);
        EditText custMobileNoEditText = findViewById(R.id.cust_mobile_no);
        orderIdEditText.setText(orderId);
        merchantIdEditText.setText("JagBro12772375477072");// JagBro84561948078995
        customerIdEditText.setText(String.valueOf(mUserManager.getUser().getUserId()));
        channelIdEditText.setText(R.string.sample_channel_id);
        industryTypeIdEditText.setText(R.string.sample_industry_type_id);//sample_industry_type_id_staging
        websiteEditText.setText("JagBrosWAP");
        theme.setText(R.string.sample_theme);
        custEmailIdEditText.setText(mUserManager.getUser().getEmailID());
        custMobileNoEditText.setText(mUserManager.getUser().getContactNo());
        transactionAmount.setText(String.valueOf(mPlan.getDisAmount()));
        //transactionAmount.setText("1");

    }

    public void onStartTransaction(View view) {
        if (mPlan.getDisAmount() == 0) {
            updatePlan(mPlan.getServiceID(), getIntent().getStringExtra("couponcode"), 0);
        } else {
            PaytmPGService Service = PaytmPGService.getProductionService();
            Map<String, String> paramMap = new HashMap<String, String>();

            // these are mandatory parameters
            EditText orderIdEditText = findViewById(R.id.order_id);
            EditText transactionAmount = findViewById(R.id.transaction_amount);
            EditText merchantIdEditText = findViewById(R.id.merchant_id);
            EditText customerIdEditText = findViewById(R.id.customer_id);
            EditText channelIdEditText = findViewById(R.id.channel_id);
            EditText industryTypeIdEditText = findViewById(R.id.industry_type_id);
            EditText websiteEditText = findViewById(R.id.website);
            EditText theme = findViewById(R.id.theme);
            EditText custEmailIdEditText = findViewById(R.id.cust_email_id);
            EditText custMobileNoEditText = findViewById(R.id.cust_mobile_no);

            paramMap.put("ORDER_ID", (orderIdEditText).getText().toString());
            paramMap.put("MID", (merchantIdEditText).getText().toString());
            paramMap.put("CUST_ID", (customerIdEditText).getText().toString());
            paramMap.put("CHANNEL_ID", (channelIdEditText).getText().toString());
            paramMap.put("INDUSTRY_TYPE_ID", (industryTypeIdEditText).getText().toString());
            paramMap.put("WEBSITE", (websiteEditText).getText().toString());
            paramMap.put("TXN_AMOUNT", (transactionAmount).getText().toString());
            paramMap.put("THEME", (theme).getText().toString());
            paramMap.put("EMAIL", (custEmailIdEditText).getText().toString());
            paramMap.put("MOBILE_NO", (custMobileNoEditText).getText().toString());
            paramMap.put("CALLBACK_URL", "https://admin4.youth4work.com/paytm/VerifyChecksum.aspx");
            PaytmOrder Order = new PaytmOrder(paramMap);
            PaytmMerchant Merchant = new PaytmMerchant(
                    "https://admin4.youth4work.com/paytm/GenerateChecksum.aspx",
                    "https://admin4.youth4work.com/paytm/VerifyChecksum.aspx");

            Service.initialize(Order, Merchant, null);
            /*Service.queryStatus(PlanReviewActivity.this, Service, "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS", new PaytmStatusQuery("Prep100001728", "JagBro12772375477072"), null, new PaytmStatusQueryCallback() {
                @Override

                public void onStatusQueryCompleted(Bundle var1) {
                    String status=var1.getString("STATUS");
                    Toast.makeText(PlanReviewActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusQueryFailed(String var1) {

                }
            });
*/
            Service.startPaymentTransaction(this, true, true,
                    new PaytmPaymentTransactionCallback() {
                        @Override
                        public void someUIErrorOccurred(String inErrorMessage) {
                            // Some UI Error Occurred in Payment Gateway Activity.
                            // This may be due to initialization of views in
                            // Payment Gateway Activity or may be due to
                            // initialization of webview.
                            // Error Message details
                            // the error occurred.
                            Toast.makeText(PlanReviewActivity.this, "Some UI Error Occurred in Payment Gateway Activity", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onTransactionSuccess(Bundle inResponse) {
                            // After successful transaction this method gets called.
                            // Response bundle contains the merchant response
                            // parameters.
                            Log.d("LOG", "Payment Transaction is successful " + inResponse);
                            // Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                            Service.queryStatus(PlanReviewActivity.this, Service, "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS", new PaytmStatusQuery(inResponse.getString("ORDERID"), inResponse.getString("MID")), null, new PaytmStatusQueryCallback() {
                                @Override
                                public void onStatusQueryCompleted(Bundle bundle) {
                                    if (inResponse.getString("STATUS").equals(bundle.getString("STATUS")) && inResponse.getString("TXNAMOUNT").equals(bundle.getString("TXNAMOUNT"))) {
                                        Product product = new Product()
                                                .setId(String.valueOf(mUserManager.getCategory().getCatid()))
                                                .setName(mUserManager.getCategory().getCategory())
                                                .setCategory("Prep Pack")
                                                .setBrand("Youth4work")
                                                .setVariant(mPlan.getServiceName())
                                                .setPrice(mPlan.getDisAmount())
                                                .setQuantity(1);
                                        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                                                .setTransactionId("PayTM" + inResponse.getString("TXNID"))
                                                .setTransactionAffiliation("Youth4work - App")
                                                .setTransactionRevenue(mPlan.getDisAmount());
                                        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                                                .addProduct(product)
                                                .setProductAction(productAction);

                                        mTracker.setScreenName("Paytm Payment Success");
                                        mTracker.send(builder.build());
                                        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(self);
                                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(self);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "PayTM" + inResponse.getString("TXNID"));
                                        bundle1.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                                        bundle1.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(mPlan.getDisAmount()));
                                        bundle1.putDouble(FirebaseAnalytics.Param.VALUE,mPlan.getDisAmount());
                                        bundle1.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle1);
                                        Bundle bundlefb = new Bundle();
                                        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                                        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
                                        bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "[{\"id\": \"" + mPlan.getServiceID() + "\", \"quantity\": 1, \"item_price\": " + mPlan.getDisAmount() + "}]");
                                        appEventsLogger.logPurchase(BigDecimal.valueOf(mPlan.getDisAmount()), Currency.getInstance("INR"), bundlefb);
                                        PaymentStatusActivity.show(PlanReviewActivity.this, true, "PayTM" + inResponse.getString("TXNID"), mPlan.getServiceID(), true, new Double(inResponse.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
                                    } else {
                                        Service.cancelTransaction(PlanReviewActivity.this, new PaytmCancel(inResponse.getString("MID"), inResponse.getString("ORDERID")), null, new PaytmCancelTransaction() {
                                            @Override
                                            public void onCancellationSuccess() {
                                                UserUpgrade userUpgrade=new UserUpgrade(mUserManager.getUser().getUserId(),(int)(inResponse.getDouble("TXNAMOUNT")),mPlan.getServiceID(),paramMap.get("ORDER_ID"),getString(R.string.app_name),couponCode,String.valueOf(mPlan.getDisAmount()),
                                                        BuildConfig.VERSION_NAME,"",mUserManager.getUser().getSelectedCatID(),String.valueOf(taxAmount),planValidity,System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4320));
                                                PreferencesManager.instance(PlanReviewActivity.this).savePaymentDetails(new Gson().toJson(userUpgrade));
                                                PaymentStatusActivity.show(PlanReviewActivity.this, false, inResponse.getString("TXNID"), mPlan.getServiceID(), true, new Double(inResponse.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
                                            }

                                            @Override
                                            public void onCancellationFailure() {
                                                UserUpgrade userUpgrade=new UserUpgrade(mUserManager.getUser().getUserId(),(int)(inResponse.getDouble("TXNAMOUNT")),mPlan.getServiceID(),paramMap.get("ORDER_ID"),getString(R.string.app_name),couponCode,String.valueOf(mPlan.getDisAmount()),
                                                        BuildConfig.VERSION_NAME,"",mUserManager.getUser().getSelectedCatID(),String.valueOf(taxAmount),planValidity,System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4320));
                                                PreferencesManager.instance(PlanReviewActivity.this).savePaymentDetails(new Gson().toJson(userUpgrade));
                                                PaymentStatusActivity.show(PlanReviewActivity.this, false, inResponse.getString("TXNID"), mPlan.getServiceID(), true, new Double(inResponse.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onStatusQueryFailed(String s) {
                                    UserUpgrade userUpgrade=new UserUpgrade(mUserManager.getUser().getUserId(),(int)(inResponse.getDouble("TXNAMOUNT")),mPlan.getServiceID(),paramMap.get("ORDER_ID"),getString(R.string.app_name),couponCode,String.valueOf(mPlan.getDisAmount()),
                                            BuildConfig.VERSION_NAME,"",mUserManager.getUser().getSelectedCatID(),String.valueOf(taxAmount),planValidity,System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4320));
                                    PreferencesManager.instance(PlanReviewActivity.this).savePaymentDetails(new Gson().toJson(userUpgrade));
                                    PaymentStatusActivity.show(PlanReviewActivity.this, false, inResponse.getString("TXNID"), mPlan.getServiceID(), true, new Double(inResponse.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
                                }
                            });
                        }

                        @Override
                        public void onTransactionFailure(String inErrorMessage,
                                                         Bundle inResponse) {

                            // This method gets called if transaction failed. //
                            // Here in this case transaction is completed, but with
                            // a failure. // Error Message describes the reason for
                            // failure. // Response bundle contains the merchant
                            // response parameters.
                            Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                            //Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                            UserUpgrade userUpgrade=new UserUpgrade(mUserManager.getUser().getUserId(),
                                    (int)(inResponse.getDouble("TXNAMOUNT")),
                                    mPlan.getServiceID(),paramMap.get("ORDER_ID"),
                                    getString(R.string.app_name),couponCode,String.valueOf(mPlan.getDisAmount()),
                                    BuildConfig.VERSION_NAME,"",mUserManager.getUser().getSelectedCatID(),
                                    String.valueOf(taxAmount),planValidity,System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4320));

                            PreferencesManager.instance(PlanReviewActivity.this).savePaymentDetails(new Gson().toJson(userUpgrade));
                            PaymentStatusActivity.show(PlanReviewActivity.this, false, "Error:" + inErrorMessage + " for order " + paramMap.get("ORDER_ID"), mPlan.getServiceID(), true, new Double(inResponse.getString("TXNAMOUNT")).intValue(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getDisAmount()));
                        }


                        @Override
                        public void networkNotAvailable() { // If network is not
                            // available, then this
                            // method gets called.
                        }

                        @Override
                        public void clientAuthenticationFailed(String inErrorMessage) {

                            Toast.makeText(PlanReviewActivity.this, "Failure may be due to following reasons Server error or downtime.", Toast.LENGTH_LONG).show();

                            // This method gets called if client authentication
                            // failed. // Failure may be due to following reasons //
                            // 1. Server error or downtime.
                            // 2. Server unable to
                            // generate checksum or checksum response is not in
                            // proper format.
                            // 3. Server failed to authenticate
                            // that client. That is value of payt_STATUS is 2. //
                            // Error Message describes the reason for failure.
                        }

                        @Override
                        public void onErrorLoadingWebPage(int iniErrorCode,
                                                          String inErrorMessage, String inFailingUrl) {

                        }

                        // had to be added: NOTE
                        @Override
                        public void onBackPressedCancelTransaction() {
                            // TODO Auto-generated method stub
                            UserUpgrade userUpgrade=new UserUpgrade(mUserManager.getUser().getUserId(),
                                   mPlan.getDisAmount(),
                                    mPlan.getServiceID(),paramMap.get("ORDER_ID"),
                                    getString(R.string.app_name),couponCode,String.valueOf(mPlan.getDisAmount()),
                                    BuildConfig.VERSION_NAME,"",mUserManager.getUser().getSelectedCatID(),
                                    String.valueOf(taxAmount),planValidity,System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4320));

                            PreferencesManager.instance(PlanReviewActivity.this).savePaymentDetails(new Gson().toJson(userUpgrade));

                        }

                    });
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("App Not Found",e.toString());
        }

        return false;
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        //statusView.setText(transactionDetails.toString());

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                transId=transactionDetails.getTransactionId();
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
            default:
                onTransactionCancelled();
                //throw new IllegalStateException("Unexpected value: " + transactionDetails.getStatus());
        }
    }

    @Override
    public void onTransactionSuccess() {
        if (!transId.equals("")) {
            Toast.makeText(PlanReviewActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
            //    Log.d("UPI", "responseStr: " + approvalRefNo);
            String catid=mUserManager.getUser().getSelectedCatID()!=0?String.valueOf(mUserManager.getUser().getSelectedCatID()):"1";
            String catName="Prep App";
            if(mUserManager.getCategory()!=null) {
             catid= mUserManager.getCategory().getCatid() != 0 ? String.valueOf(mUserManager.getUser().getSelectedCatID()) : "1";
             catName  = mUserManager.getCategory().getCategory().equals("") ? "Prep App" : mUserManager.getCategory().getCategory();
            }
            Product product = new Product()
                    .setId(catid)
                    .setName(catName)
                    .setCategory("Prep Pack")
                    .setBrand("Youth4work")
                    .setVariant(mPlan.getServiceName())
                    .setPrice(mPlan.getDisAmount())
                    .setQuantity(1);
            ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                    .setTransactionId("UPI" + transId)
                    .setTransactionAffiliation("Youth4work - App")
                    .setTransactionRevenue(mPlan.getDisAmount());
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addProduct(product)
                    .setProductAction(productAction);
            mTracker.setScreenName("UPI Payment Success");
            mTracker.send(builder.build());
            AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(self);
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(self);
            Bundle bundle1 = new Bundle();
            bundle1.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "UPI" + transId);
            bundle1.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
            bundle1.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(mPlan.getDisAmount()));
            bundle1.putDouble(FirebaseAnalytics.Param.VALUE, mPlan.getDisAmount());
            bundle1.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle1);
            Bundle bundlefb = new Bundle();
            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
            bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "[{\"id\": \"" + mPlan.getServiceID() + "\", \"quantity\": 1, \"item_price\": " + mPlan.getDisAmount() + "}]");
            appEventsLogger.logPurchase(BigDecimal.valueOf(mPlan.getDisAmount()), Currency.getInstance("INR"), bundlefb);
            PaymentStatusActivity.show(PlanReviewActivity.this, true, "UPI" + transId, mPlan.getServiceID(), true, mPlan.getDisAmount(), mPlan.getValidity(), couponCode, String.valueOf(taxAmount), String.valueOf(mPlan.getAmount()));
        }
    }

    @Override
    public void onTransactionSubmitted() {
            Toast.makeText(PlanReviewActivity.this, "Payment is Pending | Submitted.", Toast.LENGTH_SHORT).show();
            //PaymentStatusActivity.show(PlanReviewActivity.this,false,"UPI"+txtID,mPlan.getServiceID(),true,mPlan.getDisAmount(),mPlan.getValidity());
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(PlanReviewActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
        PaymentStatusActivity.show(PlanReviewActivity.this,false,"UPI"+" ",mPlan.getServiceID()
                ,true,mPlan.getDisAmount(),mPlan.getValidity(),couponCode, String.valueOf(taxAmount), String.valueOf(mPlan.getAmount()));

    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(PlanReviewActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAppNotFound() {

    }

   /* public void payUsingUpi(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "BCR2DN6TRWCN35T7")
                .appendQueryParameter("tid", orderId)
                .appendQueryParameter("tr", orderId)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("refUrl", "https://secure.youth4work.com")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PlanReviewActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("data",data.toString());
        ArrayList<String> dataList = new ArrayList<>();
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PlanReviewActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String transId = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    } else if (equalStr[0].toLowerCase().equals("txnId".toLowerCase())) {
                        transId = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PlanReviewActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
                Product product = new Product()
                        .setId(String.valueOf(mUserManager.getCategory().getCatid()))
                        .setName(mUserManager.getCategory().getCategory())
                        .setCategory("Prep Pack")
                        .setBrand("Youth4work")
                        .setVariant(mPlan.getServiceName())
                        .setPrice(mPlan.getDisAmount())
                        .setQuantity(1);
                ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                        .setTransactionId("UPI" + transId)
                        .setTransactionAffiliation("Youth4work - App")
                        .setTransactionRevenue(mPlan.getDisAmount());
                HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                        .addProduct(product)
                        .setProductAction(productAction);
                mTracker.setScreenName("UPI Payment Success");
                mTracker.send(builder.build());
                AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(self);
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(self);
                Bundle bundle1 = new Bundle();
                bundle1.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "UPI" + transId);
                bundle1.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                bundle1.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(mPlan.getDisAmount()));
                bundle1.putDouble(FirebaseAnalytics.Param.VALUE,mPlan.getDisAmount());
                bundle1.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle1);
                Bundle bundlefb = new Bundle();
                bundlefb.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
                bundlefb.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "[{\"id\": \"" + mPlan.getServiceID() + "\", \"quantity\": 1, \"item_price\": " + mPlan.getDisAmount() + "}]");
                appEventsLogger.logPurchase(BigDecimal.valueOf(mPlan.getDisAmount()), Currency.getInstance("INR"), bundlefb);
                PaymentStatusActivity.show(PlanReviewActivity.this, true, "UPI" + transId, mPlan.getServiceID(), true, mPlan.getDisAmount(), mPlan.getValidity(),couponCode,String.valueOf(taxAmount),String.valueOf(mPlan.getAmount()));
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PlanReviewActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                //PaymentStatusActivity.show(PlanReviewActivity.this,false,"UPI"+txtID,mPlan.getServiceID(),true,mPlan.getDisAmount(),mPlan.getValidity());
            } else {
                Toast.makeText(PlanReviewActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PlanReviewActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }*/
}
