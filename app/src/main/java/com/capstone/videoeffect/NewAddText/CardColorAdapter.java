package com.capstone.videoeffect.NewAddText;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.capstone.videoeffect.R;


public class CardColorAdapter extends BaseAdapter {
    private Context context;
    private final int[] mobileValues;

    static class RecordHolder {
        ImageView imageItem;

        RecordHolder() {
        }
    }

    public CardColorAdapter(Context context, int[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(R.layout.listitem_color, parent, false);
            holder = new RecordHolder();
            holder.imageItem = (ImageView) row.findViewById(R.id.imgbutton);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        holder.imageItem.setTag(holder);
        holder.imageItem.setColorFilter(this.mobileValues[position]);
        return row;
    }

    public int getCount() {
        return this.mobileValues.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
