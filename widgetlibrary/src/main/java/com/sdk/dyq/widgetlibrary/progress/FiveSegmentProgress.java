package com.sdk.dyq.widgetlibrary.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import com.sdk.dyq.widgetlibrary.Utils.ViewUtils;

/**
 * 五种颜色段区域圆
 */

public class FiveSegmentProgress extends View {
    private Paint paint;
    private float strokeWidth;
    private RectF rectF;

    private float arcAngle;//所要画的整个弧的总度数
    private int progress; // 当前百分比
    private int max;
    private float Type1Progress;
    private float Type2Progress;
    private float Type3Progress;
    private float Type4Progress;
    private float Type5Progress;

    private int unfinishedStrokeColor;

    private float startAngle;//圆弧起始角度，单位为度

    private final int default_stroke_width = 16;//弧形宽度16dp
    private final int default_unfinished_color = Color.rgb(64, 62, 69);//#403E45

    private final float default_max = 100;//最大进度
    private final float default_arc_angle = 360f;//默认弧度

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_PROGRESS = "progress";

    private static final String INSTANCE_PROGRESS1 = "progress1";
    private static final String INSTANCE_PROGRESS2 = "progress2";
    private static final String INSTANCE_PROGRESS3 = "progress3";
    private static final String INSTANCE_PROGRESS4 = "progress4";
    private static final String INSTANCE_PROGRESS5 = "progress5";

    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";


    //五部分各项数据参数
    private final int blue_color = Color.rgb(36,168,253);
    private final int green_color = Color.rgb(29,230,119);
    private final int slight_yellow_color= Color.rgb(255,191,34);
    private final int normal_yellow_color = Color.rgb(255,139,34);
    private final int dark_yellow_color = Color.rgb(255,86,34);



    public FiveSegmentProgress(Context context) {
        this(context,null);
    }

    public FiveSegmentProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FiveSegmentProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rectF = new RectF();
        strokeWidth = default_stroke_width;
        strokeWidth = ViewUtils.dp2px(getContext().getResources(), strokeWidth);//dp转px
        setArcAngle(default_arc_angle);
        startAngle = 270 - arcAngle / 2f;//设置起始角度，方法画圆默认起始为右中间
        setMax(default_max);
        unfinishedStrokeColor = default_unfinished_color;

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.BUTT);
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

        rectF.set(strokeWidth / 2f, strokeWidth / 2f, mWidth - strokeWidth / 2f, mHeight - strokeWidth / 2f);

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(unfinishedStrokeColor);
        //圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);//画整个圆为默认颜色

        float finishedSweepAngle = progress / (float) getMax() * arcAngle; // 当前的弧度
        float finished1SweepAngle = Type1Progress / (float) getMax() * arcAngle;
        float finished2SweepAngle = Type2Progress / (float) getMax() * arcAngle;
        float finished3SweepAngle = Type3Progress / (float) getMax() * arcAngle;
        float finished4SweepAngle = Type4Progress / (float) getMax() * arcAngle;

        if(progress >= getMax() && finishedSweepAngle >= arcAngle){
            progress = getMax();
            finishedSweepAngle = arcAngle;
        }
        if(progress > 0){
            //第一阶段
            paint.setColor(blue_color);
            canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint);//画完成部分地百分比圈颜色
        }
        if(progress > Type1Progress) {
            //第二阶段
            paint.setColor(green_color);
            canvas.drawArc(rectF, startAngle + finished1SweepAngle , finishedSweepAngle-finished1SweepAngle, false, paint);//画完成部分地百分比圈颜色
        }
        if(progress > (Type1Progress + Type2Progress)) {
            //第三阶段
            paint.setColor(slight_yellow_color);
            canvas.drawArc(rectF, startAngle + finished1SweepAngle + finished2SweepAngle , finishedSweepAngle-finished1SweepAngle-finished2SweepAngle, false, paint);//画完成部分地百分比圈颜色
        }
        if(progress > (Type1Progress + Type2Progress + Type3Progress)) {
            //第四阶段
            paint.setColor(normal_yellow_color);
            canvas.drawArc(rectF, startAngle + finished1SweepAngle + finished2SweepAngle + finished3SweepAngle, finishedSweepAngle-finished1SweepAngle-finished2SweepAngle-finished3SweepAngle, false, paint);//画完成部分地百分比圈颜色
        }
        if(progress > (Type1Progress + Type2Progress + Type3Progress + Type4Progress )) {
            //第五阶段
            paint.setColor(dark_yellow_color);
            canvas.drawArc(rectF, startAngle + finished1SweepAngle + finished2SweepAngle + finished3SweepAngle + finished4SweepAngle,finishedSweepAngle-finished1SweepAngle-finished2SweepAngle-finished3SweepAngle-finished4SweepAngle, false, paint);//画完成部分地百分比圈颜色
        }
    }



    /**
     * 设置进度
     * @param type1Progress
     * @param type2Progress
     * @param type3Progress
     * @param type4Progress
     * @param type5Progress
     */
    public void setAllType(float type1Progress,float type2Progress,float type3Progress, float type4Progress , float type5Progress){
        setMax((type1Progress + type2Progress + type3Progress + type4Progress + type5Progress));
        this.Type1Progress = type1Progress;
        this.Type2Progress = type2Progress;
        this.Type3Progress = type3Progress;
        this.Type4Progress = type4Progress;
        this.Type5Progress = type5Progress;
    }

    public float getType1Progress() {
        return Type1Progress;
    }

    public float getType2Progress() {
        return Type2Progress;
    }

    public float getType3Progress() {
        return Type3Progress;
    }

    public float getType4Progress() {
        return Type4Progress;
    }

    public float getType5Progress() {
        return Type5Progress;
    }

    /**
     * 设置最大进度
     *
     * @param max 最大进度
     */
    public void setMax(float max) {
        if (max > 0) {
            this.max = Math.round(max);
        }else{
            this.max = 0;
        }
    }
    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > max) {
            this.progress %= max;
        }
        invalidate();
    }

    /**
     * 获取进度条宽度
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置进度条宽度
     *
     * @param strokeWidth 进度条宽度
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * 设置进度
     */
    public int getMax() {
        return max;
    }

    /**
     * 获取进度条弧度
     */
    public float getArcAngle() {
        return arcAngle;
    }
    /**
     * 获取进度
     */
    public int getProgress() {
        return progress;
    }


    /**
     * 设置进度条弧度
     *
     * @param arcAngle 进度条弧度(0,360)
     */
    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());

        bundle.putFloat(INSTANCE_PROGRESS1, getType1Progress());
        bundle.putFloat(INSTANCE_PROGRESS2, getType2Progress());
        bundle.putFloat(INSTANCE_PROGRESS3, getType3Progress());
        bundle.putFloat(INSTANCE_PROGRESS4, getType4Progress());
        bundle.putFloat(INSTANCE_PROGRESS5, getType5Progress());

        bundle.putFloat(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            setMax(bundle.getFloat(INSTANCE_MAX));
            setAllType(bundle.getFloat(INSTANCE_PROGRESS1), bundle.getFloat(INSTANCE_PROGRESS2),bundle.getFloat(INSTANCE_PROGRESS3),
                    bundle.getFloat(INSTANCE_PROGRESS4),bundle.getFloat(INSTANCE_PROGRESS5));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));

            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
