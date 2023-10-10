package com.capstone.videoeffect.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class Glob {
    public static String Edit_Folder_name = "PixelEffect";
    public static final float HUE_CYAN = 180.0f;
    public static final float HUE_MAGENTA = 300.0f;
    public static final float HUE_VIOLET = 270.0f;
    public static ArrayList<String> IMAGEALLARY = new ArrayList();
    public static String package_name = "https://play.google.com/store/apps/details?id=";
    public static String app_link = package_name;
    public static String app_name = "Pixel Effect";
    public static String privacy_link = "http://photovideozone.com/admin/policy.html";
    public static Bitmap txtBitmap;

    public static boolean createDirIfNotExists(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), "/" + path);
        if (file.exists()) {
            System.out.println("Can't create folder");
            return true;
        }
        file.mkdir();
        if (file.mkdirs()) {
            return true;
        }
        return false;
    }

    public static void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                    Log.i("Invalid Image", "Delete Image");
                } else if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                    IMAGEALLARY.add(ss);
                }
                System.out.println(ss);
            }
            return;
        }
        System.out.println("Empty Folder");
    }
}
