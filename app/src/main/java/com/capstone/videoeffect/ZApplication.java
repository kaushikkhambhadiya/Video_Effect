package com.capstone.videoeffect;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import iknow.android.utils.BaseUtils;
import nl.bravobit.ffmpeg.FFmpeg;

public class ZApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    BaseUtils.init(this);
    initFFmpegBinary(this);
    MultiDex.install(this);
  }

  private void initFFmpegBinary(Context context) {
    if (!FFmpeg.getInstance(context).isSupported()) {
      Log.e("ZApplication","Android cup arch not supported!");
    }
  }
}
