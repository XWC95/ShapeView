package com.github.xwc.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by xwc on 2018/2/24.
 */

public abstract class ClipHelper implements ClipPathCreator {

    protected final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void setUpClipPath(int width, int height) {
        path.reset();
        final Path clipPath = getClipPath(width, height);
        if (clipPath != null) {
            path.set(clipPath);
        }
    }

    protected final Path getClipPath(int width, int height) {
        return createClipPath(width, height);
    }

    @NonNull
    public Bitmap createMask(int width, int height) {
        final Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mask);
        canvas.drawPath(path, paint);
        return mask;
    }


}
