package com.capstone.videoeffect.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.TypedValue;

public class ImageLoadingUtils {
    private Context context;

    public ImageLoadingUtils(Context context) {
        this.context = context;
    }

    public int convertDipToPixels(float dips) {
        return (int) TypedValue.applyDimension(1, dips, this.context.getResources().getDisplayMetrics());
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public Bitmap decodeBitmapFromPath(String filePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, convertDipToPixels(150.0f), convertDipToPixels(200.0f));
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
