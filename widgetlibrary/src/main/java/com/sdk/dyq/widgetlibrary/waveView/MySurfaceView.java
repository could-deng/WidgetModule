package com.sdk.dyq.widgetlibrary.waveView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceHolder:Surface的控制器,处理它的Canvas画的效果和动画，控制表面、大小、像素等
 * SurfaceHolder.Callback：接收Surface变化的消息
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class GLThread extends Thread{

    }
}
