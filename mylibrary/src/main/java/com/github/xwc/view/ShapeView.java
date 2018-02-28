package com.github.xwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * Created by xwc on 2018/2/24.
 */

public class ShapeView extends View {

    private int borderWidthPx = 0;

    private int borderColor = Color.BLUE;

    private int shapeType; //默认是圆形

    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomRightRadius;
    private int bottomLeftRadius;

    private float percentBottom = 0.5f;
    private float percentLeft = 0f;
    private float percentRight = 0f;

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private float radian = 0.2f; // radians of heart

    private int sides = 4;
    private float turn = 0f; // Turn 0 °


    public ShapeView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
            shapeType = typedArray.getInteger(R.styleable.ShapeView_shape_type, shapeType);
            if (shapeType == 0) {
                borderWidthPx = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_circle_borderWidth, borderWidthPx);
                borderColor = typedArray.getColor(R.styleable.ShapeView_shape_circle_borderColor, borderColor);
            }
            if (shapeType == 1) {
                radius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_radius, radius);
                topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_topLeftRadius, topLeftRadius);
                topRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_topRightRadius, topRightRadius);
                bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_bottomLeftRadius, bottomLeftRadius);
                bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_bottomRightRadius, bottomRightRadius);
            }
            if (shapeType == 2) {
                percentLeft = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentLeft, percentLeft);
                percentBottom = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentBottom, percentBottom);
                percentRight = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentRight, percentRight);
            }
            if (shapeType == 3) {
                radian = typedArray.getFloat(R.styleable.ShapeView_shape_heart_radian, radian);
            }

            if (shapeType == 4) {
                sides = typedArray.getInteger(R.styleable.ShapeView_shape_polygon_side, sides);
                turn = typedArray.getFloat(R.styleable.ShapeView_shape_polygon_turn, turn);
            }
            if (shapeType == 5) {
                turn = typedArray.getFloat(R.styleable.ShapeView_shape_star_turn, turn);
            }

            typedArray.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        super.setClipHelper(new ClipHelper() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                switch (shapeType) {
                    case 0: //circle
                        setCirclePath(path, width, height);
                        break;
                    case 1://roundRect
                        RectF rectF = new RectF();
                        rectF.set(0, 0, width, height);
                        if (radius > 0) {
                            setRoundRectPath(rectF, path, radius, radius, radius, radius);
                        } else {
                            setRoundRectPath(rectF, path, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
                        }
                        break;
                    case 2://triangle
                        setTrianglePath(path, width, height);
                        break;
                    case 3://heart
                        setHeartPath(path, width, height);
                        break;
                    case 4: //equilateralPolygon
                        RectF polygonRectF = new RectF();
                        polygonRectF.set(0, 0, width, height);
                        setPolygonPath(polygonRectF, path);
                        break;
                    case 5: //star
                        setStarPath(path, width / 2);
                        break;
                }
                return path;
            }
        });
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (shapeType == 0 && borderWidthPx > 0) {
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min((getWidth() - borderWidthPx) / 2f, (getHeight() - borderWidthPx) / 2f), borderPaint);

        }
    }

    private void setCirclePath(Path path, int width, int height) {
        //xy为圆的圆心 radius为圆的半径 Diection.CW 顺时针方向
        path.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CW);
    }

    private void setRoundRectPath(RectF rect, Path path, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
        topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
        topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
        bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
        bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

        path.moveTo(rect.left + topLeftDiameter, rect.top);

        path.lineTo(rect.right - topRightDiameter, rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.top + topRightDiameter);
        path.lineTo(rect.right, rect.bottom - bottomRightDiameter);
        path.quadTo(rect.right, rect.bottom, rect.right - bottomRightDiameter, rect.bottom);
        path.lineTo(rect.left + bottomLeftDiameter, rect.bottom);
        path.quadTo(rect.left, rect.bottom, rect.left, rect.bottom - bottomLeftDiameter);
        path.lineTo(rect.left, rect.top + topLeftDiameter);
        path.quadTo(rect.left, rect.top, rect.left + topLeftDiameter, rect.top);
        path.close();
    }

    private void setTrianglePath(Path path, int width, int height) {
        path.moveTo(0, percentLeft * height);
        path.lineTo(percentBottom * width, height);
        path.lineTo(width, percentRight * height);
        path.close();
    }

    private void setHeartPath(Path path, int width, int height) {
        path.moveTo(0.5f * width, 0.16f * height);
        path.cubicTo(0.15f * width, -radian * height, -0.4f * width, 0.45f * height, 0.5f * width, height);
        path.moveTo(0.5f * width, height);
        path.cubicTo(width + 0.4f * width, 0.45f * height, width - 0.15f * width, -radian * height, 0.5f * width, 0.16f * height);
        path.close();
    }

    private void setPolygonPath(RectF rect, Path path) {
        if (sides < 3) {
            return;
        }
        float r = (rect.right - rect.left) / 2;
        float mX = (rect.right + rect.left) / 2;
        float my = (rect.top + rect.bottom) / 2;
        for (int i = 0; i <= sides; i++) {
            // - 0.5 : Turn 90 °
            float alpha = Double.valueOf(((2f / sides) * i - turn) * Math.PI).floatValue();
            float nextX = mX + Double.valueOf(r * Math.cos(alpha)).floatValue();
            float nextY = my + Double.valueOf(r * Math.sin(alpha)).floatValue();
            if (i == 0) {
                path.moveTo(nextX, nextY);
            } else {
                path.lineTo(nextX, nextY);
            }
        }
    }

    private void setStarPath(Path path, int halfWidth) {
        if (turn > 0) { //旋转角度大于0度
            path.moveTo(turnX(halfWidth, 0, halfWidth * 0.73f), turnY(halfWidth, 0, halfWidth * 0.73f));
            path.lineTo(turnX(halfWidth, halfWidth * 2f, halfWidth * 0.73f), turnY(halfWidth, halfWidth * 2f, halfWidth * 0.73f));
            path.lineTo(turnX(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f));
            path.lineTo(turnX(halfWidth, halfWidth, 0), turnY(halfWidth, halfWidth, 0));//A
            path.lineTo(turnX(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f));
        } else {
            path.moveTo(0, halfWidth * 0.73f);
            path.lineTo(halfWidth * 2, halfWidth * 0.73f);
            path.lineTo(halfWidth * 0.38f, halfWidth * 1.9f);
            path.lineTo(halfWidth, 0);
            path.lineTo(halfWidth * 1.62f, halfWidth * 1.9f);
        }
        path.close();
    }

    /**
     * X坐标围绕中心点旋转
     *
     * @param halfX
     * @param x
     * @param y
     * @return
     */
    public float turnX(int halfX, float x, float y) {
        return (float) ((x - halfX) * Math.cos(turn) + (y - halfX) * Math.sin(turn) + halfX);
    }

    /**
     * Y坐标围绕中心点旋转
     *
     * @param halfX
     * @param x
     * @param y
     * @return
     */
    public float turnY(int halfX, float x, float y) {
        return (float) (-(x - halfX) * Math.sin(turn) + (y - halfX) * Math.cos(turn) + halfX);
    }


    public int getBorderWidthPx() {
        return borderWidthPx;
    }

    public void setBorderWidthPx(int borderWidthPx) {
        this.borderWidthPx = borderWidthPx;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getShapeType() {
        return shapeType;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
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

    public Paint getBorderPaint() {
        return borderPaint;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    public float getHeartRadian() {
        return radian;
    }

    public void setHeartRadian(float radian) {
        this.radian = radian;
    }

    public float getRadian() {
        return radian;
    }

    public void setRadian(float radian) {
        this.radian = radian;
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
}
