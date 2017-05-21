package com.sdk.dyq.widgetlibrary.TextView;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * 动态翻滚动画TextView
 */
public class AnimationNumberTextView extends TextView {

    private long animationDefaultDuration;
    private TimeInterpolator interpolator;

    public AnimationNumberTextView(Context context) {
        super(context);
        init();
    }

    public AnimationNumberTextView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public AnimationNumberTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        init();
    }

    private void init() {
        animationDefaultDuration = 2000; //默认2秒动画
        interpolator = new AccelerateDecelerateInterpolator();
    }


    public void setAnimationDuration(long animationDuration) {
        if (animationDuration > 0)
            animationDefaultDuration = animationDuration;
    }


    public void showFloatText1(float floatNumber) {
        if (floatNumber < 0) {
            if (floatNumber < 1000) {
                setText(String.format("%.2f", floatNumber));
            } else if (floatNumber < 10000) {
                setText(String.format("%.1f", floatNumber));
            } else {
                setText(String.format("%.0f", floatNumber));
            }
        } else if (Math.abs(floatNumber - 0) < 0.01) {
            //setText("0.00");
            //等于0时,由随机数倒序动画到0
            //float initNumber = (float) (Math.random() * 100);
            ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
            animator.setDuration(10);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //float t = (float) animation.getAnimatedValue();
                    setText("0.00");
                }
            });
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofFloat(0.0f, floatNumber);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float t = (float) animation.getAnimatedValue();
                    if (t < 1000) {
                        setText(String.format("%.2f", t));
                    } else if (t < 10000) {
                        setText(String.format("%.1f", t));
                    } else {
                        setText(String.format("%.0f", t));
                    }
                    //setText(String.format("%.2f", t));
                }
            });
            animator.start();
        }
    }

    private void showFloatText2(int floatNumber) {
        if (floatNumber < 0) {
            setText(String.format("%d", 0));
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(0, floatNumber);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int t = (int) animation.getAnimatedValue();
                    setText(String.format("%d", t));
                }
            });
            animator.start();
        }
    }

    /**
     * 设置整型数字
     *
     * @param intNumber 整型数字,大于或等于0才会有动效
     */
    public void setIntegerText(int intNumber) {
        if (intNumber < 0) {
            setText(String.format("%d", intNumber));
        } else if (intNumber == 0) {////等于0时,由随机数倒序动画到0
            int initNumber = (int) (Math.random() * 100);
            ValueAnimator animator = ValueAnimator.ofInt(initNumber, 0);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int t = (int) animation.getAnimatedValue();
                    setText(String.format("%d", t));
                }
            });
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(0, intNumber);
            animator.setDuration(animationDefaultDuration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int t = (int) animation.getAnimatedValue();
                    setText(String.format("%d", t));
                }
            });
            animator.start();
        }
    }

    private boolean isCanChange = true;

    /**
     * 设置整型数字
     *
     * @param nextNumber 整型数字,大于或等于0才会有动效
     */
    public void setIntegerAddText(int nextNumber) {
        if (nextNumber <= 0) {
            setText(String.format("%04d", 0));
        } else {
            try {
                if (!isCanChange) return;
                String stringNumber = getText().toString();
                int preNumber = Integer.parseInt(stringNumber);
                if (nextNumber == preNumber) return;
                ValueAnimator animator = ValueAnimator.ofInt(preNumber, nextNumber);
                animator.setDuration(500);
                animator.setInterpolator(new LinearInterpolator());
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isCanChange = false;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isCanChange = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int t = (int) animation.getAnimatedValue();
                        if (t < 10) {
                            setText(String.format("%04d", t));
                        } else if (t < 100) {
                            setText(String.format("%04d", t));
                        } else if (t < 1000) {
                            setText(String.format("%04d", t));
                        } else {
                            setText(String.format("%.1f", t));
                        }
                        //setText(String.format("%d", t));
                    }
                });
                animator.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}