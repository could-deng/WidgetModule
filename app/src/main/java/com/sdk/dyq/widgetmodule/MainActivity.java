package com.sdk.dyq.widgetmodule;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sdk.dyq.widgetlibrary.swiperefresh.SwipeLoadLayout;
import com.sdk.dyq.widgetmodule.adapter.ListViewAdapter;
import com.sdk.dyq.widgetmodule.manager.ThreadManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeLoadLayout swipeLayout;
    private ListView swipe_target;
    private ListViewAdapter lvAdapter;
    private List<Integer> dataList;

    private int messageIndex = 1;//页码

    /**
     * 仅用于模拟主角面更新操作
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    stopRefresh(getDataList(),messageIndex);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private List<Integer> getDataList(){
        if(dataList == null){
            dataList = new ArrayList<>();
            for(int i =0;i<10;i++){
                dataList.add(i);
            }
        }
        return dataList;
    }
    private void init(){
        swipeLayout = (SwipeLoadLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnLoadMoreListener(refreshListener);
        swipe_target = (ListView) findViewById(R.id.swipe_target);

        swipe_target.setAdapter(getLvAdapter());
        lvAdapter.setmData(getDataList());
    }

    public ListViewAdapter getLvAdapter() {
        if(lvAdapter == null){
            lvAdapter = new ListViewAdapter(this);
        }
        return lvAdapter;
    }

    /**
     * 模拟数据请求成功后数据增加
     */
    private void addTestData(){
        List<Integer> ll = getDataList();
        for(int i =0;i<10;i++){
            ll.add(i);
        }
        messageIndex++;
    }

    /**
     * 加载更多监听器
     */
    SwipeLoadLayout.OnLoadMoreListener refreshListener = new SwipeLoadLayout.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //进行网络请求
            ThreadManager.executeOnNetWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        addTestData();
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    /**
     * 刷新操作
     * @param list
     * @param index
     */
    public void stopRefresh(List<Integer> list, int index) {
        if (list == null || list.size() <= 0) {
            if (swipeLayout != null)
                swipeLayout.setLoadingNothing();
            return;
        } else {
            if (swipeLayout != null) {
                swipeLayout.setLoadMoreEnabled(true);
                swipeLayout.setLoadingMore(false);
            }
        }
        if (index > 2) {
            getLvAdapter().setmData(list);
        }else{
            if(swipe_target!=null){
                swipe_target.smoothScrollToPosition(0);
            }
            getLvAdapter().setmData(list);
        }
    }

    @Override
    protected void onDestroy() {
        handler = null;
        super.onDestroy();
    }
}
