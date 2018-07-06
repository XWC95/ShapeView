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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by xwc on 2018/7/3.
 */

public class ButtonShapeView extends Shape {

    private IClipPath clipPath;

    /**
     * 圆角矩形
     */
    public static final int ROUND_RECT = 1;

    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomRightRadius;
    private int bottomLeftRadius;

    private float[] roundedCorners;

    boolean firstDispatchDraw = true;

    public ButtonShapeView(@NonNull Context context) {
        this(context, null);
    }

    public ButtonShapeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonShapeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonShapeView);

        radius = typedArray.getDimensionPixelSize(R.styleable.ButtonShapeView_shape_roundRect_radius, radius);
        topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ButtonShapeView_shape_roundRect_topLeftRadius, topLeftRadius);
        topRightRadius = typedArray.getDimensionPixelSize(R.styleable.ButtonShapeView_shape_roundRect_topRightRadius, topRightRadius);
        bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ButtonShapeView_shape_roundRect_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ButtonShapeView_shape_roundRect_bottomRightRadius, bottomRightRadius);

        if (radius != 0) {
            roundedCorners = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        } else if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
            roundedCorners = new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomLeftRadius, bottomLeftRadius, bottomRightRadius, bottomRightRadius};
        }

        typedArray.recycle();

        super.setClipHelper(new ClipHelper() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                clipPath = new RoundRectPath(ButtonShapeView.this);
                clipPath.setClipPath(path, width, height);
                return path;
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (defaultColor != 0 && firstDispatchDraw) {
            if (defaultDrawable == 0) {
                setBackgroundColor(defaultColor);
                firstDispatchDraw = false;
            }
        }

        if (borderWidthPx > 0) {
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);
            if (borderDashGap > 0 && borderDashWidth > 0) {
                borderPaint.setPathEffect(new DashPathEffect(new float[]{borderDashWidth, borderDashGap}, 0));
            }
            canvas.drawPath(getClipHelper().mPath, borderPaint);
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public void setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public void setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }

    public void setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public void setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
    }

    public float[] getRoundedCorners() {
        return roundedCorners;
    }

    public void setRoundedCorners(float[] roundedCorners) {
        this.roundedCorners = roundedCorners;
    }
}
