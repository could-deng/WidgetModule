package com.sdk.dyq.widgetlibrary.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RefreshFooterBaseLayout extends FrameLayout implements SwipeTrigger {

    public RefreshFooterBaseLayout(Context context) {
        this(context, null);
    }

    public RefreshFooterBaseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshFooterBaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onLoadMore() {
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
    }
}
