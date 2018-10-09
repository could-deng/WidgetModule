package com.sdk.dyq.widgetlibrary.HandShaker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sdk.dyq.widgetlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class HandShakerView extends RelativeLayout {
    ImageView iv_hand;
    AnimationDrawable handAnimDrawable;
    ImageView tv_hand_shaker_circle_bg;
    ObjectAnimator bgRotationAnim;
    long bgRotationCurrTime;

    ImageView iv_voucher_left_1;
    ImageView iv_voucher_left_2;
    ImageView iv_voucher_right_1;
    ImageView iv_voucher_right_2;

    ImageView iv_coin_left1;
    ImageView iv_coin_left2;
    ImageView iv_coin_left3;
    ImageView iv_coin_left4;
    ImageView iv_coin_right1;
    ImageView iv_coin_right2;
    ImageView iv_coin_right3;


    LinearInterpolator linearInterpolator;
    boolean firstInited;//是否执行了startAnim

    public HandShakerView(Context context) {
        super(context);
    }

    public HandShakerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_hand_shaker, this, true);
        iv_hand = (ImageView) view.findViewById(R.id.iv_hand);
        tv_hand_shaker_circle_bg = (ImageView) view.findViewById(R.id.tv_hand_shaker_circle_bg);
        iv_voucher_left_1 = (ImageView) view.findViewById(R.id.iv_voucher_left_1);
        iv_voucher_left_2 = (ImageView) view.findViewById(R.id.iv_voucher_left_2);
        iv_voucher_right_1 = (ImageView) view.findViewById(R.id.iv_voucher_right_1);
        iv_voucher_right_2 = (ImageView) view.findViewById(R.id.iv_voucher_right_2);

        iv_coin_left1 = (ImageView) view.findViewById(R.id.iv_coin_left_1);
        iv_coin_left2 = (ImageView) view.findViewById(R.id.iv_coin_left_2);
        iv_coin_left3 = (ImageView) view.findViewById(R.id.iv_coin_left_3);
        iv_coin_left4 = (ImageView) view.findViewById(R.id.iv_coin_left_4);

        iv_coin_right1 = (ImageView) view.findViewById(R.id.iv_coin_right_1);
        iv_coin_right2 = (ImageView) view.findViewById(R.id.iv_coin_right_2);
        iv_coin_right3 = (ImageView) view.findViewById(R.id.iv_coin_right_3);
//        addView(view);//如果View view = LayoutInflater.from(context).inflate(R.layout.layout_hand_shaker,null)则需要addView(view);
        linearInterpolator = new LinearInterpolator();
    }

    //region===动画效果============

    /**
     * 手挥动动画
     *
     * @return
     */
    private boolean startHandShakeAnim() {
        if (iv_hand != null) {
            iv_hand.setImageResource(R.drawable.bg_hand_shaker);
            handAnimDrawable = (AnimationDrawable) iv_hand.getDrawable();
            if (handAnimDrawable != null) {
                handAnimDrawable.start();
                return true;
            }
        }
        return false;
    }

    /**
     * 背景圆圈转动动画
     *
     * @return
     */
    private boolean startBgAnim() {
        if (tv_hand_shaker_circle_bg == null) {
            return false;
        }
        if (bgRotationAnim == null) {
            bgRotationAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg, "rotation", 0f, 360f);
            bgRotationAnim.setDuration(5000);
            bgRotationAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
            bgRotationAnim.setInterpolator(linearInterpolator);
            bgRotationAnim.setRepeatMode(ValueAnimator.RESTART);//
        }

//        if (!firstInited) {
        bgRotationAnim.start();
//            firstInited = true;
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                if (bgRotationAnim.isPaused()) {
//                    bgRotationAnim.resume();
//                    bgRotationAnim.setCurrentPlayTime(bgRotationCurrTime);
//                }
//            }
//        }

        return true;
    }

    /**
     * 开启icon位移+上下浮动动画
     *
     * @param view
     * @param translateTime
     */
    protected void startIconAnim(final View view, long startX, long startY, long desX, long desY, long translateTime, boolean temporaryBigger, long startDelay) {
        if(view.getVisibility() == INVISIBLE){
            view.setVisibility(VISIBLE);
        }
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator transferAnimX = ObjectAnimator.ofFloat(view, "translationX", startX, desX);
        transferAnimX.setDuration(translateTime);
        transferAnimX.setInterpolator(linearInterpolator);
        animators.add(transferAnimX);

        ObjectAnimator transferAnimY = ObjectAnimator.ofFloat(view, "translationY", startY, desY);
        transferAnimY.setDuration(translateTime);
        transferAnimY.setInterpolator(linearInterpolator);
        animators.add(transferAnimY);

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        alphaAnim.setDuration(translateTime);
        alphaAnim.setInterpolator(linearInterpolator);
        animators.add(alphaAnim);

        ObjectAnimator scaleXAnim, scaleYAnim;
        if (temporaryBigger) {
            scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.2f, 1f);
            scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.2f, 1f);
        } else {
            scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
            scaleXAnim.setInterpolator(linearInterpolator);
            scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
            scaleYAnim.setInterpolator(linearInterpolator);
        }
        scaleXAnim.setDuration(translateTime);
        animators.add(scaleXAnim);

        scaleYAnim.setDuration(translateTime);
        animators.add(scaleYAnim);

        AnimatorSet translateAnimatorSet = new AnimatorSet();//位移动画集合
        translateAnimatorSet.playTogether(animators);


        AnimatorSet totalAnimationSet = new AnimatorSet();//总的动画集合
        totalAnimationSet.play(translateAnimatorSet)
                .before(getFlowAnim(view, desX, desY, 200));
        totalAnimationSet.setStartDelay(startDelay);
        totalAnimationSet.start();
    }

    /**
     * 上下浮动效果
     *
     * @param view
     * @param desX       起点X坐标
     * @param desY       起点Y坐标
     * @param startDelay 启动动画延时
     */
    private AnimatorSet getFlowAnim(View view, long desX, long desY, long startDelay) {
        List<Animator> flowAnimators = new ArrayList<>();
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", desY - 6.0f, desY + 6.0f, desY - 6.0f);
        translationYAnim.setDuration(800);
        translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
        translationYAnim.start();
        flowAnimators.add(translationYAnim);
        AnimatorSet flowAnimatorSet = new AnimatorSet();
        flowAnimatorSet.playTogether(flowAnimators);
        flowAnimatorSet.setStartDelay(startDelay);

        return flowAnimatorSet;
    }

//endregion===动画效果============

    //region===动画的开启与关闭==

    private int viewWidth;
    private int viewHeight;

    public void startAnim() {
        startHandShakeAnim();
        startBgAnim();
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        int right2Width = iv_voucher_right_2.getMeasuredWidth();
        //卡券icon
        startIconAnim(iv_voucher_right_1, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (3 / 4F)), 0, 3000, false, 300);
        startIconAnim(iv_voucher_right_2, viewWidth / 2L, viewHeight / 2L, viewWidth-right2Width, (long) (viewHeight * (2 / 7F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_1, viewWidth / 2L, viewHeight / 2L, -20, (long) (viewHeight * (1 / 7F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_2, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (1 / 4F)), (long) (viewHeight * (1 / 2F) - 10), 3000, false, 300);
        //金币icon
        startIconAnim(iv_coin_right1, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (3 / 4F)), (long) (viewHeight * (2 / 7F)), 3000, false, 0);
        startIconAnim(iv_coin_right2, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (3 / 4F))+50, (long) (viewHeight * (2 / 7F))+50, 3000, false, 300);
        startIconAnim(iv_coin_right3, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (2 / 3F)), (long) (viewHeight * (1 / 2F))-50, 3000, false, 300);

    }

    public void testAnim() {
        startIconAnim(iv_voucher_right_1, viewWidth / 2L, viewHeight / 2L, (long) (viewWidth * (3 / 4F)), 0, 3000, false, 300);
    }

    public void stopAnim() {
        if (handAnimDrawable != null) {
            handAnimDrawable.stop();
        }
        if (bgRotationAnim != null) {
            bgRotationCurrTime = bgRotationAnim.getCurrentPlayTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                bgRotationAnim.pause();
            }
        }
    }

    //endregion===动画的开启与关闭==


}
