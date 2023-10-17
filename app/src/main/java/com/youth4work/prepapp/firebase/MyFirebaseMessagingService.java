package com.youth4work.prepapp.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.ui.startup.SplashActivity;
import com.youth4work.prepapp.util.PreferencesManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;


/**
 * Created by Youth4Work on 27-Sep-16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Calling method to generate notification
        sendNotification(remoteMessage);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(RemoteMessage message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        Bundle data = new Bundle();
        for (Map.Entry<String, String> entry : message.getData().entrySet()) {
            data.putString(entry.getKey(), entry.getValue());
        }

        intent.putExtra("push", data);
        String msg;
        Bitmap imageurl = null;
        if (data.size() > 0) {
            String Url = data.getString("deeplinkurl");
            intent.putExtra("deeplinkurl", Url);
            String imageUri = data.getString("imageurl");
            if(imageUri!=null && !imageUri.equals(""))
                imageurl = getBitmapfromUrl(imageUri);
        }
        if (message.getNotification() != null) {
            msg = message.getNotification().getBody();
        } else if(data.getString("body")!=null) {
            msg = data.getString("body");
        }else{
            msg = data.getString("message");
        }
        Random r = new Random();
        int actID = r.nextInt(1000);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, actID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //  int Icon = R.mipmap.ic_launcher; //R.mipmap.ic_launcher;
        Bitmap appIcon = null;
        try {

            appIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        }
        catch (Exception ignored) {

        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_small_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setLargeIcon(appIcon)
                .setAutoCancel(true)
                .setLights(Color.GREEN, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (imageurl != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(imageurl));
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(actID, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppEventsLogger.setPushNotificationsRegistrationId(s);
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + s);
        PreferencesManager mPreferencesManager = PreferencesManager.instance(this);
        mPreferencesManager.setGCMToken(s);
        Log.e("NEW_TOKEN",s);
    }

}