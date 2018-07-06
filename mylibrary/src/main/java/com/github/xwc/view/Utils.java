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

/**
 * Created by xwc on 2018/3/2.
 */

public class Utils {
    /**
     * 角度转弧度公式
     *
     * @param degree
     * @return
     */
    public static float degree2Radian(int degree) {
        return (float) (Math.PI * degree / 180);
    }

    /**
     * 余弦
     * @param num
     * @return
     */
    public static  float cos(int num) {
        return (float) Math.cos(num * Math.PI / 180);
    }

    /**
     * 正弦
     * @param num
     * @return
     */
    public static  float sin(int num) {
        return (float) Math.sin(num * Math.PI / 180);
    }


    /**
     * X坐标围绕中心点旋转
     *
     * @param halfX
     * @param x
     * @param y
     * @return
     */
    public static float turnX(int halfX, float x, float y,float turn) {
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
    public static float turnY(int halfX, float x, float y,float turn) {
        return (float) (-(x - halfX) * Math.sin(turn) + (y - halfX) * Math.cos(turn) + halfX);
    }
}
