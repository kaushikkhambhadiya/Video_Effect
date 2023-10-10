package com.capstone.videoeffect.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ResizableImageView extends AppCompatImageView {
    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil((double) ((((float) width) * ((float) d.getIntrinsicHeight())) / ((float) d.getIntrinsicWidth())));
            setMeasuredDimension(width, width);
            return;
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
