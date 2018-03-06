package com.github.xwc.view;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by xwc on 2018/3/2.
 *
 */

public interface ClipPath {

    /**
     * 创建图形返回图形path
     * @param width  .
     * @param height .
     * @return Path .
     */
    Path createClipPath(int width, int height);

    /**
     * 圆形
     * @param path .
     * @param width .
     * @param height .
     */
    void setCirclePath(Path path, int width, int height);

    /**
     * 圆角矩形
     * @param rect .
     * @param path .
     * @param topLeftDiameter .
     * @param topRightDiameter .
     * @param bottomRightDiameter .
     * @param bottomLeftDiameter .
     */
    void setRoundRectPath(RectF rect, Path path, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter);

    void setTriangleBroadPath(Path path, int width, int height);

    /**
     * 三角形
     * @param path .
     * @param width .
     * @param height .
     */
    void setTrianglePath(Path path, int width, int height);

    /**
     * 心形
     * @param path .
     * @param width .
     * @param height .
     */
    void setHeartPath(Path path, int width, int height);

    /**
     * 正多边形
     * @param rect .
     * @param path .
     */
    void setPolygonPath(RectF rect, Path path);

    /**
     * 五角星
     * @param path .
     * @param outR .
     * @param inR .
     */
    void setStarPath(Path path, float outR, float inR);

    /**
     * 对角线
     * @param path .
     * @param width .
     * @param height .
     */
    void setDiagonalPath(Path path, int width, int height);


}
