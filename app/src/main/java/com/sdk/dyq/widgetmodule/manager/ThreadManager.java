package com.sdk.dyq.widgetmodule.manager;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import com.sdk.dyq.widgetmodule.utils.DataUtils;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuanqiang on 2017/5/12.
 */

public final class ThreadManager {
    /**
     * 主线程
     */
    private static Handler mUiHandler;
    private static final Object mMainHandlerLock = new Object();



    /**
     * 副线程1
     */
    private static HandlerThread SUB_THREAD1;
    private static Handler SUB_THREAD1_HANDLER;


    /**
     * AsyncTask的默认线程池Executor. 负责长时间的任务(网络访问) 默认3个线程
     */
    public static final Executor NETWORK_EXECUTOR;
    static{
        NETWORK_EXECUTOR = initNetworkExecutor();
    }

    /**
     * 在网络线程上执行异步操作. 该线程池负责网络请求等操作 长时间的执行(如网络请求使用此方法执行) 当然也可以执行其他 线程和AsyncTask公用
     *
     * @param run
     */
    public static void executeOnNetWorkThread(Runnable run) {
        try {
            NETWORK_EXECUTOR.execute(run);
        } catch (RejectedExecutionException e) {
        }
    }

    /**
     * 获取主线程Handler
     * @return
     */
    public static Handler getmUiHandler(){
        if(mUiHandler == null){
            synchronized (mMainHandlerLock){
//                if(mUiHandler == null){
                    mUiHandler = new Handler();
//                }
            }
        }
        return mUiHandler;
    }


    /**
     * 获取副线程1Handler
     * @return
     */
    public static Handler getSubThread1Handler(){
        if(SUB_THREAD1_HANDLER == null){
            synchronized (ThreadManager.class){
                SUB_THREAD1 = new HandlerThread("SUB1");
                SUB_THREAD1.setPriority(Thread.MIN_PRIORITY);
                SUB_THREAD1.start();
                SUB_THREAD1_HANDLER = new Handler(SUB_THREAD1.getLooper());
            }
        }
        return SUB_THREAD1_HANDLER;
    }

    /**
     * 初始化专门用来网络访问的线程池
     * @return
     */
    private static Executor initNetworkExecutor() {
        Executor result;
        // 3.0以上
        if (Build.VERSION.SDK_INT >= 11) {
            //result = AsyncTask.THREAD_POOL_EXECUTOR;
            result = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        // 3.0以下, 反射获取
        else {
            Executor tmp;
            try {
                Field field = AsyncTask.class.getDeclaredField("sExecutor");
                field.setAccessible(true);
                tmp = (Executor) field.get(null);
            } catch (Exception e) {
                tmp = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>());
            }
            result = tmp;
        }

        if (result instanceof ThreadPoolExecutor) {
            //线程数改为CPU 核数 +1
            ((ThreadPoolExecutor) result).setCorePoolSize(DataUtils.getPhoneCpuCoreNum() + 1);
        }

        return result;
    }

}
