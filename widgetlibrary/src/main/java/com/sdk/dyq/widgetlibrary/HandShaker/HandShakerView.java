package com.sdk.dyq.widgetlibrary.HandShaker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sdk.dyq.widgetlibrary.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HandShakerView extends RelativeLayout {

    ImageView iv_hand;
    AnimationDrawable handAnimDrawable;
    ImageView tv_hand_shaker_circle_bg;
    ImageView iv_hand_close;
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

    IconListener listener;
    LinearInterpolator linearInterpolator;

    public HandShakerView(Context context) {
        super(context);
    }

    public HandShakerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    RelativeLayout container;
    Random random;

    private Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }


    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_hand_shaker, this, true);
        container = (RelativeLayout) view.findViewById(R.id.rl_hand_shake_container);
        container.setBackgroundColor(Color.argb(250, 0, 0, 0));

        iv_hand_close = (ImageView) view.findViewById(R.id.iv_hand_close);
        iv_hand_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onIconClose();
                }
                endAnim();
            }
        });

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

    public void setIconListener(IconListener listener) {
        this.listener = listener;
    }

    public interface IconListener {
        void onIconClose();
    }


    //region=====动画开始前先调整UI位置======

    private boolean locationInited = false;
    private boolean initHandLocation = false;
    private float handY = -1;
    private int viewWidth;
    private int viewHeight;

    private void initLayout() {
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        initHandLocationLayout();
        initBgHeight();
        locationInited = true;
    }

    private void initBgHeight() {
        if (tv_hand_shaker_circle_bg != null) {
            LayoutParams lp = (LayoutParams) tv_hand_shaker_circle_bg.getLayoutParams();
            lp.height = viewWidth;
            tv_hand_shaker_circle_bg.setLayoutParams(lp);
        }
    }

    private void initHandLocationLayout() {
        if (iv_hand != null && !initHandLocation) {
            iv_hand.setY(-iv_hand.getHeight() / 4F);//iv_hand.getY()
            initHandLocation = true;
            handY = iv_hand.getY();
        }
    }

    //endregion=====动画开始前先调整UI位置======


    //region===动画的开启与关闭==

    /**
     * 最先摆动出现动画
     */
    public void startUpDownAnim() {
        Log.e("TT","startUpDownAnim");

        if (!locationInited) {
            initLayout();
        }
        startHandShakeAnim();

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

        firstAnimatorSet.playTogether(animators);
        firstAnimatorSet.start();
    }

    private AnimatorSet firstAnimatorSet = new AnimatorSet();//开始洒金币卡券动画前的全部动画集合
    private AnimatorSet animatorSetVoucherR1 = new AnimatorSet();
    private AnimatorSet animatorSetVoucherR2 = new AnimatorSet();
    private AnimatorSet animatorSetVoucherL1 = new AnimatorSet();
    private AnimatorSet animatorSetVoucherL2 = new AnimatorSet();
    private AnimatorSet animatorSetCoinR1 = new AnimatorSet();
    private AnimatorSet animatorSetCoinR2 = new AnimatorSet();
    private AnimatorSet animatorSetCoinR3 = new AnimatorSet();
    private AnimatorSet animatorSetCoinL1 = new AnimatorSet();
    private AnimatorSet animatorSetCoinL2 = new AnimatorSet();
    private AnimatorSet animatorSetCoinL3 = new AnimatorSet();
    private AnimatorSet animatorSetCoinL4 = new AnimatorSet();

    /**
     * 洒开金币和卡券动画
     */
    private void startExporeAnim() {
        //卡券icon
        int left_1_width = iv_voucher_left_1.getWidth();
        Log.e("TT","left_1_width.width" +left_1_width);
        startIconAnim(iv_voucher_right_1, (long) (viewWidth * (0.777F)), (long) (viewHeight * 0.219F), false, animatorSetVoucherR1);
        startIconAnim(iv_voucher_right_2, (long) (viewWidth - 90), (long) (viewHeight * (0.379F)), true, animatorSetVoucherR2);
        startIconAnim(iv_voucher_left_1, -25, (long) (viewHeight * (0.315F)), true, animatorSetVoucherL1);
        startIconAnim(iv_voucher_left_2, (long) (viewWidth * (0.253F)), (long) (viewHeight * 0.5F), false, animatorSetVoucherL2);
        //金币icon
        startIconAnim(iv_coin_right1, (long) (viewWidth * 0.769F), (long) (viewHeight * 0.333F), false, animatorSetCoinR1);
        startIconAnim(iv_coin_right2, (long) (viewWidth * 0.817F), (long) (viewHeight * 0.45F), false, animatorSetCoinR2);
        startIconAnim(iv_coin_right3, (long) (viewWidth * 0.682F), (long) (viewHeight * 0.491F), false, animatorSetCoinR3);

        startIconAnim(iv_coin_left1, (long) (viewWidth * 0.3), (long) (viewHeight * 0.308), false, animatorSetCoinL1);
        startIconAnim(iv_coin_left2, (long) (viewWidth * 0.437), (long) (viewHeight * 0.38), true, animatorSetCoinL2);
        startIconAnim(iv_coin_left3, (long) (viewWidth * 0.289), (long) (viewHeight * 0.422), false, animatorSetCoinL3);
        startIconAnim(iv_coin_left4, 28, (long) (viewHeight * 0.39), false, animatorSetCoinL4);
    }

    /**
     * 销毁控件
     */
    public void endAnim() {
        if (handAnimDrawable != null) {
            handAnimDrawable.stop();
        }
        firstAnimatorSet.end();
        firstAnimatorSet.removeAllListeners();
        animatorSetVoucherR1.end();
        animatorSetVoucherR2.end();
        animatorSetVoucherL1.end();
        animatorSetVoucherL2.end();
        animatorSetCoinR1.end();
        animatorSetCoinR2.end();
        animatorSetCoinR3.end();
        animatorSetCoinL1.end();
        animatorSetCoinL2.end();
        animatorSetCoinL3.end();
        animatorSetCoinL4.end();
    }

    public boolean isPause = false;
    public void stopAnim() {
        Log.e("TT","stopAnim()");
        if (handAnimDrawable != null) {
            handAnimDrawable.stop();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(firstAnimatorSet.isRunning())
                firstAnimatorSet.pause();
            if (animatorSetVoucherR1.isRunning())
                animatorSetVoucherR1.pause();
            if (animatorSetVoucherR2.isRunning())
                animatorSetVoucherR2.pause();
            if (animatorSetVoucherL1.isRunning())
                animatorSetVoucherL1.pause();
            if (animatorSetVoucherL2.isRunning())
                animatorSetVoucherL2.pause();
            if (animatorSetCoinR1.isRunning())
                animatorSetCoinR1.pause();
            if (animatorSetCoinR2.isRunning())
                animatorSetCoinR2.pause();
            if (animatorSetCoinR3.isRunning())
                animatorSetCoinR3.pause();
            if (animatorSetCoinL1.isRunning())
                animatorSetCoinL1.pause();
            if (animatorSetCoinL2.isRunning())
                animatorSetCoinL2.pause();
            if (animatorSetCoinL3.isRunning())
                animatorSetCoinL3.pause();
            if (animatorSetCoinL4.isRunning())
                animatorSetCoinL4.pause();
        }
        isPause = true;
    }

    public void resumeAnim() {
        if (handAnimDrawable != null) {
            handAnimDrawable.run();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(firstAnimatorSet.isPaused())
                firstAnimatorSet.resume();
            if (animatorSetVoucherR1.isPaused())
                animatorSetVoucherR1.resume();
            if (animatorSetVoucherR2.isPaused())
                animatorSetVoucherR2.resume();
            if (animatorSetVoucherL1.isPaused())
                animatorSetVoucherL1.resume();
            if (animatorSetVoucherL2.isPaused())
                animatorSetVoucherL2.resume();
            if (animatorSetCoinR1.isPaused())
                animatorSetCoinR1.resume();
            if (animatorSetCoinR2.isPaused())
                animatorSetCoinR2.resume();
            if (animatorSetCoinR3.isPaused())
                animatorSetCoinR3.resume();
            if (animatorSetCoinL1.isPaused())
                animatorSetCoinL1.resume();
            if (animatorSetCoinL2.isPaused())
                animatorSetCoinL2.resume();
            if (animatorSetCoinL3.isPaused())
                animatorSetCoinL3.resume();
            if (animatorSetCoinL4.isPaused())
                animatorSetCoinL4.resume();
        }
        isPause = false;
    }


    //endregion===动画的开启与关闭======


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
                handAnimDrawable.stop();
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
     */
    protected void startIconAnim(final View view, long desX, long desY, boolean temporaryBigger, AnimatorSet totalAnimationSet) {
        if (view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
            view.setAlpha(0);
        }
//        desX = desX - view.getWidth() / 2;
//        desY = desY - view.getHeight() / 2;

        long translateTime = 400;
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

        totalAnimationSet.play(translateAnimatorSet)
                .before(getFlowAnim(view, desX, desY, 100));
        totalAnimationSet.setStartDelay(getRandom().nextInt(201));
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
        ObjectAnimator translationYAnim;
        if (getRandom().nextInt(2) == 1) {
            translationYAnim = ObjectAnimator.ofFloat(view, "translationY", desY - 6.0f, desY + 6.0f, desY - 6.0f);
        } else {
            translationYAnim = ObjectAnimator.ofFloat(view, "translationY", desY + 6.0f, desY - 6.0f, desY + 6.0f);
        }
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

}
