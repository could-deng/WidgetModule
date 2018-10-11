package com.sdk.dyq.widgetlibrary.HandShaker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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

    RelativeLayout container;

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_hand_shaker, this, true);
        container = (RelativeLayout) view.findViewById(R.id.rl_hand_shake_container);
        container.setBackgroundColor(Color.argb(250, 0, 0, 0));

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
     * 开启icon位移+上下浮动动画
     *
     * @param view
     * @param translateTime
     */
    protected void startIconAnim(final View view, long desX, long desY, long translateTime, boolean temporaryBigger, long startDelay) {
        if (view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
            view.setAlpha(0);
        }
        desX = desX - view.getWidth() / 2;
        desY = desY - view.getHeight() / 2;

        translateTime = 400;
        long startX = viewWidth / 2L;
        long startY = viewHeight / 2L;

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

    private void initBgHeight() {
        if (tv_hand_shaker_circle_bg != null) {
            LayoutParams lp = (LayoutParams) tv_hand_shaker_circle_bg.getLayoutParams();
            lp.height = viewWidth;
            tv_hand_shaker_circle_bg.setLayoutParams(lp);
        }
    }

    //region=====onLayout()完成后======
    private boolean locationInited = false;

    private void initLayout() {

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

//        initHandLocationLayout();
        initBgHeight();
        locationInited = true;
    }

    private boolean initHandLocation = false;
    private float handY = -1;

    private void initHandLocationLayout() {
        if (iv_hand != null && !initHandLocation) {
            iv_hand.setY(iv_hand.getY() - iv_hand.getHeight() / 4F);
            initHandLocation = true;
            handY = iv_hand.getY();
        }
    }

    //endregion=====onLayout()完成后======


    //region===动画的开启与关闭==

    private int viewWidth;
    private int viewHeight;

    /**
     * 最先摆动出现动画
     */
    public void startUpDownAnim() {
        if (!locationInited) {
            initLayout();
        }
        startHandShakeAnim();

        AnimatorSet handShowSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();

        //region====渐变颜色+上下动画========
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(iv_hand, "alpha", 0, 1);
        alphaAnim.setDuration(500);
        alphaAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startExporeAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animators.add(alphaAnim);

        ObjectAnimator upDownAnim = ObjectAnimator.ofFloat(iv_hand, "translationY", handY, handY - 90.0f, handY);
        upDownAnim.setDuration(1000);
        animators.add(upDownAnim);

        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg, "translationY", 0, 0 - 90.0f, 0);
        translationYAnim.setDuration(1000);
        animators.add(translationYAnim);
        //endregion====渐变颜色+上下动画========

        //region=====背景发光动画=========

        ObjectAnimator bgRotationAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg, "rotation", 0f, 360f);
        bgRotationAnim.setDuration(5000);
        bgRotationAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        bgRotationAnim.setInterpolator(linearInterpolator);
        bgRotationAnim.setRepeatMode(ValueAnimator.RESTART);//
        animators.add(bgRotationAnim);

        ObjectAnimator bgShineAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg, "alpha", 1f, 0.3f, 1f);
        bgShineAnim.setDuration(2000);
        bgShineAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        bgShineAnim.setInterpolator(linearInterpolator);
        bgShineAnim.setRepeatMode(ValueAnimator.INFINITE);
        animators.add(bgShineAnim);
        //endregion=====背景发光动画=========

        handShowSet.playTogether(animators);
        handShowSet.start();
    }

    /**
     * 洒开金币和卡券动画
     */
    private void startExporeAnim() {
        //卡券icon
        startIconAnim(iv_voucher_right_1, (long) (viewWidth * (0.777F)), (long) (viewHeight * 0.219F), 3000, false, 100);
        startIconAnim(iv_voucher_right_2, (long) (viewWidth - (14F)), (long) (viewHeight * (0.379F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_1, 5, (long) (viewHeight * (0.315F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_2, (long) (viewWidth * (0.253F)), (long) (viewHeight * 0.5F), 3000, false, 100);
        //金币icon
        startIconAnim(iv_coin_right1, (long) (viewWidth * 0.769F), (long) (viewHeight * 0.333F), 3000, false, 0);
        startIconAnim(iv_coin_right2, (long) (viewWidth * 0.817F), (long) (viewHeight * 0.45F), 3000, false, 100);
        startIconAnim(iv_coin_right3, (long) (viewWidth * 0.682F), (long) (viewHeight * 0.491F), 3000, false, 100);

        startIconAnim(iv_coin_left1, (long) (viewWidth * 0.3), (long) (viewHeight * 0.308), 3000, false, 0);
        startIconAnim(iv_coin_left2, (long) (viewWidth * 0.437), (long) (viewHeight * 0.38), 3000, true, 0);
        startIconAnim(iv_coin_left3, (long) (viewWidth * 0.289), (long) (viewHeight * 0.422), 3000, false, 0);
        startIconAnim(iv_coin_left4, 28, (long) (viewHeight * 0.39), 3000, false, 0);

    }


    public void stopAnim() {
        if (handAnimDrawable != null) {
            handAnimDrawable.stop();
        }
    }

    //endregion===动画的开启与关闭==

}
