package com.capstone.videoeffect;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.videoeffect.utils.StorageUtil;
import com.capstone.videoeffect.widget.MovieWrapperView;
import com.capstone.videoeffect.widget.PlayerTimer;
import com.daasuu.gpuv.composer.FillMode;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FilterAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String STREAM_URL_MP4_VOD_LONG = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

    private GPUPlayerView gpuPlayerView;
    private SimpleExoPlayer player;
    private PlayerTimer playerTimer;
    private GlFilter filter;
    private FilterAdjuster adjuster;
    List<FilterType> filterTypes;
    ArrayList<Integer> filterImages;
    FilterAdapter adapter;

    private SeekBar timeSeekBar;
    TextView tvstarttime, tvendtime;
    ImageView ivplaypause;
    private Handler mHandler = new Handler();

    Boolean isplaying = true;

    TextView btsave, cancelBtn;
    //    private GlFilter glFilter = new GlFilterGroup(new GlMonochromeFilter(), new GlVignetteFilter());
    private GlFilter glFilter = null;
    private String videoPath;

    //  ProgressDialog progressDialog;
    private AnimatedCircleLoadingView animatedCircleLoadingView;

    Toolbar toolbar;
    Intent intent = null;

    //Firebase Storage
    StorageReference strRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

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
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setActionBar("Set Video Effect");

        Intent intent = getIntent();
        STREAM_URL_MP4_VOD_LONG = intent.getStringExtra(VideoHomeActivity.FILEPATH);

        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);

        setUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpSimpleExoPlayer();
        setUoGlPlayerView();
        updateProgressBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
        if (playerTimer != null) {
            playerTimer.stop();
            playerTimer.removeMessages(0);
        }
    }

    private void setUpViews() {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        strRef = FirebaseStorage.getInstance().getReference(user.getUid()).child("Videos");

        tvstarttime = findViewById(R.id.tvstarttime);
        tvendtime = findViewById(R.id.tvendtime);

        // seek
        timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player == null) return;

                if (fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
                mHandler.removeCallbacks(updateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
                mHandler.removeCallbacks(updateTimeTask);
                player.seekTo(seekBar.getProgress());
                updateProgressBar();
            }
        });

        updateProgressBar();

        //SET Play_pause
        ivplaypause = findViewById(R.id.ivplaypause);
        ivplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isplaying) {
                    isplaying = false;
                    player.setPlayWhenReady(false);
                    ivplaypause.setImageResource(R.drawable.play);
                } else {
                    isplaying = true;
                    player.setPlayWhenReady(true);
                    ivplaypause.setImageResource(R.drawable.pause);
                }
            }
        });

        SeekBar filterSeekBar = (SeekBar) findViewById(R.id.filterSeekBar);
        filterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (adjuster != null) {
                    adjuster.adjust(filter, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        // list
        RecyclerView mRvFilters = findViewById(R.id.rvFilterView);
        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        filterTypes = FilterType.createFilterList();
        filterImages = FilterType.getFilterImages();
        adapter = new FilterAdapter(this, filterTypes, filterImages);
        adapter.setClickListener(this);
        mRvFilters.setAdapter(adapter);


        btsave = findViewById(R.id.btsave);
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCodec();
            }
        });

        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void startCodec() {

        videoPath = getVideoFilePath();

        //    progressDialog.setMax(50);

        //      progressDialog.show();
        btsave.setClickable(false);
        cancelBtn.setClickable(false);
        btsave.setBackgroundResource(R.drawable.disable_bg);
        cancelBtn.setBackgroundResource(R.drawable.disable_bg);

        startLoading();

        if (glFilter == null) {
            glFilter = FilterType.createGlFilter(FilterType.DEFAULT, this);
        }


        com.daasuu.gpuv.composer.GPUMp4Composer GPUMp4Composer = null;
        GPUMp4Composer = new GPUMp4Composer(STREAM_URL_MP4_VOD_LONG, videoPath)
                // .rotation(Rotation.ROTATION_270)
                //.size(720, 720)
                .fillMode(FillMode.PRESERVE_ASPECT_CROP)
                .filter(glFilter)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d(TAG, "onProgress = " + progress);

                        runOnUiThread(() -> {
                            int current_progrss = (int) (progress * 100);
                            //         progressDialog.setProgress(current_progrss);
                            changePercent(current_progrss / 2);
                        });
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                        exportMp4ToGallery(getApplicationContext(), videoPath);
                        runOnUiThread(() -> {
                            //   progressDialog.setProgress(50);

                            String cache_folder = StorageUtil.getCacheDir();
                            File dir = new File(cache_folder);
                            if (dir.exists())
                                deleteDir(dir);



                            ApplyWaterMark(videoPath);

//                            Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
//                            intent.putExtra(HomeActivity.FILEPATH, videoPath);
//                            startActivity(intent);
                        });
                    }

                    @Override
                    public void onCanceled() {
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.d(TAG, "onFailed()");
                    }
                })
                .start();
    }

    private void ApplyWaterMark(String path) {

        videoPath = getVideoFilePath();
        //   progressDialog.setMax(100);

        GlFilter watermark = FilterType.getWaterMarkEffect(this);

        com.daasuu.gpuv.composer.GPUMp4Composer GPUMp4Composer = null;
        GPUMp4Composer = new GPUMp4Composer(path, videoPath)
                // .rotation(Rotation.ROTATION_270)
                //.size(720, 720)
                .fillMode(FillMode.PRESERVE_ASPECT_CROP)
                .filter(watermark)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d(TAG, "onProgress = " + progress);
                        runOnUiThread(() -> {
                            int current_status = (int) ((progress * 100) / 2) + 50;
                            if (current_status == 99) {
                                current_status = 100;
                                changePercent(current_status);
                            } else {
                                changePercent(current_status);
                            }
                            Log.i("current_status", current_status + "");
                        });
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                        exportMp4ToGallery(getApplicationContext(), videoPath);
                        runOnUiThread(() -> {

                            //        progressDialog.setProgress(100);
                            //        progressDialog.dismiss();




                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    Toast.makeText(MainActivity.this, "Successfully saved at =" + videoPath, Toast.LENGTH_SHORT).show();

                                    File fdelete = new File(path);
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + path);
                                        } else {
                                            System.out.println("file not Deleted :" + path);
                                        }
                                    }

                                    btsave.setClickable(true);
                                    cancelBtn.setClickable(true);
                                    btsave.setBackgroundResource(R.drawable.bg_rate);
                                    cancelBtn.setBackgroundResource(R.drawable.bg_rate);

                                    uploadData(Uri.parse(videoPath));

                                }
                            }, 3000);
                        });
                    }

                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled()");
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.d(TAG, "onFailed()");
                    }
                })
                .start();
    }

    public void exportMp4ToGallery(Context context, String filePath) {
        // ビデオのメタデータを作成する
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        // MediaStoreに登録
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));

    }


    @Override
    public void onItemClick(View view, int position) {

        filter = FilterType.createGlFilter(filterTypes.get(position), getApplicationContext());
        adjuster = FilterType.createFilterAdjuster(filterTypes.get(position));
        findViewById(R.id.filterSeekBarLayout).setVisibility(adjuster != null ? View.VISIBLE : View.GONE);
        gpuPlayerView.setGlFilter(filter);

        changeFilter(filterTypes.get(position));
    }

    private void setUpSimpleExoPlayer() {

        TrackSelector trackSelector = new DefaultTrackSelector();

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getResources().getString(R.string.app_name)), defaultBandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(STREAM_URL_MP4_VOD_LONG));

        // SimpleExoPlayer
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        // Prepare the player with the source.
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
    }


    private void setUoGlPlayerView() {
        gpuPlayerView = new GPUPlayerView(this);
        gpuPlayerView.setSimpleExoPlayer(player);
        gpuPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(gpuPlayerView);
        gpuPlayerView.onResume();
    }


    private void setUpTimer() {
        playerTimer = new PlayerTimer();
        playerTimer.setCallback(new PlayerTimer.Callback() {
            @Override
            public void onTick(long timeMillis) {
                long position = player.getCurrentPosition();
                long duration = player.getDuration();

                if (duration <= 0) return;

                timeSeekBar.setMax((int) duration / 1000);
                timeSeekBar.setProgress((int) position / 1000);

                updateProgressBar();
            }
        });
        playerTimer.start();
    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {

        public void run() {
            if (player != null) {
                timeSeekBar.setProgress((int) player.getCurrentPosition());
                timeSeekBar.setMax((int) player.getDuration());

                int elapsed = (int) player.getDuration();
                elapsed = elapsed / 1000;
                long s = elapsed % 60;
                long m = (elapsed / 60) % 60;
                long h = (elapsed / (60 * 60)) % 24;
                if (h > 0) {
                    tvendtime.setText(String.format("%d:%02d:%02d", h, m, s));
                } else {
                    tvendtime.setText(String.format("%02d:%02d", m, s));
                }

                int currenttime = (int) player.getCurrentPosition();
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

        }
    };


    private void releasePlayer() {
        gpuPlayerView.onPause();
        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).removeAllViews();
        gpuPlayerView = null;
        player.stop();
        player.release();
        player = null;
    }

    public File getAndroidMoviesFolder() {

        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES + "/" + getResources().getString(R.string.app_name));


        boolean success = true;
        if (!moviesDir.exists()) {
            success = moviesDir.mkdirs();
        }

        return moviesDir;
    }

    public String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "filter_apply.mp4";
    }

    private void changeFilter(FilterType filter) {
        glFilter = null;
        glFilter = FilterType.createGlFilter(filter, this);
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    private void uploadData(Uri videoUri) {
        Uri uri = Uri.parse("file://"+ videoUri);
        final StorageReference ref = strRef.child(uri.getLastPathSegment());
        ref.putFile(uri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        final Uri taskResult = urlTask.getResult();
                        final String downloadUrl = taskResult.toString();
                        Log.i("download_url",downloadUrl);

                        Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                        intent.putExtra(VideoHomeActivity.FILEPATH, videoPath);
                        startActivity(intent);
                    }
                });
    }
}
