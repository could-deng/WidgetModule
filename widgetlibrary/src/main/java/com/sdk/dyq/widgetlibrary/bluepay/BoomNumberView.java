package com.sdk.dyq.widgetlibrary.bluepay;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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


/**
 * 爆炸数字滚动控件
 * Created by deng on 2018/5/24.
 */

public class BoomNumberView extends RelativeLayout implements RandomTextView.TextAnimCallBack {

    public static final String TAG = "BoomNumberView";

    private static float NumTextSizeDefault = 32F;
    private float numTextSize;//字体大小

    private BoomSoundPlayer soundPlayer;
    RandomTextView randomTextView;
    BoomColorFlowerView flowerView;
    BoomColorFlowerView boom_view_left;
    TextView tv_num_b;
    TextView tv_num_right;
    float perSizeArea;//一个字符的宽度
    private String afterNum;
    private String origin;
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

        View container_view = LayoutInflater.from(context).inflate(R.layout.layout_boom_num_view,null);
        randomTextView = (RandomTextView) container_view.findViewById(R.id.tv_random_num);
        if(randomTextView!=null){
            randomTextView.setCallBack(this);
        }
        randomTextView.setGravity(CENTER_HORIZONTAL);

        tv_num_b = (TextView) container_view.findViewById(R.id.tv_num_b);


        tv_num_b.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);
        randomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,numTextSize);

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
        tv_num_right = (TextView)( container_view.findViewById(R.id.tv_num_right));
        tv_num_right.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );

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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(soundPlayer!=null){
            soundPlayer.releaseResource();
        }
    }


    /**
     * 开始启动动画
     * @param origin
     * @param afterNum
     * @param txtNumRight
     */
    public void startBoomAnim(String origin,String afterNum,String txtNumRight){
        String strDif = "";
        charDifNum = origin.length()-afterNum.length();
        if(charDifNum>0){
            for (int i = 0;i<charDifNum;i++){
                strDif += " ";
            }
            afterNum = strDif+afterNum;
        }else if(charDifNum<0){
            for (int i = 0;i<(-charDifNum);i++){
                strDif += " ";
            }
            origin = strDif+origin;
        }

        Log.e(TAG,"startBoomAnim()");
        if(soundPlayer == null){
            soundPlayer = new BoomSoundPlayer(getContext());
        }
        if (soundPlayer == null) return;
//        soundPlayer.playSingleSound(R.raw.pay_music_num_scroll);
        if(randomTextView!=null){
            randomTextView.start(origin,afterNum,RandomTextView.FIRSTF_LAST);
        }
        this.afterNum = afterNum;
        this.origin = txtNumRight;
    }


    @Override
    public void onAnimFinish() {
//        BlueLog.e(TAG,"onAnimFinish()");
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
        if(tv_num_right!=null && (origin.length()-afterNum.length()>0)){
            RelativeLayout.LayoutParams tv_num_right_lp = (RelativeLayout.LayoutParams) tv_num_right.getLayoutParams();
            tv_num_right_lp.leftMargin = (int) (perSizeArea*(-1)*(origin.length()-afterNum.length()));
            tv_num_right.setLayoutParams(tv_num_right_lp);
        }
        if(tv_num_right!=null && !TextUtils.isEmpty(origin)){
//            tv_num_right.setText(String.format(getContext().getString(R.string.pay_money_text),origin));
        }
        final ValueAnimator animator1 = ObjectAnimator.ofFloat(tv_num_right, "alpha",0,1);//淡出效果
        animator1.setDuration(1000);
        animator1.setInterpolator(new AccelerateInterpolator());
        animator1.start();
    }

    /**
     * 如果变化前后的数字长度有差，则调整B的位置
     */
    private void adjustLocation(){
        if(charDifNum!=0) {
            float[] perWidth = new float[1];
            randomTextView.getPaint().getTextWidths("0", perWidth);
            RelativeLayout.LayoutParams tv_num_b_lp = (LayoutParams) tv_num_b.getLayoutParams();
            tv_num_b_lp.rightMargin = (int) ((-1) * (charDifNum * perWidth[0]));
            tv_num_b.setLayoutParams(tv_num_b_lp);
        }
    }
}
