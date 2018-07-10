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

import static com.github.xwc.view.ShapeView.HEART;

/**
 * Created by xwc on 2018/7/4.
 */
@ShapeType(value = HEART, superClass = IClipPath.class)
public class HeartPath implements IClipPath {

    private ShapeView shapeView;

    public HeartPath(Object... objects) {
        if (objects[0] instanceof ShapeView) {
            this.shapeView = (ShapeView) objects[0];
        }
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
