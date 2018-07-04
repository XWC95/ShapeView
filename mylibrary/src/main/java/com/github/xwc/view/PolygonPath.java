package com.github.xwc.view;

import android.graphics.Path;
import android.graphics.RectF;

import static com.github.xwc.view.ShapeView.POLYGON;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(POLYGON)
public class PolygonPath implements IClipPath {

    private ShapeView shapeView;

    public PolygonPath(ShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        RectF polygonRectF = new RectF();
        polygonRectF.set(0, 0, width, height);
        setPolygonPath(polygonRectF, path);
    }

    public void setPolygonPath(RectF rect, Path path) {
        int sides = shapeView.getSides();
        if (sides < 3) {
            return;
        }
        float r = (rect.right - rect.left) / 2 - shapeView.borderWidthPx;
        float mX = (rect.right + rect.left) / 2;
        float my = (rect.top + rect.bottom) / 2;
        for (int i = 0; i <= sides; i++) {
            // - 0.5 : Turn 90 °
            float alpha = Double.valueOf(((2f / sides) * i - shapeView.getTurn()) * Math.PI).floatValue();
            float nextX = mX + Double.valueOf(r * Math.cos(alpha)).floatValue();
            float nextY = my + Double.valueOf(r * Math.sin(alpha)).floatValue();
            if (i == 0) {
                path.moveTo(nextX, nextY);
            } else {
                path.lineTo(nextX, nextY);
            }
        }
        path.close();
    }
}
