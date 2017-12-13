package com.sdk.dyq.widgetlibrary.waveView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 声音声浪效果
 */

public class WaveView extends View {
    Context context;

    //三条线的画笔

    private Paint mPaint = new Paint();

    private Path mFirstPath = new Path();
    private Path mCenterPath = new Path();
    private Path mSecondPath = new Path();

    //128个采样点,因为画一条曲线，64个点，人眼就看不出来了
    private static final int SAMPLINT_SIZE = 128;

    private float[] mSamplingX;//保存采样点的间隔
    private float[] mMapX;//映射采样点坐标

    //画布宽高
    int mWidth;
    int mHeight;

    int mCenterHeight;//高的中心
    int mAmplitede;//振幅

    long startTime = System.currentTimeMillis();

    private final RectF rectF = new RectF();
    private final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);//仅仅显示重叠在上面的部分
    private final int mBackGroudColor = Color.rgb(24,33,41);
    private final int mCenterPathColor = Color.argb(30,24,55,32);



    public WaveView(Context context) {
        this(context,null);
        this.context = context;
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            mHandler.sendEmptyMessageDelayed(0,16);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if(mSamplingX == null) {

            mWidth = canvas.getWidth();
            mHeight = canvas.getHeight();

            mCenterHeight = mHeight >> 1;
            mAmplitede = mHeight >> 3;

            mSamplingX = new float[SAMPLINT_SIZE+1];
            mMapX = new float[SAMPLINT_SIZE +1];

            float gap = (mWidth/ (float) SAMPLINT_SIZE);

            float x;
            for(int i =0 ;i<SAMPLINT_SIZE;i++){
                x = i * gap;
                mSamplingX[i] = x;
                mMapX[i] = (x / (float) mWidth)*4 -2;//映射坐标点坐标
            }

        }
        canvas.drawColor(mBackGroudColor);

        mFirstPath.rewind();
        mSecondPath.rewind();
        mCenterPath.rewind();

        mFirstPath.moveTo(0,mCenterHeight);
        mSecondPath.moveTo(0,mCenterHeight);
        mCenterPath.moveTo(0,mCenterHeight);

        float offSet = (System.currentTimeMillis()- startTime)/500F;


        float x ;
        float curV ;


        for(int i=0;i<=SAMPLINT_SIZE;i++){
            x = mSamplingX[i];

            //计算y
            curV = i<SAMPLINT_SIZE?(float)(mAmplitede*calcValue(mMapX[i],offSet)):0;

            mFirstPath.lineTo(x,mCenterHeight + curV);
            mSecondPath.lineTo(x,mCenterHeight - curV);
            mCenterPath.lineTo(x,mCenterHeight + curV/5F);
        }

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);

        canvas.drawPath(mFirstPath,mPaint);
        canvas.drawPath(mSecondPath,mPaint);
        canvas.drawPath(mCenterPath,mPaint);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(xfermode);

        int saveCount = canvas.saveLayer(0,0,mWidth,mHeight,null,Canvas.ALL_SAVE_FLAG);//保存画布

        //着色器
        mPaint.setShader(new LinearGradient(0,mCenterHeight+mAmplitede,mWidth,mCenterHeight-mAmplitede,
                Color.BLUE,Color.GREEN, Shader.TileMode.CLAMP));

        rectF.set(0,mCenterHeight-mAmplitede,mWidth,mCenterHeight+mAmplitede);

        canvas.drawRect(rectF,mPaint);

        //复位画笔
        mPaint.setShader(null);
        mPaint.setXfermode(null);

        //叠加图层绘制相交部分
        canvas.restoreToCount(saveCount);




        super.onDraw(canvas);
//        mHandler.sendEmptyMessageDelayed(0,16);



    }

    /**
     * https://www.desmos.com/calculator 画函数图
     * @param mapX
     * @param offSet
     * @return
     */
    private double calcValue(float mapX,float offSet){

        offSet %= 2;

        double sinFunc = Math.sin(0.75*Math.PI*mapX - offSet * Math.PI);
        double recessionFunc = Math.pow((4/(4+Math.pow(mapX,4))),2.5);

        return sinFunc * recessionFunc;
    }




}
