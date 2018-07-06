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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by xwc on 2018/2/24.
 */

public abstract class ClipHelper  {

    protected final Path mPath = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * @param width  图像宽
     * @param height 图像高
     */
    public void setUpClipPath(int width, int height) {
        mPath.reset();
        final Path clipPath = getClipPath(width, height);
        if (clipPath != null) {
            mPath.set(clipPath);
        }
    }

    /**
     * @param width
     * @param height
     * @return Path
     */
    protected final Path getClipPath(int width, int height) {
        return createClipPath(width, height);
    }

    /**
     * 创建目标图像
     *
     * @param width  图像宽
     * @param height 图像高
     * @return bitmap
     */
    public Bitmap createMask(int width, int height) {
        final Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mask);
        canvas.drawPath(mPath, paint);
        return mask;
    }

    /**
     * 创建图形返回图形path
     * @param width  .
     * @param height .
     * @return Path .
     */
    abstract Path createClipPath(int width, int height);

//    @Override
//    public void setTriangleBroadPath(Path path, int width, int height) {
//        float borderWidth = shapeView.borderWidthPx;
//        path.moveTo(0 + borderWidth, shapeView.getPercentLeft() * height + borderWidth / 2);
//        path.lineTo(shapeView.getPercentBottom() * width - borderWidth / 2, height - borderWidth);
//        path.lineTo(width - borderWidth, shapeView.getPercentBottom() * height + borderWidth / 2);
//        path.close();
//    }


}
