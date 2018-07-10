/*
 * Copyright 2018 xwc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xwc.view;

import android.graphics.Path;

import com.github.xwc.annotations.ShapeType;

import java.util.Objects;

import static com.github.xwc.view.ShapeView.CIRCLE;
import static com.github.xwc.view.ShapeView.DIAGONAL;
import static com.github.xwc.view.ShapeView.DIRECTION_LEFT;
import static com.github.xwc.view.ShapeView.POSITION_BOTTOM;
import static com.github.xwc.view.ShapeView.POSITION_LEFT;
import static com.github.xwc.view.ShapeView.POSITION_RIGHT;
import static com.github.xwc.view.ShapeView.POSITION_TOP;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(value = DIAGONAL, superClass = IClipPath.class)
public class DiagonalPath implements IClipPath {

    private ShapeView shapeView;

    public DiagonalPath(Object... objects) {
        if (objects[0] instanceof ShapeView) {
            this.shapeView = (ShapeView) objects[0];
        }
    }

    @Override
    public void setClipPath(Path path, int width, int height) {
        if (shapeView == null) {
            return;
        }
        final float perpendicularHeight = (float) (width * Math.tan(Math.toRadians(shapeView.getDiagonalAngle())));
        final boolean isDirectionLeft = shapeView.getDiagonalDirection() == DIRECTION_LEFT;
        int paddingLeft = shapeView.getPaddingLeft();
        int paddingRight = shapeView.getPaddingRight();
        int paddingTop = shapeView.getPaddingTop();
        int paddingBottom = shapeView.getPaddingBottom();
        switch (shapeView.getDiagonalPosition()) {
            case POSITION_BOTTOM:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft, paddingRight);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - perpendicularHeight - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - perpendicularHeight - paddingBottom);
                    path.lineTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.close();
                }
                break;
            case POSITION_TOP:
                if (isDirectionLeft) {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(width - paddingRight, paddingTop + perpendicularHeight);
                    path.lineTo(paddingLeft, paddingTop);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(paddingLeft, paddingTop + perpendicularHeight);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                }
                break;
            case POSITION_RIGHT:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight - perpendicularHeight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight - perpendicularHeight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                }
                break;
            case POSITION_LEFT:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft + perpendicularHeight, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft + perpendicularHeight, height - paddingBottom);
                    path.close();
                }
                break;
            default:
                break;
        }
    }
}
