package com.sdk.dyq.widgetmodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sdk.dyq.widgetlibrary.bluepay.BoomNumberView;
import com.sdk.dyq.widgetlibrary.bluepay.RandomTextView;
import com.sdk.dyq.widgetlibrary.progress.DashBoardProgress;
import com.sdk.dyq.widgetlibrary.progress.SegmentProgress;

/**
 * Created by Administrator on 2017/5/21.
 */

public class ProgressTestActivity extends Activity {
//    ArcProgress arc_progress;
    private SegmentProgress segmentProgress;
    private DashBoardProgress dashboard_progress;
    private PayResultCircle pay_circle;
    private TextView tv_txt;
    private RandomTextView mRandomTextView;
    private BoomNumberView boomNumberView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();
    }
    private void init(){
//        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
//        arc_progress.setProgress(80);

//        segmentProgress = (SegmentProgress) findViewById(R.id.segmentProgress);
//        segmentProgress.setAllType(20,20,30,40,44);
//        segmentProgress.animShowProgress();
//
//        dashboard_progress = (DashBoardProgress) findViewById(R.id.dashboard_progress);
//        dashboard_progress.setAnimText(111.111f);
//        dashboard_progress.setProgress((int) (111.111f * 100) / getState((long) 111.111f));
        mRandomTextView = (RandomTextView) findViewById(R.id.tv_random_tv);
        tv_txt = (TextView) findViewById(R.id.tv_txt);
        pay_circle = (PayResultCircle) findViewById(R.id.pay_circle);
        boomNumberView = (BoomNumberView) findViewById(R.id.boomNumberView);
        pay_circle.setCallback(new PayResultCircle.ProgressCallback() {
            @Override
            public void changeState(int state) {
                tv_txt.setText(state+"");
            }
        });

    }

    public void onTickClick(View view){
//        if(pay_circle!=null){
//            pay_circle.completeDraw(PayResultCircle.STATE_TICK);
//        }
//        mRandomTextView.setText("123243");
//        mRandomTextView.setPianyilian(RandomTextView.FIRSTF_LAST);
        mRandomTextView.start("123456","110.00",RandomTextView.FIRSTF_LAST);
    }
    public void onErrorClick(View view){
//        if(pay_circle!=null){
//            pay_circle.completeDraw(PayResultCircle.STATE_ERROR);
//        }
        boomNumberView.startBoomAnim();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private int getState(long number) {
        final int states[] = {5, 10, 21, 42, 100, 200, 500, 1000, 2000, 5000, 10000, 20000};
        for (int value : states) {
            if (number <= value) return value;
        }
        return 0;
    }

}
