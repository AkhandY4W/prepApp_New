package com.youth4work.prepapp.ui.startup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.base.BaseActivity;
import com.youth4work.prepapp.util.PreferencesManager;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllWebView extends BaseActivity {
    WebView mWebView;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = AllWebView.class.getSimpleName();
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    protected static String url4load, parentActivity;
    String userName = "", password = "";
    ProgressRelativeLayout progressActivity;
    Toolbar toolbar;
    TextView txtTitle;
    Button btnBack;
    // String[] idsToHide = { "y4wfooter", "row", "downloadAppContainer","footer-design","wrapper","alert-danger" };

    public static void LoadWebView(Context mContext, String loadURL, String parentActivityName) {
        url4load = loadURL;
        parentActivity = parentActivityName;
        Intent cv = new Intent(mContext, AllWebView.class);
        mContext.startActivity(cv);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        mWebView = findViewById(R.id.allwebview);
        progressActivity = findViewById(R.id.activity_term_and_condition);
        toolbar = findViewById(R.id.toolbar_top);
        txtTitle = findViewById(R.id.toolbar_title);
        btnBack = findViewById(R.id.btn_back);
        setSupportActionBar(toolbar);
        mWebView.bringChildToFront(toolbar);
        switch (parentActivity) {
            case "ChooseExamActivity":
                txtTitle.setText(mUserManager.getCategory().getCategory());
                break;
            case "DashboardActivity":
                txtTitle.setText(mUserManager.getCategory().getCategory());
                break;
            case "SignUpActivity":
                txtTitle.setText("Terms And Conditions");
                break;
            case "RegisterationActivity":
                txtTitle.setText("Terms And Conditions");
                break;
            case "VerificationActivity":
                txtTitle.setText("Edit Profile");
                break;
            default:
                txtTitle.setText(parentActivity);
        }
        btnBack.setOnClickListener(v -> finish());
        progressActivity.showLoading();
        String[] arr = PreferencesManager.instance(AllWebView.this).loadPreferences();
        if (arr.length > 0 && arr != null) {
            userName = arr[0];
            password = arr[1];
        }
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          // TODO Auto-generated method stub

                                         /* for (String s : idsToHide) {
                                              String surveyId = s;
                                              view.loadUrl("javascript:disableSection('" + surveyId + "');");
                                          }*/
                                          mWebView.loadUrl("javascript:(function() {document.getElementsByClassName('alert-danger')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsByClassName('y4wfooter')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsByClassName('downloadAppContainer')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsById('footer-design')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsById('IdActiveInfo')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsByClassName('wrapper')[0].style.display='none';})()");
                                          mWebView.loadUrl("javascript:(function() {document.getElementsByClassName('col-md-4')[0].style.display='none';})()");
                                          /*"document.getElementsByClassName('y4wfooter')[0].style.display='none';" +
                                            "document.getElementsByClassName('row')[0].style.display='none';" +
                                            "document.getElementsByClassName('downloadAppContainer')[0].style.display='none';" +
                                            "document.getElementsByClassName('footer-design')[0].style.display='none';" +
                                            "document.getElementsByClassName('wrapper')[0].style.display='none'" +*/

                                          super.onPageFinished(view, url);
                                          Handler handler = new Handler();
                                          handler.postDelayed(() ->
                                                  progressActivity.showContent(), 5000);
                                      }

                                      @SuppressWarnings("deprecation")
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          if (url.startsWith("mailto:")) {
                                              //Handle mail Urls
                                              startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                                          } else if (url.startsWith("tel:")) {
                                              //Handle telephony Urls
                                              startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                                          } else {
                                              view.loadUrl(url);
                                          }
                                          return true;
                                      }

                                      @TargetApi(Build.VERSION_CODES.N)
                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                          final Uri uri = request.getUrl();
                                          if (uri.toString().startsWith("mailto:")) {
                                              //Handle mail Urls
                                              startActivity(new Intent(Intent.ACTION_SENDTO, uri));
                                          } else if (uri.toString().startsWith("tel:")) {
                                              //Handle telephony Urls
                                              startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                          } else {
                                              //Handle Web Urls
                                              view.loadUrl(uri.toString());
                                          }
                                          return true;
                                      }
                                  }
        );
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        if (!userName.equals(" ") && !password.equals(" ")) {
            String postData = "doOperation=login&txtEmail=" + userName + "&txtPwd=" + password;
            try {
                mWebView.postUrl("https://www.youth4work.com/Users/Login?returnurl=" + URLEncoder.encode(url4load, "UTF-8"), EncodingUtils.getBytes(postData, "BASE64"));
            } catch (UnsupportedEncodingException e) {
                mWebView.loadUrl(url4load);
                e.printStackTrace();
            }
        } else {
            mWebView.loadUrl(url4load);
        }
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                // Create AndroidExampleFolder at sdcard
                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + "IMG_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[]{captureIntent});
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

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
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }
}
