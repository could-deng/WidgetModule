package com.sdk.dyq.widgetlibrary.bluepay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.sdk.dyq.widgetlibrary.R;

import java.util.Random;

/**
 * Created by deng on 2018/5/28.
 */

public class BoomColorFlowerView extends View {
    public static final String TAG = "BoomColorFlowerView";

    //碎花宽度 px
    public static final int FLOWER_WIDTH = 4;

    Paint paint;

    int width;
    int height;

//    float widthPerChar;
//    float heithPerChar;
    float t = 0;

//    private RectF rectF;
    private Handler mHandler;


    private boolean startBoom = false;
    //碎花开始区域
    private float startAreaWidth = 0;
    private float startAreaHeight = 0;

    public BoomColorFlowerView(Context context) {
        this(context,null);
    }

    public BoomColorFlowerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BoomColorFlowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }



    private void init(Context context,AttributeSet attrs){
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mRandom = new Random();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        invalidate();
                        break;
                }
            }
        };
        setStartArea(70);
    }

    //region======控件的长宽========
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
    private int measureHeight(int measureSpec)
    {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode)
        {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = (int) (startAreaWidth*3);
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingTop() + getPaddingBottom();
    }

    private int measureWidth(int measureSpec)
    {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode)
        {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = (int) (startAreaHeight*4);
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }


    public void setStartArea(int textSizeSp){
        Paint p = new Paint();
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(sp2px(textSizeSp));
        Rect mTextBounds = new Rect();
        p.getTextBounds("0", 0, 1, mTextBounds);
        startAreaHeight = mTextBounds.height();
        float widths[] = new float[1];
        p.getTextWidths("0", widths);
        startAreaWidth = widths[0];
        Log.e(TAG,"startAreaHeight="+startAreaHeight+",startAreaWidth="+startAreaWidth);
    }

    private int sp2px(float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }


    //endregion======控件的长宽========


    @Override
    protected void onDraw(Canvas canvas) {
        if(startBoom) {
            Log.e(BoomNumberView.TAG, "draw");
            canvas.translate(width / 2, height / 2);

            t += 1f / (500 / 25);

//        int flowerCenterX = (int) (width/2-widthPerChar);
//            int flowerCenterX = 0;
//            int flowerCenterY = 0;

//        Rect rect = new Rect(flowerCenterX- FLOWER_WIDTH /2,flowerCenterY- FLOWER_WIDTH /2,flowerCenterX+ FLOWER_WIDTH /2,flowerCenterY+ FLOWER_WIDTH /2);
//            float[] point0 = new float[]{flowerCenterX, flowerCenterY};
            float[] point0 = getFlowerCenter1();
            RectF rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(point0),
                    getPoint2Right(point0)));


            paint.setColor(getResources().getColor(R.color.progress2_green));
            paint.setAlpha(1);

            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter2();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(point0),
                    getPoint2Right(point0)));


            paint.setColor(Color.RED);
            paint.setAlpha(1);

            canvas.drawRect(rectF, paint);


            if (t < 1) {
                mHandler.sendEmptyMessageDelayed(1, 25);
            } else {
                startBoom = false;
                mHandler.removeCallbacksAndMessages(null);
            }
        }

        if(startAreaHeight!=0 && startAreaWidth!=0) {
            canvas.drawRect(0, 0, startAreaWidth, startAreaHeight, paint);
        }
    }

    RectF rectF1 ;
    RectF rectF2 ;
    RectF rectF3 ;
    RectF rectF4 ;
    float[] flowerCenter1;
    float[] flowerCenter2;
    float[] flowerCenter3;
    float[] flowerCenter4;
    private Random mRandom;

    private float[] getFlowerCenter1(){
        if(flowerCenter1 ==null){
            flowerCenter1 = new float[2];
            flowerCenter1[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter1[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter1;
    }
    private float[] getFlowerCenter2(){
        if(flowerCenter2 ==null){
            flowerCenter2 = new float[2];
            flowerCenter2[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter2[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter2;
    }



    public void startBoomFlower(){
        startBoom = true;
        mHandler.sendEmptyMessage(1);
    }


    /**
     * 根据一个点的坐标作为矩形的中心点画一个矩形RectF
     * @param pointCenter
     * @return
     */
    private RectF getDrawRectf(float pointCenter[]){

        RectF rectF = new RectF(pointCenter[0]- FLOWER_WIDTH /2,pointCenter[1]- FLOWER_WIDTH /2,pointCenter[0]+ FLOWER_WIDTH /2,pointCenter[1]+ FLOWER_WIDTH /2);

        return rectF;
    }

    /**
     * 获取贝塞尔曲线上的点
     * @param t
     * @param point0
     * @param point1
     * @param point2
     * @return
     */
    private float[] getBezierPoint(float t,float point0[],float point1[],float point2[]){
        float bezier[] = new float[2];
        bezier[0] = point0[0] * (1 - t)* (1 - t)
                + point1[0] * 2 * t * (1-t)
                + point2[0] * t * t;

        bezier[1] = point0[1] * (1 - t)* (1 - t)
                + point1[1]* 2 * t * (1-t)
                + point2[1] * t * t;
        Log.e(TAG,"BezierPoint,x->"+bezier[0]+",y->"+bezier[1]);
        return bezier;
    }

    /**
     * 左边
     * 根据point0获取point1
     * @param point0
     * @return
     */
    private float[] getPoint1Right(float[] point0){
        float[] point1 = new float[2];
        //point1
        point1[0] = point0[0] + (1*startAreaWidth);
        point1[1] = point0[1] - (30);//+ random.nextInt(5) + random.nextFloat();
        return point1;

    }

    /**
     * 根据point0获取point2
     * @param point0
     * @return
     */
    private float[] getPoint2Right(float[] point0){
        float[] point2 = new float[2];
        //point2
        point2[0] = point0[0]+2*startAreaWidth;
        point2[1] = point0[1];

        return point2;

    }


}
