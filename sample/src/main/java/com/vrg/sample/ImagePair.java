package com.vrg.sample;

import com.vrg.flipview.Pair;


public class ImagePair implements Pair<ImageItem> {
    private ImageItem left;
    private ImageItem right;

    public ImagePair(ImageItem left, ImageItem right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public ImageItem getLeftItem() {
        return left;
    }

    @Override
    public ImageItem getRightItem() {
        return right;
    }
}
