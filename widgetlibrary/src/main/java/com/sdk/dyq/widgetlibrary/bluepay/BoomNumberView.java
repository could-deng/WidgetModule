package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sdk.dyq.widgetlibrary.R;
import com.sdk.dyq.widgetlibrary.swiperefresh.SwipeLoadLayout;

import java.util.Random;

/**
 * 爆炸数字滚动控件
 * Created by deng on 2018/5/24.
 */

public class BoomNumberView extends RelativeLayout implements RandomTextView.TextAnimCallBack {

    public static final String TAG = "BoomNumberView";
    //碎花宽度 px
    public static final int FLOWER_WIDTH = 10;

    RandomTextView randomTextView;
    Paint paint;
    Paint paintSquareRight;
    Paint paintSquareLeft;

    int width;
    int height;

    float widthPerChar;
    float t = 0;


    private Random random;
    private RectF rectF;

    ImageView iv_right;

    private Handler mHandler;

    public BoomNumberView(Context context) {
        this(context,null);
    }

    public BoomNumberView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BoomNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        View container_view = LayoutInflater.from(context).inflate(R.layout.layout_boom_num_view,null);
        randomTextView = (RandomTextView) container_view.findViewById(R.id.tv_random_num);
        if(randomTextView!=null){
            randomTextView.setCallBack(this);
        }
        randomTextView.setGravity(CENTER_HORIZONTAL);

        addView(container_view,new LinearLayout.LayoutParams(context,attrs));


        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setStrokeCap(Paint.Cap.ROUND);

        float[] widths = new float[4];
        paint.getTextWidths("9999", widths);
        widthPerChar = widths[0];//获取第一个字符的宽度

        rectF = new RectF(10,10,10,10);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(BoomNumberView.TAG,"!!!!!!!!!!!!!!draw");
        canvas.translate(width/2, height/2);

        t += 1f/(500/25);

        int flowerCenterX = (int) (width/2-widthPerChar);
        int flowerCenterY = 0;

//        Rect rect = new Rect(flowerCenterX- FLOWER_WIDTH /2,flowerCenterY- FLOWER_WIDTH /2,flowerCenterX+ FLOWER_WIDTH /2,flowerCenterY+ FLOWER_WIDTH /2);

        float[] point0 = new float[]{flowerCenterX,flowerCenterY};
        RectF rectF = getDrawRectf(getBezierPoint(t,point0,
                getPoint1Right(point0),
                getPoint2Right(point0)));


        paintSquareLeft.setColor(getResources().getColor(R.color.progress2_green));
//        paintSquareLeft.setAlpha(1);

        canvas.drawRect(rectF,paintSquareLeft);

        if(t < 1) {
            mHandler.sendEmptyMessageDelayed(1, 25);
        }else {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 开始启动动画
     */
    public void startBoomAnim(){
        if(randomTextView!=null){
            randomTextView.start("123.45","110.00",RandomTextView.FIRSTF_LAST);
        }

    }

    private RectF getDrawRectf(float pointCenter[]){

        RectF rectF = new RectF(pointCenter[0]- FLOWER_WIDTH /2,pointCenter[1]- FLOWER_WIDTH /2,pointCenter[0]+ FLOWER_WIDTH /2,pointCenter[1]+ FLOWER_WIDTH /2);

        return rectF;
    }


    private float[] getBezierPoint(float t,float point0[],float point1[],float point2[]){
        float bezier[] = new float[2];
        bezier[0] = point0[0] * (1 - t)* (1 - t)
                + point1[0] * 2 * t * (1-t)
                + point2[0] * t * t;

        bezier[1] = point0[1] * (1 - t)* (1 - t)
                + point1[1]* 2 * t * (1-t)
                + point2[1] * t * t;
        return bezier;
    }


    private float[][] point1Right = new float[8][2];
    private float[][] point1And2Left = new float[8][4];



    /**
     * 左边
     * 根据point0获取point1和point2
     * @param point0
     * @param index 0-7
     * @return
     */
    private float[] getPoint1Right(float[] point0){
//        if(point1And2Right[index][0] == 0 || point1And2Right[index][1] == 0) {
            float[] point1 = new float[2];
            //point2
//            point1And2[2] = point0[0] + 20;
//            point1And2[3] = point0[1];

            //point1
        point1[0] = point0[0] + 10;
        point1[1] = point0[1] + 5;//+ random.nextInt(5) + random.nextFloat();

//            point1And2Right[index][0] = point0[0] + 10;
//            point1And2Right[index][1] = point0[1] + random.nextInt(5) + random.nextFloat();
//            point1And2Right[index][2] = point0[0] + 20;
//            point1And2Right[index][3] = point0[1];
//        }

        return point1;

    }

    private float[] getPoint2Right(float[] point0){
        float[] point2 = new float[2];
        //point2
        point2[0] = point0[0] + 20;
        point2[1] = point0[1];

        //point1
//        point1And2[0] = point0[0] + 10;
//        point1And2[1] = point0[1] +5;//+ random.nextInt(5) + random.nextFloat();
        return point2;

    }







    private float[] getPoint1And2Left(float[] point0){

        float[] point1And2 = new float[4];
        //point2
        point1And2[2] = point0[0]-20;
        point1And2[3] = point0[1];
        //point1
        point1And2[0] = point0[0]-10;
        point1And2[1] = point0[1]+5;//+ random.nextInt(5) + random.nextFloat();
        return point1And2;
    }







    /**
     * 获取动画集（淡出+贝塞尔曲线）
     * @param imageView
     * @return
     */
    private AnimatorSet getAnimatorSet(PointF pointStart,ImageView[] imageView) {

        AnimatorSet enter = new AnimatorSet();

        //1、缩放动画
        AnimatorSet scaleAnimator = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 0.3f, 1f);
        scaleAnimator.setDuration(300);
        scaleAnimator.playTogether(alpha, scaleX, scaleY);

        //2、贝塞尔动画
        ValueAnimator bezierAnimator = getBezierAnimator(pointStart,new BezierCallBack() {
            @Override
            public void onBezierAnim(float x, float y, float alpha) {

            }
        });

        //3、两个动画按顺序播放
        enter.playSequentially(scaleAnimator, bezierAnimator);
        return enter;
    }

    private ValueAnimator getBezierAnimator(PointF point0,final BezierCallBack callBack){
        //// TODO: 2018/5/25 需要确定三个点坐标上的规则 
        //1、构建贝塞尔曲线的四个点
//        PointF point0 = new PointF( widthPerChar , height);
        PointF point1 = new PointF(random.nextInt(width), random.nextInt(height / 2));
        PointF point2 = new PointF(random.nextInt(width), random.nextInt(height / 2) + height / 2);

        BezierEvaluator evaluator = new BezierEvaluator(point1);

        //2、创建贝塞尔属性动画
        final ValueAnimator valueAnimator = ObjectAnimator.ofObject(evaluator, point0, point2);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(500);
        //3、监听贝塞尔曲线估值器返回值
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //4、获取BezierEvaluator中evaluate()返回的运行轨迹坐标点
                PointF pointF = (PointF) animation.getAnimatedValue();
                //6、获取BezierEvaluator中evaluate()返回的参数t，设置消失动画
                float t = animation.getAnimatedFraction();//0到1区间
                callBack.onBezierAnim(pointF.x,pointF.y,(1 - t + 0.2f));
            }
        });
        return valueAnimator;
    }


    /**
     * 滚动数字结束的回调
     */
    @Override
    public void onAnimFinish(){
        Log.e(BoomNumberView.TAG,"onAnimFinish()");
        invalidate();
//        iv_right.setImageResource(R.color.progress2_green);
//        RelativeLayout.LayoutParams lp = new LayoutParams(10,10);
//        iv_right.setLayoutParams(lp);
//
//        AnimatorSet animatorSet = getAnimatorSet(iv_right);
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                //3、贝塞尔曲线动画执行后移除View
//            }
//        });
//        animatorSet.start();

    }







    public interface BezierCallBack{
        void onBezierAnim(float x,float y ,float alpha);
    }

//    private Bitmap bitmap(){
//        Bitmap.Config config = Bitmap.Config.RGB_565;
//        Bitmap bitmap = Bitmap.createBitmap(new int[]{R.color.progress1_blue},10,10,config);
//        return
//    }

}
