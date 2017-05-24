package com.roy.bkapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roy.bkapp.R;


/**
 * Created by 1vPy(Roy) on 2017/2/15.
 */

public class ImageUtils {
    public static void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

/*
    public static void displayCircleImage(Context context, String url, int height, int width, ImageView imageView, int default_pic) {
        Glide.with(context)
                .load(url)
                .override(width, height)
                .placeholder(default_pic)
                .error(default_pic)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void displayCircleImage(Context context, int height, int width, int resId, ImageView imageView, int default_pic) {
        Glide.with(context)
                .load(resId)
                .override(width, height)
                .placeholder(default_pic)
                .error(default_pic)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
*/

}
