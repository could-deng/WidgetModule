package com.sdk.dyq.widgetlibrary.progress;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.sdk.dyq.widgetlibrary.R;
import com.sdk.dyq.widgetlibrary.TextView.AnimationNumberTextView;

/**
 * 仪表盘控件
 */

public class DashBoardProgress extends RelativeLayout {
    private ArcProgress arcProgress;
    private AnimationNumberTextView tv_anim;
    private int progress;

    private long animationDefaultDuration = 2000;//默认动画秒数
    private TimeInterpolator interpolator;


    public DashBoardProgress(Context context) {
        this(context,null);
    }

    public DashBoardProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DashBoardProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_dashboard,null);
        arcProgress = (ArcProgress) view.findViewById(R.id.arcprogress);
        tv_anim = (AnimationNumberTextView) view.findViewById(R.id.tv_anim);

        addView(view, new LayoutParams(context, attrs));
        interpolator = new AccelerateInterpolator();
    }

    public void setAnimText(float value) {
        if (tv_anim != null) {
            tv_anim.showFloatText1(value);
        }
    }
    public void setProgress(int progress){
        if (progress > 0 && progress < arcProgress.getMax()) {
            this.progress = progress;
            ValueAnimator animator = ValueAnimator.ofInt(0, progress);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int t = (int) animation.getAnimatedValue();
                    arcProgress.setProgress(t);
                }
            });
            animator.start();
        } else if (progress == 0) {//为0时,倒序
            this.progress = 0;
            int initNumber = (int) (Math.random() * 100);
            ValueAnimator animator = ValueAnimator.ofInt(initNumber, 0);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int t = (int) animation.getAnimatedValue();
                    arcProgress.setProgress(t);
                }
            });
            animator.start();
        }
    }
}
