package com.github.xwc.view;

import android.graphics.Path;

import com.github.xwc.compiler.ShapeType;

import static com.github.xwc.view.ShapeView.CIRCLE;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(value = CIRCLE, superClass = IClipPath.class)
public class CirclePath implements IClipPath {

    @Override
    public void setClipPath(Path path, int width, int height) {
        path.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CW);
    }
}
