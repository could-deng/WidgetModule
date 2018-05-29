package com.sdk.dyq.widgetmodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sdk.dyq.widgetlibrary.bluepay.AutoScrollTextView;
import com.sdk.dyq.widgetlibrary.bluepay.BoomNumberView;
import com.sdk.dyq.widgetlibrary.bluepay.BoomSoundPlayer;
import com.sdk.dyq.widgetlibrary.bluepay.RandomTextView;
import com.sdk.dyq.widgetlibrary.progress.DashBoardProgress;
import com.sdk.dyq.widgetlibrary.progress.SegmentProgress;

import java.util.Random;

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

    private AutoScrollTextView mAutoScrollViewNumberView;

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
//        mRandomTextView = (RandomTextView) findViewById(R.id.tv_random_tv);
        tv_txt = (TextView) findViewById(R.id.tv_txt);
        pay_circle = (PayResultCircle) findViewById(R.id.pay_circle);
        boomNumberView = (BoomNumberView) findViewById(R.id.boomNumberView);
        pay_circle.setCallback(new PayResultCircle.ProgressCallback() {
            @Override
            public void changeState(int state) {
                tv_txt.setText(state+"");
            }
        });

//        initScrollView();
    }
//    private void initScrollView(){
//        mAutoScrollViewNumberView = (AutoScrollTextView) findViewById(R.id.id_scroll_text);
//        mAutoScrollViewNumberView.setMode(AutoScrollTextView.Mode.UP);
//        mAutoScrollViewNumberView.setNumber(7);
//        mAutoScrollViewNumberView.setTargetNumber(7);
//
//        mAutoScrollViewNumberView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Random random = new Random();
//                int start = random.nextInt(10);
//                int end = random.nextInt(10);
//                setTitle("Mode: Up , "+start+" -> " +end);
//                mAutoScrollViewNumberView.setNumber(start);
//                mAutoScrollViewNumberView.setTargetNumber(end);
//            }
//        });
//    }



    private BoomSoundPlayer soundPlayer;
    public void onTickClick(View view){
        if(soundPlayer == null){
            soundPlayer = new BoomSoundPlayer(ProgressTestActivity.this);
        }
        if (soundPlayer == null) return;
        soundPlayer.playSingleSound(R.raw.pay_music_num_scroll);

    }
    public void onErrorClick(View view){

        boomNumberView.startBoomAnim();
//        if(soundPlayer == null){
//            soundPlayer = new BoomSoundPlayer(ProgressTestActivity.this);
//        }
//        if (soundPlayer == null) return;
//        soundPlayer.playSingleSound(R.raw.pay_music_flower_boom);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPlayer != null) {
            soundPlayer.releaseResource();
            soundPlayer = null;
        }

    }
}
