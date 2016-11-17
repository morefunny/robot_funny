package com.luojituili.morefunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import api.ReceiveThreadListHandler;
import api.RobotApi;
import api.Thread;
import api.ThreadData;

import static com.luojituili.morefunny.R.id.imageView;

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
        _robotApi.GetJokeList(1001, 206, 10, _handler);

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
            holder.imageView = (ImageView) convertView.findViewById(imageView);
            holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
       // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txt_content.setText(_threadList.get(position).getContent());
        Thread thread = _threadList.get(position);
        if (thread.hasImage()) {
            Log.e("hasimage", "Yes");
            ArrayList<ThreadData> contentList = thread.getContentList();
            for (int i = 0; i < contentList.size(); i++) {
                if (contentList.get(i).getDataType().equals("pic")) {
                    Log.e("url", contentList.get(i).getData());
                    ImageLoader.getInstance().displayImage(contentList.get(i).getData(), holder.imageView);
                   // holder.imageView.setImageBitmap(returnBitMap(contentList.get(i).getData()));
                }
            }
        }
        return convertView;
    }

    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.e("image", "download succ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txt_content;
    }
}
