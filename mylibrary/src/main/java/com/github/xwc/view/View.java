package com.github.xwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by xwc on 2018/2/24.
 */

public class View extends FrameLayout {


    protected final Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected PorterDuffXfermode porterDuffXfermode;
    protected Bitmap mask;

    private ClipHelper clipHelper;

    public View(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        clipPaint.setAntiAlias(true);
        clipPaint.setColor(Color.WHITE);

        setDrawingCacheEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        setWillNotDraw(false);

        clipPaint.setColor(Color.BLACK);
        clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clipPaint.setStrokeWidth(1);

        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);

//            if(attributes.hasValue(R.styleable.ShapeView_shape_drawable)){
//                final int resourceId = attributes.getResourceId(R.styleable.ShapeOfView_clip_drawable, -1);
//                if(resourceId!=-1) {
//                    setDrawable(resourceId);
//                }
//            }

            attributes.recycle();
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
        postInvalidate();
    }

    @Override
    protected void  dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        clipPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(mask, 0.0f, 0.0f, clipPaint);
        clipPaint.setXfermode(null);
    }


    public void setClipHelper(ClipHelper clipHelper){
        this.clipHelper = clipHelper;
    }

    public ClipHelper getClipHelper() {
        return clipHelper;
    }
}
