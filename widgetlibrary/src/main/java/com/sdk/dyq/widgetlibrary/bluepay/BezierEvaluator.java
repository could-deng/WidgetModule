package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 贝塞尔曲线估值
 * Created by deng on 2018/5/25.
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {


    //中间控制点
    private PointF point1;

    public BezierEvaluator(PointF point1) {
        this.point1 = point1;
    }

//    @Override
//    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
//        return null;
//    }

    /**
     *
     * @param t
     * @param point0 起点
     * @param point2 终点
     * @return
     */
    @Override
    public PointF evaluate(float t, PointF point0, PointF point2) {
        PointF point = new PointF();
        //t 取值为 [0,1]

        /**
         * 二阶贝塞尔公式
         */
        point.x = point0.x * (1 - t)* (1 - t)
                + point1.x * 2 * t * (1-t)
                + point2.x * t * t;

        point.y = point0.y * (1 - t)* (1 - t)
                + point1.y * 2 * t * (1-t)
                + point2.y * t * t;
        /**
         * 三阶贝塞尔公式
         *
         * B(t)=(1 - t)^3 P0
         *     + 3 t (1 - t)^2 P1
         *     + 3 t^2 (1 - t) P2
         *     + t^3 P3
         *
         * B(t)=(1 - t)^3 P0
         *     + 3 t (1 - t)^2 P1
         *     + 3 t^2 (1 - t) P2
         *     + t^3 P3
         */
//        point.x = point0.x * (1 - t) * (1 - t) * (1 - t)
//                + 3 * point1.x * t * (1 - t) * (1 - t)
//                + 3 * point2.x * t * t * (1 - t)
//                + point3.x * t * t * t;
//
//        point.y = point0.y * (1 - t) * (1 - t) * (1 - t)
//                + 3 * point1.y * t * (1 - t) * (1 - t)
//                + 3 * point2.y * t * t * (1 - t)
//                + point3.y * t * t * t;

        return point;
    }
}
