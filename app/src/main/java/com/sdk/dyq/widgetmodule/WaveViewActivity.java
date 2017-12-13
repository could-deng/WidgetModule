package com.sdk.dyq.widgetmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.sdk.dyq.widgetlibrary.waveView.WaveView;

/**
 * Created by bluepay on 2017/12/13.
 */

public class WaveViewActivity extends OptionMenuActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WaveView(this), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
