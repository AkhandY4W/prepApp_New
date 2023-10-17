package com.youth4work.prepapp.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.util.PreferencesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserManager {

    @NonNull
    private static UserManager ourInstance = new UserManager();
    private static Context mContext;
    private User user;
    private Category category;

    private UserManager() {
    }

    @NonNull
    public static UserManager getInstance(Context context) {
        mContext = context;
        return ourInstance;
    }

    public User getUser() {
        if (user == null)
            user = new Gson().fromJson(PreferencesManager.instance(mContext).getUserJSON(), User.class);

        return user;
    }

    public void setUser(User user) {
        PreferencesManager.instance(mContext).setUserJSON(new Gson().toJson(user));
        this.user = user;
    }

    public Date getEndDate() {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(this.getUser().getPlanEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String formattedDate = outputFormat.format(date);
        // System.out.println(formattedDate);
        return date;
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(this.getUser().getPlanEndDate()));
        return calendar.getTime();
    */}

    public Date  getStartDate() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(this.getUser().getPlanStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Date formattedDate = outputFormat.format(date);
        // System.out.println(formattedDate);
        return date;


        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(this.getUser().getPlanStartDate()));
        return calendar.getTime();*/
    }

    public Category getCategory() {
        if (category == null)
            category = new Gson().fromJson(PreferencesManager.instance(mContext).getCategoryJSON(), Category.class);
        return category;
    }

    public void setCategory(Category category) {
        PreferencesManager.instance(mContext).setCategoryJSON(new Gson().toJson(category));
        this.category = category;
    }

    public boolean isUserLoggedIn() {
        return getUser() != null;
    }

    public int getQuestionsCount() {
        return PreferencesManager.instance(mContext).getQuestionsCount();
    }

    public void incrementQuestionsCount() {
        int currentCount = getQuestionsCount();
        PreferencesManager.instance(mContext).setQuestionsCount(currentCount + 1);
    }
}
