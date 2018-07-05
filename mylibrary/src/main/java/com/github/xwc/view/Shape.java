package com.github.xwc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xwc on 2018/2/24.
 */

public class Shape extends FrameLayout {

    private onShapeClickListener onShapeClickListener;

    protected final Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected PorterDuffXfermode porterDuffXfermode;
    protected Bitmap mask;

    private ClipHelper clipHelper;

    protected int borderWidthPx;
    protected int borderColor = Color.parseColor("#F66276");
    protected int borderDashGap;
    protected int borderDashWidth;

    //默认背景图
    @DrawableRes
    protected int defaultDrawable;
    //点击背景图
    @DrawableRes
    protected int pressedDrawable;

    @DrawableRes
    protected int drawable;

    //默认背景色
    @ColorInt
    protected int defaultColor;
    //点击背景色
    @ColorInt
    protected int pressedColor;

    //网络下载bitmap
    protected Bitmap mBitmap;

    public Shape(@NonNull Context context) {
        this(context, null);
    }

    public Shape(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Shape(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Shape);

        borderWidthPx = typedArray.getDimensionPixelSize(R.styleable.Shape_shape_borderWidth, borderWidthPx);
        borderColor = typedArray.getColor(R.styleable.Shape_shape_borderColor, borderColor);
        borderDashGap = typedArray.getDimensionPixelSize(R.styleable.Shape_shape_borderDashGap, borderDashGap);
        borderDashWidth = typedArray.getDimensionPixelSize(R.styleable.Shape_shape_borderDashWidth, borderDashGap);
        defaultDrawable = typedArray.getResourceId(R.styleable.Shape_shape_default_drawable, defaultDrawable);
        pressedDrawable = typedArray.getResourceId(R.styleable.Shape_shape_pressed_drawable, pressedDrawable);
        pressedColor = typedArray.getColor(R.styleable.Shape_shape_pressed_color, pressedColor);
        defaultColor = typedArray.getColor(R.styleable.Shape_shape_default_color, defaultColor);

        typedArray.recycle();

        clipPaint.setAntiAlias(true);
        clipPaint.setColor(Color.RED);
        clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clipPaint.setStrokeWidth(1);

        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        setDrawingCacheEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setClickable(true);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        setWillNotDraw(false);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable == 0) {
            drawable = defaultDrawable;
        }
        if (drawable != 0) {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), drawable), getMeasuredWidth(), getMeasuredHeight(), false);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, new Paint());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    private void calculateLayout() {
        if (clipHelper != null) {
            final int height = getMeasuredHeight();
            final int width = getMeasuredWidth();
            if (width > 0 && height > 0) {
                if (mask != null && !mask.isRecycled()) {
                    mask.recycle();
                }
                if (clipHelper != null) {
                    clipHelper.setUpClipPath(width, height);
                    mask = clipHelper.createMask(width, height);
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        clipPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(mask, 0.0f, 0.0f, clipPaint);
        clipPaint.setXfermode(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pressedDrawable != 0) {
                    drawable = pressedDrawable;
                    invalidate();
                } else if (pressedColor != 0) {
                    setBackgroundColor(pressedColor);
                }
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                if (onShapeClickListener != null) {
                    onShapeClickListener.onClick(this);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                touchUp();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void touchUp() {
        if (defaultDrawable != 0) {
            drawable = defaultDrawable;
            invalidate();
        } else if (defaultColor != 0) {
            setBackgroundColor(defaultColor);
        }
    }

    public void setClipHelper(ClipHelper clipHelper) {
        this.clipHelper = clipHelper;
    }

    public ClipHelper getClipHelper() {
        return clipHelper;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void reDraw() {
        invalidate();
    }

    public void setOnShapeClickListener(Shape.onShapeClickListener onShapeClickListener) {
        this.onShapeClickListener = onShapeClickListener;
    }

    public interface onShapeClickListener {
        void onClick(View v);
    }
}
