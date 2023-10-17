/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youth4work.prepapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.File;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.util.Log.DEBUG;

public class DecodeBitmapTask extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = "DecodeBitmapTask";
    private static final byte[] BUFFER = new byte[16 * 1024];
    protected final int maxWidth;
    protected final int maxHeight;
    protected final String path;

    public DecodeBitmapTask(final int maxWidth, final int maxHeight,
                            final String path) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.path = path;
    }

    public static final int getByteCount(@NonNull final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Nullable
    protected Bitmap decode() {
        final Options options = new Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            Log.d(TAG, "Decoding bounds of " + path + " failed");
            new File(path).delete();
            return null;
        }

        if (isCancelled())
            return null;

        int scale = 1;
        if (options.outWidth > maxWidth || options.outHeight > maxHeight)
            scale = Math.max(
                    Math.round((float) options.outHeight / (float) maxHeight),
                    Math.round((float) options.outWidth / (float) maxWidth));

        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        options.inPreferredConfig = ARGB_8888;
        options.inPurgeable = true;

        Bitmap decoded;
        synchronized (BUFFER) {
            options.inTempStorage = BUFFER;
            decoded = BitmapFactory.decodeFile(path, options);
        }

        if (Log.isLoggable(TAG, DEBUG)) {
            if (decoded == null) {
                Log.d(TAG, "Decoding " + path + " failed");
                new File(path).delete();
            } else
                Log.d(TAG,
                        "Decoded to " + decoded.getWidth() + "x" + decoded.getHeight()
                                + " from max size: " + maxWidth + "x" + maxHeight
                                + " using scale:" + scale + " and byte count:"
                                + getByteCount(decoded));
        }

        return decoded;
    }

    protected boolean pathExists(@NonNull final String path) {
        File file = new File(path);
        return file.exists() && file.length() > 0;
    }

    @Nullable
    protected Bitmap load() {
        if (pathExists(path))
            return decode();
        else
            return null;
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(final Void... params) {
        if (!isCancelled())
            return load();
        else
            return null;
    }

    public String getPath() {
        return path;
    }
}
