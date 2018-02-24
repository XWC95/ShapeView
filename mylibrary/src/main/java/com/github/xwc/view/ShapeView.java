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

    private int borderColor = Color.WHITE;

    private int shapeType; //默认是圆形

    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomRightRadius;
    private int bottomLeftRadius;

    private float percentBottom = 0.5f;
    private float percentLeft = 0f;
    private float percentRight = 0f;

    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
            if (shapeType == 1) {
                radius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_radius, radius);
                topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_radius, topLeftRadius);
                topRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_radius, topRightRadius);
                bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_radius, bottomLeftRadius);
                bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeView_shape_radius, bottomRightRadius);
            }
            if (shapeType == 2) {
                percentLeft = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentLeft, percentLeft);
                percentBottom = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentBottom, percentBottom);
                percentRight = typedArray.getFloat(R.styleable.ShapeView_shape_triangle_percentRight, percentRight);
            }
            typedArray.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        super.setClipPathCreator(new ClipHelper() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                switch (shapeType) {
                    case 0:
                        //xy为圆的圆心 radius为圆的半径 Diection.CW 顺时针方向
                        path.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CW);
                        break;
                    case 1:
                        RectF rectF = new RectF();
                        rectF.set(0, 0, width, height);
                        if (radius > 0) {
                            path.set(generatePath(rectF, radius, radius, radius, radius));
                        } else {
                            path.set(generatePath(rectF, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius));
                        }
                        break;
                    case 2:
                        path.moveTo(0, percentLeft * height);
                        path.lineTo(percentBottom * width, height);
                        path.lineTo(width, percentRight * height);
                        path.close();
                        break;

                }
                return path;
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        if (borderWidthPx > 0) {
//            borderPaint.setStrokeWidth(borderWidthPx);
//            borderPaint.setColor(borderColor);
//            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min((getWidth() - borderWidthPx) / 2f, (getHeight() - borderWidthPx) / 2f), borderPaint);
//        }
    }

    private Path generatePath(RectF rect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
        final Path path = new Path();

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

        return path;
    }
}
