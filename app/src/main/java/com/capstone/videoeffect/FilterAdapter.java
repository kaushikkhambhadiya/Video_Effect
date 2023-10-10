package com.capstone.videoeffect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private List<FilterType> mData;
    private ArrayList<Integer> filterImages;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    FilterAdapter(Context context, List<FilterType> data, ArrayList<Integer> filterImages) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.filterImages = filterImages;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position).name();
        holder.myTextView.setText(animal);
        holder.imgFilterView.setImageResource(filterImages.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView imgFilterView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.txtFilterName);
            imgFilterView = itemView.findViewById(R.id.imgFilterView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).name();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = convertView;
//        // reuse views
//        if (rowView == null) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            if (isWhite) {
//                rowView = inflater.inflate(R.layout.row_white_text, null);
//            } else {
//                rowView = inflater.inflate(R.layout.row_text, null);
//            }
//            // configure view holder
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.text = rowView.findViewById(R.id.label);
//            rowView.setTag(viewHolder);
//        }
//
//        ViewHolder holder = (ViewHolder) rowView.getTag();
//        String s = values.get(position).name();
//        holder.text.setText(s);
//
//        return rowView;
//    }
//
//    public FilterAdapter whiteMode() {
//        isWhite = true;
//        return this;
//    }


}

