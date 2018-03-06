package com.github.xwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by xwc on 2018/2/24.
 */

public class View extends FrameLayout implements android.view.View.OnClickListener {


    protected final Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected PorterDuffXfermode porterDuffXfermode;
    protected Bitmap mask;

    private ClipHelper clipHelper;
    private ClickListener ClickListener;

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
        clipPaint.setColor(Color.RED);
        setDrawingCacheEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        setWillNotDraw(false);

        clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clipPaint.setStrokeWidth(1);

        setOnClickListener(this);

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

    @Override
    public void onClick(android.view.View view) {
         if(ClickListener != null){
             ClickListener.onClick(view);
         }
    }

    public interface ClickListener {
        void onClick(android.view.View var1);
    }

    public void setClickListener(ClickListener clickListener) {
        this.ClickListener = clickListener;
    }
}
