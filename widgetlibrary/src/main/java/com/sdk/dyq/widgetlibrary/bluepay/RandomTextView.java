package com.sdk.dyq.widgetlibrary.bluepay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by deng on 2018/5/24.
 */

public class RandomTextView extends AppCompatTextView{
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

    //   当前text
    private String text;
    private String valueOrigin;//变化前数字
//    private String valueAfter;//最终数字



    //滚动速度数组
    private int[] pianyilianglist;
    //总滚动距离数组
    private int[] pianyiliangSum;
    //滚动完成判断，标志位1代表显示最终的值，0代表显示随机数
    private int[] overLine;



    private Paint p;
    //第一次绘制
    private boolean firstIn = true;
    //滚动中
    private boolean auto = true;

    //text int值列表
    private ArrayList<String> arrayListText;
    //text 原值列表
    private ArrayList<String> arrayListOriginText;

    //字体宽度，每个字符的宽度
    private float f0;

    //基准线
    private int baseline;

    private int measuredHeight;

    private Random random;
    private String[][] randomNum = new String[10][10];
    private Paint p_test;
    private TextAnimCallBack callBack;


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
    }

    public void setCallBack(TextAnimCallBack callBack){
        this.callBack = callBack;
    }

    //region=======设置转动速度===========

    //按系统提供的类型滚动
    public void setPianyilian(int pianyiliangTpye) {
//        this.text = getText().toString();

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
                    pianyilianglist[i] = 2 + i;
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
//        this.text = getText().toString();
//        this.text = valueAfter;

        pianyiliangSum = new int[list.length];
        overLine = new int[list.length];
        pianyilianglist = list;
    }


    //endregion=======设置转动速度===========

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("RandomTextView","draw");
        if (firstIn) {
            firstIn = false;
            super.onDraw(canvas);
            p = getPaint();
            Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
            measuredHeight = getMeasuredHeight();
            Log.d("EEEEEEE", "onDraw: " + measuredHeight);
            baseline = (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

            float[] widths = new float[4];
            p.getTextWidths("9999", widths);
            f0 = widths[0];//获取第一个字符的宽度

            p_test = getPaint();
            p_test.setColor(Color.BLUE);

            invalidate();

        }
        drawNumber(canvas);
//        if (auto) {
//            for (int j = 0; j < numLength; j++) {
//
//                pianyiliangSum[j] -= pianyilianglist[j];
//                postInvalidateDelayed(17);
//            }


        //     }
    }


    //绘制
    private void drawNumber(Canvas canvas) {
        //todo test
        canvas.drawLine(0,baseline,f0,baseline,p);

        for (int j = 0; j < numLength; j++) {//每列
            for (int i = 1; i < maxLine; i++) {//每行
                //检查当前字符位，是不是最后一个
                if (i == maxLine - 1 && i * baseline + pianyiliangSum[j] <= baseline){
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
                        //修复停止后绘制问题
                        if (this.auto)
                            invalidate();
                        this.auto = false;
                    }
                }


                if (overLine[j] == 0) {//滚动状态
                    drawText(canvas, String.valueOf(getRandomText(i,j)), 0 + f0 * j,
                            i * baseline + pianyiliangSum[j], p);
//                    drawText(canvas, setBack(arrayListText.get(j), maxLine - i - 1) + "", 0 + f0 * j,
//                            i * baseline + pianyiliangSum[j], p);

                    //canvas.drawText(setBack(arrayListText.get(j), maxLine - i - 1) + "", 0 + f0 * j,
                    //        i * baseline + pianyiliangSum[j], p);

                }else {//最后状态
                    //定位后画一次就好啦
                    if (overLine[j] == 1) {
                        overLine[j] ++ ;

                        drawText(canvas, arrayListText.get(j) + "", 0 + f0 * j,
                                baseline, p);
                        // canvas.drawText(arrayListText.get(j) + "", 0 + f0 * j,
                        //        baseline, p);
                        if(callBack!=null){
                            callBack.onAnimFinish();
                        }
                    }


                    //break;
                }


            }


        }
    }

    /**
     * 获取随机数
     * @param i
     * @param j
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

    //开始滚动

    /**
     *
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

    ;


    private static final Handler handler = new Handler();

    public void destroy() {
        auto = false;
        handler.removeCallbacks(task);

    }

    private final Runnable task = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            if (auto) {
                Log.d("RandomTextView",""+auto);
                handler.postDelayed(this, 20);

                for (int j = 0; j < numLength; j++) {
                    pianyiliangSum[j] -= pianyilianglist[j];

                }

                invalidate();
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
        else{
            return;
        }
    }


    public interface TextAnimCallBack{
        public void onAnimFinish();
    }
}
