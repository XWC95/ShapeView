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
