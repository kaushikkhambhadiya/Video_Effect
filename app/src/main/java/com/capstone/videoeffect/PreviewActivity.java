package com.capstone.videoeffect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

public class PreviewActivity extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private int stopPosition;
    private static final String POSITION = "position";
    private static final String FILEPATH = "filepath";
    TextView tvstarttime, tvendtime;
    private Handler mHandler = new Handler();
    ImageView ivplaypause;

    ImageView btcancel, btshare;
    TextView tvfilename, tvfilesize, tvduration, tvstorepath;
    String filePath;

    Boolean fromMyVideos = false;
    
    Toolbar toolbar;

    @Override
        public void onBackPressed() {
        if (fromMyVideos) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
            Intent intent = new Intent(PreviewActivity.this, VideoHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setActionBar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        toolbar = findViewById(R.id.toolbar);
        setActionBar(getResources().getString(R.string.app_name));

        Intent intent = getIntent();
        if (intent!=null) {
            fromMyVideos = intent.getBooleanExtra("fromMyVideos",false);
        }

        videoView = (VideoView) findViewById(R.id.videoView1);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        tvstarttime = findViewById(R.id.tvstarttime);
        tvendtime = findViewById(R.id.tvendtime);

        btcancel = findViewById(R.id.btcancel);
        btshare = findViewById(R.id.btshare);

        tvfilename = findViewById(R.id.tvfilename);
        tvfilesize = findViewById(R.id.tvfilesize);
        tvduration = findViewById(R.id.tvduration);
        tvstorepath = findViewById(R.id.tvstorepath);

        filePath = getIntent().getStringExtra(FILEPATH);
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        File file = new File(filePath);
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        tvfilename.setText(filename);
        tvfilesize.setText(size(file_size));
        tvstorepath.setText(filePath);

        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
//                seekBar.setMax(videoView.getDuration());
//                seekBar.postDelayed(onEverySecond, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
                videoView.seekTo(seekBar.getProgress());
                updateProgressBar();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                }
            }
        });

        updateProgressBar();


        ivplaypause = findViewById(R.id.ivplaypause);

        if (videoView.isPlaying()) {
            videoView.pause();
            ivplaypause.setImageResource(R.drawable.play);
        } else {
            videoView.start();
            ivplaypause.setImageResource(R.drawable.pause);
        }

        ivplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivplaypause.setImageResource(R.drawable.play);
                } else {
                    videoView.start();
                    ivplaypause.setImageResource(R.drawable.pause);
                }
            }
        });


        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

        btshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recommendation_text = "Download this app for creating amazing Video effects: ";

                File videoFile = new File(filePath);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", videoFile);
                ShareCompat.IntentBuilder.from(PreviewActivity.this)
                        .setStream(contentUri)
                        .setType("video/mp4")
                        .setText(recommendation_text + "https://play.google.com/store/apps/details?id=" + getPackageName())
                        .setChooserTitle("Share video...")
                        .startChooser();
            }
        });

    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {

        public void run() {
            seekBar.setProgress(videoView.getCurrentPosition());
            seekBar.setMax(videoView.getDuration());

            int elapsed = videoView.getDuration();
            elapsed = elapsed / 1000;
            long s = elapsed % 60;
            long m = (elapsed / 60) % 60;
            long h = (elapsed / (60 * 60)) % 24;
            if (h > 0) {
                tvendtime.setText(String.format("%d:%02d:%02d", h, m, s));
                tvduration.setText(String.format("%d:%02d:%02d", h, m, s));
            } else {
                tvendtime.setText(String.format("%02d:%02d", m, s));
                tvduration.setText(String.format("%02d:%02d", m, s));
            }

            int currenttime = videoView.getCurrentPosition();
            currenttime = currenttime / 1000;
            long ss = currenttime % 60;
            long mm = (currenttime / 60) % 60;
            long hh = (currenttime / (60 * 60)) % 24;
            if (h > 0)
                tvstarttime.setText(String.format("%d:%02d:%02d", hh, mm, ss));
            else
                tvstarttime.setText(String.format("%02d:%02d", mm, ss));


            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(stopPosition);
        videoView.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public String size(int size) {
        String hrSize = "";
        double m = size / 1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to Delete?")
                .setIcon(R.drawable.android_delete_black)


                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        File fdelete = new File(filePath);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
//                        System.out.println("file Deleted :" + filePath);
                                Toast.makeText(PreviewActivity.this, "file Deleted :" + filePath, Toast.LENGTH_LONG).show();
                            } else {
//                        System.out.println("file not Deleted :" + filePath);
                                Toast.makeText(PreviewActivity.this, "file not Deleted :" + filePath, Toast.LENGTH_LONG).show();
                            }
                        }
                        Intent intent = new Intent(PreviewActivity.this, VideoHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
