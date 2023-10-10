package com.capstone.videoeffect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MyVideosAdapter extends BaseAdapter {

    int THUMBSIZE = 128;
    Context context;
    ArrayList<String> f;
    LayoutInflater inflater;

    class ViewHolder {
        ImageView imageview;

        ViewHolder() {
        }
    }

    public MyVideosAdapter(Context context, ArrayList<String> f) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.f = f;
    }

    public int getCount() {
        return f.size();
    }

    public String getItem(int position) {
        return (String) f.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View tmpView = convertView;
        if (tmpView == null) {
            ViewHolder holder = new ViewHolder();
            tmpView = inflater.inflate(R.layout.custom_video_grid, null);
            holder.imageview = (ImageView) tmpView.findViewById(R.id.grid_item);
            tmpView.setTag(holder);
        }

        ViewHolder v1 = (ViewHolder) tmpView.getTag();
//        v1.imageview.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile((String) this.f.get(position)), 128, 128));

//        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
//                BitmapFactory.decodeFile(f.get(position)),
//                THUMBSIZE,
//                THUMBSIZE);

        Glide.with(context)
                .load(new File(f.get(position)))
                .placeholder(R.drawable.videoeffectlogo)
                .centerCrop()
                .into(v1.imageview);


        return tmpView;
    }
}
