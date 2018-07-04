package com.github.xwc.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.xwc.view.ShapeView;

public class ImageLoader {

    public static void loadImage(Context context, Object url, ShapeView shapeView, int width, int height) {

        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                shapeView.setBitmap(resource);
                shapeView.reDraw();
            }
        });
    }

    public static void loadImage(Context context, Object url, ShapeView shapeView) {
        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                shapeView.setBitmap(resource);
                shapeView.reDraw();
            }
        });
    }
}
