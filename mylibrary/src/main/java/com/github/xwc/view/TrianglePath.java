package com.github.xwc.view;

import android.graphics.Path;

import static com.github.xwc.view.ShapeView.TRIANGLE;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(TRIANGLE)
public class TrianglePath implements IClipPath {

    private ShapeView shapeView;

    public TrianglePath(ShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        float borderWidth = shapeView.borderWidthPx / 2;
        path.moveTo(0 + borderWidth, shapeView.getPercentLeft() * height + borderWidth);
        path.lineTo(shapeView.getPercentBottom() * width - borderWidth, height - borderWidth);
        path.lineTo(width - borderWidth, shapeView.getPercentRight() * height + borderWidth);
        path.close();
    }
}
