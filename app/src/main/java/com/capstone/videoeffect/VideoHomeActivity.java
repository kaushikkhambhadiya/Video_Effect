package com.capstone.videoeffect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimVideo;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class VideoHomeActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private static final int REQUEST_VIDEO_RECORD = 101;
    LinearLayout uploadVideo, recordvideo, myvideo;
    ImageView ivback;
    public static final String FILEPATH = "filepath";

    Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_home);

        uploadVideo = (LinearLayout) findViewById(R.id.uploadVideo);
        recordvideo = (LinearLayout) findViewById(R.id.recordvideo);
        myvideo = (LinearLayout) findViewById(R.id.myvideo);

        ivback = findViewById(R.id.ivback);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoHomeActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        recordvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideoCapture();
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    getPermission();
                else
                    uploadVideoget();

            }
        });

        myvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(VideoHomeActivity.this, MyVideosActivity.class);
                startActivity(intent);
            }
        });

    }


    private void openVideoCapture() {

        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
            intent.putExtra("android.intent.extra.durationLimit", 30);
            startActivityForResult(intent, REQUEST_VIDEO_RECORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermission() {
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(this, readExternalStorage);
        List<String> permissions = new ArrayList<String>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {
            ActivityCompat.requestPermissions(VideoHomeActivity.this,
                    params,
                    100);
        } else
            uploadVideoget();
    }

    private void uploadVideoget() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                if (data.getData() != null) {
                    LogMessage.v("Video path:: " + data.getData());
                    openTrimActivity(String.valueOf(data.getData()));
                } else {
                    Toast.makeText(this, "video uri is null", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_VIDEO_RECORD) {
                if (data.getData() != null) {
                    LogMessage.v("Video path:: " + data.getData());
                    openTrimActivity(String.valueOf(data.getData()));
                } else {
                    Toast.makeText(this, "video uri is null", Toast.LENGTH_SHORT).show();
                }
            }

//                } else {
//                    Toast.makeText(VideoHomeActivity.this, R.string.toast_cannot_retrieve_selected_video, Toast.LENGTH_SHORT).show();
//                }
            }

            if (requestCode == TrimVideo.VIDEO_TRIMMER_REQ_CODE && data != null) {
                Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(data));
                Log.d("TAG", "Trimmed path:: " + uri);
                intent = new Intent(VideoHomeActivity.this, MainActivity.class);
                intent.putExtra(VideoHomeActivity.FILEPATH, String.valueOf(uri));
                startActivity(intent);
            }
        }

    private void openTrimActivity(String data) {
        TrimVideo.activity(data)
//                  .setCompressOption(new CompressOption()) //pass empty constructor for default compress option
                .setDestination("/storage/emulated/0/DCIM/VideoEffect")
                .start(this);
    }
}
