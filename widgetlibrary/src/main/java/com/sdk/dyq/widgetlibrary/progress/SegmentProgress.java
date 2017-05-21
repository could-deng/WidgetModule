package com.sdk.dyq.widgetlibrary.progress;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdk.dyq.widgetlibrary.R;
import com.sdk.dyq.widgetlibrary.Utils.ViewUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 五种颜色段区域圆+五个小圆圈
 */

public class SegmentProgress extends RelativeLayout {

    private ValueAnimator animator;
    private int animationDefaultDuration = 2000;//默认两秒动画

    private TimeInterpolator interpolator;

    //跑步的五个区域的百分比
    private float type1_progress;
    private float type2_progress;
    private float type3_progress;
    private float type4_progress;
    private float type5_progress;

    //五个圆圈相关
    private RelativeLayout rl_circle_progress1;
    private RelativeLayout rl_circle_progress2;
    private RelativeLayout rl_circle_progress3;
    private RelativeLayout rl_circle_progress4;
    private RelativeLayout rl_circle_progress5;

    private TextView tv_circle_progress1;
    private TextView tv_circle_progress2;
    private TextView tv_circle_progress3;
    private TextView tv_circle_progress4;
    private TextView tv_circle_progress5;

    private FiveSegmentProgress fiveSegmentProgress;

    private int circle_r;//圆圈半径

    private int circle_add = -40;//五个小圆圈距离大圆的表面的距离
    private static final int small_circle_round = 17;//小圆圈半径，跟界面上五个圆圆的长宽对应



    public SegmentProgress(Context context) {
        this(context,null);
    }

    public SegmentProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_segment_progress,null);
        rl_circle_progress1 = (RelativeLayout) view.findViewById(R.id.rl_circle_progress1);
        rl_circle_progress2 = (RelativeLayout) view.findViewById(R.id.rl_circle_progress2);
        rl_circle_progress3 = (RelativeLayout) view.findViewById(R.id.rl_circle_progress3);
        rl_circle_progress4 = (RelativeLayout) view.findViewById(R.id.rl_circle_progress4);
        rl_circle_progress5 = (RelativeLayout) view.findViewById(R.id.rl_circle_progress5);

        tv_circle_progress1 = (TextView) view.findViewById(R.id.tv_circle_progress1);
        tv_circle_progress2 = (TextView) view.findViewById(R.id.tv_circle_progress2);
        tv_circle_progress3 = (TextView) view.findViewById(R.id.tv_circle_progress3);
        tv_circle_progress4 = (TextView) view.findViewById(R.id.tv_circle_progress4);
        tv_circle_progress5 = (TextView) view.findViewById(R.id.tv_circle_progress5);

        fiveSegmentProgress = (FiveSegmentProgress) view.findViewById(R.id.fiveSegmentProgress);

        addView(view, new LayoutParams(context, attrs));
        interpolator = new AccelerateDecelerateInterpolator();

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        circle_r = fiveSegmentProgress.getMeasuredWidth() / 2 + circle_add;
        rl_circle_progress1.layout(
                getSmallCircleLeft(type1_progress / 2) - ViewUtils.dp2px(getContext(),small_circle_round),
                getSmallCircleUp(type1_progress / 2) - ViewUtils.dp2px(getContext(),small_circle_round),
                getSmallCircleLeft(type1_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round));
        rl_circle_progress2.layout(
                getSmallCircleLeft(type1_progress + type2_progress / 2) - ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress / 2) - ViewUtils.dp2px(getContext(),small_circle_round),
                getSmallCircleLeft(type1_progress + type2_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round));
        rl_circle_progress3.layout(
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round));
        rl_circle_progress4.layout(
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress + type4_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress + type4_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress + type4_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress + type4_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round));
        rl_circle_progress5.layout(
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress + type4_progress + type5_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress + type4_progress + type5_progress / 2)- ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleLeft(type1_progress + type2_progress + type3_progress + type4_progress + type5_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round),
                getSmallCircleUp(type1_progress + type2_progress + type3_progress + type4_progress + type5_progress / 2) + ViewUtils.dp2px(getContext(), small_circle_round));

    }

    /**
     * 动态显示进度
     */
    public void animShowProgress() {
        if (fiveSegmentProgress != null) {
            fiveSegmentProgress.setVisibility(VISIBLE);
            showCircle(false);
            //当五个区域断的数量都为0的情况下，彩色颜色全设全灰
            if(this.type1_progress == 0 && this.type2_progress == 0 && this.type3_progress == 0 && this.type4_progress == 0 && this.type5_progress == 0){
                fiveSegmentProgress.setProgress(0);
                showCircle(false);
                return;
            }

            if (this.type1_progress >= 0 && this.type2_progress >= 0 && this.type3_progress >= 0 && this.type4_progress >= 0 && this.type5_progress >= 0 &&
                    Math.round(this.type1_progress + this.type2_progress + this.type3_progress + this.type4_progress + this.type5_progress) == fiveSegmentProgress.getMax()) {

                animator = ValueAnimator.ofInt(0, 100);
                animator.setDuration(animationDefaultDuration);
                animator.setInterpolator(interpolator);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int t = (int) animation.getAnimatedValue();
                        fiveSegmentProgress.setProgress(t);
                        if (t == 0) {
                            showCircle(false);
                        } else if (t == 100) {
                            showCircle(true);
                        }
                    }
                });
                animator.start();
            }
        }
    }



    /**
     * 选择性显示五个小圆圈，并且去层叠情况（相邻两个progress的百分比均小于5%，后者不显示）
     * @param ifshow
     */
    private void showCircle(boolean ifshow) {
        if (ifshow) {
            if(type1_progress == 0){
                rl_circle_progress1.setVisibility(View.INVISIBLE);
            }else {
                rl_circle_progress1.setVisibility(View.VISIBLE);
            }

            if(type2_progress == 0){
                rl_circle_progress2.setVisibility(View.INVISIBLE);
            }else {
                if(type2_progress < 5 && type1_progress < 5 && rl_circle_progress1.getVisibility() == View.VISIBLE){
                    rl_circle_progress2.setVisibility(View.INVISIBLE);
                }else {
                    rl_circle_progress2.setVisibility(View.VISIBLE);
                }
            }

            if(type3_progress == 0){
                rl_circle_progress3.setVisibility(View.INVISIBLE);
            }else {
                if(type3_progress < 5 && type2_progress < 5 && rl_circle_progress2.getVisibility() == View.VISIBLE){
                    rl_circle_progress3.setVisibility(View.INVISIBLE);
                }else {
                    rl_circle_progress3.setVisibility(View.VISIBLE);
                }
            }

            if(type4_progress == 0){
                rl_circle_progress4.setVisibility(View.INVISIBLE);
            }else {
                if(type4_progress < 5 && type3_progress < 5 && rl_circle_progress3.getVisibility() == View.VISIBLE){
                    rl_circle_progress4.setVisibility(View.INVISIBLE);
                }else {
                    rl_circle_progress4.setVisibility(View.VISIBLE);
                }
            }
            if(type5_progress == 0){
                rl_circle_progress5.setVisibility(View.INVISIBLE);
            }else {
                if(type5_progress < 5 && type4_progress < 5 && rl_circle_progress4.getVisibility() == View.VISIBLE){
                    rl_circle_progress5.setVisibility(View.INVISIBLE);
                }
                else if(type5_progress < 5 && type1_progress < 5 && rl_circle_progress1.getVisibility() == View.VISIBLE){
                    rl_circle_progress5.setVisibility(View.INVISIBLE);
                }
                else{
                    rl_circle_progress5.setVisibility(View.VISIBLE);
                }
            }
        } else {
            rl_circle_progress5.setVisibility(View.GONE);
            rl_circle_progress4.setVisibility(View.GONE);
            rl_circle_progress3.setVisibility(View.GONE);
            rl_circle_progress2.setVisibility(View.GONE);
            rl_circle_progress1.setVisibility(View.GONE);
        }
    }








    /**
     * 获取小圆圈距离父布局左边的距离left
     * @param progress
     * @return
     */
    private int getSmallCircleLeft(float progress) {
        int round = circle_r;
        int left;
        int angle = (int)(18 * progress / 5);
        if (angle <= 90) {
            left = (int) (getMeasuredWidth() / 2 - round * Math.sin(angle * Math.PI / 180));
            return left;
        }
        if (angle <= 180) {
            left = (int) (getMeasuredWidth() / 2 - round * Math.cos((angle - 90) * Math.PI / 180));
            return left;
        }
        if (angle <= 270) {
            left = (int) (getMeasuredWidth() / 2 + round * Math.sin((angle - 180) * Math.PI / 180));
            return left;
        }
        if (angle <= 360) {
            left = (int) (getMeasuredWidth() / 2 + round * Math.cos((angle - 270) * Math.PI / 180));
            return left;
        }
        left = (int) (getMeasuredWidth() / 2 - round * Math.cos(angle * Math.PI / 180));//+ 90
        return left;
    }

    /**
     * 获取小圆圈距离父布局顶部的距离
     * @param progress
     * @return
     */
    private int getSmallCircleUp(float progress) {
        int round = circle_r;
        float angle = (18 * progress / 5);
        int up;
        if (angle <= 90) {
            up = (int) (getMeasuredWidth() / 2 + round * Math.cos(angle * Math.PI / 180));//+ 90
            return up;
        }
        if (angle <= 180) {
            up = (int) (getMeasuredWidth() / 2 - round* Math.sin((angle - 90) * Math.PI / 180));
            return up;
        }
        if (angle <= 270) {
            up = (int) (getMeasuredWidth() / 2 - round * Math.cos((angle - 180) * Math.PI / 180));
            return up;
        }
        if (angle <= 360) {
            up = (int) (getMeasuredWidth() / 2 + round * Math.sin((angle - 270) * Math.PI / 180));
            return up;
        }
        up = (int) (getMeasuredWidth() / 2 + round * Math.cos(angle * Math.PI / 180));
        return up;
    }








    public void setAllType(float type1, float type2, float type3, float type4, float type5) {
//        if (type1 >= 0 && type2 >= 0 && type3 >= 0 && type4 >= 0 && type5 >= 0 ) {
////            heart_rate_arc_progress.setMax(type1 + type2 + type3 + type4 + type5);
//            //TODO 将具体的五个区域的数量转化为百分比
//
        float sum = type1 + type2 + type3 + type4 + type5;

        BigDecimal b;

        if(type1!=0) {
            type1_progress = ((float) (Math.round(type1 * 100))) / sum;
            b = new BigDecimal(type1_progress);
            type1_progress = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            type1_progress = 0;
        }

        if(type2!=0) {
            type2_progress = ((float) (Math.round(type2 * 100))) / sum;
            b = new BigDecimal(type2_progress);
            type2_progress = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            type2_progress = 0;
        }

        if(type3!=0) {
            type3_progress = ((float) (Math.round(type3 * 100))) / sum;
            b = new BigDecimal(type3_progress);
            type3_progress = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            type3_progress = 0;
        }
        if(type4!=0) {
            type4_progress = ((float) (Math.round(type4 * 100))) / sum;
            b = new BigDecimal(type4_progress);
            type4_progress = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            type4_progress = 0;
        }
        if(type5!=0) {
            type5_progress = ((float) (Math.round(type5 * 100))) / sum;
            b = new BigDecimal(type5_progress);
            type5_progress = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            type5_progress = 0;
        }

//            setFiveProgress(type1,type2,type3,type4,type5,type);
        fiveSegmentProgress.setAllType(type1_progress, type2_progress, type3_progress, type4_progress, type5_progress);

        DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
        String num;
        if(type1_progress != 0){
            num = df.format(type1_progress);//返回的是String类型的
            tv_circle_progress1.setText(String.format("%s%s", num, "%"));
        }
        if(type2_progress != 0) {
            num = df.format(type2_progress);//返回的是String类型的
            tv_circle_progress2.setText(String.format("%s%s", num, "%"));
        }
        if(type3_progress != 0){
            num = df.format(type3_progress);//返回的是String类型的
            tv_circle_progress3.setText(String.format("%s%s", num, "%"));
        }
        if(type4_progress != 0){
            num = df.format(type4_progress);//返回的是String类型的
            tv_circle_progress4.setText(String.format("%s%s", num, "%"));
        }
        if(type5_progress != 0){
            num = df.format(type5_progress);//返回的是String类型的
            tv_circle_progress5.setText(String.format("%s%s", num, "%"));
        }
        showCircle(true);
    }

}
