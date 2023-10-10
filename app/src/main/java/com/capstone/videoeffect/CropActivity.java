package com.capstone.videoeffect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.capstone.videoeffect.utils.ImageLoadingUtils;
import com.edmodo.cropper.CropImageView;


public class CropActivity extends Activity implements OnClickListener {
    RelativeLayout back_main;
    LinearLayout btnCrop16_9;
    LinearLayout btnCrop4_3;
    LinearLayout btnCrop4_5;
    LinearLayout btnCrop5_6;
    LinearLayout btnCropCustom;
    LinearLayout btnCropOriginal;
    LinearLayout btnCropSquare;
    CropImageView cropView;
    TextView custom_size;

    TextView org_size;
    RelativeLayout rLayout;
    TextView size4_3;
    TextView size_16_9;
    TextView size_4_5;
    TextView size_5_6;
    TextView square_size;
    ImageLoadingUtils utils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Log.d("myname1", String.valueOf(PhotoHomeActivity.bitmap.getHeight()));
        Log.d("myname2", String.valueOf(PhotoHomeActivity.bitmap.getWidth()));

        this.cropView = (CropImageView) findViewById(R.id.CropImageView);
        this.cropView.setFixedAspectRatio(true);
        this.cropView.setAspectRatio(1, 1);
        this.utils = new ImageLoadingUtils(getApplicationContext());
        this.cropView.setImageBitmap(PhotoHomeActivity.bitmap);
        this.rLayout = (RelativeLayout) findViewById(R.id.next_main);

        this.back_main = (RelativeLayout) findViewById(R.id.back_main);

        this.org_size = (TextView) findViewById(R.id.org_size);
        this.square_size = (TextView) findViewById(R.id.square_size);
        this.custom_size = (TextView) findViewById(R.id.custom_size);
        this.size_4_5 = (TextView) findViewById(R.id.size_4_5);
        this.size_5_6 = (TextView) findViewById(R.id.size_5_6);
        this.size_16_9 = (TextView) findViewById(R.id.size_16_9);
        this.size4_3 = (TextView) findViewById(R.id.size4_3);
        this.btnCropOriginal = (LinearLayout) findViewById(R.id.btnCropOriginal);
        this.btnCropSquare = (LinearLayout) findViewById(R.id.btnCropSquare);
        this.btnCrop4_5 = (LinearLayout) findViewById(R.id.btnCrop4_5);
        this.btnCrop5_6 = (LinearLayout) findViewById(R.id.btnCrop5_6);
        this.btnCrop4_3 = (LinearLayout) findViewById(R.id.btnCrop4_3);
        this.btnCropCustom = (LinearLayout) findViewById(R.id.btnCropCustom);
        this.btnCrop16_9 = (LinearLayout) findViewById(R.id.btnCrop16_9);


        back_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CropActivity.this.onBackPressed();
            }
        });

        rLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoHomeActivity.bitmap = CropActivity.this.cropView.getCroppedImage();
                CropActivity.this.startActivity(new Intent(CropActivity.this, ImageEditingActivity.class));
                CropActivity.this.finish();
            }
        });
    }


    protected void onResume() {
        super.onResume();
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCropOriginal:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(PhotoHomeActivity.bitmap.getWidth(), PhotoHomeActivity.bitmap.getHeight());
                changebackground(R.id.btnCropOriginal);
                return;
            case R.id.btnCropSquare:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(1, 1);
                changebackground(R.id.btnCropSquare);
                return;
            case R.id.btnCropCustom:
                this.cropView.setFixedAspectRatio(false);
                changebackground(R.id.btnCropCustom);
                return;
            case R.id.btnCrop4_5:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(4, 5);
                changebackground(R.id.btnCrop4_5);
                return;
            case R.id.btnCrop5_6:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(5, 6);
                changebackground(R.id.btnCrop5_6);
                return;
            case R.id.btnCrop16_9:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(16, 9);
                changebackground(R.id.btnCrop16_9);
                return;
            case R.id.btnCrop4_3:
                this.cropView.setFixedAspectRatio(true);
                this.cropView.setAspectRatio(4, 3);
                changebackground(R.id.btnCrop4_3);
                return;
            default:
                return;
        }
    }

    private void changebackground(int id) {
        this.btnCropOriginal.setBackgroundColor(0);
        this.btnCropSquare.setBackgroundColor(0);
        this.btnCrop4_5.setBackgroundColor(0);
        this.btnCrop5_6.setBackgroundColor(0);
        this.btnCrop4_3.setBackgroundColor(0);
        this.btnCrop16_9.setBackgroundColor(0);
        this.btnCropCustom.setBackgroundColor(0);
        this.org_size.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.square_size.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.custom_size.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.size_4_5.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.size_5_6.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.size_16_9.setTextColor(getResources().getColor(R.color.crop_op_text));
        this.size4_3.setTextColor(getResources().getColor(R.color.crop_op_text));
        switch (id) {
            case R.id.btnCropOriginal:
                this.btnCropOriginal.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.org_size.setTextColor(-1);
                return;
            case R.id.btnCropSquare:
                this.btnCropSquare.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.square_size.setTextColor(-1);
                return;
            case R.id.btnCropCustom:
                this.btnCropCustom.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.custom_size.setTextColor(-1);
                return;
            case R.id.btnCrop4_5:
                this.btnCrop4_5.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.size_4_5.setTextColor(-1);
                return;
            case R.id.btnCrop5_6:
                this.btnCrop5_6.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.size_5_6.setTextColor(-1);
                return;
            case R.id.btnCrop16_9:
                this.btnCrop16_9.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.size_16_9.setTextColor(-1);
                return;
            case R.id.btnCrop4_3:
                this.btnCrop4_3.setBackgroundColor(getResources().getColor(R.color.crop_op_bg));
                this.size4_3.setTextColor(-1);
                return;
            default:
                return;
        }
    }
}
