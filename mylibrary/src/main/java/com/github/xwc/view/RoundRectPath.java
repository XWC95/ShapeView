package com.github.xwc.view;

import android.graphics.Path;
import android.graphics.RectF;

import static com.github.xwc.view.ButtonShapeView.ROUND_RECT;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(ROUND_RECT)
public class RoundRectPath implements IClipPath {

    private ButtonShapeView buttonShapeView;

    public RoundRectPath(ButtonShapeView buttonShapeView) {
        this.buttonShapeView = buttonShapeView;
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        RectF rectF = new RectF();
        int borderWidthPx = buttonShapeView.borderWidthPx;
        rectF.set(borderWidthPx, borderWidthPx, width - borderWidthPx, height - borderWidthPx);

        if (rectF.width() < width && rectF.height() < height) {
            if(buttonShapeView.getRoundedCorners() != null){
                path.addRoundRect(rectF, buttonShapeView.getRoundedCorners(), Path.Direction.CW);
            }
        }
    }
}
