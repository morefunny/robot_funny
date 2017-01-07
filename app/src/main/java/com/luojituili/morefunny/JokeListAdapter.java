package com.luojituili.morefunny;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
    private static final int TYPE_JOKE_GIF = 3;
    private static final int TYPE_LONG_TEXT = 4;
    private Context mContext;
    private SwipeRefreshLayout.OnRefreshListener _swipeListener;

    private SwipeRefreshLayout _swipeLayout;
    private ListView _listview;
    private ArrayList<Thread> _threadList = new ArrayList<Thread>();
    private ArrayList<Integer> _notifyPositionList = new ArrayList<Integer>();

    public JokeListAdapter(Context mContext, SwipeRefreshLayout.OnRefreshListener _swipeListener,
                           SwipeRefreshLayout swipeLayout, ListView listview) {

        this._swipeListener = _swipeListener;
        this._swipeLayout = swipeLayout;
        this.mContext = mContext;
        this._listview = listview;
    }

    public void appendThreadList(ArrayList<Thread> threadList) {

        _threadList.addAll(threadList);
        notifyDataSetChanged();
    }

    public void addThreadList(ArrayList<Thread> threadList) {

        if (_notifyPositionList.size() > 0) {
            for (int i = 0; i < _notifyPositionList.size(); i++) {
                int index = _notifyPositionList.get(i).intValue();
                if (index >=0 && index <_threadList.size()) {
                    for (int j = index - 5; j < index + 5; j++) {
                        if (j < 0 || j >= _threadList.size()) {
                            continue;
                        }
                        Thread thread = _threadList.get(j);
                        if (thread.isNotify() == false) {
                            continue;
                        }

                        _threadList.remove(j);
                    }
                }
            }
            _notifyPositionList.clear();
        }

        if (threadList.size() == 0) {
            return;
        }

        Thread endNotify = new Thread(true, "上次看到这里，点击刷新");
        threadList.add(endNotify);

        _notifyPositionList.add(new Integer(threadList.size())-1);
        _threadList.addAll(0, threadList);
        notifyDataSetChanged();
    }

    public Thread getItemData(int pos) {
        return _threadList.get(pos);
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

        if (thread.isLongText()) {
            return TYPE_LONG_TEXT;
        }

        if (thread.isNotify()) {
            return TYPE_JOKE_NOTIFY;
        }

        if (thread.isGif()) {
            return TYPE_JOKE_GIF;
        }

        if (thread.isPic()) {
            return TYPE_JOKE_IMAGE;
        }

        return TYPE_JOKE_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    public void loadImage(final SimpleDraweeView imageView, final ThreadData data, final int imageWidth) {

        final ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFinalImageSet(String id,  ImageInfo imageInfo,  Animatable anim) {
                if (imageInfo == null) {
                    return;
                }

                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                imageView.setLayoutParams(layoutParams);
            }


        };

        if (data.getDataType().equals("gif")) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.getThumb()))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setLowResImageRequest(request)
                    .setUri(Uri.parse(data.getData()))
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .build();
            imageView.setController(controller);

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(mContext.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setProgressBarImage(new ProgressBarDrawable())
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            imageView.setHierarchy(hierarchy);
            return;
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(data.getData()))
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .build();

        imageView.setController(controller);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();
        imageView.setHierarchy(hierarchy);
    }

    private View getImageView(int position, View convertView, ViewGroup parent) {

        ViewHolderImageItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_image,parent,false);
            holder = new ViewHolderImageItem();
            holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.imageView);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
            holder.summery.txtUp = (TextView)convertView.findViewById(R.id.robot_digg);
            holder.summery.txtDown = (TextView)convertView.findViewById(R.id.robot_bury);
            holder.summery.txtComment = (TextView)convertView.findViewById(R.id.robot_comment_count);

            WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            ViewGroup.LayoutParams lp = holder.txtContent.getLayoutParams();
            lp.width = width*9/10;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.txtContent.setLayoutParams(lp);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderImageItem) convertView.getTag();
        }

        Thread thread = _threadList.get(position);

        holder.txtContent.setText(_threadList.get(position).getContent());
        holder.summery.txtUp.setText(thread.getUpCount());
        holder.summery.txtComment.setText(thread.getCommentCount());
        holder.summery.txtDown.setText(thread.getDownCount());
        
        if (thread.hasImage()) {
            Log.e("hasimage", "Yes");
            ArrayList<ThreadData> contentList = thread.getContentList();
            for (int i = 0; i < contentList.size(); i++) {
                ThreadData threadData = contentList.get(i);
                String dataType = threadData.getDataType();
                if (dataType.equals("pic") || dataType.equals("gif")) {

                    WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
                    Log.e("image", String.format("%d",width));

                    loadImage(holder.imageView, threadData, width*9/10);
                    /*DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setUri(url)
                            .setAutoPlayAnimations(true)
                            .setTapToRetryEnabled(true)
                            .build();*/
                }
            }
        }

        return convertView;
    }


    private View getLongTextView(int position, View convertView, ViewGroup parent) {

        ViewHolderImageItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_image,parent,false);
            holder = new ViewHolderImageItem();
            holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.imageView);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
            holder.summery.txtUp = (TextView)convertView.findViewById(R.id.robot_digg);
            holder.summery.txtDown = (TextView)convertView.findViewById(R.id.robot_bury);
            holder.summery.txtComment = (TextView)convertView.findViewById(R.id.robot_comment_count);

            WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            ViewGroup.LayoutParams lp = holder.txtContent.getLayoutParams();
            lp.width = width*9/10;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.txtContent.setLayoutParams(lp);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderImageItem) convertView.getTag();
        }

        Thread thread = _threadList.get(position);

        holder.txtContent.setText(_threadList.get(position).getTitle());
        holder.summery.txtUp.setText(thread.getUpCount());
        holder.summery.txtComment.setText(thread.getCommentCount());
        holder.summery.txtDown.setText(thread.getDownCount());

        ArrayList<ThreadData> contentList = thread.getContentList();
        for (int i = 0; i < contentList.size(); i++) {
            ThreadData threadData = contentList.get(i);
            String dataType = threadData.getDataType();
            if (dataType.equals("pic") || dataType.equals("gif")) {

                WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                Log.e("image", String.format("%d",width));


                String url = threadData.getData();
                loadImage(holder.imageView, threadData, width*9/10);
                /*DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(url)
                        .setAutoPlayAnimations(true)
                        .setTapToRetryEnabled(true)
                        .build();*/
                break;
            }
        }

        return convertView;
    }

    public void onItemVisible(int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_JOKE_GIF:
                Log.e("list", "gif");

                break;
        }

    }

    private View getTextView(int position, View convertView, ViewGroup parent) {

        ViewHolderTextItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_text,parent,false);
            holder = new ViewHolderTextItem();
            holder.txtContent = (TextView) convertView.findViewById(R.id.robot_list_item_txt_content);

            holder.summery.txtUp = (TextView)convertView.findViewById(R.id.robot_digg);
            holder.summery.txtDown = (TextView)convertView.findViewById(R.id.robot_bury);
            holder.summery.txtComment = (TextView)convertView.findViewById(R.id.robot_comment_count);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderTextItem) convertView.getTag();
        }

        Thread thread = _threadList.get(position);
        holder.summery.txtUp.setText(thread.getUpCount());
        holder.summery.txtComment.setText(thread.getCommentCount());
        holder.summery.txtDown.setText(thread.getDownCount());

        // holder.img_icon.setImageResource(mData.get(position).getImgId());
        holder.txtContent.setText(thread.getContent());
        return convertView;
    }


    private View getNotifyView(final int position, View convertView, ViewGroup parent) {

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
        holder.txtContent.setTag(new Integer(position));
        holder.txtContent.setText(_threadList.get(position).getContent());
        holder.txtContent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("notify click", "onClick");
                _listview.setSelection(0);
                Integer pos = (Integer) v.getTag();
                if (pos.intValue() < _threadList.size()) {
                    _threadList.remove(pos.intValue());
                }

                _swipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        _swipeLayout.setRefreshing(true);
                    }
                });

                _swipeListener.onRefresh();
            }
        });
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        switch (type) {
            case TYPE_JOKE_GIF:
                Log.e("list", "gif");
                convertView = getImageView(position, convertView, parent);
                break;
            case TYPE_JOKE_IMAGE:
                convertView = getImageView(position, convertView, parent);
                break;
            case TYPE_JOKE_TEXT:
                Log.e("list", "text");
                convertView = getTextView(position, convertView, parent);
                break;
            case TYPE_JOKE_NOTIFY:
                convertView = getNotifyView(position, convertView, parent);
                break;
            case TYPE_LONG_TEXT:
                convertView = getLongTextView(position, convertView, parent);
                break;
            default:
                break;
        }

        return convertView;
    }

    private class ViewHolderSummery {
        TextView txtUp;
        TextView txtDown;
        TextView txtComment;
    }

    private class ViewHolderImageItem{
        SimpleDraweeView imageView;
        TextView txtContent;

        ViewHolderSummery summery = new ViewHolderSummery();
    }

    private class ViewHolderTextItem {
        TextView txtContent;
        ViewHolderSummery summery = new ViewHolderSummery();
    }
}
