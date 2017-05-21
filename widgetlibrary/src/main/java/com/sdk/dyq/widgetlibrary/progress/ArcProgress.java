package com.sdk.dyq.widgetlibrary.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.sdk.dyq.widgetlibrary.Utils.ViewUtils;

/**
 * 半圆进度条
 */

public class ArcProgress extends View {
    private Paint paint;
    private RectF rectF;

    private final int default_stroke_width = 16;//弧形宽度16dp
    private final int default_finished_color = Color.rgb(253, 138, 34);//#FD8A22
    private final int default_unfinished_color = Color.rgb(64, 62, 69);//#403E45
    private final int default_max = 100;//最大进度
    private final float default_arc_angle = 280f;//默认弧度


    private float startAngle;//圆弧起始角度，单位为度
    private int progress;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_PROGRESS = "progress";

    public ArcProgress(Context context) {
        this(context,null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        startAngle = 270 - default_arc_angle / 2f;//起始角度为130度，起始为右中间

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ViewUtils.dp2px(context, default_stroke_width));
        paint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int mWidth;
        if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }

        int mHeight;
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = ViewUtils.dp2px(getContext(),15);
        } else {
            mHeight = heightSpecSize;
        }
        if(rectF == null){
            rectF = new RectF();
        }
        rectF.set(default_stroke_width,default_stroke_width,mWidth-default_stroke_width,mHeight-default_stroke_width);
        setMeasuredDimension(mWidth,mHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        float finishedSweepAngle = progress / (float)default_max * default_arc_angle;

        paint.setColor(default_unfinished_color);
        canvas.drawArc(rectF,startAngle,default_arc_angle,false,paint);//默认颜色

        if(finishedSweepAngle>0){
            paint.setColor(default_finished_color);
            canvas.drawArc(rectF,startAngle,finishedSweepAngle,false,paint);
        }
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > default_max) {
            this.progress %= default_max;
        }
        invalidate();
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_PROGRESS,progress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            progress = bundle.getInt(INSTANCE_PROGRESS);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);

    }
    public int getMax(){
        return default_max;
    }
}

