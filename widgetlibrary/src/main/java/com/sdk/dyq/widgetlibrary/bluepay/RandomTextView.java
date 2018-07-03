package com.sdk.dyq.widgetlibrary.bluepay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by deng on 2018/5/24.
 */

public class RandomTextView extends AppCompatTextView{
    public static final String TAG = "RandomTextView";
    //高位快
    public static final int FIRSTF_FIRST = 0;
    //高位慢
    public static final int FIRSTF_LAST = 1;
    //速度相同
    public static final int ALL = 2;
    //用户自定义速度
    public static final int USER = 3;
    //偏移速度类型
    private int pianyiliangTpye;

    //   滚动总行数 可设置
    private int maxLine = 10;
    //   当前字符串长度
    private int numLength = 0;

    private String text;//最终数字
    private String valueOrigin;//变化前数字

    //滚动速度数组
    private int[] pianyilianglist;
    //总滚动距离数组
    private int[] pianyiliangSum;
    //滚动完成判断，
//    0表示还没绘制到最后一行，1表示为绘制到最后一行没有进行最后的定位绘制，2表示已经进行了定位绘制。
    private int[] overLine;



    private Paint p;
    //第一次绘制
    private boolean firstIn = true;
    //滚动中
    private boolean auto = true;

    //text int值列表
    private ArrayList<String> arrayListText;
    //text 变化前原值列表
    private ArrayList<String> arrayListOriginText;

    //字体宽度，每个字符的宽度
    private float f0;

    //基准线
    private int baseline;

    private int measuredHeight;

    private Random random;
    private String[][] randomNum = new String[10][10];
    private TextAnimCallBack callBack;
    private boolean finishOnce;

    public RandomTextView(Context context) {
        this(context,null);
    }
    public RandomTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        random = new Random();
        for(int i = 0;i<10;i++){
            for(int j = 0;j<10;j++){
                randomNum[i][j] = "-1";
            }
        }

        p = getPaint();
        p.setTextAlign(Paint.Align.LEFT);
    }

    public void setCallBack(TextAnimCallBack callBack){
        this.callBack = callBack;
    }

    //region=======设置转动速度(偏移量)===========

    //按系统提供的类型滚动
    public void setPianyilian(int pianyiliangTpye) {
        pianyiliangSum = new int[text.length()];
        overLine = new int[text.length()];
        pianyilianglist = new int[text.length()];
        switch (pianyiliangTpye) {
            case FIRSTF_FIRST:
                for (int i = 0; i < text.length(); i++) {
                    pianyilianglist[i] = 10 - i;
                }

                break;
            case FIRSTF_LAST:
                for (int i = 0; i < text.length(); i++) {
                    pianyilianglist[i] = 15 + i;
                }

                break;
            case ALL:
                for (int i = 0; i < text.length(); i++) {
                    pianyilianglist[i] = 15;
                }

                break;
        }
    }

    //自定义滚动速度数组
    public void setPianyilian(int[] list) {
        pianyiliangSum = new int[list.length];
        overLine = new int[list.length];
        pianyilianglist = list;
    }


    //endregion=======设置转动速度(偏移量)===========


    //region========控件的布局位置、长宽等等==============

    private Rect mTextBounds = new Rect();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                p.getTextBounds("0", 0, 1, mTextBounds);
                result = mTextBounds.height();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingTop() + getPaddingBottom()+20;
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
                if(valueOrigin!=null) {
                    float[] widthPer = new float[4];
                    p.getTextWidths("0000", widthPer);
                    result = (int) Math.ceil(widthPer[0]*valueOrigin.length());

                }else{
                    p.getTextBounds("0", 0, 1, mTextBounds);
                    result = mTextBounds.width();
                }

                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }


//    public void setTextSizeDP(int mTextSize)
//    {
//        this.mTextSize = dp2px(mTextSize);
//        if(p!=null){
//            p.setTextSize(this.mTextSize);
//        }
//        requestLayout();
//        invalidate();
//    }
//    public void setTextSizeSP(int mTextSize){
//        this.mTextSize = sp2px(mTextSize);
//        if(p!=null){
//            p.setTextSize(this.mTextSize);
//        }
//        requestLayout();
//        invalidate();
//    }

//    public void setTextColor(int mTextColor)
//    {
//        this.mTextColor = mTextColor;
//        if(p!=null){
//            p.setColor(mTextColor);
//        }
//        invalidate();
//    }

    private int dp2px(float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }


    private int sp2px(float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }

    //endregion========控件的布局位置、长宽等等==============




    @Override
    protected void onDraw(Canvas canvas) {
        if (firstIn) {
            firstIn = false;
            super.onDraw(canvas);

            Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
            measuredHeight = getMeasuredHeight();
            baseline = (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//            baseline = measuredHeight;

            float[] widths = new float[4];
            p.getTextWidths("0000", widths);
            f0 = widths[0];//获取第一个字符的宽度

            invalidate();
        }
        drawNumber(canvas);
    }

    //绘制
    private void drawNumber(Canvas canvas) {
        for (int j = 0; j < numLength; j++) {//每列
            for (int i = 1; i < maxLine; i++) {//每行
                //检查当前字符位，是不是最后一个
                if (i == maxLine - 1 && i * baseline + pianyiliangSum[j] <= baseline){//最后一行并且
                    //归零偏移量，修改标志位
                    pianyilianglist[j] = 0;
                    overLine[j] = 1;

                    //判段所有字符位是否全部绘制到最后一个
                    int auto = 0;
                    for (int k = 0; k < numLength; k++) {
                        auto += overLine[k];
                    }
                    if (auto == numLength * 2 - 1) {

                        handler.removeCallbacks(task);
                        this.auto = false;
                        if (this.auto) {//通知进行最后一次绘制
                            invalidate();
                            return;
                        }

                    }
                }


                if (overLine[j] == 0) {//滚动状态
                    drawText(canvas, String.valueOf(getRandomText(i,j)), 0 + f0 * j,
                            i * baseline + pianyiliangSum[j], p);

                }else {//最后状态
                    //定位后画一次就好啦
                    if (overLine[j] == 1) {
                        overLine[j] ++ ;

                        drawText(canvas, arrayListText.get(j) + "", 0 + f0 * j,
                                baseline, p);

                        // canvas.drawText(arrayListText.get(j) + "", 0 + f0 * j,
                        //        baseline, p);
                        if(i == maxLine-1 && j == 0){
//                            boolean ifEnd = true;
//                            for(int kk = 0 ;kk<overLine.length;kk++){
//                                if(overLine[kk]<2){
//                                    ifEnd = false;
//                                    break;
//                                }
//                            }
//                            if(ifEnd && callBack!=null){
//                                callBack.onAnimFinish();
//                            }
                            if(callBack!=null && !finishOnce) {
                                callBack.onAnimFinish();
                                finishOnce = true;
//                                String testStr = "";
//                                for(int qq =0;qq<overLine.length;qq++){
//                                    testStr+=overLine[qq]+",";
//                                }
//                                Log.e(TAG,"onAnimFinish(),overLine:"+testStr);
                            }
                        }
                    }
                    //break;
                }


            }


        }
    }

    /**
     * 获取随机数
     * @param i 行
     * @param j 列
     * @return
     */
    private String getRandomText(int i , int j){
        if(i == 9){
            randomNum[i][j] =arrayListText.get(j);
        } else if(i == 1){
            randomNum[i][j] = arrayListOriginText.get(j);
        } else{
            if (randomNum[i][j] == "-1") {
                randomNum[i][j] = random.nextInt(10)+"";
            }
        }
        return randomNum[i][j] + "";
    }


    //设置上方数字0-9递减
    private int setBack(int c, int back) {

        if (back == 0) return c;

        back = back % 10;

        int re = c - back;

        if (re < 0) re = re + 10;

        return re;
    }


    /**
     * 开始滚动
     * @param originNum
     */
    public void start(String originNum , String afterNum, int rollType){
        this.valueOrigin = originNum;
        this.text = afterNum ;
        numLength = text.length();

        arrayListText = getList(text);
        arrayListOriginText = getList(valueOrigin);

        setPianyilian(rollType);
        setText(valueOrigin);

        handler.postDelayed(task, 266);
        auto = true;

    }

    public void setMaxLine(int l) {
        this.maxLine = l;
    }

    private ArrayList<String> getList(String s) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {

            String ss = s.substring(i, i + 1);

//            int a = Integer.parseInt(ss);

            arrayList.add(ss);
        }
        return arrayList;

    }


    private static final Handler handler = new Handler();

    public void destroy() {
        Log.e(TAG,"销毁RandomTextView");
        auto = false;
        finishOnce = false;
        handler.removeCallbacks(task);

    }

    private final Runnable task = new Runnable() {

        public void run() {
            if (auto) {
                handler.postDelayed(this, 20);
                for (int j = 0; j < numLength; j++) {
                    pianyiliangSum[j] -= pianyilianglist[j];
                }
                invalidate();
            }else{

            }

        }
    };


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }


    /**
     * 绘制字（）
     * @param mCanvas
     * @param text
     * @param x
     * @param y
     * @param p
     */
    private void drawText(Canvas mCanvas, String text, float x, float y, Paint p) {
        if (y >= -measuredHeight && y <= 2 * measuredHeight) {
            mCanvas.drawText(text + "", x, y, p);
        }
    }


    public interface TextAnimCallBack{
        void onAnimFinish();
    }

}
