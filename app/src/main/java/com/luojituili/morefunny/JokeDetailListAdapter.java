package com.luojituili.morefunny;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
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

import api.Thread;
import api.ThreadData;


/**
 * Created by sherlockhua on 2016/11/16.
 */

public class JokeDetailListAdapter extends BaseAdapter {

    private static final int TYPE_JOKE_TEXT = 0;
    private static final int TYPE_JOKE_IMAGE = 1;
    private static final int TYPE_JOKE_GIF = 2;

    private Context mContext;

    private ListView _listview;
    private Thread _thread;

    public JokeDetailListAdapter(Context mContext, ListView listview, Thread thread) {

        this.mContext = mContext;
        this._listview = listview;
        this._thread = thread;

        Log.e("joke size", String.format("%d", thread.getContentList().size()));
    }

    @Override
    public int getCount() {
        return _thread.getContentList().size();
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
        ThreadData data = _thread.getContentList().get(position);
        String dataType = data.getDataType();

        if (dataType.equals("gif")) {
            return TYPE_JOKE_GIF;
        }

        if (dataType.equals("pic")) {
            return TYPE_JOKE_IMAGE;
        }

        return TYPE_JOKE_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public void loadImage(final SimpleDraweeView imageView, final String url, final int imageWidth) {

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

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .build();

        imageView.setController(controller);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder
                //.setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                //.setPlaceholderImage(new MyCustomDrawable())
                //.setBackgrounds(backgroundList)
                //.setOverlays(overlaysList)
                .build();
        imageView.setHierarchy(hierarchy);
    }

    private View getImageView(int position, View convertView, ViewGroup parent) {

        ViewHolderImageItem holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_detail_item_image, parent,false);
            holder = new ViewHolderImageItem();
            holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderImageItem) convertView.getTag();
        }

        ThreadData threadData = _thread.getContentList().get(position);
        String dataType = threadData.getDataType();
        if (dataType.equals("pic") || dataType.equals("gif")) {

            WindowManager wm = (WindowManager)parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            Log.e("image", String.format("%d",width));

            String url = threadData.getData();
            loadImage(holder.imageView, url, width);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(url)
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .build();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_detail_item_text,parent,false);
            holder = new ViewHolderTextItem();
            holder.txtContent = (TextView) convertView.findViewById(R.id.robot_list_item_txt_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolderTextItem) convertView.getTag();
        }

        ThreadData data = _thread.getContentList().get(position);
        holder.txtContent.setText(data.getData());
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
            default:
                break;
        }

        return convertView;
    }

    private class ViewHolderImageItem{
        SimpleDraweeView imageView;
    }

    private class ViewHolderTextItem {
        TextView txtContent;
    }
}
