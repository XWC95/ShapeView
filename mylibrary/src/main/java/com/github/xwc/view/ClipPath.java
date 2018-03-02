package com.github.xwc.view;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by xwc on 2018/3/2.
 */

public interface ClipPath {

    Path createClipPath(int width, int height);

    void setCirclePath(Path path, int width, int height);

    void setRoundRectPath(RectF rect, Path path, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter);

    void setTriangleBroadPath(Path path, int width, int height);

    void setTrianglePath(Path path, int width, int height);

    void setHeartPath(Path path, int width, int height);

    void setPolygonPath(RectF rect, Path path);

    void setStarPath(Path path, float outR, float inR);

    void setDiagonalPath(Path path, int width, int height);


}
