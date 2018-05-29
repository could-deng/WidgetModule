package com.sdk.dyq.widgetlibrary.bluepay;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by deng on 2018/5/29.
 */

public class BoomSoundPlayer {

    private SoundPool mSoundPool;
    private Queue<Integer> mPlayedStreamId;
    private final int MAX_STREAMS = 1;// 能够同时播放的音效数量
    private final float lowMusicVolumeRatio = 0.1f;

    private int iResIdCount;//资源的个数
    private int iPlayedIndex;//当前播放完成的资源position，从1开始
    private int iLoadIndex;//当前加载完成的资源position，从1开始

    private Queue<Integer> mPreparedSoundId; //加载完成的资源序列

    private ThreadPoolExecutor controlSoundExecutor;


    private final SoundPool.OnLoadCompleteListener loadCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(final SoundPool sp, final int soundID,
                                   int status) {
            if (status != 0)
                return;
//            addPreparedSoundId(soundID);
//            setLoadIndex(getLoadIndex() + 1);
//            if (getLoadIndex() >= getResIdCount()) {//如果全部资源播放完毕
//                getThreadPoolExecutor().execute(myPlayRunnable);
//            }
            Log.e("SoundPlayerBoom","soundID="+soundID+",status="+status);
        }
    };

    private final Runnable myPlayRunnable = new Runnable() {
        public void run() {
            while (true) {
                int soundID = getPreparedSoundId();
                int streamId = getSoundPool().play(soundID, 1, 1, 1, 0, 1);//播放
                setPlayedIndex(getPlayedIndex() + 1);//设置播放完成的个数
                getPlayedSoundQueue().offer(streamId);
            }
        }
    };

    private synchronized int getPreparedSoundId() {
        Integer id = getPreparedSoundQueue().poll();//移除并返回头部元素
        return (id == null) ? -1 : id;
    }




    private Context mContext;

    public BoomSoundPlayer(Context context) {
        mContext = context;
        clear();
    }

    private Context getContext() {
        return mContext;
    }

    private SoundPool getSoundPool() {
        if (mSoundPool == null)
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,
                    0);

        return mSoundPool;
    }
    private synchronized void addPreparedSoundId(int soundId) {
        try {
            getPreparedSoundQueue().offer(soundId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Queue<Integer> getPreparedSoundQueue() {
        if (mPreparedSoundId == null)
            mPreparedSoundId = new LinkedList<>();
        return mPreparedSoundId;
    }

    /**
     * 播报单个声效
     *
     * @param resId 单个声效资源ID
     */
    public void playSingleSound(int resId) {
        int resIds[] = new int[1];
        resIds[0] = resId;
        if (!setPlayResources(resIds))
            return;
        getSoundPool().load(getContext(), resId, 1);
    }

    private int getResIdCount() {
        return iResIdCount;
    }

    private void setResIdCount(int iCount)
    {
        iResIdCount = iCount;
    }

    private synchronized void setLoadIndex(int iLoadIndex)
    {
        this.iLoadIndex = iLoadIndex;
    }
    private int getLoadIndex() {
        return iLoadIndex;
    }

    private synchronized void setPlayedIndex(int iIndex)
    {
        iPlayedIndex = iIndex;
    }
    private int getPlayedIndex() {
        return iPlayedIndex;
    }


    private synchronized boolean setPlayResources(int[] resIds) {
        if (resIds == null)
            return false;
//        stopAll();
//        setResIdCount(resIds.length);
        getSoundPool().setOnLoadCompleteListener(loadCompleteListener);
        setLoadIndex(0);
        setPlayedIndex(0);
        return true;
    }


    /**
     * 停止播放所有的声效资源
     */
    private void stopAll() {
        Integer id;
        while ((id = getPlayedSoundQueue().poll()) != null) {
            getSoundPool().stop(id);
        }
        getPlayedSoundQueue().clear();

    }
    public void releaseResource() {
        stopAll();
        if (mSoundPool != null) {
            getSoundPool().release();
        }
        mSoundPool = null;
        if (mPlayedStreamId != null)
            getPlayedSoundQueue().clear();
        mPlayedStreamId = null;
    }


    private Queue<Integer> getPlayedSoundQueue() {
        if (mPlayedStreamId == null)
            mPlayedStreamId = new LinkedList<>();
        return mPlayedStreamId;
    }
    private ThreadPoolExecutor getThreadPoolExecutor() {
        if (controlSoundExecutor == null)
            controlSoundExecutor = new ThreadPoolExecutor(1, 1, 60,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        return controlSoundExecutor;
    }

    private void clear() {
        iResIdCount = 0;
        iPlayedIndex = 0;
        iLoadIndex = 0;
    }
}
