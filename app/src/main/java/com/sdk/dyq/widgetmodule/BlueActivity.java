package com.sdk.dyq.widgetmodule;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

/**
 * Created by deng on 2018/5/30.
 */

public class BlueActivity extends Activity implements View.OnClickListener{
    private Button mButton1 ;
    private Button mButton2 ;

    SoundPool soundPool;
    HashMap<Integer,Integer> musicId=new HashMap<Integer,Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        //初始化soundPool,第一个参数音频流的个数，第二个参数是音频流的质量
        soundPool=new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        // 通过 load 方法加载指定音频流，并将返回的音频 ID 放入 musicId 中
        musicId.put(1, soundPool.load(this, R.raw.pay_music_num_scroll, 1));
        musicId.put(2, soundPool.load(this, R.raw.pay_music_flower_boom,1));
        mButton1 = (Button) findViewById(R.id.btok1);
        mButton1.setOnClickListener(this);
        mButton2 = (Button) findViewById(R.id.btok2);
        mButton2.setOnClickListener(this);

    }
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.btok1:
                soundPool.play(musicId.get(1),15,15, 0, 0, 1f);
                break;
            case R.id.btok2:
                soundPool.play(musicId.get(2),15,15, 0, 0, 1f);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //M:lyj 释放音源资源
        soundPool.release();
    }

}
