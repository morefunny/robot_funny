package com.luojituili.morefunny;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import api.Thread;
import api.ThreadData;


/**
 * Created by sherlockhua on 2016/11/16.
 */

public class JokeListAdapter extends BaseAdapter {

    private static final int TYPE_JOKE_TEXT = 0;
    private static final int TYPE_JOKE_IMAGE = 1;
    private static final int TYPE_JOKE_NOTIFY = 2;
    private Context mContext;
    private ArrayList<Thread> _threadList = new ArrayList<Thread>();

    public JokeListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void appendThreadList(ArrayList<Thread> threadList) {
        Thread endNotify = new Thread(true, "上次看到这，点击刷新");
        //Thread startNotify = new Thread(true, "本次更新了10个内容");
        //threadList.add(0, startNotify);
        threadList.add(endNotify);

        _threadList.addAll(0, threadList);
        notifyDataSetChanged();
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
    public int getItemViewType(int position) {
        Thread thread = _threadList.get(position);
        if (thread.isNotify()) {
            return TYPE_JOKE_NOTIFY;
        }

        if (thread.hasImage()) {
            return TYPE_JOKE_IMAGE;
        }

        return TYPE_JOKE_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private View getImageView(int position, View convertView, ViewGroup parent) {

        ViewHolderImageItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_image,parent,false);
            holder = new ViewHolderImageItem();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);

            WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            ViewGroup.LayoutParams lp = holder.txtContent.getLayoutParams();
            lp.width = width*9/10;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.txtContent.setLayoutParams(lp);

            ViewGroup.LayoutParams lpImage = holder.imageView.getLayoutParams();
            lpImage.width = width*9/10;
            lpImage.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.imageView.setLayoutParams(lpImage);
            holder.imageView.setMaxWidth(width);
            holder.imageView.setMaxHeight(100*width);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderImageItem) convertView.getTag();
        }
        // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txtContent.setText(_threadList.get(position).getContent());

        Thread thread = _threadList.get(position);
        if (thread.hasImage()) {
            Log.e("hasimage", "Yes");
            ArrayList<ThreadData> contentList = thread.getContentList();
            for (int i = 0; i < contentList.size(); i++) {
                if (contentList.get(i).getDataType().equals("pic")) {
                    Log.e("url", contentList.get(i).getData());

                    WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
                    Log.e("image", String.format("%d",width));

                    Glide.with(holder.imageView.getContext())
                            .load(contentList.get(i).getData())
                            .placeholder(R.mipmap.ic_placeholder)
                            .error(R.mipmap.ic_placeholder)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imageView);
                }
            }
        }

        return convertView;
    }

    private View getTextView(int position, View convertView, ViewGroup parent) {

        ViewHolderTextItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_text,parent,false);
            holder = new ViewHolderTextItem();
            holder.txtContent = (TextView) convertView.findViewById(R.id.robot_list_item_txt_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderTextItem) convertView.getTag();
        }
        // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txtContent.setText(_threadList.get(position).getContent());
        return convertView;
    }


    private View getNotifyView(int position, View convertView, ViewGroup parent) {

        ViewHolderTextItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_notify,parent,false);
            holder = new ViewHolderTextItem();
            holder.txtContent = (TextView) convertView.findViewById(R.id.robot_list_item_notify_txt);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderTextItem) convertView.getTag();
        }
        // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txtContent.setText(_threadList.get(position).getContent());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        switch (type) {
            case TYPE_JOKE_IMAGE:
                convertView = getImageView(position, convertView, parent);
                break;
            case TYPE_JOKE_TEXT:
                convertView = getTextView(position, convertView, parent);
                break;
            case TYPE_JOKE_NOTIFY:
                convertView = getNotifyView(position, convertView, parent);
                break;
            default:
                break;
        }

        return convertView;
    }

    private class ViewHolderImageItem{
        ImageView imageView;
        TextView txtContent;
    }

    private class ViewHolderTextItem {
        TextView txtContent;
    }
}
