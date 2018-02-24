package com.github.xwc.view;

import android.graphics.Path;

public interface ClipPathCreator {
    Path createClipPath(int width, int height);
}