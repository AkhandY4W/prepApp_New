package com.youth4work.prepapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.Section;
import com.youth4work.prepapp.network.model.Subject;

import java.util.List;

//import io.intercom.android.sdk.Intercom;

public class PreferencesManager {

    private static final String USER_PREF_FILE_NAME = "UserDataPrefs";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static PreferencesManager sInstance;
    private SharedPreferences mPreferences;
    private Editor mEditor;
    private PreferencesManager(@NonNull Context ctx) {
        mPreferences = ctx.getSharedPreferences(USER_PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static PreferencesManager instance(@NonNull Context ctx) {
        if (sInstance == null)
            sInstance = new PreferencesManager(ctx);
        return sInstance;
    }

    @Nullable
    public String getUserJSON() {
        return mPreferences.getString(Keys.USER, null);
    }

    public void setUserJSON(String userJson) {
        mEditor.putString(Keys.USER, userJson).apply();
    }

    @Nullable
    public String getGCMToken() {
        return mPreferences.getString(Keys.GCM_TOKEN, null);
    }

    public void setGCMToken(String token) {
        mEditor.putString(Keys.GCM_TOKEN, token).apply();
    }
    @Nullable
    public String getid() {
        return mPreferences.getString(PreferencesManager.Keys.USERID, null);
    }


    public void setid(String userJson) {
        mEditor.putString(PreferencesManager.Keys.USERID, userJson).apply();
    }

    public void setGCMRegistered(boolean isRegistered) {
        mEditor.putBoolean(Keys.GCM_REGISTERED, isRegistered).apply();
    }

    public boolean isGCMRegistered(boolean isRegistered) {
        return mPreferences.getBoolean(Keys.GCM_REGISTERED, false);
    }


    @Nullable
    public String[] getToken() {
        //return mPreferences.getString(Keys.TOKEN, null);
        String[] arr = new String[2];
        // Get value
        arr[0]=  mPreferences.getString(Keys.TOKEN, " ");
        arr[1]  = mPreferences.getString(Keys.TIME,  "052665");
        return arr;
    }

    public void settoken(String userJson,String  time) {
        mEditor.putString(Keys.TOKEN, userJson);
        mEditor.putString(Keys.TIME,time);
        mEditor.apply();
    }
    public void saveLoginDetails(String userName, String password) {
        //fill input boxes with stored login and pass
        mEditor.putString(PREF_UNAME, userName);
        mEditor.putString(PREF_PASSWORD, password);
        mEditor.commit();
    }

    public String[] loadPreferences() {
        String[] arr = new String[2];
        // Get value
        arr[0]=  mPreferences.getString(PREF_UNAME, " ");
        arr[1]  = mPreferences.getString(PREF_PASSWORD, " ");
        return arr;
    }
    public void clearAllUserData() {
        String gcmToken = getGCMToken();

        setUserJSON(null);
        setCategoryJSON(null);
        setGCMToken(null);
        mEditor.remove(Keys.USER);
        mEditor.remove(Keys.CATEGORY);
        mEditor.remove(Keys.GCM_REGISTERED);
        mEditor.remove(Keys.GCM_TOKEN);
        mEditor.remove(PREF_PASSWORD);
        mEditor.remove(PREF_UNAME);
        mEditor.clear();
        mEditor.commit();
        mEditor.clear().apply();

        // This reset's the Intercom SDK's cache of your user's identity and wipes the slate clean.
        // io.intercom.android.sdk.Intercom.client().reset();
        // Now that you have logged your user out and reset, you can register a new
        // unidentified user in their place.
        // io.intercom.android.sdk.Intercom.client().registerUnidentifiedUser();
        setisNewUser(false);
        setGCMToken(gcmToken);


    }

    @Nullable
    public String getCategoryJSON() {
        return mPreferences.getString(Keys.CATEGORY, null);
    }

    public void setCategoryJSON(String categoryJson) {
        mEditor.putString(Keys.CATEGORY, categoryJson).apply();
    }

    public void setMYPrepList(List<Category> categories) {
        mEditor.putString(Keys.MY_EXAMS, new Gson().toJson(categories)).apply();
    }

    public void setMyTopicsList(List<Subject> subjects) {
        if (subjects == null)
            mEditor.remove(Keys.MY_SUBJECTS);
        else
            mEditor.putString(Keys.MY_SUBJECTS, new Gson().toJson(subjects)).apply();

    }

    public List<Subject> getMyTopicsList() {
        return new Gson().fromJson(mPreferences.getString(Keys.MY_SUBJECTS, null), new TypeToken<List<Subject>>() {
        }.getType());
    }

    public void setMySectionList(List<Section> subjects) {
        if (subjects == null)
            mEditor.remove(Keys.MY_SUBJECTS);
        else
            mEditor.putString(Keys.MY_SUBJECTS, new Gson().toJson(subjects)).apply();

    }

    public List<Section> getMySectionList() {
        return new Gson().fromJson(mPreferences.getString(Keys.MY_SUBJECTS, null), new TypeToken<List<Section>>() {
        }.getType());
    }

    public List<Category> getMYPrepList() {
        return new Gson().fromJson(mPreferences.getString(Keys.MY_EXAMS, null), new TypeToken<List<Category>>() {
        }.getType());
    }
    public void savePaymentDetails(String userUpgrade)
    {
        mEditor.putString(Keys.PAYMENT,userUpgrade).apply();
    }
    public String getPendingPaymentDetails()
    {
        return mPreferences.getString(Keys.PAYMENT,null);
    }
    public int getQuestionsCount() {
        return mPreferences.getInt(Keys.QUESTIONS_COUNT, 0);
    }

    public void setQuestionsCount(int questionCount) {
        mEditor.putInt(Keys.QUESTIONS_COUNT, questionCount).apply();
    }

    private interface Keys {
        String USER = "user";
        String GCM_TOKEN = "gcm_token";
        String GCM_REGISTERED = "gcm_registered";
        String CATEGORY = "category";
        String MY_EXAMS = "my_exams";
        String MY_SUBJECTS = "my_subjects";
        String QUESTIONS_COUNT = "questions_count";
        String isNewUser = "isNewUser";
        String TOKEN = "token";
        String USERID = "userid";
        String TIME = "time";
        String PAYMENT="payment";
    }

    public void setisNewUser(boolean isNewUser) {
        mEditor.putBoolean(Keys.isNewUser, isNewUser).apply();
    }

    public boolean isNewUser() {
        return mPreferences.getBoolean(Keys.isNewUser, true);
    }
}
