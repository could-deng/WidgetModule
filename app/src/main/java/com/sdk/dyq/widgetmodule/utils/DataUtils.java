package com.sdk.dyq.widgetmodule.utils;

import android.os.Build;

/**
 * Created by yuanqiang on 2017/5/12.
 */

public class DataUtils {
    /**
     * 获取手机Cpu核数
     *
     * @return 手机cpu核数
     */
    public static int getPhoneCpuCoreNum() {
        if (Build.VERSION.SDK_INT >= 17) {
            int num = Runtime.getRuntime().availableProcessors();
            if (num <= 0) {
                return 2;
            }
            return num;
        } else {
            return 2;
        }
    }
}
