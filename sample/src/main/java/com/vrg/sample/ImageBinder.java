package com.vrg.sample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vrg.flipview.FlipView;


public class ImageBinder implements FlipView.ViewBinder<ImageItem> {
    private ImageView photoView;
    private TextView nameView;

    @Override
    public void initView(View frontView) {
        photoView = frontView.findViewById(R.id.item_image);
        nameView = frontView.findViewById(R.id.item_text);
    }

    @Override
    public void bindView(ImageItem item) {
        Glide.with(photoView.getContext()).load(item.getUserPhoto()).into(photoView);
        nameView.setText(item.getUserName());
    }

    @Override
    public int getFrontSideLayoutRes() {
        return R.layout.item_front;
    }

    @Override
    public int getBackSideLayoutRes() {
        return R.layout.item_back;
    }
}
