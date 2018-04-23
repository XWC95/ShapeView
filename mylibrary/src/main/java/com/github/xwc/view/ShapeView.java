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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by xwc on 2018/2/24.
 */

public class ShapeView extends View implements View.OnClickListener {

    private int borderWidthPx = 0;
    private int borderColor = Color.parseColor("#F66276");
    private int borderDashGap = 0;
    private int borderDashWidth = 0;

    //默认背景图
    private int resDefault = 0;
    //点击背景图
    private int resPressed =0;

    private int resourceId = 0;

    //默认背景色
    private int defaultColor = 0;
    //点击背景色
    private int pressedColor = 0;

    //网络下载bitmap
    private Bitmap mBitmap;

    private int shapeType; //default circle

    /**
     * 圆形
     */
    public static final int CIRCLE = 0;
    /**
     * 圆角矩形
     */
    public static final int ROUND_RECT = 1;
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


    // 矩形圆角  //
    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomRightRadius;
    private int bottomLeftRadius;


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
            resDefault = typedArray.getResourceId(R.styleable.ShapeView_shape_default_drawable, resDefault);
            resPressed = typedArray.getResourceId(R.styleable.ShapeView_shape_pressed_drawable, resPressed);
            pressedColor = typedArray.getColor(R.styleable.ShapeView_shape_pressed_color, pressedColor);
            defaultColor = typedArray.getColor(R.styleable.ShapeView_shape_default_color, defaultColor);


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
        setOnClickListener(this);

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
        if (resourceId == 0) {
            resourceId = resDefault;
        }
        if (resourceId != 0) {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), resourceId), getMeasuredWidth(), getMeasuredHeight(), false);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, new Paint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (resPressed != 0) {
                resourceId = resPressed;
                invalidate();
                return super.onTouchEvent(event);
            } else if (pressedColor != 0) {
                setBackgroundColor(pressedColor);
                return super.onTouchEvent(event);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (clickListener != null) {
                clickListener.onClick(this);
            }
            touchUp();
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            touchUp();
        }
        if (event.getX() < 0 || event.getX() > getWidth() || event.getY() < 0 || event.getY() > getHeight()) {
            touchUp();
        }
        return super.onTouchEvent(event);
    }

    private void touchUp() {
        if (resDefault != 0) {
            resourceId = resDefault;
            invalidate();
        } else if (defaultColor != 0) {
            setBackgroundColor(defaultColor);
        }
    }


    boolean firstDispatchDraw = true;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (defaultColor != 0 && firstDispatchDraw) {
            if (resDefault == 0) {
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
                case 0:
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min((getWidth() - borderWidthPx) / 2f, (getHeight() - borderWidthPx) / 2f), borderPaint);
                    break;
                case 1:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
//                case 2:
//                    Path mPath = new Path();
//                    setTriangleBroadPath(mPath, getWidth(), getHeight());
//                    canvas.drawPath(mPath, borderPaint);
//                    break;
                case 3:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    //Path mPath = new Path();
                    //setHeartPath3(mPath, getMeasuredWidth(), getMeasuredHeight());
                    //canvas.drawPath(mPath, borderPaint);
                    break;
                case 4:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
                case 5:
                    canvas.drawPath(getClipHelper().mPath, borderPaint);
                    break;
//                case 6:
//                    canvas.drawPath(getClipHelper().mPath, borderPaint);
//                    break;
            }

        }
    }


    /**
     * 重画
     */
    public void reDraw() {
        invalidate();
    }


//    private void setStarPath(Path mPath, int halfWidth) {
//        if (turn > 0) { //旋转角度大于0度
//            mPath.moveTo(turnX(halfWidth, 0, halfWidth * 0.73f), turnY(halfWidth, 0, halfWidth * 0.73f));
//            mPath.lineTo(turnX(halfWidth, halfWidth * 2f, halfWidth * 0.73f), turnY(halfWidth, halfWidth * 2f, halfWidth * 0.73f));
//            mPath.lineTo(turnX(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 0.38f, halfWidth * 1.9f));
//            mPath.lineTo(turnX(halfWidth, halfWidth, 0), turnY(halfWidth, halfWidth, 0));//A
//            mPath.lineTo(turnX(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f), turnY(halfWidth, halfWidth * 1.62f, halfWidth * 1.9f));
//        } else {
//            mPath.moveTo(0, halfWidth * 0.73f);
//            mPath.lineTo(halfWidth * 2, halfWidth * 0.73f);
//            mPath.lineTo(halfWidth * 0.38f, halfWidth * 1.9f);
//            mPath.lineTo(halfWidth, 0);
//            mPath.lineTo(halfWidth * 1.62f, halfWidth * 1.9f);
//        }
//        mPath.close();
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

    public ShapeView setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        return this;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public void onClick(android.view.View v) {

    }
}
