package com.capstone.videoeffect.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.capstone.videoeffect.PhotoHomeActivity;
import com.capstone.videoeffect.R;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;


public class FrameAdapter extends BaseAdapter {
    ArrayList<Integer> collage;
    Context context1;
    private int height;
    ImageView img_editing;
    ImageView img_main;
    private LayoutInflater inflater ;
    private int width;

    public FrameAdapter(Context context, ArrayList<Integer> stickers) {
        this.context1 = context;
        this.collage = stickers;
        inflater = (LayoutInflater) this.context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.collage.size();
    }

    public Object getItem(int i) {
        return this.collage.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.frame_item, null);
        }
        this.img_main = (ImageView) convertView.findViewById(R.id.iv_main);
        this.img_editing = (ImageView) convertView.findViewById(R.id.img_editing);
        DisplayMetrics metrics = this.context1.getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        int resource = ((Integer) this.collage.get(position)).intValue();
        this.img_main.setImageBitmap(PhotoHomeActivity.bitmap);
        Glide.with(this.context1).load(Integer.valueOf(resource)).override(this.width / 4, this.height / 7).into(this.img_editing);
        this.img_editing.setColorFilter(ContextCompat.getColor(this.context1, R.color.white));
        System.gc();
        return convertView;
    }
}
