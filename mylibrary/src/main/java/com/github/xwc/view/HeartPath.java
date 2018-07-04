package com.github.xwc.view;

import android.graphics.Path;

import static com.github.xwc.view.ShapeView.HEART;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(HEART)
public class HeartPath implements IClipPath {

    private ShapeView shapeView;

    public HeartPath(ShapeView shapeView) {
        this.shapeView = shapeView;
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        int borderWidth = shapeView.borderWidthPx / 2;
        path.moveTo(0.5f * width, shapeView.getHeartYPercent() * height + borderWidth);
        path.cubicTo(0.15f * width + borderWidth, -shapeView.getHeartRadian() * height + borderWidth, -0.4f * width + borderWidth, 0.45f * height + borderWidth, 0.5f * width, height - borderWidth);
//        mPath.moveTo(0.5f * width, height);
        path.cubicTo(width + 0.4f * width - borderWidth, 0.45f * height + borderWidth, width - 0.15f * width - borderWidth, -shapeView.getHeartRadian() * height + borderWidth, 0.5f * width, shapeView.getHeartYPercent() * height + borderWidth);
        path.close();
    }
}
