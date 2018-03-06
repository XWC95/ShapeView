package com.github.xwc.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import static com.github.xwc.view.ShapeView.CIRCLE;
import static com.github.xwc.view.ShapeView.DIAGONAL;
import static com.github.xwc.view.ShapeView.DIRECTION_LEFT;
import static com.github.xwc.view.ShapeView.HEART;
import static com.github.xwc.view.ShapeView.POLYGON;
import static com.github.xwc.view.ShapeView.POSITION_BOTTOM;
import static com.github.xwc.view.ShapeView.POSITION_LEFT;
import static com.github.xwc.view.ShapeView.POSITION_RIGHT;
import static com.github.xwc.view.ShapeView.POSITION_TOP;
import static com.github.xwc.view.ShapeView.ROUND_RECT;
import static com.github.xwc.view.ShapeView.STAR;
import static com.github.xwc.view.ShapeView.TRIANGLE;

/**
 * Created by xwc on 2018/2/24.
 */

public abstract class ClipHelper implements ClipPath {

    protected final Path mPath = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ShapeView shapeView;

    /**
     * @param view ShapeView
     */
    public ClipHelper(ShapeView view) {
        shapeView = view;
    }

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
     * 根据shapeType设置不同path
     *
     * @param path      Path
     * @param shapeType 图像类型
     * @param width     图像宽
     * @param height    图像高
     */
    public void setClipPath(Path path, int shapeType, int width, int height) {
        switch (shapeType) {
            case CIRCLE: //circle
                setCirclePath(path, width, height);
                break;
            case ROUND_RECT://roundRect
                RectF rectF = new RectF();
                rectF.set(0, 0, width, height);
                int radius = shapeView.getRadius();
                if (shapeView.getRadius() > 0) {
                    setRoundRectPath(rectF, path, radius, radius, radius, radius);
                } else {
                    int topLeftRadius = shapeView.getTopLeftRadius();
                    int topRightRadius = shapeView.getTopRightRadius();
                    int bottomRightRadius = shapeView.getBottomRightRadius();
                    int bottomLeftRadius = shapeView.getBottomLeftRadius();
                    setRoundRectPath(rectF, path, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
                }
                break;
            case TRIANGLE://triangle
                setTrianglePath(path, width, height);
                break;
            case HEART://heart
                setHeartPath(path, width, height);
                break;
            case POLYGON: //equilateralPolygon
                RectF polygonRectF = new RectF();
                polygonRectF.set(0, 0, width, height);
                setPolygonPath(polygonRectF, path);
                break;
            case STAR: //star
                //setStarPath(mPath, width / 2);
                //float outR = width / 2f - borderWidthPx *2;
                //float inR = outR * sin(18) / sin(180 - 36 - 18) - borderWidthPx;

                float outR = width / 2f - shapeView.getBorderWidthPx();
                float inR = outR * Utils.sin(18) / Utils.sin(180 - 36 - 18) - shapeView.getBorderWidthPx();
                setStarPath(path, outR, inR);
                break;
            case DIAGONAL:
                setDiagonalPath(path, width, height);
                break;
            default:
                break;
        }
    }


    @Override
    public void setCirclePath(Path path, int width, int height) {
        //xy为圆的圆心 radius为圆的半径 Diection.CW 顺时针方向
        path.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CW);
    }


    @Override
    public void setRoundRectPath(RectF rect, Path path, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
        topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
        topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
        bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
        bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;
        float borderWidth = shapeView.getBorderWidthPx() / 2;
        path.moveTo(rect.left + topLeftDiameter + borderWidth, rect.top + borderWidth);

        path.lineTo(rect.right - topRightDiameter - borderWidth, rect.top + borderWidth);
        path.quadTo(rect.right - borderWidth, rect.top + borderWidth, rect.right - borderWidth, rect.top + topRightDiameter + borderWidth);
        path.lineTo(rect.right - borderWidth, rect.bottom - bottomRightDiameter - borderWidth);
        path.quadTo(rect.right - borderWidth, rect.bottom - borderWidth, rect.right - bottomRightDiameter - borderWidth, rect.bottom - borderWidth);
        path.lineTo(rect.left + bottomLeftDiameter + borderWidth, rect.bottom - borderWidth);
        path.quadTo(rect.left + borderWidth, rect.bottom - borderWidth, rect.left + borderWidth, rect.bottom - bottomLeftDiameter - borderWidth);
        path.lineTo(rect.left + borderWidth, rect.top + topLeftDiameter + borderWidth);
        path.quadTo(rect.left + borderWidth, rect.top + borderWidth, rect.left + topLeftDiameter + borderWidth, rect.top + borderWidth);
        path.close();
    }

    @Override
    public void setTriangleBroadPath(Path path, int width, int height) {
        float borderWidth = shapeView.getBorderWidthPx();
        path.moveTo(0 + borderWidth, shapeView.getPercentLeft() * height + borderWidth / 2);
        path.lineTo(shapeView.getPercentBottom() * width - borderWidth / 2, height - borderWidth);
        path.lineTo(width - borderWidth, shapeView.getPercentBottom() * height + borderWidth / 2);
        path.close();
    }


    @Override
    public void setTrianglePath(Path path, int width, int height) {
        float borderWidth = shapeView.getBorderWidthPx() / 2;
        path.moveTo(0 + borderWidth, shapeView.getPercentLeft() * height + borderWidth);
        path.lineTo(shapeView.getPercentBottom() * width - borderWidth, height - borderWidth);
        path.lineTo(width - borderWidth, shapeView.getPercentRight() * height + borderWidth);
        path.close();
    }


    @Override
    public void setHeartPath(Path path, int width, int height) {
        int borderWidth = shapeView.getBorderWidthPx() / 2;
        path.moveTo(0.5f * width, shapeView.getHeartYPercent() * height + borderWidth);
        path.cubicTo(0.15f * width + borderWidth, -shapeView.getHeartRadian() * height + borderWidth, -0.4f * width + borderWidth, 0.45f * height + borderWidth, 0.5f * width, height - borderWidth);
//        mPath.moveTo(0.5f * width, height);
        path.cubicTo(width + 0.4f * width - borderWidth, 0.45f * height + borderWidth, width - 0.15f * width - borderWidth, -shapeView.getHeartRadian() * height + borderWidth, 0.5f * width, shapeView.getHeartYPercent() * height + borderWidth);
        path.close();
    }


    @Override
    public void setPolygonPath(RectF rect, Path path) {
        int sides = shapeView.getSides();
        if (sides < 3) {
            return;
        }
        float r = (rect.right - rect.left) / 2 - shapeView.getBorderWidthPx();
        float mX = (rect.right + rect.left) / 2;
        float my = (rect.top + rect.bottom) / 2;
        for (int i = 0; i <= sides; i++) {
            // - 0.5 : Turn 90 °
            float alpha = Double.valueOf(((2f / sides) * i - shapeView.getTurn()) * Math.PI).floatValue();
            float nextX = mX + Double.valueOf(r * Math.cos(alpha)).floatValue();
            float nextY = my + Double.valueOf(r * Math.sin(alpha)).floatValue();
            if (i == 0) {
                path.moveTo(nextX, nextY);
            } else {
                path.lineTo(nextX, nextY);

            }
        }
        path.close();
    }



    @Override
    public void setStarPath(Path path, float outR, float inR) {

//        mPath.moveTo(outR * cos(72 * 0) + outR, outR * sin(72 * 0) + outR);
//
//        mPath.moveTo(outR * cos(72 * 0) + outR, outR * sin(72 * 0) + outR);
//        mPath.lineTo(inR * cos(72 * 0 + 36) + outR, inR * sin(72 * 0 + 36) + outR);
//        mPath.lineTo(outR * cos(72 * 1) + outR, outR * sin(72 * 1) + outR);
//        mPath.lineTo(inR * cos(72 * 1 + 36) + outR, inR * sin(72 * 1 + 36) + outR);
//        mPath.lineTo(outR * cos(72 * 2) + outR, outR * sin(72 * 2) + outR);
//        mPath.lineTo(inR * cos(72 * 2 + 36) + outR, inR * sin(72 * 2 + 36) + outR);
//        mPath.lineTo(outR * cos(72 * 3) + outR, outR * sin(72 * 3) + outR);
//        mPath.lineTo(inR * cos(72 * 3 + 36) + outR, inR * sin(72 * 3 + 36) + outR);
//        mPath.lineTo(outR * cos(72 * 4) + outR, outR * sin(72 * 4) + outR);
//        mPath.lineTo(inR * cos(72 * 4 + 36) + outR, inR * sin(72 * 4 + 36) + outR);
//        mPath.close();

        int borderWidthPx = shapeView.getBorderWidthPx();
        float radius = outR;
        float radian = Utils.degree2Radian(36); // 36为五角星的角度
        float radiusIn = (float) (radius * Math.sin(radian / 2) / Math
                .cos(radian)); // 中间五边形的半径

        path.moveTo((float) (radius * Math.cos(radian / 2) + borderWidthPx), borderWidthPx); // 此点为多边形的起点
        path.lineTo((float) (radius * Math.cos(radian / 2) + radiusIn
                        * Math.sin(radian) + borderWidthPx),
                (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) * 2 + borderWidthPx),
                (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) + radiusIn
                        * Math.cos(radian / 2) + borderWidthPx),
                (float) (radius + radiusIn * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo(
                (float) (radius * Math.cos(radian / 2) + radius
                        * Math.sin(radian) + borderWidthPx), (float) (radius + radius
                        * Math.cos(radian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) + borderWidthPx), radius + radiusIn + borderWidthPx);
        path.lineTo(
                (float) (radius * Math.cos(radian / 2) - radius
                        * Math.sin(radian) + borderWidthPx), (float) (radius + radius
                        * Math.cos(radian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) - radiusIn
                        * Math.cos(radian / 2) + borderWidthPx),
                (float) (radius + radiusIn * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo(borderWidthPx, (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(radian / 2) - radiusIn
                        * Math.sin(radian) + borderWidthPx),
                (float) (radius - radius * Math.sin(radian / 2) + borderWidthPx));

        path.close();
    }



    @Override
    public void setDiagonalPath(Path path, int width, int height) {
        final float perpendicularHeight = (float) (width * Math.tan(Math.toRadians(shapeView.getDiagonalAngle())));
        final boolean isDirectionLeft = shapeView.getDiagonalDirection() == DIRECTION_LEFT;
        int paddingLeft = shapeView.getPaddingLeft();
        int paddingRight = shapeView.getPaddingRight();
        int paddingTop = shapeView.getPaddingTop();
        int paddingBottom = shapeView.getPaddingBottom();
        switch (shapeView.getDiagonalPosition()) {
            case POSITION_BOTTOM:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft, paddingRight);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - perpendicularHeight - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - perpendicularHeight - paddingBottom);
                    path.lineTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.close();
                }
                break;
            case POSITION_TOP:
                if (isDirectionLeft) {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(width - paddingRight, paddingTop + perpendicularHeight);
                    path.lineTo(paddingLeft, paddingTop);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(paddingLeft, paddingTop + perpendicularHeight);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                }
                break;
            case POSITION_RIGHT:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight - perpendicularHeight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight - perpendicularHeight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                }
                break;
            case POSITION_LEFT:
                if (isDirectionLeft) {
                    path.moveTo(paddingLeft + perpendicularHeight, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft, height - paddingBottom);
                    path.close();
                } else {
                    path.moveTo(paddingLeft, paddingTop);
                    path.lineTo(width - paddingRight, paddingTop);
                    path.lineTo(width - paddingRight, height - paddingBottom);
                    path.lineTo(paddingLeft + perpendicularHeight, height - paddingBottom);
                    path.close();
                }
                break;
            default:
                break;
        }
    }
}
