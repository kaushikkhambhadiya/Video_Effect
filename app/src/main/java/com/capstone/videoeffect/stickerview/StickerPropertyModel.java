package com.capstone.videoeffect.stickerview;

import java.io.Serializable;

public class StickerPropertyModel implements Serializable {
    private static final long serialVersionUID = 3800737478616389410L;
    private float degree;
    private int horizonMirror;
    private int order;
    private float scaling;
    private long stickerId;
    private String stickerURL;
    private String text;
    private float xLocation;
    private float yLocation;

    public int getHorizonMirror() {
        return this.horizonMirror;
    }

    public void setHorizonMirror(int horizonMirror) {
        this.horizonMirror = horizonMirror;
    }

    public String getStickerURL() {
        return this.stickerURL;
    }

    public void setStickerURL(String stickerURL) {
        this.stickerURL = stickerURL;
    }

    public long getStickerId() {
        return this.stickerId;
    }

    public void setStickerId(long stickerId) {
        this.stickerId = stickerId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getxLocation() {
        return this.xLocation;
    }

    public void setxLocation(float xLocation) {
        this.xLocation = xLocation;
    }

    public float getyLocation() {
        return this.yLocation;
    }

    public void setyLocation(float yLocation) {
        this.yLocation = yLocation;
    }

    public float getDegree() {
        return this.degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getScaling() {
        return this.scaling;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
