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
 * Created by xwc on 2018/2/24.
 */

public class ShapeView extends Shape {

    /**
     * 圆形
     */
    public static final int CIRCLE = 0;

    /**
     * 三角形
     */
    public static final int TRIANGLE = 2;
    /**
     * 心形
     */
    public static final int HEART = 3;
    /**
     * 正多边形
     */
    public static final int POLYGON = 4;
    /**
     * 五角星形
     */
    public static final int STAR = 5;
    /**
     * 对角线形
     */
    public static final int DIAGONAL = 6;

    //Default is CIRCLE
    protected int shapeType;

    private float percentBottom = 0.5f;
    private float percentLeft = 0f;
    private float percentRight = 0f;

    private float heartRadian = 0.2f; // radians of heart
    private float heartYPercent = 0.16f;

    private int sides = 4;
    private float turn = 0f; // 旋转角度

    //diagonal
    public static final int POSITION_BOTTOM = 1;
    public static final int POSITION_TOP = 2;
    public static final int POSITION_LEFT = 3;
    public static final int POSITION_RIGHT = 4;
    public static final int DIRECTION_LEFT = 1;

    private int diagonalPosition = POSITION_TOP;
    private int diagonalDirection = DIRECTION_LEFT;
    private int diagonalAngle = 0;

    boolean firstDispatchDraw = true;

    private IClipPath clipPath;

    public ShapeView(@NonNull Context context) {
        this(context, null);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
            shapeType = typedArray.getInteger(R.styleable.ShapeView_shape_type, shapeType);

            if (shapeType == TRIANGLE) {
                percentLeft = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentLeft, percentLeft);
                percentBottom = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentBottom, percentBottom);
                percentRight = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentRight, percentRight);
            }

            if (shapeType == HEART) {
                heartRadian = typedArray.getFloat(R.styleable.ShapeView_shape_heart_radian, heartRadian);
                heartYPercent = typedArray.getFloat(R.styleable.ShapeView_shape_heart_YPercent, heartYPercent);
            }

            if (shapeType == POLYGON) {
                sides = typedArray.getInteger(R.styleable.ShapeView_shape_polygon_side, sides);
                turn = typedArray.getFloat(R.styleable.ShapeView_shape_polygon_turn, turn);
            }

            if (shapeType == DIAGONAL) {
                diagonalAngle = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_angle, diagonalAngle);
                diagonalDirection = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_direction, diagonalDirection);
                diagonalPosition = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_position, diagonalPosition);
            }

            typedArray.recycle();
        }

        super.setClipHelper(new ClipHelper() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                clipPath =  IClipPathFactory.create(shapeType,ShapeView.this);
                clipPath.setClipPath(path,width,height);
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

            switch (shapeType) {
                case CIRCLE:
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min((getWidth() - borderWidthPx) / 2f, (getHeight() - borderWidthPx) / 2f), borderPaint);
                    break;
                case HEART:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
                case POLYGON:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
                case STAR:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
            }
        }
    }

    public float getPercentBottom() {
        return percentBottom;
    }

    public void setPercentBottom(float percentBottom) {
        this.percentBottom = percentBottom;
    }

    public float getPercentLeft() {
        return percentLeft;
    }

    public void setPercentLeft(float percentLeft) {
        this.percentLeft = percentLeft;
    }

    public float getPercentRight() {
        return percentRight;
    }

    public void setPercentRight(float percentRight) {
        this.percentRight = percentRight;
    }

    public float getHeartRadian() {
        return heartRadian;
    }

    public void setHeartRadian(float heartRadian) {
        this.heartRadian = heartRadian;
    }

    public float getHeartYPercent() {
        return heartYPercent;
    }

    public void setHeartYPercent(float heartYPercent) {
        this.heartYPercent = heartYPercent;
    }

    public int getSides() {
        return sides;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public float getTurn() {
        return turn;
    }

    public void setTurn(float turn) {
        this.turn = turn;
    }

    public int getDiagonalPosition() {
        return diagonalPosition;
    }

    public void setDiagonalPosition(int diagonalPosition) {
        this.diagonalPosition = diagonalPosition;
    }

    public int getDiagonalDirection() {
        return diagonalDirection;
    }

    public void setDiagonalDirection(int diagonalDirection) {
        this.diagonalDirection = diagonalDirection;
    }

    public int getDiagonalAngle() {
        return diagonalAngle;
    }

    public void setDiagonalAngle(int diagonalAngle) {
        this.diagonalAngle = diagonalAngle;
    }

    public int getShapeType() {
        return shapeType;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }
}
