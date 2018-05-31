package com.sdk.dyq.widgetlibrary.bluepay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sdk.dyq.widgetlibrary.R;

import java.util.Random;

/**
 * Created by deng on 2018/5/28.
 */

public class BoomColorFlowerView extends View {
    public static final String TAG = "BoomColorFlowerView";

    //动画的总时常,单位ms
    public static final int TIME_Totally = 500;
    //动画刷新的间隔,单位ms
    public static final int TIME_REFRESH_DELAY = 20;

    public static final int Time_ALPHA_SHOW = 117;
    public static final int TIME_ALPHA_GONE = 150;

    //碎花宽度 px
    public static final int FLOWER_WIDTH = 4;

    Paint paint;
    private Matrix matrix;

    int width;
    int height;

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
        matrix=new Matrix();
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);

        mRandom = new Random();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1://调整矩形位置
                        invalidate();
                        break;
                    case 2:

                        break;
                }
            }
        };
//        setStartArea(50);
    }

    //region======控件的长宽========
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.e(TAG,"onSizeChanged(),width"+width+",height"+height);
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
                result = (int) (startAreaHeight*3);
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
                result = (int) (startAreaWidth*4);
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }


//    public void setStartArea(int textSizeSp){
//        Paint p = new Paint();
//        p.setTextAlign(Paint.Align.LEFT);
//        p.setTextSize(sp2px(textSizeSp));
//        Rect mTextBounds = new Rect();
//        p.getTextBounds("0", 0, 1, mTextBounds);
//        startAreaHeight = mTextBounds.height();
//        float widths[] = new float[1];
//        p.getTextWidths("0", widths);
//        startAreaWidth = widths[0];
//        Log.e(TAG,"startAreaHeight="+startAreaHeight+",startAreaWidth="+startAreaWidth);
//    }
//
//    private int sp2px(float dpVal)
//    {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                dpVal, getResources().getDisplayMetrics());
//    }

    public void setStartArea(float width,float height){
        this.startAreaHeight = height;
        this.startAreaWidth = width;
        requestLayout();
    }

    //endregion======控件的长宽========

    int alpha = 0;
    int animNowDuration = 0;
    @Override
    protected void onDraw(Canvas canvas) {
//        Log.e(BoomNumberView.TAG, "draw(),getMeasureWidth"+getMeasuredWidth()+",getMeasureHeight"+getMeasuredHeight());
        canvas.translate(0, height / 3);
        if(startBoom) {
            t += 1f / ((float)TIME_Totally / TIME_REFRESH_DELAY);
            if(animNowDuration<Time_ALPHA_SHOW) {
                alpha += 255f / ((float) Time_ALPHA_SHOW / TIME_REFRESH_DELAY);
                if (alpha > 255f) {
                    alpha = 255;
                }
            }else if(animNowDuration >= TIME_Totally-TIME_ALPHA_GONE && animNowDuration<TIME_Totally){
                alpha -= 255f/((float)TIME_ALPHA_GONE/TIME_REFRESH_DELAY);
                if(alpha<=0){
                    alpha = 0;
                }
            }

            animNowDuration += ((float)TIME_Totally / TIME_REFRESH_DELAY);

            //region====画八个矩形=====

            float[] point0 = getFlowerCenter1();
            RectF rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(0,point0,1,0),
                    getPoint2Right(point0,1,0)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color1));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter2();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(1,point0,1,-1),
                    getPoint2Right(point0,1,-1)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color2));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter3();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(2,point0,1,1),
                    getPoint2Right(point0,1,1)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color3));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter4();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(3,point0,2,0),
                    getPoint2Right(point0,2,0)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color4));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);


            point0 = getFlowerCenter5();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(4,point0,2,-1),
                    getPoint2Right(point0,2,-1)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color5));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter6();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(5,point0,3,-1),
                    getPoint2Right(point0,3,-1)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color5));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter7();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(6,point0,3,0),
                    getPoint2Right(point0,3,0)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color1));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            point0 = getFlowerCenter8();
            rectF = getDrawRectf(getBezierPoint(t, point0,
                    getPoint1Right(7,point0,3,1),
                    getPoint2Right(point0,3,1)));
            paint.setColor(getResources().getColor(R.color.boom_flower_color1));
            paint.setAlpha(alpha);
            canvas.drawRect(rectF, paint);

            //endregion====画八个矩形=====

            if (t < 1) {
                mHandler.sendEmptyMessageDelayed(1, TIME_REFRESH_DELAY);
            } else if(t == 1){
                mHandler.sendEmptyMessageDelayed(1, TIME_REFRESH_DELAY);
            }else{
                startBoom = false;
                invalidate();
                mHandler.removeCallbacksAndMessages(null);
            }
        }else {
//            if (startAreaHeight != 0 && startAreaWidth != 0) {
//                canvas.drawRect(0, 0, startAreaWidth, startAreaHeight, paint);
//            }
        }
    }

    //region============八个矩形==========
    float[] flowerCenter1;
    float[] flowerCenter2;
    float[] flowerCenter3;
    float[] flowerCenter4;
    float[] flowerCenter5;
    float[] flowerCenter6;
    float[] flowerCenter7;
    float[] flowerCenter8;
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
    private float[] getFlowerCenter3(){
        if(flowerCenter3 ==null){
            flowerCenter3 = new float[2];
            flowerCenter3[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter3[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter3;
    }
    private float[] getFlowerCenter4(){
        if(flowerCenter4 ==null){
            flowerCenter4 = new float[2];
            flowerCenter4[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter4[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter4;
    }private float[] getFlowerCenter5(){
        if(flowerCenter5 ==null){
            flowerCenter5 = new float[2];
            flowerCenter5[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter5[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter5;
    }
    private float[] getFlowerCenter6(){
        if(flowerCenter6 ==null){
            flowerCenter6 = new float[2];
            flowerCenter6[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter6[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter6;
    }
    private float[] getFlowerCenter7(){
        if(flowerCenter7 ==null){
            flowerCenter7 = new float[2];
            flowerCenter7[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter7[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter7;
    }
    private float[] getFlowerCenter8(){
        if(flowerCenter8 ==null){
            flowerCenter8 = new float[2];
            flowerCenter8[0] = mRandom.nextInt((int) startAreaWidth - 2*FLOWER_WIDTH);
            flowerCenter8[1] = mRandom.nextInt((int) startAreaHeight - 2*FLOWER_WIDTH);
        }
        return flowerCenter8;
    }

    //endregion============八个矩形==========




    public void startBoomFlower(){
        startBoom = true;
        t = 0;
        animNowDuration = 0;
        mHandler.sendEmptyMessage(1);
    }


    /**
     * 根据一个点的坐标作为矩形的中心点画一个矩形RectF
     * @param pointCenter
     * @return
     */
    private RectF getDrawRectf(float pointCenter[]){

        RectF rectF = new RectF(pointCenter[0]- FLOWER_WIDTH /2,pointCenter[1]- FLOWER_WIDTH /2,pointCenter[0]+ FLOWER_WIDTH /2,pointCenter[1]+ FLOWER_WIDTH /2);
//        Log.e(TAG,"BoomColorFlowerView,矩形"+rectF.left+",top"+rectF.top+",right"+rectF.right+",bottom"+rectF.bottom);
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
//        Log.e(TAG,"BezierPoint,x->"+bezier[0]+",y->"+bezier[1]);
        return bezier;
    }

    /**
     * 根据point0获取point1
     * @param point0
     * @param xLevel 1,2,3
     * @param yLevel -1,0,1
     * @return
     */
    private float[] getPoint1Right(int index,float[] point0,int xLevel,int yLevel){
        float[] point1 = new float[2];
        //point1
        point1[0] = (point0[0] + xLevel*startAreaWidth)/2;//+mRandom.nextFloat()*50;
        if(rectfRandom[index] == 0){
            rectfRandom[index] = point0[1] - xLevel*10-mRandom.nextInt(10);
        }
        point1[1] = rectfRandom[index];//+ random.nextInt(5) + random.nextFloat();
        return point1;
    }
    private float rectfRandom[] = new float[8];


    /**
     * 根据point0获取point2
     * @param point0
     * @param xLevel 1,2,3
     * @param yLevel -1,0,1
     * @return
     */
    private float[] getPoint2Right(float[] point0,int xLevel,int yLevel){
        float[] point2 = new float[2];
        //point2
        point2[0] = point0[0]+xLevel*startAreaWidth;
        point2[1] = point0[1]+yLevel*startAreaHeight/2;

        return point2;
    }


}
