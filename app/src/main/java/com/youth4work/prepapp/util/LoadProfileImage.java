package com.youth4work.prepapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public LoadProfileImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Nullable
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

