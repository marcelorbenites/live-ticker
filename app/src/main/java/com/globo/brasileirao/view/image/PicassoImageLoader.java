package com.globo.brasileirao.view.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoImageLoader implements ImageLoader {

    private final Context context;

    public PicassoImageLoader(Context context) {
        this.context = context;
    }

    @Override public void loadFromNetwork(ImageView imageView, String url, int width, int height, @DrawableRes int placeholder) {
        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .resize(width, height)
                .centerInside()
                .into(imageView);
    }
}
