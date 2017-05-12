package com.sdk.dyq.widgetlibrary.swiperefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sdk.dyq.widgetlibrary.R;


public class RefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {

    private ImageView iv_loading;
    private ImageView iv_pull;
    private AnimationDrawable mAnimDrawableLoading;
    private AnimationDrawable mAnimDrawablePull;

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        iv_pull = (ImageView) findViewById(R.id.iv_pull);
        mAnimDrawableLoading = (AnimationDrawable) iv_loading.getBackground();
        mAnimDrawablePull = (AnimationDrawable) iv_pull.getBackground();
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!mAnimDrawablePull.isRunning()) {
            mAnimDrawablePull.start();
        }
    }

    @Override
    public void onRelease() {
        iv_loading.setVisibility(VISIBLE);
        iv_pull.setVisibility(GONE);
        if (!mAnimDrawableLoading.isRunning()) {
            mAnimDrawableLoading.start();
            if (mAnimDrawablePull.isRunning()) {
                mAnimDrawablePull.stop();
            }
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {
        if (mAnimDrawableLoading.isRunning()) {
            mAnimDrawableLoading.stop();
        }
        if (mAnimDrawablePull.isRunning()) {
            mAnimDrawablePull.stop();
        }
        iv_loading.setVisibility(GONE);
        iv_pull.setVisibility(VISIBLE);
    }
}
