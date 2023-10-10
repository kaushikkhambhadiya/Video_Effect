package com.capstone.videoeffect.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

public class Effects {
    public static ImageView applyEffectNone(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect1(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect2(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(getColorMatrixSepia()));
        return img;
    }

    private static ColorMatrix getColorMatrixSepia() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1.0f, 1.0f, 0.8f, 1.0f);
        colorMatrix.postConcat(colorScale);
        return colorMatrix;
    }

    public static ImageView applyEffect3(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.8f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect4(ImageView img) {
        float translate = ((-0.5f * 3.2f) + 0.5f) * 255.0f;
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{3.2f, 0.0f, 0.0f, 0.0f, translate, 0.0f, 3.2f, 0.0f, 0.0f, translate, 0.0f, 0.0f, 3.2f, 0.0f, translate, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect5(ImageView img) {
        float translate = ((-0.5f * 1.5f) + 0.5f) * 255.0f;
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.5f, 0.0f, 0.0f, 0.0f, translate, 0.0f, 1.5f, 0.0f, 0.0f, translate, 0.0f, 0.0f, 1.5f, 0.0f, translate, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect6(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect7(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -0.1f, -0.1f, -0.1f, 0.0f, 1.0f}));
        return img;
    }

    public static ImageView applyEffect8(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.0f, 0.0f, 90.0f, -0.213f, -0.715f, -0.072f, 0.0f, 255.0f}));
        return img;
    }

    public static ImageView applyEffect9(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect10(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -90.0f, 0.213f, 0.715f, 0.072f, 0.0f, 255.0f}));
        return img;
    }

    public static ImageView applyEffect11(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -90.0f, -0.213f, -0.715f, -0.072f, 0.0f, 255.0f}));
        return img;
    }

    public static ImageView applyEffect12(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 90.0f, 0.213f, 0.715f, 0.072f, 0.0f, 255.0f}));
        return img;
    }

    public static ImageView applyEffect13(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect14(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.390625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.1640625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.6796875f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect15(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.5234375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.203125f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.015625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.28125f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect16(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0390625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.3671875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.4921875f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect17(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.5625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.565625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.5234375f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect18(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.9609375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.6171875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.40625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8046875f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect19(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{0.7109375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.34375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.35625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.9921875f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect20(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.0703125f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.25f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.140625f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.4140625f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect21(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.1953125f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.671875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3984375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7265625f, 0.0f}));
        return img;
    }

    public static ImageView applyEffect22(ImageView img) {
        img.setColorFilter(new ColorMatrixColorFilter(new float[]{1.1953125f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.671875f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3984375f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.8671875f, 0.0f}));
        return img;
    }
}
