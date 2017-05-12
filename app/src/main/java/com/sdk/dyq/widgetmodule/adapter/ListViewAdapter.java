package com.sdk.dyq.widgetmodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdk.dyq.widgetmodule.R;

import java.util.List;

/**
 * Created by yuanqiang on 2017/5/12.
 */

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Integer> mData ;
    private Context mContext;

    public ListViewAdapter(Context mContext) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    public void setmData(List<Integer> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData == null){
            return 0;
        } else {
            return mData.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    private View createConvertView(){
        View convertView = mInflater.inflate(R.layout.listview_item,null);

        ViewHolder holder = new ViewHolder();
        holder.textView = (TextView) convertView.findViewById(R.id.tv_lv_item);

        convertView.setTag(holder);
        return convertView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(mData == null || mData.size() <= position){
            return null;
        }
        int item = mData.get(position);
        if (convertView == null) {
            convertView = createConvertView();
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if(holder!=null) {
            holder.textView.setText(String.valueOf(item));
        }
        return convertView;
    }



    private final class ViewHolder{
        public TextView textView;
    }
}
