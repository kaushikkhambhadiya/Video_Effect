package com.capstone.videoeffect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.capstone.videoeffect.R;
import com.capstone.videoeffect.view.ResizableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StickerAdapter extends BaseAdapter {
    Context context;
    ImageView img_editing;
    private LayoutInflater inflater;
    ArrayList<Integer> stickers;
    private ViewHolder viewholder;

    public class ViewHolder {
        ResizableImageView img_gallery;
    }

    public StickerAdapter(Context context, ArrayList<Integer> stickers) {
        this.context = context;
        this.stickers = stickers;
        inflater = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public int getCount() {
        return this.stickers.size();
    }

    public Object getItem(int i) {
        return this.stickers.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.sticker_item, null);
        }
        this.img_editing = (ImageView) convertView.findViewById(R.id.img_editing);
        Picasso.get().load(((Integer) this.stickers.get(position)).intValue()).into(this.img_editing);
        System.gc();
        return convertView;
    }
}
