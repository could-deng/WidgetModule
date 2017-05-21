package com.sdk.dyq.widgetmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sdk.dyq.widgetlibrary.progress.ArcProgress;
import com.sdk.dyq.widgetlibrary.progress.DashBoardProgress;
import com.sdk.dyq.widgetlibrary.progress.SegmentProgress;

/**
 * Created by Administrator on 2017/5/21.
 */

public class ProgressTestActivity extends OptionMenuActivity {
//    ArcProgress arc_progress;
    private SegmentProgress segmentProgress;
    private DashBoardProgress dashboard_progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();
    }
    private void init(){
//        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
//        arc_progress.setProgress(80);

        segmentProgress = (SegmentProgress) findViewById(R.id.segmentProgress);
        segmentProgress.setAllType(20,20,30,40,44);
        segmentProgress.animShowProgress();

        dashboard_progress = (DashBoardProgress) findViewById(R.id.dashboard_progress);
        dashboard_progress.setAnimText(111.111f);
        dashboard_progress.setProgress((int) (111.111f * 100) / getState((long) 111.111f));
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
