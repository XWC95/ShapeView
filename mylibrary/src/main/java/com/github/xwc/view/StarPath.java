package com.github.xwc.view;

import android.graphics.Path;

import static com.github.xwc.view.ShapeView.STAR;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(STAR)
public class StarPath implements IClipPath {

    private ShapeView shapeView;

    public StarPath(ShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        float outR = width / 2f - shapeView.borderWidthPx;
        float inR = outR * Utils.sin(18) / Utils.sin(180 - 36 - 18) - shapeView.borderWidthPx;
        setStarPath(path, outR, inR);
    }

    public void setStarPath(Path path, float outR, float inR) {

        int borderWidthPx = shapeView.borderWidthPx;
        float radius = outR;
        float radian = Utils.degree2Radian(36); // 36为五角星的角度
        float radiusIn = (float) (radius * Math.sin(radian / 2) / Math
            .cos(radian)); // 中间五边形的半径

        path.moveTo((float) (radius * Math.cos(radian / 2) + borderWidthPx), borderWidthPx); // 此点为多边形的起点
        path.lineTo((float) (radius * Math.cos(radian / 2) + radiusIn
                * Math.sin(radian) + borderWidthPx),
            (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) * 2 + borderWidthPx),
            (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) + radiusIn
                * Math.cos(radian / 2) + borderWidthPx),
            (float) (radius + radiusIn * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo(
            (float) (radius * Math.cos(radian / 2) + radius
                * Math.sin(radian) + borderWidthPx), (float) (radius + radius
                * Math.cos(radian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) + borderWidthPx), radius + radiusIn + borderWidthPx);
        path.lineTo(
            (float) (radius * Math.cos(radian / 2) - radius
                * Math.sin(radian) + borderWidthPx), (float) (radius + radius
                * Math.cos(radian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) - radiusIn
                * Math.cos(radian / 2) + borderWidthPx),
            (float) (radius + radiusIn * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo(borderWidthPx, (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) - radiusIn
                * Math.sin(radian) + borderWidthPx),
            (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));

        path.close();
    }
}
