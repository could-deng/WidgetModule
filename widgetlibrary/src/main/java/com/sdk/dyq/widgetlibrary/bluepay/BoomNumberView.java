package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.sdk.dyq.widgetlibrary.R;

/**
 * 爆炸数字滚动控件
 * Created by deng on 2018/5/24.
 */

public class BoomNumberView extends RelativeLayout implements RandomTextView.TextAnimCallBack {

    public static final String TAG = "BoomNumberView";


    RandomTextView randomTextView;
    BoomColorFlowerView flowerView;
    BoomColorFlowerView boom_view_left;

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
        boom_view_left = (BoomColorFlowerView) container_view.findViewById(R.id.boom_view_left);
        changeBottomLayoutStartAnimation(boom_view_left);

        float[] area = getStartArea(50);
        flowerView.setStartArea(area[0],area[1]);
        boom_view_left.setStartArea(area[0],area[1]);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) flowerView.getLayoutParams();
        lp.leftMargin = (int) area[0]*(-1);
        flowerView.setLayoutParams(lp);

        LinearLayout.LayoutParams left_lp = (LinearLayout.LayoutParams) boom_view_left.getLayoutParams();
        left_lp.rightMargin = (int) (area[0]*(-1));
        boom_view_left.setLayoutParams(left_lp);

        addView(container_view,new LinearLayout.LayoutParams(context,attrs));
    }
    /**
     *  底部布局从180旋转至90
     */
    private void changeBottomLayoutStartAnimation(View layout) {
        layout.setVisibility(View.VISIBLE);
        ObjectAnimator mOpenAnimator = ObjectAnimator.ofFloat(layout, "RotationY", 0, 180);
//        mOpenAnimator.setDuration(500);
        mOpenAnimator.start();
    }


    public float[] getStartArea(int textSizeSp){
        float[] area = new float[2];
        Paint p = new Paint();
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(sp2px(textSizeSp));
        Rect mTextBounds = new Rect();
        p.getTextBounds("0", 0, 1, mTextBounds);
        area[1] = mTextBounds.height();
        float widths[] = new float[1];
        p.getTextWidths("0", widths);
        area[0] = widths[0];
        return area;
    }

    private int sp2px(float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
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
            randomTextView.start("12.3","11.0",RandomTextView.FIRSTF_LAST);
        }
    }


    @Override
    public void onAnimFinish() {
        if(flowerView!=null){
            flowerView.startBoomFlower();
        }
        if(boom_view_left!=null){
            boom_view_left.startBoomFlower();
        }
    }
}
