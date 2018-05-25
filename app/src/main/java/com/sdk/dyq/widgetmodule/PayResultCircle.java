package com.sdk.dyq.widgetmodule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 *  显示支付结果动画控件
 * Created by deng on 2018/5/22.
 */

public class PayResultCircle extends View {

    //屏幕适配->单位为dp
    private int circleRadius = 28;
    private int barWidth = 3;
    private int rimWidth = 3;


    //region====自由旋转模式==============

    //旋转模式，自由旋转模式为true，标准画圈模式为false
    private boolean isSpinning = false;

    //bar最短的长度
    private final int barLength = 16;
    //最上面自由滚动的圆圈最长的长度
    private int barMaxLength = 270;

    private boolean fillRadius = false;

    private double timeStartGrowing = 0;
    private double barSpinCycleTime = 460;
    /*
     * 每次bar绘制的长度都是由最短的长度加上ExtraLength，这个长度是通过对时间的计算来得到的
     * 像是一个山峰，先变大，在变小，但是不可以超过MaxLength
     */
    private float barExtraLength = 0;
    private boolean barGrowingFromFront = true;

    private long pausedTimeWithoutGrowing = 0;
    private final long pauseGrowingTime = 200;

    //endregion====自由旋转模式==============




    //Colors (with defaults)
    private int barColor = Color.parseColor("#1787f6");
    private int rimColor = 0x00FFFFFF;//圆弧的背景色

    //Paints
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();

    //画圆圈外围的Rectangles
    private RectF circleBounds = new RectF();

    //旋转的速度
    private float spinSpeed = 230.0f;
    private long lastTimeAnimated = 0;

    private float mProgress = 0.0f;
    private float mTargetProgress = 0.0f;



    private int saveToDrawState = STATE_CIRCLE;//保存下将要绘制的阶段
    private int nowDrawingState = STATE_CIRCLE;//当前绘制的阶段
    public static final int STATE_CIRCLE = 0;
    public static final int STATE_TICK = 1;
    public static final int STATE_ERROR = 2;

    //划勾
    private int tickSize = 20;//勾所在的正方形区域边长
    private float lineProgress = 0.0f;//当前勾的进度。  1.0f代表该线完成
    private float lineSpeed = 0.1f;//勾的增长速度
    private boolean isDrawFirst = false;//划勾的第一笔
    private boolean isDrawSecond = false;//划勾的第二笔
    private Handler handler;//防止每次invalidate造成CPU消耗浪费

    private ProgressCallback callback;


    public PayResultCircle(Context context) {
        this(context,null);
    }

    public PayResultCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PayResultCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttribute();
        isSpinning = true;
        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    invalidate();
                }
            }
        };

    }
    public void setCallback(ProgressCallback callback){
        this.callback = callback;
    }
    private void parseAttribute(){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, metrics);
        rimWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth, metrics);
        circleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, circleRadius, metrics);

        tickSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tickSize, metrics);
    }

    /**
     * 测量控件的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = circleRadius + this.getPaddingLeft() + this.getPaddingRight();
        int viewHeight = circleRadius + this.getPaddingTop() + this.getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(viewWidth, widthSize);
        } else {
            //Be whatever you want
            width = viewWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(viewHeight, heightSize);
        } else {
            //Be whatever you want
            height = viewHeight;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * Set the bounds of the component
     * 设置边框，就是绘制圆圈的正方形，紧贴圆形
     */
    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

            int minValue = Math.min(layout_width - paddingLeft - paddingRight,
                    layout_height - paddingBottom - paddingTop);
            //最终确定下来的圆直径
            int circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth * 2);

            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop;

            circleBounds = new RectF(xOffset + barWidth,
                    yOffset + barWidth,
                    xOffset + circleDiameter - barWidth,
                    yOffset + circleDiameter - barWidth);
    }

    /**
     * 初始化画笔
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStrokeWidth(barWidth);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeCap(Paint.Cap.ROUND);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupBounds(w,h);
        setupPaints();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //变量记录做完这次onDraw之后是否需要继续invalidate。
        boolean mustInvalidate = false;
        if(nowDrawingState == STATE_TICK) {
            if (isDrawFirst && !isDrawSecond) {
                //开始画第一条线，获取圆心坐标
                float centerX = circleBounds.centerX();
                float centerY = circleBounds.centerY();
                //绘制外面的圆圈
                canvas.drawArc(circleBounds, 360, 360, false, barPaint);
                //根据速度来绘制直线
                canvas.drawLine(centerX - tickSize / 2, centerY, centerX - tickSize / 2 + tickSize / 2 * lineProgress,
                        centerY + tickSize / 2 * lineProgress, barPaint);
                //当绘制完第一条直线，开始绘制第二条
                if (lineProgress >= 1.0f) {
                    lineProgress = 0f;
                    isDrawSecond = true;
                } else {
                    lineProgress += lineSpeed;
                }
                mustInvalidate = true;
            } else if (isDrawFirst && isDrawSecond) {
                //开始画第二条线，获取圆心
                float centerX = circleBounds.centerX();
                float centerY = circleBounds.centerY();
                //首先要画出第一条直线和外面的圆圈
                canvas.drawArc(circleBounds, 360, 360, false, barPaint);
                canvas.drawLine(centerX - tickSize / 2, centerY, centerX, centerY + tickSize / 2, barPaint);

                canvas.drawLine(centerX, centerY + tickSize / 2, centerX + tickSize / 2 * lineProgress, centerY +
                        tickSize / 2 - tickSize * lineProgress, barPaint);
                //当第二条直线绘制完毕，停止invalidate
                if (lineProgress >= 1.0f) {
                    if(callback!=null){
                        callback.changeState(nowDrawingState);
                    }
                    return;
                } else {
                    lineProgress += lineSpeed;
                    if (lineProgress > 1)
                        lineProgress = 1f;
                    mustInvalidate = true;
                }
            }
        }else if(nowDrawingState == STATE_ERROR){
            float centerX = circleBounds.centerX();
            float centerY = circleBounds.centerY();
            //首先要画出第一条直线和外面的圆圈
            canvas.drawArc(circleBounds, 360, 360, false, barPaint);
            canvas.drawLine(centerX,centerY - tickSize / 2,centerX,centerY-tickSize/2+( 3 * tickSize /4 ) * lineProgress,barPaint);
            if(lineProgress >= 1.0f){
                canvas.drawPoint(centerX,centerY + tickSize/2,barPaint);
                if(callback!=null){
                    callback.changeState(nowDrawingState);
                }
                return;
            }else{
                lineProgress += lineSpeed;
                if(lineProgress >1){
                    lineProgress = 1f;
                }
                mustInvalidate = true;
            }
        }
        else{
            canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

            //自由旋转模式
            if (isSpinning) {
                //Draw the spinning bar
                mustInvalidate = true;

                long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
                float deltaNormalized = deltaTime * spinSpeed / 1000.0f;

                updateBarLength(deltaTime);

                mProgress += deltaNormalized;//自动转时的progress变化
                if (mProgress > 360) {
                    mProgress -= 360f;
//                    runCallback(-1.0f);//回调
                }

                /*
                 * 因为对外面圆圈也就是bar的长度是通过上次绘制时间与当前时间的差的计算来得到的。
                 * 这里对页面的刷新进行了延迟，导致圆圈旋转的时候有点不流畅
                 * 在这里加上5之后，会让其看起来更流畅,不会越位
                 */
                lastTimeAnimated = SystemClock.uptimeMillis() + 5;

                float from = mProgress - 90;
                float length = barLength + barExtraLength;

                if (isInEditMode()) {
                    from = 0;
                    length = 135;
                }

                if(saveToDrawState!=STATE_CIRCLE && from + length >= 360){
                    nowDrawingState = saveToDrawState;
                    if(nowDrawingState == STATE_TICK) {
                        isDrawFirst = true;
                        isDrawSecond = false;
                    }
                }

                canvas.drawArc(circleBounds, from, length, false, barPaint);
            }
            else {//标准画圈模式

                float oldProgress = mProgress;
                if (mProgress != mTargetProgress) {
                    //We smoothly increase the progress bar
                    mustInvalidate = true;

                    float deltaTime = (float) (SystemClock.uptimeMillis() - lastTimeAnimated) / 1000;
                    float deltaNormalized = deltaTime * spinSpeed;

                    mProgress = Math.min(mProgress + deltaNormalized, mTargetProgress);
                    lastTimeAnimated = SystemClock.uptimeMillis() + 15;
                }
                //回调
//            if (oldProgress != mProgress) {
//                runCallback();
//            }

                float offset = 0.0f;
                float progress = mProgress;

                if (isInEditMode()) {
                    progress = 360;
                }
                canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint);
            }

        }
        if (mustInvalidate) {
            Message message = handler.obtainMessage();
            message.what = 1;
            int delayMills = 25;
            if (!isDrawFirst)
                delayMills = 15;
            handler.sendMessageDelayed(message, delayMills);
        }


    }
    /**
     * 当该控件重新变为可见的时候，需要重新开始旋转，所以要重新获取时间
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    /**
     * 这个函数是计算该次绘制圆圈的时候圆弧的长度，主要是得到barExtraLength的大小
     * 最后将该值和barLength（bar长度的最小值）叠加起来
     * 该函数是通过对时间的计算来得到值
     * @param deltaTimeInMilliSeconds
     */
    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
            timeStartGrowing += deltaTimeInMilliSeconds;

            if (timeStartGrowing > barSpinCycleTime) {
                // We completed a size change cycle
                // (growing or shrinking)
                timeStartGrowing -= barSpinCycleTime;
                //if(barGrowingFromFront) {
                pausedTimeWithoutGrowing = 0;
                //}
                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI) / 2 + 0.5f;
            float destLength = (barMaxLength - barLength);

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (barExtraLength - newLength);//弧度增长进行的progress变化
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }
    }

    /**
     * 开始打勾/打感叹号
     */
    public void completeDraw(int state) {
        barMaxLength = 360;
        saveToDrawState = state;
        lineProgress = 0.0f;
        invalidate();
    }


    public interface ProgressCallback {
        public void changeState(int state);
    }

}
