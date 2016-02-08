package com.globo.brasileirao.view.image;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

public interface ImageLoader {

    /**
     * Load image from network into image view resizing the image with provided width and height in pixels.
     * @param imageView view to loadFromNetwork the image into.
     * @param url  url to image.
     * @param width width in pixels.
     * @param height height in pixels.
     * @param placeholder drawable resource to work as a placeholder while image is not loaded.
     */
    void loadFromNetwork(ImageView imageView, String url, int width, int height, @DrawableRes int placeholder);

}
