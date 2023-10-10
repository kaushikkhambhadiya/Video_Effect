package com.capstone.videoeffect.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.capstone.videoeffect.R;

import java.util.ArrayList;


public class GlareAdapter extends BaseAdapter {
    ArrayList<Integer> collage;
    Context context;
    private int height;
    ImageView img_editing;
    private ImageView img_main;
    private LayoutInflater inflater;
    private int width;

    public GlareAdapter(Context context, ArrayList<Integer> stickers) {
        this.context = context;
        this.collage = stickers;
        inflater = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
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
        this.img_editing = (ImageView) convertView.findViewById(R.id.img_editing);
        this.img_main = (ImageView) convertView.findViewById(R.id.iv_main);
        this.img_main.setVisibility(View.GONE);
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        Glide.with(this.context).load(Integer.valueOf(((Integer) this.collage.get(position)).intValue())).override(this.width / 4, this.height / 7).into(this.img_editing);
        System.gc();
        return convertView;
    }
}
