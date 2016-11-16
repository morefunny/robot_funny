package com.luojituili.morefunny;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import api.ReceiveThreadListHandler;
import api.RobotApi;
import api.Thread;

/**
 * Created by sherlockhua on 2016/11/16.
 */

public class JokeListAdapter extends BaseAdapter {

    private Context mContext;
    private RobotApi _robotApi = new RobotApi();
    private ArrayList<Thread> _threadList = new ArrayList<Thread>();

    private ReceiveThreadListHandler _handler = new ReceiveThreadListHandler() {
        public void onReceiveThreadList(int catId, ArrayList<Thread> data){

            Log.e("catId", String.format("%d", catId));
            Log.e("count", String.format("%d", data.size()));
            for (int i = 0; i< data.size(); i++) {
                Log.e("threadId", String.format("%d", data.get(i).getThreadId()));
            }
            _threadList = data;
            notifyDataSetChanged();
        }
    };

    public JokeListAdapter(Context mContext) {
        this.mContext = mContext;
        _robotApi.GetJokeList(1001, 204, 10, _handler);

    }

    @Override
    public int getCount() {
        return _threadList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_text,parent,false);
            holder = new ViewHolder();
           // holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
       // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txt_content.setText(_threadList.get(position).getContent());
        return convertView;
    }

    private class ViewHolder{
        ImageView img_icon;
        TextView txt_content;
    }
}
