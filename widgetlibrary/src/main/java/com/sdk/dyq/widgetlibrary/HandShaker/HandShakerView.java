package com.sdk.dyq.widgetlibrary.HandShaker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sdk.dyq.widgetlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class HandShakerView extends RelativeLayout {
    ImageView iv_hand;
    AnimationDrawable handAnimDrawable;
    ImageView tv_hand_shaker_circle_bg;
    AnimatorSet bgAnimatorSet;//bg动画集
    List<Animator> bgAnimators = new ArrayList<>();
    ObjectAnimator bgRotationAnim;
    ObjectAnimator bgShineAnim;
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
        container.setBackgroundColor(Color.argb(250,0,0,0));

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
            bgAnimators.add(bgRotationAnim);
        }
        if(bgShineAnim == null){
            bgShineAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg,"alpha",1f,0.3f,1f);
            bgShineAnim.setDuration(2000);
            bgShineAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
            bgShineAnim.setInterpolator(linearInterpolator);
            bgShineAnim.setRepeatMode(ValueAnimator.INFINITE);
            bgAnimators.add(bgShineAnim);
        }
//        bgRotationAnim.start();
//        bgShineAnim.start();
        if(bgAnimatorSet==null) {
            bgAnimatorSet = new AnimatorSet();
            bgAnimatorSet.playTogether(bgAnimators);
        }
        bgAnimatorSet.start();

        return true;
    }

    /**
     * 开启icon位移+上下浮动动画
     *
     * @param view
     * @param translateTime
     */
    protected void startIconAnim(final View view, long desX, long desY, long translateTime, boolean temporaryBigger, long startDelay) {
        if(view.getVisibility() == INVISIBLE){
            view.setVisibility(VISIBLE);
        }
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


    private void initLayout(){

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        LinearLayout.LayoutParams lpContainer = (LinearLayout.LayoutParams) getLayoutParams();
        lpContainer.height = viewWidth;
        lpContainer.width = viewWidth;
        setLayoutParams(lpContainer);
        initHandLocationLayout();
    }

    private boolean initHandLocation = false;

    private void initHandLocationLayout(){
        if(iv_hand != null && !initHandLocation){
            iv_hand.setY(iv_hand.getY() - iv_hand.getHeight()/4F);
            initHandLocation = true;
        }
    }

    //region===动画的开启与关闭==

    private int viewWidth;
    private int viewHeight;

    /**
     * 最先摆动出现动画
     */
    public void startUpDownAnim(){


        AnimatorSet handShowSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(iv_hand,"alpha",0,1);
        alphaAnim.setDuration(500);
        alphaAnim.setRepeatCount(1);
        animators.add(alphaAnim);

        ObjectAnimator upDownAnim = ObjectAnimator.ofFloat(iv_hand,"translationY",  - 12.0f, 12.0f, - 12.0f);
        upDownAnim.setDuration(1000);
        upDownAnim.setRepeatCount(1);
        animators.add(upDownAnim);


        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(tv_hand_shaker_circle_bg, "translationY",  - 12.0f, 12.0f, - 12.0f);
        translationYAnim.setDuration(1000);
        translationYAnim.setRepeatCount(1);
        translationYAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animators.add(translationYAnim);
        handShowSet.playTogether(animators);
        handShowSet.start();
    }
    public void startAnim() {
        startHandShakeAnim();
        startBgAnim();

        int right2Width = iv_voucher_right_2.getMeasuredWidth();
        //卡券icon
        startIconAnim(iv_voucher_right_1, (long) (viewWidth * (0.73F)), 0, 3000, false, 100);
        startIconAnim(iv_voucher_right_2, (long) (viewWidth-(2*right2Width/3F)), (long) (viewHeight * (2 / 7F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_1, -20, (long) (viewHeight * (1 / 7F)), 3000, true, 0);
        startIconAnim(iv_voucher_left_2, (long) (viewWidth * (1 / 4F)), (long) (viewHeight * (1 / 2F) - 10), 3000, false, 100);
        //金币icon
        startIconAnim(iv_coin_right1, (long) (viewWidth * (3 / 4F)), (long) (viewHeight * (2 / 7F)), 3000, false, 0);
        startIconAnim(iv_coin_right2,(long) (viewWidth * (3 / 4F))+50, (long) (viewHeight * (2 / 7F))+50, 3000, false, 100);
        startIconAnim(iv_coin_right3, (long) (viewWidth * (2 / 3F)), (long) (viewHeight * (1 / 2F))-50, 3000, false, 100);

//        startIconAnim(iv_coin_left1,);
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initLayout();
    }
}
