package com.sdk.dyq.widgetmodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sdk.dyq.widgetlibrary.HandShaker.HandShakerView;

public class HandShakerAct extends Activity {
    HandShakerView hand_shaker_view;
    private boolean doShake;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_shaker);
        init();
    }

    public void onClick(View view){
        hand_shaker_view.testAnim();
    }
    private void init() {
        hand_shaker_view = (HandShakerView) findViewById(R.id.hand_shaker_view);
    }

    private void animAction(){
        if(hand_shaker_view!=null)
            hand_shaker_view.post(new Runnable() {
                @Override
                public void run() {
                    hand_shaker_view.startAnim();
                }
            });
    }
    private void animStop(){
        if(hand_shaker_view!=null)
            hand_shaker_view.post(new Runnable() {
                @Override
                public void run() {
                    hand_shaker_view.stopAnim();
                }
            });
    }
    @Override
    protected void onResume() {
        super.onResume();
        animAction();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        animStop();
    }
}
