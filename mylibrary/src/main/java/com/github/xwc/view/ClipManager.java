package com.github.xwc.view;

import android.graphics.Bitmap;
import android.graphics.Path;

public interface ClipManager {

    Bitmap createMask(int width, int height);

    Path getShadowConvexPath();

    void setupClipLayout(int width, int height);
}