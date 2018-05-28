package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
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
import java.util.Random;

import static com.sdk.dyq.widgetlibrary.bluepay.BoomColorFlowerView.FLOWER_WIDTH;

/**
 * 爆炸数字滚动控件
 * Created by deng on 2018/5/24.
 */

public class BoomNumberView extends RelativeLayout implements RandomTextView.TextAnimCallBack {

    public static final String TAG = "BoomNumberView";


    RandomTextView randomTextView;
    BoomColorFlowerView flowerView;


    int width;
    int height;

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

        flowerView = (BoomColorFlowerView) container_view.findViewById(R.id.boom_view);
//        flowerView.setStartArea(70);
        addView(container_view,new LinearLayout.LayoutParams(context,attrs));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.e(BoomNumberView.TAG,"!!!!!!!!!!!!!!draw");
    }


    /**
     * 开始启动动画
     */
    public void startBoomAnim(){
        if(randomTextView!=null){
//            randomTextView.setTextSizeSP(190);
            randomTextView.start("12345","11000",RandomTextView.FIRSTF_LAST);
        }

    }


    @Override
    public void onAnimFinish() {
        if(flowerView!=null){
            flowerView.startBoomFlower();
        }
    }
}
