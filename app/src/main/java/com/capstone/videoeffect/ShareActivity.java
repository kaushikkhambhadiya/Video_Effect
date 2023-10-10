package com.capstone.videoeffect;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.videoeffect.utils.Glob;

import java.io.File;
import java.util.ArrayList;

public class ShareActivity extends Activity implements OnClickListener {

    private ImageView finalimg;
    private ImageView home;
    private ImageView ic_back;
    private TextView ic_path;
    private ImageView iv_facebook;
    private ImageView iv_instagram;
    private ImageView iv_whatsapp;
    private ArrayList<String> listIcon = new ArrayList();
    private ArrayList<String> listName = new ArrayList();
    private ArrayList<String> listUrl = new ArrayList();

    class C13491 implements OnClickListener {
        C13491() {
        }

        public void onClick(View v) {
            try {
                ShareActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) ShareActivity.this.listUrl.get(0))));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ShareActivity.this, "You don't have Google Play installed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class C13502 implements OnClickListener {
        C13502() {
        }

        public void onClick(View v) {
            try {
                ShareActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) ShareActivity.this.listUrl.get(1))));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ShareActivity.this, "You don't have Google Play installed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class C13513 implements OnClickListener {
        C13513() {
        }

        public void onClick(View v) {
            try {
                ShareActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String) ShareActivity.this.listUrl.get(2))));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ShareActivity.this, "You don't have Google Play installed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        bindview();
    }

    private void bindview() {

        this.ic_back = (ImageView) findViewById(R.id.ic_back);
        this.ic_back.setOnClickListener(this);
        this.finalimg = (ImageView) findViewById(R.id.finalimg);
        this.finalimg.setImageBitmap(ImageEditingActivity.finalEditedImage);
        this.ic_path = (TextView) findViewById(R.id.ic_path);
        this.ic_path.setText(ImageEditingActivity.urlForShareImage);
        this.home = (ImageView) findViewById(R.id.home);
        this.home.setOnClickListener(this);
        this.iv_whatsapp = (ImageView) findViewById(R.id.iv_whatsapp);
        this.iv_whatsapp.setOnClickListener(this);
        this.iv_instagram = (ImageView) findViewById(R.id.iv_instagram);
        this.iv_instagram.setOnClickListener(this);
        this.iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        this.iv_facebook.setOnClickListener(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }


    public void onClick(View view) {

        Uri image = Uri.fromFile(new File(ImageEditingActivity.urlForShareImage));
        Log.i("image_url", String.valueOf(image));

        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        shareIntent.putExtra("android.intent.extra.TEXT", Glob.app_name + " Created By : " + Glob.app_link+getApplicationContext().getPackageName());
        shareIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(ImageEditingActivity.urlForShareImage)));
        switch (view.getId()) {
            case R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("ToHome", true);
                startActivity(intent);
                finish();
                return;
            case R.id.ic_back:
                finish();
                return;
            case R.id.iv_whatsapp:
                try {
                    startActivity(shareIntent);
                    return;
                } catch (Exception e) {
                    Toast.makeText(this, "No share app installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.iv_facebook:
                try {
                    shareIntent.setPackage("com.facebook.katana");
                    startActivity(shareIntent);
                    return;
                } catch (Exception e3) {
                    Toast.makeText(this, "Facebook doesn't installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.iv_instagram:
                try {
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);
                    return;
                } catch (Exception e4) {
                    Toast.makeText(this, "Instagram doesn't installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            default:
                return;
        }
    }
}

