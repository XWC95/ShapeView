package com.github.xwc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by xwc on 2018/2/24.
 */

public class ShapeView extends View {

    private int borderWidthPx = 0;
    private int borderColor = Color.parseColor("#F66276");
    private int borderDashGap = 0;
    private int borderDashWidth = 0;
    private int resourceId = -1;

    private int shapeType; //default circle

    public static final int CIRCLE = 0;
    public static final int ROUND_RECT = 1;
    public static final int TRIANGLE = 2;
    public static final int HEART = 3;
    public static final int POLYGON = 4;
    public static final int STAR = 5;
    public static final int DIAGONAL = 6;


    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomRightRadius;
    private int bottomLeftRadius;
    @ColorInt
    private int pressedColor = -1;
    @ColorInt
    private int defaultColor = 1;

    private float percentBottom = 0.5f;
    private float percentLeft = 0f;
    private float percentRight = 0f;

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private float heartRadian = 0.2f; // radians of heart
    private float heartYPercent = 0.16f;


    private int sides = 4;
    private float turn = 0f; // Turn 0 °


    //diagonal
    public static final int POSITION_BOTTOM = 1;
    public static final int POSITION_TOP = 2;
    public static final int POSITION_LEFT = 3;
    public static final int POSITION_RIGHT = 4;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;

    private int diagonalPosition = POSITION_TOP;
    private int diagonalDirection = DIRECTION_LEFT;
    private int diagonalAngle = 0;


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
            borderWidthPx = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_borderWidth, borderWidthPx);
            borderColor = typedArray.getColor(R.styleable.ShapeView_shape_borderColor, borderColor);
            borderDashGap = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_borderDashGap, borderDashGap);
            borderDashWidth = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_borderDashWidth, borderDashGap);
            resourceId = typedArray.getResourceId(R.styleable.ShapeView_shape_drawable, resourceId);


            if (shapeType == 1) {
                radius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_radius, radius);
                topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_topLeftRadius, topLeftRadius);
                topRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_topRightRadius, topRightRadius);
                bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_bottomLeftRadius, bottomLeftRadius);
                bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_roundRect_bottomRightRadius, bottomRightRadius);
                pressedColor = typedArray.getColor(R.styleable.ShapeView_shape_roundRect_pressed_color, pressedColor);
                defaultColor = typedArray.getColor(R.styleable.ShapeView_shape_roundRect_default_color, defaultColor);

            }
            if (shapeType == 2) {
                percentLeft = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentLeft, percentLeft);
                percentBottom = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentBottom, percentBottom);
                percentRight = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentRight, percentRight);
            }
            if (shapeType == 3) {
                heartRadian = typedArray.getFloat(R.styleable.ShapeView_shape_heart_radian, heartRadian);
                heartYPercent = typedArray.getFloat(R.styleable.ShapeView_shape_heart_YPercent, heartYPercent);
            }

            if (shapeType == 4) {
                sides = typedArray.getInteger(R.styleable.ShapeView_shape_polygon_side, sides);
                turn = typedArray.getFloat(R.styleable.ShapeView_shape_polygon_turn, turn);
            }
//            if (shapeType == 5) {
//                turn = typedArray.getFloat(R.styleable.ShapeView_shape_star_turn, turn);
//            }
            if (shapeType == 6) {
                diagonalAngle = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_angle, diagonalAngle);
                diagonalDirection = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_direction, diagonalDirection);
                diagonalPosition = typedArray.getInteger(R.styleable.ShapeView_shape_diagonal_position, diagonalPosition);
            }


            typedArray.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        super.setClipHelper(new ClipHelper(this) {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                setClipPath(path, shapeType, width, height);
                return path;
            }
        });

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resourceId != -1) {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resourceId),getMeasuredWidth(), getMeasuredHeight(), false);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pressedColor != -1) {
                    setBackgroundColor(pressedColor);
                    return true;
                }
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
                if (defaultColor != -1) {
                    setBackgroundColor(defaultColor);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (defaultColor != -1) {
                    setBackgroundColor(defaultColor);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    boolean firstDispatchDraw = true;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (defaultColor != -1 && firstDispatchDraw) {
            setBackgroundColor(defaultColor);
            firstDispatchDraw = false;
        }

        if (borderWidthPx > 0) {
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);
            if (borderDashGap > 0 && borderDashWidth > 0) {
                borderPaint.setPathEffect(new DashPathEffect(new float[]{borderDashWidth, borderDashGap}, 0));
            }

            switch (shapeType) {
                case 0:
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min((getWidth() - borderWidthPx) / 2f, (getHeight() - borderWidthPx) / 2f), borderPaint);
                    break;
                case 1:
                    canvas.drawPath(getClipHelper().path, borderPaint);
                    break;
//                case 2:
//                    Path path = new Path();
//                    setTriangleBroadPath(path, getWidth(), getHeight());
//                    canvas.drawPath(path, borderPaint);
//                    break;
                case 3:
                    canvas.drawPath(getClipHelper().path, borderPaint);
                    //Path path = new Path();
                    //setHeartPath3(path, getMeasuredWidth(), getMeasuredHeight());
                    //canvas.drawPath(path, borderPaint);
                    break;
                case 4:
                    canvas.drawPath(getClipHelper().path, borderPaint);
                    break;
                case 5:
                    canvas.drawPath(getClipHelper().path, borderPaint);
                    break;
//                case 6:
//                    canvas.drawPath(getClipHelper().path, borderPaint);
//                    break;
            }

        }
    }


//    private void setStarPath(Path path, int halfWidth) {
//        if (turn > 0) { //旋转角度大于0度
//            path.moveTo(turnX(halfWidth, 0, halfWidth * 0.73f), turnY(halfWidth, 0, halfWidth * 0.73f));
//            path.lineTo(turnX(halfWidth, halfWidth * 2f, halfWidth * 0.73f), turnY(halfWidth, halfWidth * 2f, halfWidth * 0.73f));
//            path.lineTo(turnX(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f));
//            path.lineTo(turnX(halfWidth, halfWidth, 0), turnY(halfWidth, halfWidth, 0));//A
//            path.lineTo(turnX(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f));
//        } else {
//            path.moveTo(0, halfWidth * 0.73f);
//            path.lineTo(halfWidth * 2, halfWidth * 0.73f);
//            path.lineTo(halfWidth * 0.38f, halfWidth * 1.9f);
//            path.lineTo(halfWidth, 0);
//            path.lineTo(halfWidth * 1.62f, halfWidth * 1.9f);
//        }
//        path.close();
//    }


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
        return heartRadian;
    }

    public void setHeartRadian(float radian) {
        this.heartRadian = radian;
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

    public int getBorderDashGap() {
        return borderDashGap;
    }

    public void setBorderDashGap(int borderDashGap) {
        this.borderDashGap = borderDashGap;
    }

    public int getBorderDashWidth() {
        return borderDashWidth;
    }

    public void setBorderDashWidth(int borderDashWidth) {
        this.borderDashWidth = borderDashWidth;
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

    public int getPressedColor() {
        return pressedColor;
    }

    public void setPressedColor(int pressedColor) {
        this.pressedColor = pressedColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public float getHeartYPercent() {
        return heartYPercent;
    }

    public void setHeartYPercent(float heartYPercent) {
        this.heartYPercent = heartYPercent;
    }

    public int getResourceId() {
        return resourceId;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
