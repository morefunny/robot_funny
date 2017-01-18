package com.luojituili.morefunny;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import api.ReceiveThreadListHandler;
import api.RobotApi;
import api.Thread;
import api.ThreadData;


/**
 * Created by sherlockhua on 2016/10/30.
 */

public class JokePage extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout _swipeLayout;
    private JokeListAdapter _adapter;
    private String _categoryName;
    private int _categoryId;
    private RobotApi _robotApi = new RobotApi();
    private SimpleDiskCache _threadCache = null;
    private final String _configFileName = "update.index";
    private  int _curUpdateIndex = 0;
    private int _curHistoryIndex = 0;
    private int _curOffset = -1;

    public JokePage() {

    }

    public void setCategoryId(int categoryId) {
        _categoryId = categoryId;
    }

    public void setCategoryName(String name) {
        _categoryName = name;
    }

    public String getCategoryName() {

        return _categoryName;
    }

    public int getCategoryId() {
        return _categoryId;
    }



    private void toast(String text) {
        Toast toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;
    }

    private ReceiveThreadListHandler _handler = new ReceiveThreadListHandler() {
        public void onReceiveThreadList(int code, int catId, ArrayList<Thread> data){

            Log.e("catId", String.format("%d", catId));
            Log.e("count", String.format("%d", data.size()));

            _curOffset += data.size();
            Log.e("tid", String.format("offset[%d]", _curOffset));
            if (code == 200) {

                try {
                    if (data.size() > 0) {
                        JokePage.this.toast(String.format("本次更新了%d个内容", data.size()));
                        _curUpdateIndex++;
                        _threadCache.putArrayList(getCacheKey(_curUpdateIndex), data);
                    } else {
                        JokePage.this.toast("没有发现新内容，休息一下吧！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                _adapter.addThreadList(data);
            }

            _swipeLayout.setRefreshing(false);
        }
    };


    public void loadCategoryPos() {

        SharedPreferences sp = getContext().getSharedPreferences(_configFileName, Context.MODE_PRIVATE);
        _curOffset = sp.getInt(getOffsetKey(), -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_page, container, false);
        _swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        _swipeLayout.setOnRefreshListener(this);

        final ListView jokeList = (ListView) view.findViewById(R.id.joke_view);
        _adapter = new JokeListAdapter(JokePage.this.getContext(), this, _swipeLayout, jokeList);
        jokeList.setAdapter(_adapter);

        try {
            _threadCache = SimpleDiskCache.getCache(this.getContext().getString(R.string.text_cache));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadCategoryPos();
        jokeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("click", "item click");
                Intent intent = new Intent(getActivity(), JokeDetailPage.class);
                Thread data = _adapter.getItemData(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("thread", data);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        jokeList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                switch (scrollState)
                {
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        if (Fresco.getImagePipeline().isPaused()) {
                            Fresco.getImagePipeline().resume();
                        }

                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            try {
                                _curHistoryIndex--;
                                if (_curHistoryIndex <= 0) {
                                    return;
                                }

                                ArrayList<Thread> threadList = _threadCache.getArrayList(getCacheKey(_curHistoryIndex));
                                _adapter.appendThreadList(threadList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //int position = view.getFirstVisiblePosition();
                        //_adapter.onItemVisible(position);
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        if (!Fresco.getImagePipeline().isPaused()) {
                            Fresco.getImagePipeline().pause();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

                Log.e("list", "gif");
                if (_adapter.getCount() == 0) {
                    return;
                }

                WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();

                int first = jokeList.getFirstVisiblePosition();
                int last = jokeList.getLastVisiblePosition();
                boolean found = false;
                for (int i = first; i <= last; i++) {
                    Thread thread = _adapter.getItemData(i);
                    for (int j = 0; j < thread.getContentList().size(); j++) {
                        ThreadData threadData = thread.getContentList().get(j);
                        String dataType = threadData.getDataType();
                        if (dataType.equals("gif")) {
                            SimpleDraweeView imageView = (SimpleDraweeView)jokeList.findViewWithTag(threadData.getData());
                            if (imageView == null) {
                                continue;
                            }

                            int[] location = new int[2];
                            imageView.getLocationOnScreen(location);
                            int imageOffset = imageView.getHeight()/2 + location[1];
                            Log.e("trace", String.format("%d[%d] min[%d]max[%d]", i, location[1], height/5, height*4/5));
                            if (!found && imageOffset <= height*4/5 && imageOffset > height/5) {
                                if (imageView.getController().getAnimatable() != null) {
                                    if (imageView.getController().getAnimatable().isRunning() == false) {
                                        imageView.getController().getAnimatable().start();
                                    }
                                }
                                //found = true;
                            } else {
                                if (imageView.getController().getAnimatable() != null) {
                                    imageView.getController().getAnimatable().stop();
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && Util.isNeedUpdate(_categoryId)) {
            loadData();
        }
    }

    public String getUpdateIndexKey() {
        return String.format("%d-index", _categoryId);
    }

    public String getOffsetKey() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%d-%s-offset-pos", _categoryId, df.format(new Date()));
    }

    public String getReadMinKey() {
        return String.format("%d-min-pos", _categoryId);
    }


    public String getCacheKey(int curIndex) {
        return String.format("%d-%d", _categoryId, curIndex);
    }

    @Override
    public  void onResume() {
        Log.e("jokepage", "resume");
        super.onResume();

        SharedPreferences sp = getContext().getSharedPreferences(_configFileName, Context.MODE_PRIVATE);
        _curUpdateIndex = sp.getInt(getUpdateIndexKey(), 0);
        if (_curUpdateIndex <= 0) {
            return;
        }

        _curHistoryIndex = _curUpdateIndex;
        try {
            ArrayList<Thread> threadList = _threadCache.getArrayList(getCacheKey(_curUpdateIndex));
            if (threadList.size() == 0) {
                return;
            }

            _adapter.appendThreadList(threadList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        Log.e("jokepage", "stop");
        SharedPreferences sp = getContext().getSharedPreferences(_configFileName, Context.MODE_PRIVATE);
        sp.edit().putInt(getUpdateIndexKey(), _curUpdateIndex).commit();
        sp.edit().putInt(getOffsetKey(), _curOffset).commit();
        super.onStop();
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _robotApi.getJokeList(-1, Util.getImei(), _categoryId, _curOffset, 10, _handler);
                }
            }, 365
        );
    }

    public void loadData() {
        if (_swipeLayout == null) {
            return;
        }

        _swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                _swipeLayout.setRefreshing(true);
            }
        });

        onRefresh();
    }
}
