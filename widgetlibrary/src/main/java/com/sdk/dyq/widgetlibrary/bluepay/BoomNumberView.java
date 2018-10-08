package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sdk.dyq.widgetlibrary.R;
import java.text.DecimalFormat;


/**
 * 爆炸数字滚动控件
 * Created by deng on 2018/5/24.
 */

public class BoomNumberView extends RelativeLayout implements RandomTextView.TextAnimCallBack {

    public static final String TAG = "BoomNumberView";

    private static float NumTextSizeDefault = 32F;
    private static int ViewColorDefault = Color.BLACK;
    private float numTextSize;//字体大小
    private int viewColor;

    private BoomSoundPlayer soundPlayer;
    RandomTextView randomTextView;
    RandomTextView randomTextView2;
    BoomColorFlowerView flowerView;
    BoomColorFlowerView boom_view_left;
    TextView tv_num_b;//左边的B符号
    TextView tv_num_right;//右边的划横线的数字
    TextView tv_dot;

    private String originRightNum;//右边划横线的数字
    float perSizeArea;//一个字符的宽度
    int width;
    int height;
    private int charDifNum=0;//数字变化前后的字符相差数

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
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BoomNumberView);
        numTextSize = ta.getDimension(R.styleable.BoomNumberView_num_size,NumTextSizeDefault);
        viewColor = ta.getColor(R.styleable.BoomNumberView_view_color, ViewColorDefault);

        View container_view = LayoutInflater.from(context).inflate(R.layout.layout_boom_num_view,null);
        tv_dot = (TextView) container_view.findViewById(R.id.tv_dot);
        randomTextView = (RandomTextView) container_view.findViewById(R.id.tv_random_num);
        randomTextView2 = (RandomTextView) container_view.findViewById(R.id.tv_random_num2);
        if(randomTextView!=null){
            randomTextView.setCallBack(this);
        }
        randomTextView.setGravity(CENTER_HORIZONTAL);
        randomTextView2.setGravity(CENTER_HORIZONTAL);

        tv_num_b = (TextView) container_view.findViewById(R.id.tv_num_b);


        tv_num_b.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);
        randomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);
        randomTextView2.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);
        tv_dot.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);

        tv_dot.setTextColor(viewColor);
        tv_num_b.setTextColor(viewColor);
        randomTextView.setTextColor(viewColor);
        randomTextView2.setTextColor(viewColor);

        //左右两边烟花view
        flowerView = (BoomColorFlowerView) container_view.findViewById(R.id.boom_view);
        boom_view_left = (BoomColorFlowerView) container_view.findViewById(R.id.boom_view_left);
        changeBottomLayoutStartAnimation(boom_view_left);
        float[] area = getStartArea(numTextSize);
        flowerView.setStartArea(area[0],area[1]);
        boom_view_left.setStartArea(area[0],area[1]);
        perSizeArea = area[0];
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flowerView.getLayoutParams();
        lp.leftMargin = (int) perSizeArea*(-1);
        flowerView.setLayoutParams(lp);

        RelativeLayout.LayoutParams left_lp = (RelativeLayout.LayoutParams) boom_view_left.getLayoutParams();
        left_lp.rightMargin = (int) (perSizeArea*(-1));
        boom_view_left.setLayoutParams(left_lp);


        //划横线的原价
        tv_num_right = (TextView)(container_view.findViewById(R.id.tv_num_right));
        tv_num_right.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

        addView(container_view,new LinearLayout.LayoutParams(context,attrs));
    }
    /**
     *  底部布局从180旋转至90
     */
    private void changeBottomLayoutStartAnimation(View layout) {
        layout.setVisibility(View.VISIBLE);
        ObjectAnimator mOpenAnimator = ObjectAnimator.ofFloat(layout, "RotationY", 0, 180);
        mOpenAnimator.start();
    }

    /**
     * 获取烟花控件的范围
     * @param textSizePx
     * @return
     */
    public float[] getStartArea(float textSizePx){
        float[] area = new float[2];
        Paint p = new Paint();
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(textSizePx);
        Rect mTextBounds = new Rect();
        p.getTextBounds("0", 0, 1, mTextBounds);
        area[1] = mTextBounds.height();
        float widths[] = new float[1];
        p.getTextWidths("0", widths);
        area[0] = widths[0];
        return area;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(soundPlayer!=null){
            soundPlayer.releaseResource();
        }
    }

    public void departNumToTwo(String numOrigin,String numAfter,String origin){
        originRightNum = origin;

        DecimalFormat fnum = new DecimalFormat("0.00");
        float o1 = Float.parseFloat(numOrigin);
        String formatedNumOrigin = fnum.format(o1);
        float o2 = Float.parseFloat(numAfter);
        String formatedNumAfter = fnum.format(o2);

        String num1Origin = formatedNumOrigin.substring(0,formatedNumOrigin.indexOf("."));
        String num2Origin = formatedNumOrigin.substring(formatedNumOrigin.indexOf(".")+1,formatedNumOrigin.length());

        String num1After = formatedNumAfter.substring(0,formatedNumAfter.indexOf("."));
        String num2After = formatedNumAfter.substring(formatedNumAfter.indexOf(".")+1,formatedNumAfter.length());


        String strDif = "";
        charDifNum = num1Origin.length()-num1After.length();
        if(charDifNum>0){
            for (int i = 0;i<charDifNum;i++){
                strDif += " ";
            }
            num1After = strDif+num1After;
        }else if(charDifNum<0){
            for (int i = 0;i<(-charDifNum);i++){
                strDif += " ";
            }
            num1Origin = strDif+num1Origin;
        }


        Log.e(TAG,"startBoomAnim()");
        if(soundPlayer == null){
            soundPlayer = new BoomSoundPlayer(getContext());
        }
        if (soundPlayer == null) return;
        soundPlayer.playSingleSound(R.raw.pay_music_num_scroll);
        if(randomTextView!=null){
            randomTextView.start(num1Origin,num1After,RandomTextView.FIRSTF_LAST,0);
        }

        if(num2Origin.equals("00") && num2After.equals("00") && randomTextView2 != null){
            tv_dot.setVisibility(GONE);
            randomTextView2.setVisibility(GONE);
        }else {
            if (randomTextView2 != null) {
                randomTextView2.start(num2Origin, num2After, RandomTextView.FIRSTF_LAST, 2);
            }
        }


    }

    @Override
    public void onAnimFinish() {
        adjustLocation();
        if(soundPlayer == null){
            soundPlayer = new BoomSoundPlayer(getContext());
        }
        if (soundPlayer == null) return;
        soundPlayer.playSingleSound(R.raw.pay_music_flower_boom);
        if(flowerView!=null){
            flowerView.startBoomFlower();
        }
        if(boom_view_left!=null){
            boom_view_left.startBoomFlower();
        }
        slowlyShowNumRight();
    }
    /**
     * 逐渐出现左边的划横线的原价
     */
    public void slowlyShowNumRight(){
        if(tv_num_right!=null && !TextUtils.isEmpty(originRightNum)){
//            tv_num_right.setText(String.format(getContext().getString(R.string.pay_money_text),originRightNum));
        }
        final ValueAnimator animator1 = ObjectAnimator.ofFloat(tv_num_right, "alpha",0,1);//淡出效果
        animator1.setDuration(1000);
        animator1.setInterpolator(new AccelerateInterpolator());
        animator1.start();
    }

    /**
     * 如果变化前后的数字长度有差，则调整位置
     */
    private void adjustLocation(){
        if(charDifNum!=0) {
            float[] perWidth = new float[1];
            randomTextView.getPaint().getTextWidths("0", perWidth);
            LinearLayout.LayoutParams randomTextView_lp = (LinearLayout.LayoutParams) randomTextView.getLayoutParams();
            randomTextView_lp.leftMargin = (int) ((-1) * (charDifNum * perWidth[0]));
            randomTextView.setLayoutParams(randomTextView_lp);
        }
    }
}
