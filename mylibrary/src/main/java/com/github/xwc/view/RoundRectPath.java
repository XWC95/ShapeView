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
import android.graphics.RectF;

/**
 * Created by xwc on 2018/7/4.
 */
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

        if (rectF.width() <= width && rectF.height() <= height) {
            path.addRoundRect(rectF, buttonShapeView.getRoundedCorners(), Path.Direction.CW);
        }
    }
}
