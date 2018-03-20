# ShapView
给任何一个View剪裁不同形状，并且代替shape

[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)


 ## APK文件

扫描二维码 或者 点击二维码 下载

[![ShapeView](https://github.com/xwc520/ShapView/raw/master/image/qrcode.png)](https://github.com/xwc520/ShapView/raw/master/image/app-release.apk)

#### v1.2.2 
    1.可展示默认颜色和点击时颜色
    2.可展示默认图片和点击时图片

#### v1.2.1 
    1.新增shape_drawble属性
    2.url方式加载图片 
    
 <img src="image/image9.png" width="150px" height="150px" /><img src="image/image10.jpg" width="150px" width="280px"/>

## Gradle
```
compile 'com.github.xwc:ShapeView:1.2.2'
```
## preview
<img src="image/example1.jpg" width="280px"/><img src="image/example2.jpg" width="280px"/><img src="image/example3.jpg" width="280px"/>


## example
<img src="image/image1.png" />

```xml
<com.github.xwc.view.ShapeView
     android:id="@+id/heartShapeView"
     android:layout_width="150dp"
     android:layout_height="150dp"
     app:shape_borderWidth="3dp"
     app:shape_heart_YPercent="0.16"
     app:shape_heart_radian="0.2"
     app:shape_drawable="@mipmap/image4"
     app:shape_type="heart">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="ShapeView"
        android:textColor="#87CEEB"
        android:textSize="18sp"
        android:textStyle="bold" />

</com.github.xwc.view.ShapeView>
```



## Code
```java
      /**
     * 圆形
     */
    @Override
    public void setCirclePath(Path path, int width, int height) {
        //xy为圆的圆心 radius为圆的半径 Diection.CW 顺时针方向
        path.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CW);
    }

    /**
     * 带圆角的矩形
     */
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

    /**
     * 三角形
     */
    @Override
    public void setTrianglePath(Path path, int width, int height) {
        float borderWidth = shapeView.getBorderWidthPx() / 2;
        path.moveTo(0 + borderWidth, shapeView.getPercentLeft() * height + borderWidth);
        path.lineTo(shapeView.getPercentBottom() * width - borderWidth, height - borderWidth);
        path.lineTo(width - borderWidth, shapeView.getPercentRight() * height + borderWidth);
        path.close();
    }

    /**
     * 心形
     */
    @Override
    public void setHeartPath(Path path, int width, int height) {
        int borderWidth = shapeView.getBorderWidthPx() / 2;
        path.moveTo(0.5f * width, 0.16f * height + borderWidth);
        path.cubicTo(0.15f * width + borderWidth, -shapeView.getRadian() * height + borderWidth, -0.4f * width + borderWidth, 0.45f * height + borderWidth, 0.5f * width, height - borderWidth);
//        path.moveTo(0.5f * width, height);
        path.cubicTo(width + 0.4f * width - borderWidth, 0.45f * height + borderWidth, width - 0.15f * width - borderWidth, -shapeView.getRadian() * height + borderWidth, 0.5f * width, 0.16f * height + borderWidth);
        path.close();
    }

    /**
     * 正多边形
     */
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


    /**
     * 星星
     */
    @Override
    public void setStarPath(Path path, float outR, float inR) {

        int borderWidthPx = shapeView.getBorderWidthPx();
        float radius = outR;
        float heartRadian = Utils.degree2Radian(36);// 36为五角星的角度
        float radius_in = (float) (radius * Math.sin(heartRadian / 2) / Math
                .cos(heartRadian)); // 中间五边形的半径

        path.moveTo((float) (radius * Math.cos(heartRadian / 2) + borderWidthPx), borderWidthPx);// 此点为多边形的起点
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) + radius_in
                        * Math.sin(heartRadian) + borderWidthPx),
                (float) (radius - radius * Math.sin(heartRadian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) * 2 + borderWidthPx),
                (float) (radius - radius * Math.sin(heartRadian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) + radius_in
                        * Math.cos(heartRadian / 2) + borderWidthPx),
                (float) (radius + radius_in * Math.sin(heartRadian / 2) + borderWidthPx));
        path.lineTo(
                (float) (radius * Math.cos(heartRadian / 2) + radius
                        * Math.sin(heartRadian) + borderWidthPx), (float) (radius + radius
                        * Math.cos(heartRadian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) + borderWidthPx), radius + radius_in + borderWidthPx);
        path.lineTo(
                (float) (radius * Math.cos(heartRadian / 2) - radius
                        * Math.sin(heartRadian) + borderWidthPx), (float) (radius + radius
                        * Math.cos(heartRadian) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) - radius_in
                        * Math.cos(heartRadian / 2) + borderWidthPx),
                (float) (radius + radius_in * Math.sin(heartRadian / 2) + borderWidthPx));
        path.lineTo(borderWidthPx, (float) (radius - radius * Math.sin(heartRadian / 2) + borderWidthPx));
        path.lineTo((float) (radius * Math.cos(heartRadian / 2) - radius_in
                        * Math.sin(heartRadian) + borderWidthPx),
                (float) (radius - radius * Math.sin(heartRadian / 2) + borderWidthPx));

        path.close();
    }


    /**
     * 对角线图形
     */
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
        }
    }
```


## attrs 属性
```
  <declare-styleable name="ShapeView">
        <!--边框颜色-->
        <attr name="shape_borderColor" format="color" />
        <!--边框宽度-->
        <attr name="shape_borderWidth" format="dimension" />
        <!--虚线间隙-->
        <attr name="shape_borderDashGap" format="dimension" />
        <!--虚线宽度-->
        <attr name="shape_borderDashWidth" format="dimension" />
        <!-- res图片-->
        <attr name="shape_drawable" format="reference" />


        <!--形状类型-->
        <attr name="shape_type" format="enum">
            <!--圆形-->
            <enum name="circle" value="0" />
            <!--圆角矩形-->
            <enum name="roundRect" value="1" />
            <!--三角形-->
            <enum name="triangle" value="2" />
            <!--心形-->
            <enum name="heart" value="3" />
            <!--正多边形-->
            <enum name="polygon" value="4" />
            <!--五角星-->
            <enum name="star" value="5" />
            <!--对角线-->
            <enum name="diagonal" value="6" />
        </attr>

        <!--圆角矩形的四边弧度可分别配置或者直接使用shape_roundRect_radius同时配置-->
        <attr name="shape_roundRect_bottomLeftRadius" format="dimension" />
        <attr name="shape_roundRect_bottomRightRadius" format="dimension" />
        <attr name="shape_roundRect_topLeftRadius" format="dimension" />
        <attr name="shape_roundRect_topRightRadius" format="dimension" />
        <attr name="shape_roundRect_radius" format="dimension" />
        <!--圆角矩形点击颜色-->
        <attr name="shape_roundRect_pressed_color" format="color" />
        <!--圆角矩形默认颜色-->
        <attr name="shape_roundRect_default_color" format="color" />


        <!--三角形三个顶点的起始位置 0～1 float类型-->
        <attr name="shape_triangle_percentLeft" format="float" />
        <attr name="shape_triangle_percentBottom" format="float" />
        <attr name="shape_triangle_percentRight" format="float" />


        <!--心形弧度-->
        <attr name="shape_heart_radian" format="float" />
        <!--心形上面连接点位置 0～1 float类型-->
        <attr name="shape_heart_YPercent" format="float" />

        <!--多边形形边数-->
        <attr name="shape_polygon_side" format="integer" />
        <!--多边形按照XY中心点旋转  0.5为90°-->
        <attr name="shape_polygon_turn" format="float" />

        <!-- <attr name="shape_star_turn" format="float" />-->


        <!--对角线对角的起点 -->
        <attr name="shape_diagonal_direction" format="enum">
            <enum name="left" value="1" />
            <enum name="right" value="2" />
        </attr>
        <!--对角线对角在哪个位置-->
        <attr name="shape_diagonal_position" format="enum">
            <enum name="bottom" value="1" />
            <enum name="top" value="2" />
            <enum name="left" value="3" />
            <enum name="right" value="4" />
        </attr>
        <attr name="shape_diagonal_angle" format="integer" />

    </declare-styleable>
```



### <a href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=947017886@qq.com" >联系邮箱</a>

## LICENSE
```
Copyright 2018 xwc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

















