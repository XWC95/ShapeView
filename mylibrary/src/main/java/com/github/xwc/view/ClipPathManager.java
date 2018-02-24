package com.github.xwc.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by xwc on 2018/2/24.
 */

public class ClipPathManager implements ClipManager {

    protected final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ClipPathCreator createClipPath = null;

    @NonNull
    @Override
    public Bitmap createMask(int width, int height) {
        final Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mask);
        canvas.drawPath(path, paint);
        return mask;
    }

    @Nullable
    @Override
    public Path getShadowConvexPath() {
        return path;
    }

    @Override
    public void setupClipLayout(int width, int height) {
        path.reset();
        final Path clipPath = createClipPath(width, height);
        if (clipPath != null) {
            path.set(clipPath);
        }
    }

    protected final Path createClipPath(int width, int height) {
        if (createClipPath != null) {
            return createClipPath.createClipPath(width, height);
        }
        return null;
    }

    public void setClipPathCreator(ClipPathCreator clipPathCreator) {
        createClipPath = clipPathCreator;
    }

    public interface ClipPathCreator {
        Path createClipPath(int width, int height);
    }
}
