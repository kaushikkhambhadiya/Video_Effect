package com.capstone.videoeffect;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.capstone.videoeffect.Adapters.CreationAdapter;
import com.capstone.videoeffect.utils.Glob;

import java.io.File;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MyCreationActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 5;
    private ImageView bback;
    private CreationAdapter galleryAdapter;
    private GridView lstList;
    private ImageView noImage;



    class C13481 implements OnClickListener {
        C13481() {
        }

        public void onClick(View v) {
            MyCreationActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_my_creation);
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        bindView();
    }

    private void bindView() {
        this.noImage = (ImageView) findViewById(R.id.novideoimg);
        this.lstList = (GridView) findViewById(R.id.lstList);
        getImages();
        if (Glob.IMAGEALLARY.size() <= 0) {
            this.noImage.setVisibility(View.VISIBLE);
            this.lstList.setVisibility(View.GONE);
        } else {
            this.noImage.setVisibility(View.GONE);
            this.lstList.setVisibility(View.VISIBLE);
        }
        this.bback = (ImageView) findViewById(R.id.back);
        this.bback.setOnClickListener(new C13481());
        Collections.sort(Glob.IMAGEALLARY);
        Collections.reverse(Glob.IMAGEALLARY);
        this.galleryAdapter = new CreationAdapter(this, Glob.IMAGEALLARY);
        this.lstList.setAdapter(this.galleryAdapter);
    }

    private void getImages() {
        if (VERSION.SDK_INT < 23) {
            fetchImage();
        } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == getPackageManager().PERMISSION_GRANTED) {
            fetchImage();
        } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != getPackageManager().PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
        }
    }

    private void fetchImage() {
        Glob.IMAGEALLARY.clear();
        Glob.listAllImages(new File("/storage/emulated/0/" + Glob.Edit_Folder_name + "/"));
    }

    public void onBackPressed() {
        finish();
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults[0] == 0) {
                    fetchImage();
                    return;
                } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != getPackageManager().PERMISSION_GRANTED) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
